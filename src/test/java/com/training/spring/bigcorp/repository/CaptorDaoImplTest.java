package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Site;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@JdbcTest
@ContextConfiguration(classes = {DaoTestConfig.class})
public class CaptorDaoImplTest {

    @Autowired
    private CaptorDao captorDao;

    private Site site;

    @Before
    public void init() {
        site = new Site("name");
        site.setId("site1");
    }

    @Test
    public void findSiteById() {
        captorDao.create(new Captor("Nouveau captor", site));
        List<Captor> captors = captorDao.findBySiteId(site.getId());
        Assertions.assertThat(captors)
                .hasSize(3)
                .extracting(Captor::getName)
                .contains("Nouveau captor");
    }

    @Test
    public void create() {
        Assertions.assertThat(captorDao.findAll()).hasSize(2);
        captorDao.create(new Captor("New captor", site));

        Assertions.assertThat(captorDao.findAll())
                .hasSize(3)
                .extracting(Captor::getName)
                .contains("Eolienne", "Laminoire à chaud", "New captor");
    }

    @Test
    public void findById() {
        Captor captor = captorDao.findById("c1");
        Assertions.assertThat(captor.getName()).isEqualTo("Eolienne");
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Captor captor = captorDao.findById("unkwown");
        Assertions.assertThat(captor).isNull();
    }

    @Test
    public void findAll() {
        List<Captor> captors = captorDao.findAll();
        Assertions.assertThat(captors)
                .hasSize(2)
                .extracting("id", "name")
                .contains(Tuple.tuple("c1", "Eolienne"))
                .contains(Tuple.tuple("c2", "Laminoire à chaud"));
    }

    @Test
    public void update() {
        Captor captor = captorDao.findById("c1");
        Assertions.assertThat(captor.getName()).isEqualTo("Eolienne");

        captor.setName("Captor updated");
        captorDao.update(captor);

        captor = captorDao.findById("c1");
        Assertions.assertThat(captor.getName()).isEqualTo("Captor updated");

    }

    @Test
    public void deleteById() {
        Captor newCaptor = new Captor("New captor", site);
        captorDao.create(newCaptor);
        Assertions.assertThat(captorDao.findById(newCaptor.getId())).isNotNull();

        captorDao.deleteById(newCaptor.getId());
        Assertions.assertThat(captorDao.findById(newCaptor.getId())).isNull();

    }

    @Test
    public void deleteByIdShouldThrowExceptionWhenIdIsUsedAsForeinKey() {
        Assertions.assertThatThrownBy(()-> captorDao.deleteById("c1"))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }



}