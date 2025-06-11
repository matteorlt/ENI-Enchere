package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.ArticleAVendre;
import eni.ecole.enienchere.bo.Categorie;
import eni.ecole.enienchere.bo.Utilisateur;
import eni.ecole.enienchere.bo.Adresse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleRowMapper implements RowMapper<ArticleAVendre> {

    @Override
    public ArticleAVendre mapRow(ResultSet rs, int rowNum) throws SQLException {
        ArticleAVendre article = new ArticleAVendre();
        article.setNo_article(rs.getLong("no_article"));
        article.setNom_article(rs.getString("nom_article"));
        article.setDescription(rs.getString("description"));
        article.setDate_debut_enchere(rs.getDate("date_debut_enchere"));
        article.setDate_fin_enchere(rs.getDate("date_fin_enchere"));
        article.setStatut(rs.getInt("statut"));
        article.setPrix_initial(rs.getInt("prix_initial"));
        article.setPrix_vente(rs.getInt("prix_vente"));
        
        // Mapping de la catégorie
        Categorie categorie = new Categorie();
        categorie.setNo_categorie(rs.getInt("no_categorie"));
        categorie.setLibelle(rs.getString("libelle"));
        article.setCategorie(categorie);
        
        // Mapping du vendeur
        Utilisateur vendeur = new Utilisateur();
        vendeur.setPseudo(rs.getString("pseudo"));
        vendeur.setNom(rs.getString("nom"));
        vendeur.setPrenom(rs.getString("prenom"));
        vendeur.setEmail(rs.getString("email"));
        vendeur.setTelephone(rs.getString("telephone"));
        article.setVendeur(vendeur);
        
        // Mapping de l'adresse de retrait
        Adresse adresse = new Adresse();
        adresse.setNo_adresse(rs.getLong("no_adresse"));
        adresse.setRue(rs.getString("rue"));
        adresse.setCode_postal(rs.getString("code_postal"));
        adresse.setVille(rs.getString("ville"));
        article.setAdresse_retrait(adresse);
        
        return article;
    }
}