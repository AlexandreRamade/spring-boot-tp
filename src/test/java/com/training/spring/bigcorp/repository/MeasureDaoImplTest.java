package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.Site;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@JdbcTest
@ContextConfiguration(classes = {DaoTestConfig.class})
public class MeasureDaoImplTest {

    @Autowired
    private MeasureDao measureDao;

    private Site site;
    private Captor captor;
    private Instant now;

    @Before
    public void init() {
        site = new Site("Site test");
        site.setId("site1");

        captor = new Captor("Captor test", site);
        captor.setId("c2");

        now = Instant.parse("2019-06-11T15:00:00Z");
    }

    @Test
    public void create() {
        Assertions.assertThat(measureDao.findAll()).hasSize(10);
        measureDao.create(new Measure(now, 30_000_000, captor));

        Assertions.assertThat(measureDao.findAll())
                .hasSize(11)
                .extracting(Measure::getInstant)
                .contains(now);
    }

    @Test
    public void findById() {
        Measure measure = measureDao.findById(2L);
        Assertions.assertThat(measure.getValueInWatt()).isEqualTo(1_000_124);
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Measure measure = measureDao.findById(-1L);
        Assertions.assertThat(measure).isNull();
    }

    @Test
    public void findAll() {
        List<Measure> measures = measureDao.findAll();
        Assertions.assertThat(measures)
                .hasSize(10)
                .extracting(Measure::getValueInWatt)
                .contains(1_000_124)
                .contains(-900_124);
    }

    @Test
    public void update() {
        Measure measure = measureDao.findById(2L);
        Assertions.assertThat(measure.getValueInWatt()).isEqualTo(1_000_124);

        measure.setValueInWatt(3_000_333);
        measure.setInstant(now);
        measureDao.update(measure);

        measure = measureDao.findById(2L);
        Assertions.assertThat(measure.getValueInWatt()).isEqualTo(3_000_333);
        Assertions.assertThat(measure.getInstant()).isEqualTo(now);

    }

    @Test
    public void deleteById() {
        Measure newMeasure = new Measure(now, 3_000_000, captor);
        newMeasure.setId(11L);

        measureDao.create(newMeasure);
        Assertions.assertThat(measureDao.findById(11L)).isNotNull();

        measureDao.deleteById(11L);
        Assertions.assertThat(measureDao.findById(11L)).isNull();
    }
}