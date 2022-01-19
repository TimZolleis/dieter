package dev.elektronisch.dieter.server.account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.elektronisch.dieter.common.dto.authentication.LoginRequest;
import dev.elektronisch.dieter.common.dto.authentication.RegistrationRequest;
import dev.elektronisch.dieter.common.dto.authentication.TokenResponse;
import dev.elektronisch.dieter.common.dto.authentication.VerificationRequest;
import dev.elektronisch.dieter.server.exception.*;
import dev.elektronisch.dieter.server.mail.MailingService;
import dev.elektronisch.dieter.server.organisation.OrganisationMembership;
import dev.elektronisch.dieter.server.organisation.OrganisationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public final class AccountService {

    private static final String ISSUER = "dieter";

    private final AccountRepository repository;

    private final PasswordEncoder passwordEncoder;
    private final MailingService mailingService;
    private final OrganisationService organisationService;

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long millisToLive;

    public AccountService(final AccountRepository repository,
                          final PasswordEncoder passwordEncoder,
                          final MailingService mailingService,
                          final OrganisationService organisationService,
                          @Value("${jwt.secret}") final String secret,
                          @Value("${jwt.millisToLive}") final long millisToLive) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.mailingService = mailingService;
        this.organisationService = organisationService;
        this.algorithm = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        this.millisToLive = millisToLive;
    }

    public TokenResponse handleLogin(final LoginRequest request) {
        return repository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .filter(account -> passwordEncoder.matches(request.getPassword(), account.getPassword()))
                .map(account -> {
                    if (!account.isVerified()) {
                        throw new NotVerifiedException();
                    }

                    final Date createdDate = new Date();
                    final Date expirationDate = calculateExpirationDate(createdDate);
                    final String token = createToken(account, createdDate, expirationDate);
                    return new TokenResponse(token, expirationDate.getTime());
                }).orElseThrow(InvalidCredentialsException::new);
    }

    public UUID handleRegistration(final RegistrationRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new UsernameTakenException();
        }

        if (repository.existsByEmail(request.getUsername())) {
            throw new EmailTakenException();
        }

        final AccountEntity account = repository.save(new AccountEntity(request.getUsername(), request.getFirstName(), request.getLastName(), request.getEmail(), passwordEncoder.encode(request.getPassword())));

        final String fullName = account.getFirstName() + " " + account.getLastName();
        mailingService.sendMail(fullName, account.getEmail(), "verification",
                "%name%", fullName, "%accountId%", account.getId().toString());

        return account.getId();
    }

    public void handleVerification(final VerificationRequest request) {
        final AccountEntity account = repository.findById(request.getAccountId())
                .orElseThrow(AccountNotFoundException::new);
        if (account.isVerified()) {
            throw new AlreadyVerifiedException();
        }

        account.setVerified(true);
        repository.save(account);
    }

    public DecodedJWT verifyToken(final String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    private String createToken(final AccountEntity account, final Date createdDate, final Date expirationDate) {
        final JWTCreator.Builder builder = JWT.create()
                .withSubject(account.getUsername())
                .withIssuedAt(createdDate)
                .withIssuer(ISSUER)
                .withExpiresAt(expirationDate);

        builder.withClaim("accountId", account.getId().toString());
        builder.withClaim("firstName", account.getFirstName());
        builder.withClaim("lastName", account.getLastName());
        builder.withClaim("email", account.getEmail());
        builder.withClaim("admin", account.isAdmin());

        final List<String> organisationIds = organisationService.getMemberships(account.getId()).stream()
                .map(OrganisationMembership::getOrganisationId)
                .map(UUID::toString)
                .collect(Collectors.toList());
        builder.withClaim("organisations", organisationIds);

        return builder.sign(algorithm);
    }

    private Date calculateExpirationDate(final Date createdDate) {
        return new Date(createdDate.getTime() + millisToLive);
    }
}
