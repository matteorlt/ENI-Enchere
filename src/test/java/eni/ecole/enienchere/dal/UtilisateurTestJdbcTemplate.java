package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @Test
    public void TestUpdateUtilisateur(){
        var utilisateur = new Utilisateur();
        var adresse = new Adresse();
        adresse.setNo_adresse(2);
        utilisateur.setPseudo("test5_admin");
        utilisateur.setNom("Alex");
        utilisateur.setPrenom("René");
        utilisateur.setEmail("alexrene@gmail.com");
        utilisateur.setTelephone("0653369874");
        utilisateur.setMot_de_passe("password");
        utilisateur.setAdresse(adresse);

        String pseudo = utilisateurDAO.create(utilisateur);

        utilisateur.setPseudo(pseudo);
        utilisateur.setNom("Alex");
        utilisateur.setPrenom("Gérard");

        utilisateur.setEmail("alexrene@gmail.com");
        utilisateur.setTelephone("0653369874");
        utilisateur.setAdresse(adresse);

        utilisateurDAO.update(utilisateur);

        Utilisateur utilisateurUpdate = utilisateurDAO.read(pseudo);

        assertEquals("Gérard",utilisateurUpdate.getPrenom(),"Le prénom dévrait être Gérard");

    }

    @Test
    public void TestDeleteUtilisteur(){

        var pseudo = "test4_admin";
        utilisateurDAO.delete(pseudo);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            utilisateurDAO.read(pseudo);
        }, "La lecture de l'utilisateur supprimé devrait lever une exception EmptyResultDataAccessException");

    }



}
