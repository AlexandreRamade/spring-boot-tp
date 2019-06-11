package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.Site;
import com.training.spring.bigcorp.utils.H2DateConverter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class MeasureDaoImpl implements MeasureDao{

    private NamedParameterJdbcTemplate jdbcTemplate;
    private H2DateConverter h2DateConverter;

    private static String SELECT_WITH_JOIN = "SELECT m.id, m.instant, m.value_in_watt, m.captor_id, c.name AS captor_name, c.site_id, s.name AS site_name " +
            "FROM MEASURE m INNER JOIN CAPTOR c ON m.captor_id=c.id INNER JOIN SITE s ON c.site_id = s.id ";

    public MeasureDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, H2DateConverter h2DateConverter) {
        this.jdbcTemplate = jdbcTemplate;
        this.h2DateConverter = h2DateConverter;
    }

    @Override
    public void create(Measure measure) {
        jdbcTemplate.update("INSERT INTO MEASURE (id, instant, value_in_watt, captor_id) VALUES (:id, :instant, :value_in_watt, :captor_id)",
                new MapSqlParameterSource().addValue("id", measure.getId())
        .addValue("instant", measure.getInstant())
        .addValue("value_in_watt", measure.getValueInWatt())
        .addValue("captor_id", measure.getCaptor().getId()));
    }

    @Override
    public Measure findById(Long measureId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_WITH_JOIN + "WHERE m.id= :id",
                    new MapSqlParameterSource("id", measureId),
                    this::measureMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Measure> findAll() {
        return jdbcTemplate.query(SELECT_WITH_JOIN, this::measureMapper);
    }

    @Override
    public void update(Measure measure) {
        jdbcTemplate.update("UPDATE MEASURE SET instant= :instant, value_in_watt = :value_in_watt WHERE id= :id",
                new MapSqlParameterSource().addValue("instant", measure.getInstant())
        .addValue("value_in_watt", measure.getValueInWatt())
        .addValue("id", measure.getId()));
    }

    @Override
    public void deleteById(Long measureId) {
        jdbcTemplate.update("DELETE FROM MEASURE WHERE id= :id", new MapSqlParameterSource("id", measureId));
    }

    private Measure measureMapper(ResultSet rs, int rowNum) throws SQLException {
        Site site = new Site(rs.getString("site_name"));
        site.setId(rs.getString("site_id"));

        Captor captor = new Captor(rs.getString("captor_name"), site);
        captor.setId(rs.getString("captor_id"));

        Measure measure = new Measure(h2DateConverter.convert(rs.getString("instant")),
                rs.getInt("value_in_watt"),
                captor);
        measure.setId(rs.getLong("id"));

        return measure;
    }


}
