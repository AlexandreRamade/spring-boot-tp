package com.training.spring.bigcorp.service.measure;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.MeasureStep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MeasureServiceConfigurationTest.class})
public class SimulatedMeasureServiceTest {

    @Autowired
    private SimulatedMeasureService service;

    /**
     * Captor used in tests
     */
    private Captor captor = new Captor("test");

    /**
     * Start instant used in test
     */
    Instant start = Instant.parse("2018-09-01T22:00:00Z");

    /**
     * End instant used in test
     */
    Instant end = start.plusSeconds(60 * 60 * 24);


    @Test
    public void testReadMeasuresThrowsExceptionWhenArgIsNull() {
        assertThatThrownBy(() -> service.readMeasures(null, start, end, MeasureStep.ONE_DAY)).isInstanceOf(IllegalArgumentException.class).hasMessage("captor is required");
        assertThatThrownBy(() -> service.readMeasures(captor, null, end, MeasureStep.ONE_DAY)).isInstanceOf(IllegalArgumentException.class).hasMessage("start is required");
        assertThatThrownBy(() -> service.readMeasures(captor, start, null, MeasureStep.ONE_DAY)).isInstanceOf(IllegalArgumentException.class).hasMessage("end is required");
        assertThatThrownBy(() -> service.readMeasures(captor, start, end, null)).isInstanceOf(IllegalArgumentException.class).hasMessage("step is required");
        assertThatThrownBy(() -> service.readMeasures(captor, end, start, MeasureStep.ONE_DAY)).isInstanceOf(IllegalArgumentException.class).hasMessage("start must be before end");
    }

    @Test
    public void testReadMeasures() {
        List<Measure> measures = service.readMeasures(captor, start, end, MeasureStep.ONE_HOUR);

        //Wee should have 24 values, one for each hour
        assertThat(measures).hasSize(24);

        //For the moment we have always the same value
        assertThat(measures).extracting(Measure::getValueInWatt).contains(12_000_000);

        //And we have a value for each hour of the period
        assertThat(measures)
                .extracting(Measure::getInstant)
                .extracting(Instant::toString)
                .containsExactly(
                        "2018-09-01T22:00:00Z",
                        "2018-09-01T23:00:00Z",
                        "2018-09-02T00:00:00Z",
                        "2018-09-02T01:00:00Z",
                        "2018-09-02T02:00:00Z",
                        "2018-09-02T03:00:00Z",
                        "2018-09-02T04:00:00Z",
                        "2018-09-02T05:00:00Z",
                        "2018-09-02T06:00:00Z",
                        "2018-09-02T07:00:00Z",
                        "2018-09-02T08:00:00Z",
                        "2018-09-02T09:00:00Z",
                        "2018-09-02T10:00:00Z",
                        "2018-09-02T11:00:00Z",
                        "2018-09-02T12:00:00Z",
                        "2018-09-02T13:00:00Z",
                        "2018-09-02T14:00:00Z",
                        "2018-09-02T15:00:00Z",
                        "2018-09-02T16:00:00Z",
                        "2018-09-02T17:00:00Z",
                        "2018-09-02T18:00:00Z",
                        "2018-09-02T19:00:00Z",
                        "2018-09-02T20:00:00Z",
                        "2018-09-02T21:00:00Z");

    }















}