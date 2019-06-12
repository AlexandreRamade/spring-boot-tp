package com.training.spring.bigcorp.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="CAPTOR")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Captor {
    /**
     * Captor id
     */
    @Id
    private String id = UUID.randomUUID().toString();

    @Version
    private Integer version;

    /**
     * Captor name
     */
    @Column(name="name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="site_id", nullable = false)
    private Site site;

    public Captor() {
    }

    /**
     * Constructor to use with required property
     * @param name
     */
    public Captor(String name, Site site) {
        this.name = name;
        this.site = site;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Captor site = (Captor) o;
        return Objects.equals(name, site.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, site);
    }

    @Override
    public String toString() {
        return "Captor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
