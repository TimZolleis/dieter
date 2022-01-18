package dev.elektronisch.dieter.server.organisation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface OrganisationMembershipRepository extends JpaRepository<OrganisationMembership, UUID> {
    Set<OrganisationMembership> findAllByOrganisationId(final UUID organisationId);

    Set<OrganisationMembership> findAllByAccountId(final UUID accountId);
}
