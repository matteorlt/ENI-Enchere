package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.ArticleAVendre;
import eni.ecole.enienchere.bo.Categorie;
import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleDAOImpl implements ArticleDAO {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ArticleRowMapper articleRowMapper;

    @Autowired
    public ArticleDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.articleRowMapper = new ArticleRowMapper();
    }

    @Override
    public List<ArticleAVendre> getId(Integer articleId) {
        String sql = "SELECT a.*, c.libelle, u.pseudo, u.nom, u.prenom, u.email, u.telephone, " +
                "ad.no_adresse, ad.rue, ad.code_postal, ad.ville " +
                "FROM ARTICLES_A_VENDRE a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.id_utilisateur = u.pseudo " +
                "LEFT JOIN ADRESSES ad ON a.no_adresse_retrait = ad.no_adresse " +
                "WHERE a.no_article = ?";
        return jdbcTemplate.query(sql, articleRowMapper, articleId);
    }

    @Override
    public void insert(ArticleAVendre article) {
        String sql = "INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, " +
                "date_fin_encheres, statut_enchere, prix_initial, prix_vente, id_utilisateur, no_categorie, no_adresse_retrait) " +
                "VALUES (:nom, :description, :dateDebut, :dateFin, :statut_enchere, :prixInitial, :prixVente, :vendeur, :categorie, :adresse)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nom", article.getNom_article())
                .addValue("description", article.getDescription())
                .addValue("dateDebut", article.getDate_debut_enchere())
                .addValue("dateFin", article.getDate_fin_enchere())
                .addValue("statut_enchere", article.getStatut())
                .addValue("prixInitial", article.getPrix_initial())
                .addValue("prixVente", article.getPrix_vente())
                .addValue("vendeur", article.getVendeur().getPseudo())
                .addValue("categorie", article.getCategorie().getNo_categorie())
                .addValue("adresse", article.getAdresse_retrait().getNo_adresse());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);
        article.setNo_article(keyHolder.getKey().longValue());
    }

    @Override
    public void update(ArticleAVendre article) {
        String sql = "UPDATE ARTICLES_A_VENDRE SET nom_article = ?, description = ?, " +
                "date_debut_encheres = ?, date_fin_encheres = ?, statut_enchere = ?, prix_initial = ?, " +
                "prix_vente = ?, id_utilisateur = ?, no_categorie = ?, no_adresse_retrait = ? WHERE no_article = ?";

        
        int rowsAffected = jdbcTemplate.update(sql,
                article.getNom_article(),
                article.getDescription(),
                article.getDate_debut_enchere(),
                article.getDate_fin_enchere(),
                article.getStatut(),
                article.getPrix_initial(),
                article.getPrix_vente(),
                article.getVendeur().getPseudo(),
                article.getCategorie().getNo_categorie(),
                article.getAdresse_retrait().getNo_adresse(),
                article.getNo_article());

    }

    @Override
    public void delete(ArticleAVendre article) {
        String sql = "DELETE FROM ARTICLES_A_VENDRE WHERE no_article = ?";
        jdbcTemplate.update(sql, article.getNo_article());
    }

    @Override
    public List<ArticleAVendre> findAll() {
        String sql = "SELECT a.*, c.libelle, u.pseudo, u.nom, u.prenom, u.email, u.telephone, " +
                "ad.no_adresse, ad.rue, ad.code_postal, ad.ville " +
                "FROM ARTICLES_A_VENDRE a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.id_utilisateur = u.pseudo " +
                "LEFT JOIN ADRESSES ad ON a.no_adresse_retrait = ad.no_adresse";
        return jdbcTemplate.query(sql, articleRowMapper);
    }

    @Override
    public List<ArticleAVendre> findByCategorie(Integer categorieId) {
        String sql = "SELECT a.*, c.libelle, u.pseudo, u.nom, u.prenom, u.email, u.telephone, " +
                "ad.no_adresse, ad.rue, ad.code_postal, ad.ville " +
                "FROM ARTICLES_A_VENDRE a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.id_utilisateur = u.pseudo " +
                "LEFT JOIN ADRESSES ad ON a.no_adresse_retrait = ad.no_adresse " +
                "WHERE a.no_categorie = ?";
        return jdbcTemplate.query(sql, articleRowMapper, categorieId);
    }

    @Override
    public List<ArticleAVendre> findByVendeur(String vendeurPseudo) {
        String sql = "SELECT a.*, c.libelle, u.pseudo, u.nom, u.prenom, u.email, u.telephone, " +
                "ad.no_adresse, ad.rue, ad.code_postal, ad.ville " +
                "FROM ARTICLES_A_VENDRE a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.id_utilisateur = u.pseudo " +
                "LEFT JOIN ADRESSES ad ON a.no_adresse_retrait = ad.no_adresse " +
                "WHERE a.id_utilisateur = ?";
        return jdbcTemplate.query(sql, articleRowMapper, vendeurPseudo);
    }

    @Override
    public List<ArticleAVendre> findByNom(String nom) {
        String sql = "SELECT a.*, c.libelle, u.pseudo, u.nom, u.prenom, u.email, u.telephone, " +
                "ad.no_adresse, ad.rue, ad.code_postal, ad.ville " +
                "FROM ARTICLES_A_VENDRE a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.id_utilisateur = u.pseudo " +
                "LEFT JOIN ADRESSES ad ON a.no_adresse_retrait = ad.no_adresse " +
                "WHERE a.nom_article LIKE ?";
        return jdbcTemplate.query(sql, articleRowMapper, "%" + nom + "%");
    }

    @Override
    public List<ArticleAVendre> findByPrixInitial(Integer prixInitial) {
        String sql = "SELECT a.*, c.libelle, u.pseudo, u.nom, u.prenom, u.email, u.telephone, " +
                "ad.no_adresse, ad.rue, ad.code_postal, ad.ville " +
                "FROM ARTICLES_A_VENDRE a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.id_utilisateur = u.pseudo " +
                "LEFT JOIN ADRESSES ad ON a.no_adresse_retrait = ad.no_adresse " +
                "WHERE a.prix_initial = ?";
        return jdbcTemplate.query(sql, articleRowMapper, prixInitial);
    }

    @Override
    public List<ArticleAVendre> findByNomAndCategorie(String nom, String categorie) {
        StringBuilder sql = new StringBuilder("SELECT a.*, c.libelle, u.pseudo, u.nom, u.prenom, u.email, u.telephone, " +
                "ad.no_adresse, ad.rue, ad.code_postal, ad.ville " +
                "FROM ARTICLES_A_VENDRE a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.id_utilisateur = u.pseudo " +
                "LEFT JOIN ADRESSES ad ON a.no_adresse_retrait = ad.no_adresse WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (nom != null && !nom.isEmpty()) {
            sql.append(" AND a.nom_article LIKE :nom");
            params.addValue("nom", "%" + nom + "%");
        }
        if (categorie != null && !categorie.isEmpty()) {
            sql.append(" AND c.libelle = :categorie");
            params.addValue("categorie", categorie);
        }
        return namedParameterJdbcTemplate.query(sql.toString(), params, articleRowMapper);
    }

    @Override
    public List<ArticleAVendre> findByNomAndCategorieAndVendeur(String nom, String categorie, String vendeurPseudo) {
        StringBuilder sql = new StringBuilder("SELECT a.*, c.libelle, u.pseudo, u.nom, u.prenom, u.email, u.telephone, " +
                "ad.no_adresse, ad.rue, ad.code_postal, ad.ville " +
                "FROM ARTICLES_A_VENDRE a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.id_utilisateur = u.pseudo " +
                "LEFT JOIN ADRESSES ad ON a.no_adresse_retrait = ad.no_adresse WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();
        
        if (nom != null && !nom.isEmpty()) {
            sql.append(" AND a.nom_article LIKE :nom");
            params.addValue("nom", "%" + nom + "%");
        }
        if (categorie != null && !categorie.isEmpty()) {
            sql.append(" AND c.libelle = :categorie");
            params.addValue("categorie", categorie);
        }
        if (vendeurPseudo != null && !vendeurPseudo.isEmpty()) {
            sql.append(" AND a.id_utilisateur = :vendeurPseudo");
            params.addValue("vendeurPseudo", vendeurPseudo);
        }
        
        return namedParameterJdbcTemplate.query(sql.toString(), params, articleRowMapper);
    }

    @Override
    public List<ArticleAVendre> findArticlesAvecEncheresDe(String pseudoUtilisateur) {
        String sql = "SELECT DISTINCT a.*, c.libelle, u.pseudo, u.nom, u.prenom, u.email, u.telephone, " +
                "ad.no_adresse, ad.rue, ad.code_postal, ad.ville " +
                "FROM ARTICLES_A_VENDRE a " +
                "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
                "JOIN UTILISATEURS u ON a.id_utilisateur = u.pseudo " +
                "LEFT JOIN ADRESSES ad ON a.no_adresse_retrait = ad.no_adresse " +
                "JOIN ENCHERES e ON a.no_article = e.no_article " +
                "WHERE e.id_utilisateur = ?";
        return jdbcTemplate.query(sql, articleRowMapper, pseudoUtilisateur);
    }

    @Override
    public void updateStatutOnly(Long articleId, int nouveauStatut) {
        String sql = "UPDATE ARTICLES_A_VENDRE SET statut_enchere = ? WHERE no_article = ?";
        int rowsAffected = jdbcTemplate.update(sql, nouveauStatut, articleId);
    }

    @Override
    public int getStatutFromDB(Long articleId) {
        String sql = "SELECT statut_enchere FROM ARTICLES_A_VENDRE WHERE no_article = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, articleId);
    }
}
