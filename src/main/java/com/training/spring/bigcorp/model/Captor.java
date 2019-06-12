package com.training.spring.bigcorp.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="CAPTOR")
public class Captor {
    /**
     * Captor id
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Captor name
     */
    @Column(name="name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="site_id", nullable = false)
    private Site site;

    /**
     * Source type to measure
     * @see PowerSource
      */
    @Column(name="power_source", nullable = false)
    @Enumerated(EnumType.STRING)
    public PowerSource powerSource;

    @Column(name="default_power_in_watt")
    public Integer defaultPowerInWatt;

    public Captor() {
    }

    /**
     * Constructor to use with required property
     * @param name
     */
    public Captor(String name, Site site) {
        this.name = name;
        this.site = site;
        this.powerSource = PowerSource.SIMULATED;
        this.defaultPowerInWatt = 1000000;
    }
    public Captor(String name, Site site, PowerSource powerSource) {
        this.name = name;
        this.site = site;
        this.powerSource = powerSource;
        this.defaultPowerInWatt = 1000000;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public PowerSource getPowerSource() {
        return powerSource;
    }

    public void setPowerSource(PowerSource powerSource) {
        this.powerSource = powerSource;
    }

    public int getDefaultPowerInWatt() {
        return defaultPowerInWatt;
    }

    public void setDefaultPowerInWatt(int defaultPowerInWatt) {
        defaultPowerInWatt = defaultPowerInWatt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Captor site = (Captor) o;
        return Objects.equals(name, site.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, site, powerSource, defaultPowerInWatt);
    }

    @Override
    public String toString() {
        return "Captor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
