package eni.ecole.enienchere.dal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;

@SpringBootTest
public class UtilisateurTestJdbcTemplate {

    @Autowired
    UtilisateurDAO utilisateurDAO;



    @Test
    public void TestReadUtilisateurByPseudo(){
        var pseudo = "coach_admin";
        var utilisateur = utilisateurDAO.read(pseudo);
        Assert.notNull(utilisateur, "Objet vide test fail");
    }

}
