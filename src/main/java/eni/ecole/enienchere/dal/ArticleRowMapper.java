package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.ArticleAVendre;
import eni.ecole.enienchere.bo.Enchere;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ArticleRowMapper implements RowMapper<ArticleAVendre> {

    @Override
    public ArticleAVendre mapRow(ResultSet rs, int rowNum) throws SQLException {
        ArticleAVendre article = new ArticleAVendre();

        article.setNom_article(rs.getString("nom_article"));
        article.setDescription(rs.getString("description"));
        article.setDate_debut_enchere(rs.getDate("date_debut_enchere"));
        article.setDate_fin_enchere(rs.getDate("date_fin_enchere"));
        article.setStatut(rs.getInt("statut"));
        article.setPrix_initial(rs.getInt("prix_initial"));
        article.setPrix_vente(rs.getInt("prix_vente"));

        return article;
    }
}