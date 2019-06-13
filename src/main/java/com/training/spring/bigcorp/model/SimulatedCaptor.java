package com.training.spring.bigcorp.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@DiscriminatorValue("SIMULATED")
public class SimulatedCaptor extends Captor {

    @NotNull
    private Integer minPowerInWatt;

    @NotNull
    private Integer maxPowerInWatt;

    @AssertTrue(message = "minPowerInWatt should be less than maxPowerInWatt")
    public boolean isValid() {
        return this.minPowerInWatt <= this.maxPowerInWatt;
    }


    public SimulatedCaptor() {
        super();
    }

    public SimulatedCaptor(String name, Site site, Integer minPowerInWatt, Integer maxPowerInWatt) {
        super(name, site, PowerSource.SIMULATED);
        this.minPowerInWatt = minPowerInWatt;
        this.maxPowerInWatt = maxPowerInWatt;
    }

    @Override
    public String toString() {
        return "SimulatedCaptor{" +
                "minPowerInWatt=" + minPowerInWatt +
                ", maxPowerInWatt=" + maxPowerInWatt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SimulatedCaptor that = (SimulatedCaptor) o;
        return Objects.equals(minPowerInWatt, that.minPowerInWatt) &&
                Objects.equals(maxPowerInWatt, that.maxPowerInWatt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), minPowerInWatt, maxPowerInWatt);
    }

    public Integer getMinPowerInWatt() {
        return minPowerInWatt;
    }

    public void setMinPowerInWatt(Integer minPowerInWatt) {
        this.minPowerInWatt = minPowerInWatt;
    }

    public Integer getMaxPowerInWatt() {
        return maxPowerInWatt;
    }

    public void setMaxPowerInWatt(Integer maxPowerInWatt) {
        this.maxPowerInWatt = maxPowerInWatt;
    }
}
