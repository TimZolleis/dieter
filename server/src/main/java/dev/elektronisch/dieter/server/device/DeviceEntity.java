package dev.elektronisch.dieter.server.device;

import dev.elektronisch.dieter.common.dto.device.TerminationReason;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "dieter_devices")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public final class DeviceEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;
    @Type(type = "uuid-char")
    @Column(name = "organisation_id", nullable = false)
    private UUID organisationId;
    @Column(name = "termination_reason")
    private TerminationReason terminationReason;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", nullable = false)
    private Date modifiedAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_seen_at", nullable = false, updatable = false)
    private Date lastSeenAt;
}
