package com.training.spring.bigcorp.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name="MEASURE")
public class Measure {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Moment the measurement was made
     */
    @Column(name="instant", nullable = false)
    private Instant instant;

    /**
     * Value of the measure in Watt
     */
    @Column(name="value_in_watt", nullable = false)
    private Integer valueInWatt;

    /**
     * Captor that made the measurement
     */
    @ManyToOne
    @JoinColumn(name="captor_id", nullable = false)
    private Captor captor;

    public Measure() {
    }

    /**
     * Constructor to use with required property
     * @param instant
     * @param valueInWatt
     * @param captor
     */
    public Measure(Instant instant, Integer valueInWatt, Captor captor) {
        this.instant = instant;
        this.valueInWatt = valueInWatt;
        this.captor = captor;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "instant=" + instant +
                ", valueInWatt=" + valueInWatt +
                ", captor=" + captor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measure measure = (Measure) o;
        return Objects.equals(instant, measure.instant) &&
                Objects.equals(valueInWatt, measure.valueInWatt) &&
                Objects.equals(captor, measure.captor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instant, valueInWatt, captor);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public Integer getValueInWatt() {
        return valueInWatt;
    }

    public void setValueInWatt(Integer valueInWatt) {
        this.valueInWatt = valueInWatt;
    }

    public Captor getCaptor() {
        return captor;
    }

    public void setCaptor(Captor captor) {
        this.captor = captor;
    }
}
