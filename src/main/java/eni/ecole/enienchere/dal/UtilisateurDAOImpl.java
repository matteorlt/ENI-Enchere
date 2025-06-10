package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO{
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private String FIND_BY_PSEUDO = "SELECT pseudo, nom, prenom, email, telephone, credit, administrateur, no_adresse from UTILISATEURS WHERE pseudo = :pseudo";


    @Override
    public void create(Utilisateur utilisateur) {

    }

    @Override
    public Utilisateur read(String pseudo) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", pseudo);

        return jdbcTemplate.queryForObject(FIND_BY_PSEUDO, namedParameters, new UtilisateurRowMapper());
    }

    @Override
    public List<Utilisateur> readAll() {
        return List.of();
    }

    @Override
    public Utilisateur update(String pseudo) {
        return null;
    }

    @Override
    public void delete(String pseudo) {

    }
}
