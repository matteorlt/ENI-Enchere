package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO{
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final static String FIND_BY_PSEUDO = "SELECT * from UTILISATEURS join ROLES on UTILISATEURS.administrateur=ROLES.IS_ADMIN WHERE pseudo = ?";

    private final static String UPDATE = "UPDATE UTILISATEURS SET nom=:nom, prenom=:prenom, email=:email, telephone=:telephone, mot_de_passe=:mot_de_passe, no_adresse=:no_adresse WHERE pseudo = :pseudo";
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
//         Étape 1 on execute la requete, elle renvoi autant de lignes que de roles
        var rows = jdbcTemplate.queryForList(FIND_BY_PSEUDO, pseudo);



        if (rows.isEmpty())
            return null;

        // Étape 2 : Construit l'objet Personne à partir de la première ligne
        Map<String, Object> firstRow = rows.get(0);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo((String) firstRow.get("pseudo"));
        utilisateur.setNom((String) firstRow.get("nom"));
        utilisateur.setPrenom((String) firstRow.get("prenom"));
        utilisateur.setEmail((String) firstRow.get("email"));
        utilisateur.setTelephone((String) firstRow.get("telephone"));
        utilisateur.setMot_de_passe((String) firstRow.get("mot_de_passe"));
        utilisateur.setCredit((int) firstRow.get("credit"));
        utilisateur.setAdministrateur((boolean) firstRow.get("administrateur"));
        utilisateur.setAdresse(new Adresse());
        utilisateur.getAdresse().setNo_adresse((int) firstRow.get("no_adresse"));




        // Étape 3 : Extrait tous les rôles de toutes les lignes
        var roles = rows.stream()
                .map(row -> (String) row.get("role"))
                .filter(Objects::nonNull) // Filtre les éventuels NULL
                .map(SimpleGrantedAuthority::new).toList();

        utilisateur.setAuthorities(roles);
        return utilisateur;



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
}
