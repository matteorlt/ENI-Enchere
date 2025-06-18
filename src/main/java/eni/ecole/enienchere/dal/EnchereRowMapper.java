package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Enchere;
import eni.ecole.enienchere.bo.Utilisateur;
import eni.ecole.enienchere.bo.Adresse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class EnchereRowMapper implements RowMapper<Enchere> {

    @Override
    public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
        Enchere enchere = new Enchere();
        
        // Propriétés de base de l'enchère
        enchere.setDate_enchere(rs.getTimestamp("date_enchere").toLocalDateTime());
        enchere.setMontant_enchere(rs.getInt("montant_enchere"));
        
        // Récupérer l'utilisateur (acquereur) depuis la jointure
        String pseudo = rs.getString("pseudo");
        if (pseudo != null && !pseudo.isEmpty()) {
            Utilisateur acquereur = new Utilisateur();
            acquereur.setPseudo(pseudo);
            acquereur.setNom(rs.getString("nom"));
            acquereur.setPrenom(rs.getString("prenom"));
            acquereur.setEmail(rs.getString("email"));
            acquereur.setTelephone(rs.getString("telephone"));
            acquereur.setCredit(rs.getInt("credit"));
            acquereur.setAdministrateur(rs.getBoolean("administrateur"));
            
            // Créer l'adresse
            Adresse adresse = new Adresse();
            adresse.setNo_adresse(rs.getLong("no_adresse"));
            acquereur.setAdresse(adresse);
            
            enchere.setAcquereur(acquereur);
        }
        
        return enchere;
    }
}
