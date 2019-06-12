package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Site;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
public class SiteDaoImplTest {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void create() {
        Assertions.assertThat(siteDao.findAll()).hasSize(1);
        siteDao.persist(new Site("Nouveau site"));

        Assertions.assertThat(siteDao.findAll())
                .hasSize(2)
                .extracting(Site::getName)
                .contains("Bigcorp Lyon", "Nouveau site");
    }

    @Test
    public void findById() {
        Site site = siteDao.findById("site1");
        Assertions.assertThat(site.getName()).isEqualTo("Bigcorp Lyon");
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Site site = siteDao.findById("unkwown");
        Assertions.assertThat(site).isNull();
    }

    @Test
    public void findAll() {
        List<Site> sites = siteDao.findAll();
        Assertions.assertThat(sites)
                .hasSize(1)
                .extracting(Site::getName)
                .contains("Bigcorp Lyon");
    }

    @Test
    public void update() {
        Site site = siteDao.findById("site1");
        Assertions.assertThat(site.getName()).isEqualTo("Bigcorp Lyon");

        site.setName("Site updated");
        siteDao.persist(site);

        site = siteDao.findById("site1");
        Assertions.assertThat(site.getName()).isEqualTo("Site updated");

    }

    @Test
    public void deleteById() {
        Site newSite = new Site("Nouveau site");
        siteDao.persist(newSite);
        Assertions.assertThat(siteDao.findById(newSite.getId())).isNotNull();

        siteDao.delete(newSite);
        Assertions.assertThat(siteDao.findById(newSite.getId())).isNull();
    }

    @Test
    public void deleteByIdShouldThrowExceptionWhenIdIsUsedAsForeignKey() {
        Site site = siteDao.findById("site1");
        Assertions.assertThatThrownBy(() -> {
            siteDao.delete(site);
            entityManager.flush();
        }).isExactlyInstanceOf(PersistenceException.class)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    }


}