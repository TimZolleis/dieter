package dev.elektronisch.dieter.server.device;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
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
    @Column(name = "mac_address", nullable = false)
    private String macAddress;
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;
    @Column(name = "hostname", nullable = false)
    private String hostname;
    @Column(name = "online", nullable = false)
    private boolean online;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", nullable = false)
    private Date modifiedAt;
}
