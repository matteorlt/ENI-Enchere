package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.ArticleAVendre;
import eni.ecole.enienchere.bo.Categorie;
import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class ArticleDAOImpl implements ArticleDAO {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ArticleDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<ArticleAVendre> rowMapper = (ResultSet rs, int rowNum) -> {
        ArticleAVendre article = new ArticleAVendre();
        article.setNo_article(rs.getLong("no_article"));
        article.setNom_article(rs.getString("nom_article"));
        article.setDescription(rs.getString("description"));
        article.setDate_debut_enchere(rs.getDate("date_debut_enchere"));
        article.setDate_fin_enchere(rs.getDate("date_fin_enchere"));
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
        article.setVendeur(vendeur);
        
        return article;
    };

    @Override
    public List<ArticleAVendre> getId(Integer articleId) {
        String sql = "SELECT a.*, c.libelle, u.pseudo FROM ARTICLES_VENDUS a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.no_utilisateur = u.no_utilisateur " +
                "WHERE a.no_article = ?";
        return jdbcTemplate.query(sql, rowMapper, articleId);
    }

    @Override
    public void insert(ArticleAVendre article) {
        String sql = "INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_enchere, " +
                "date_fin_enchere, prix_initial, prix_vente, no_utilisateur, no_categorie) " +
                "VALUES (:nom, :description, :dateDebut, :dateFin, :prixInitial, :prixVente, :vendeur, :categorie)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nom", article.getNom_article())
                .addValue("description", article.getDescription())
                .addValue("dateDebut", article.getDate_debut_enchere())
                .addValue("dateFin", article.getDate_fin_enchere())
                .addValue("prixInitial", article.getPrix_initial())
                .addValue("prixVente", article.getPrix_vente())
                .addValue("vendeur", article.getVendeur().getPseudo())
                .addValue("categorie", article.getCategorie().getNo_categorie());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);
        article.setNo_article(keyHolder.getKey().longValue());
    }

    @Override
    public void update(ArticleAVendre article) {
        String sql = "UPDATE ARTICLES_VENDUS SET nom_article = ?, description = ?, " +
                "date_debut_enchere = ?, date_fin_enchere = ?, prix_initial = ?, " +
                "prix_vente = ?, no_utilisateur = ?, no_categorie = ? WHERE no_article = ?";

        jdbcTemplate.update(sql,
                article.getNom_article(),
                article.getDescription(),
                article.getDate_debut_enchere(),
                article.getDate_fin_enchere(),
                article.getPrix_initial(),
                article.getPrix_vente(),
                article.getVendeur().getPseudo(),
                article.getCategorie().getNo_categorie(),
                article.getNo_article());
    }

    @Override
    public void delete(ArticleAVendre article) {
        String sql = "DELETE FROM ARTICLES_VENDUS WHERE no_article = ?";
        jdbcTemplate.update(sql, article.getNo_article());
    }

    @Override
    public List<ArticleAVendre> findAll() {
        String sql = "SELECT a.*, c.libelle, u.pseudo FROM ARTICLES_VENDUS a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.no_utilisateur = u.no_utilisateur";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<ArticleAVendre> findByCategorie(Integer categorieId) {
        String sql = "SELECT a.*, c.libelle, u.pseudo FROM ARTICLES_VENDUS a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.no_utilisateur = u.no_utilisateur " +
                "WHERE a.no_categorie = ?";
        return jdbcTemplate.query(sql, rowMapper, categorieId);
    }

    @Override
    public List<ArticleAVendre> findByVendeur(Long vendeurId) {
        String sql = "SELECT a.*, c.libelle, u.pseudo FROM ARTICLES_VENDUS a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.no_utilisateur = u.no_utilisateur " +
                "WHERE a.no_utilisateur = ?";
        return jdbcTemplate.query(sql, rowMapper, vendeurId);
    }

    @Override
    public List<ArticleAVendre> findByNom(String nom) {
        String sql = "SELECT a.*, c.libelle, u.pseudo FROM ARTICLES_VENDUS a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.no_utilisateur = u.no_utilisateur " +
                "WHERE a.nom_article LIKE ?";
        return jdbcTemplate.query(sql, rowMapper, "%" + nom + "%");
    }

    @Override
    public List<ArticleAVendre> findByPrixInitial(Integer prixInitial) {
        String sql = "SELECT a.*, c.libelle, u.pseudo FROM ARTICLES_VENDUS a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.no_utilisateur = u.no_utilisateur " +
                "WHERE a.prix_initial = ?";
        return jdbcTemplate.query(sql, rowMapper, prixInitial);
    }
}
