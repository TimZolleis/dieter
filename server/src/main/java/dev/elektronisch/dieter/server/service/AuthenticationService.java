package dev.elektronisch.dieter.server.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.elektronisch.dieter.common.model.authentication.LoginRequest;
import dev.elektronisch.dieter.common.model.authentication.RegistrationRequest;
import dev.elektronisch.dieter.common.model.authentication.TokenResponse;
import dev.elektronisch.dieter.common.model.authentication.VerificationRequest;
import dev.elektronisch.dieter.server.entity.AccountEntity;
import dev.elektronisch.dieter.server.exception.*;
import dev.elektronisch.dieter.server.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public final class AuthenticationService {

    private static final String ISSUER = "dieter";

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long millisToLive;

    public AuthenticationService(final AccountRepository repository,
                                 final PasswordEncoder passwordEncoder,
                                 @Value("${jwt.secret}") final String secret,
                                 @Value("${jwt.millisToLive}") final long millisToLive) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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

    public void handleRegistration(final RegistrationRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new UsernameTakenException();
        }

        if (repository.existsByEmail(request.getUsername())) {
            throw new EmailTakenException();
        }

        repository.save(new AccountEntity(request.getUsername(), request.getFirstName(), request.getLastName(), request.getEmail(), passwordEncoder.encode(request.getPassword())));

        // TODO send verification mail
    }

    public void handleVerification(final VerificationRequest request) {
        final AccountEntity account = repository.findById(request.getUuid())
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

        // Checking if user is system-admin
        if (account.isAdmin()) {
            builder.withClaim("admin", true);
        }

        return builder.sign(algorithm);
    }

    private Date calculateExpirationDate(final Date createdDate) {
        return new Date(createdDate.getTime() + millisToLive);
    }
}
