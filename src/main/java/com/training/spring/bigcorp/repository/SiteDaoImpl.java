package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Site;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SiteDaoImpl implements SiteDao {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public SiteDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Site element) {

    }

    @Override
    public Site findById(String s) {
        return null;
    }

    @Override
    public List<Site> findAll() {
        List<Site> sites = new ArrayList<>();
        try(Connection conn = dataSource.getConnection()) {
            String sql = "select id, name from SITE";
            try(ResultSet resultSet = stmt.executeQuery()) {
                while(resultSet.next()) {
                    Site s = new Site(resultSet.getString("name"));
                    s.setId(resultSet.getString("id"));
                    sites.add(s);
                }
            }
        } catch(SQLException e) {
            throw new DatabaseException("impossible de lire les sites", e);
        }
        return sites;
    }

    @Override
    public void update(Site element) {
        jdbcTemplate.update("update SITE set name=:name where id=:id",
                new MapSqlParameterSource().addValue("id", site.getId())).addValue("name", site.getName());
    }

    @Override
    public void deleteById(String s) {

    }
}
