package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdresseDAOImpl implements AdresseDAO{

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AdresseDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    private final static String FIND_BY_ID = "SELECT no_adresse, rue, code_postal, ville, adresse_eni from ADRESSES WHERE no_adresse = :no_adresse";
    private final static String UPDATE = "UPDATE ADRESSE SET rue=:rue, code_postal=:code_postal, ville=:ville, adresse_eni=:adresse_eni WHERE no_adresse = :no_adresse";
    private final static String INSERT = "INSERT INTO ADRESSES (rue, code_postal, ville, adresse_eni) values (:rue, :code_postal, :ville, :adresse_eni)";
    private final static String DELETE = "DELETE FROM ADRESSE where no_adresse=:no_adresse";
    private final static String SELECT_ALL = "SELECT * FROM ADRESSE";
    private final static String FIND_ID = "SELECT no_adresse from ADRESSES WHERE rue = :rue AND code_postal=:code_postal AND ville=:ville  ";


    @Override
    public void create(Adresse adresse) {


        var namedParameters = new MapSqlParameterSource();

        namedParameters.addValue("no_adresse", adresse.getNo_adresse());
        namedParameters.addValue("rue", adresse.getRue());
        namedParameters.addValue("code_postal", adresse.getCode_postal());
        namedParameters.addValue("ville", adresse.getVille());
        namedParameters.addValue("adresse_eni", adresse.isAdresse_eni());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT, namedParameters, keyHolder);
//        var no_adresse4 = namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namedParameters, new BeanPropertyRowMapper<>());
//        System.out.println(no_adresse4);




        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            adresse.setNo_adresse(generatedId.longValue());
        }







    }

    @Override
    public Adresse read(int no_adresse) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("no_adresse", no_adresse);

        return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namedParameters, new AdresseRowMapper());
    }

    @Override
    public List<Adresse> readAll() {
        return List.of();
    }

    @Override
    public void update(Adresse adresse) {
//        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
//        namedParameters.addValue("rue", adresse.getRue());
//        namedParameters.addValue("cp", adresse.getCode_postal());
//        namedParameters.addValue("ville", adresse.getVille());
//
//        namedParameterJdbcTemplate.update(UPDATE, namedParameters);
    }

    @Override
    public void delete(int no_adresse) {

    }
}
