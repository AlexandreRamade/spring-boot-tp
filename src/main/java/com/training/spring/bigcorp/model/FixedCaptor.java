package com.training.spring.bigcorp.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@DiscriminatorValue("FIXED")
public class FixedCaptor extends Captor {

    @NotNull
    private Integer defaultPowerInWatt;

    public FixedCaptor() {
        super();
    }

    public FixedCaptor(String name, Site site, Integer defaultPowerInWatt) {
        super(name, site, PowerSource.FIXED);
        this.defaultPowerInWatt = defaultPowerInWatt;
    }

    @Override
    public String toString() {
        return "FixedCaptor{" +
                "defaultPowerInWatt=" + defaultPowerInWatt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FixedCaptor that = (FixedCaptor) o;
        return Objects.equals(defaultPowerInWatt, that.defaultPowerInWatt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), defaultPowerInWatt);
    }

    public Integer getDefaultPowerInWatt() {
        return defaultPowerInWatt;
    }

    public void setDefaultPowerInWatt(Integer defaultPowerInWatt) {
        this.defaultPowerInWatt = defaultPowerInWatt;
    }
}
