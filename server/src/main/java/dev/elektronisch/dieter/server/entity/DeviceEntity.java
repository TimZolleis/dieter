package dev.elektronisch.dieter.server.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "dieter_devices")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "uuid")
public final class DeviceEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false, updatable = false)
    private UUID uuid;
    @Column(name = "organisation_id", nullable = false)
    private UUID organisationId;
}
