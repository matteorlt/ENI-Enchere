package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO{
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String FIND_BY_PSEUDO = "SELECT pseudo, nom, prenom, email, telephone, credit, administrateur, no_adresse from UTILISATEURS WHERE pseudo = :pseudo";
    private final static String UPDATE = "UPDATE UTILISATEURS SET nom=:nom, prenom=:prenom, email=:email, telephone=:telephone, mot_de_passe=:mot_de_passe, no_adresse=:no_adresse WHERE pseudo = :pseudo";
    private final static String UPDATE_CREDIT = "UPDATE UTILISATEURS SET credit = :credit WHERE pseudo = :pseudo";
    private final static String INSERT = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, mot_de_passe, credit, no_adresse ) values (:pseudo, :nom, :prenom, :email, :telephone, :mot_de_passe, :credit, :no_adresse )";
    private final static String DELETE = "DELETE FROM UTILISATEURS where pseudo=:pseudo";
    private final static String SELECT_ALL = "SELECT * FROM UTILISATEURS";

    @Override
    public String create(Utilisateur utilisateur) {

        var namedParameters = new MapSqlParameterSource();

        namedParameters.addValue("pseudo", utilisateur.getPseudo());
        namedParameters.addValue("nom", utilisateur.getNom());
        namedParameters.addValue("prenom", utilisateur.getPrenom());
        namedParameters.addValue("email", utilisateur.getEmail());
        namedParameters.addValue("telephone", utilisateur.getTelephone());
        namedParameters.addValue("mot_de_passe", utilisateur.getMot_de_passe());
        namedParameters.addValue("credit", utilisateur.getCredit());
        namedParameters.addValue("no_adresse", utilisateur.getAdresse().getNo_adresse());

        namedParameterJdbcTemplate.update(INSERT, namedParameters);
        return utilisateur.getPseudo();


    }

    @Override
    public Utilisateur read(String pseudo) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", pseudo);

        return namedParameterJdbcTemplate.queryForObject(FIND_BY_PSEUDO, namedParameters, new UtilisateurRowMapper());
    }

    @Override
    public List<Utilisateur> readAll() {
        return namedParameterJdbcTemplate.query(SELECT_ALL, new UtilisateurRowMapper());
    }



    @Override
    public void update(Utilisateur utilisateur) {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", utilisateur.getPseudo());
        namedParameters.addValue("nom", utilisateur.getNom());
        namedParameters.addValue("prenom", utilisateur.getPrenom());
        namedParameters.addValue("email", utilisateur.getEmail());
        namedParameters.addValue("telephone", utilisateur.getTelephone());
        namedParameters.addValue("no_adresse", utilisateur.getAdresse().getNo_adresse());
        namedParameters.addValue("mot_de_passe", utilisateur.getMot_de_passe());

        namedParameterJdbcTemplate.update(UPDATE, namedParameters);
    }

    @Override
    public void delete(String pseudo) {
        var namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", pseudo);

        namedParameterJdbcTemplate.update(DELETE, namedParameters);

    }

    @Override
    public void updateCredit(String pseudo, int nouveauCredit) {
        var namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", pseudo);
        namedParameters.addValue("credit", nouveauCredit);

        namedParameterJdbcTemplate.update(UPDATE_CREDIT, namedParameters);
    }
}
