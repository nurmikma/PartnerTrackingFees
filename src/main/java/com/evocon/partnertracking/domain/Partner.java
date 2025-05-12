package com.evocon.partnertracking.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Partner.
 */
@Entity
@Table(name = "partner")
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "partner_name", nullable = false)
    private String partnerName;

    // Default constructor required by JPA
    public Partner() {}

    // Constructor with validation
    public Partner(String partnerName) {
        this.partnerName = partnerName;
    }

    // Getters and setters
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartnerName() {
        return this.partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    // Equals and hashCode methods based on ID
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Partner)) {
            return false;
        }
        return getId() != null && getId().equals(((Partner) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // toString method
    @Override
    public String toString() {
        return "Partner{" + "id=" + id + ", partnerName='" + partnerName + '\'' + '}';
    }
}
