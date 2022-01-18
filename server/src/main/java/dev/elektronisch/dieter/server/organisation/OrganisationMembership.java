package dev.elektronisch.dieter.server.organisation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "dieter_organisation_memberships")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public final class OrganisationMembership {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Type(type = "uuid-char")
    @Column(name = "account_id", nullable = false)
    private UUID accountId;
    @Type(type = "uuid-char")
    @Column(name = "organisation_id", nullable = false)
    private UUID organisationId;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
}
