package dev.elektronisch.dieter.server.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {
    Set<DeviceEntity> findAllByOrganisationId(UUID organisationId);
}
