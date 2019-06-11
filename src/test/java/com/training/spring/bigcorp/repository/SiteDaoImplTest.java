package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Site;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@JdbcTest
@ContextConfiguration(classes = {DaoTestConfig.class})
public class SiteDaoImplTest {

    @Autowired
    private SiteDao siteDao;

    @Test
    public void create() {
        Assertions.assertThat(siteDao.findAll()).hasSize(1);
        siteDao.create(new Site("Nouveau site"));

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
        siteDao.update(site);

        site = siteDao.findById("site1");
        Assertions.assertThat(site.getName()).isEqualTo("Site updated");

    }

    @Test
    public void deleteById() {
        Site newSite = new Site("Nouveau site");
        siteDao.create(newSite);
        Assertions.assertThat(siteDao.findById(newSite.getId())).isNotNull();

        siteDao.deleteById(newSite.getId());
        Assertions.assertThat(siteDao.findById(newSite.getId())).isNull();
    }

    @Test
    public void deleteByIdShouldThrowExceptionWhenIdIsUsedAsForeinKey() {
        Assertions.assertThatThrownBy(()-> siteDao.deleteById("site1"))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }


}