package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
    private final static String INSERT = "INSERT INTO ADRESSES (no_adresse, rue, code_postal, ville, adresse_eni) values (:no_adresse, :rue, :code_postal, :ville, :adresse_eni)";
    private final static String DELETE = "DELETE FROM ADRESSE where no_adresse=:no_adresse";
    private final static String SELECT_ALL = "SELECT * FROM ADRESSE";


    @Override
    public long create(Adresse adresse) {
        var namedParameters = new MapSqlParameterSource();

        namedParameters.addValue("no_adresse", adresse.getNo_adresse());
        namedParameters.addValue("rue", adresse.getRue());
        namedParameters.addValue("code_postal", adresse.getCode_postal());
        namedParameters.addValue("ville", adresse.getVille());
        namedParameters.addValue("adresse_eni", adresse.isAdresse_eni());


        namedParameterJdbcTemplate.update(INSERT, namedParameters);
        return adresse.getNo_adresse();

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

    }

    @Override
    public void delete(int no_adresse) {

    }
}
