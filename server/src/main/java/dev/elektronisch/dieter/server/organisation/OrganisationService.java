package dev.elektronisch.dieter.server.organisation;

import dev.elektronisch.dieter.server.exception.OrganisationNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public final class OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final OrganisationMembershipRepository membershipRepository;

    public OrganisationService(final OrganisationRepository repository,
                               final OrganisationMembershipRepository membershipRepository) {
        this.organisationRepository = repository;
        this.membershipRepository = membershipRepository;
    }

    public Set<OrganisationMembership> getMemberships(final UUID accountId) {
        return membershipRepository.findAllByAccountId(accountId);
    }

    public OrganisationEntity getOrganisation(final UUID id) {
        return organisationRepository.findById(id).orElseThrow(OrganisationNotFoundException::new);
    }
}
