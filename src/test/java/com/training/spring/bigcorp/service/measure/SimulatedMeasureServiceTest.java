package com.training.spring.bigcorp.service.measure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.spring.bigcorp.controller.dto.MeasureDto;
import com.training.spring.bigcorp.model.*;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@RestClientTest(SimulatedMeasureService.class)
public class SimulatedMeasureServiceTest {


    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimulatedMeasureService service;


    /**
     * Captor used in tests
     */
    private SimulatedCaptor captor = new SimulatedCaptor("test", new Site("Florange"), 500_000, 1_000_000);

    /**
     * Start instant used in test
     */
    Instant start = Instant.parse("2018-09-01T22:00:00Z");

    /**
     * End instant used in test
     */
    Instant end = start.plusSeconds(60 * 60 * 24);



    @Test
    public void readMeasures() throws Exception {
        Instant start = Instant.parse("2018-09-01T22:00:00Z");
        Instant end = Instant.parse("2018-09-01T22:30:00Z");
        List<MeasureDto> expectedMeasures = Arrays.asList(
                new MeasureDto(Instant.parse("2018-09-01T22:00:00Z"), 1234),
                new MeasureDto(Instant.parse("2018-09-01T22:30:00Z"), 4567)
        );

        String expectedJson = objectMapper.writeValueAsString(expectedMeasures);
        String request = "http://localhost:8090/measures?start=2018-09-01T22:00:00Z&" +
                "end=2018-09-01T22:30:00Z&min=500000&max=1000000&step=3600";
        this.server.expect(MockRestRequestMatchers.requestTo(request))
                .andRespond(MockRestResponseCreators.withSuccess(expectedJson, MediaType.APPLICATION_JSON));

        List<Measure> measures = service.readMeasures(captor, start, end,
                MeasureStep.ONE_HOUR);

        assertThat(measures).hasSize(2);
        // And we have a value for each hour of the period
        assertThat(measures)
                .extracting("instant", "valueInWatt")
                .containsExactly(
                        Tuple.tuple(Instant.parse("2018-09-01T22:00:00Z"),1234),
                        Tuple.tuple(Instant.parse("2018-09-01T22:30:00Z"),4567)
                );
    }








    @Test
    public void testReadMeasuresThrowsExceptionWhenArgIsNull() {
        assertThatThrownBy(() -> service.readMeasures(null, start, end, MeasureStep.ONE_DAY)).isInstanceOf(IllegalArgumentException.class).hasMessage("captor is required");
        assertThatThrownBy(() -> service.readMeasures(captor, null, end, MeasureStep.ONE_DAY)).isInstanceOf(IllegalArgumentException.class).hasMessage("start is required");
        assertThatThrownBy(() -> service.readMeasures(captor, start, null, MeasureStep.ONE_DAY)).isInstanceOf(IllegalArgumentException.class).hasMessage("end is required");
        assertThatThrownBy(() -> service.readMeasures(captor, start, end, null)).isInstanceOf(IllegalArgumentException.class).hasMessage("step is required");
        assertThatThrownBy(() -> service.readMeasures(captor, end, start, MeasureStep.ONE_DAY)).isInstanceOf(IllegalArgumentException.class).hasMessage("start must be before end");
    }

    /*
    @Test
    public void testReadMeasures() {
        List<Measure> measures = service.readMeasures(captor, start, end, MeasureStep.ONE_HOUR);

        //Wee should have 24 values, one for each hour
        assertThat(measures).hasSize(24);

        //For the moment we have always the same value
        assertThat(measures).extracting(Measure::getValueInWatt).contains(bigCorpApplicationProperties.getMeasure().getDefaultSimulated());

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
    */














}