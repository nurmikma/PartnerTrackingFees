package com.evocon.partnertracking.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "client_name", nullable = false)
    private String clientName;

    // ManyToOne relationship with Partner entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id") // Relationship between Client and Partner using partner_id
    private Partner partner;

    // Default constructor required by JPA
    public Client() {}

    // Custom constructor with validation
    public Client(String clientName, Partner partner) {
        this.clientName = clientName;
        this.partner = partner;
    }

    // Getters and setters
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    // Equals and hashCode methods based on ID
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return getId() != null && getId().equals(((Client) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // toString method
    @Override
    public String toString() {
        return (
            "Client{" +
            "id=" +
            id +
            ", clientName='" +
            clientName +
            '\'' +
            ", partner=" +
            (partner != null ? partner.getId() : "null") +
            '}'
        );
    }
}
