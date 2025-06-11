package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Enchere;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DaoEnchereImpl implements DaoEnchere {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String SELECT_ALL = "SELECT * FROM ENCHERES";
    private final String SELECT_BY_ARTICLE = "SELECT * FROM ENCHERES WHERE no_article = :no_article ORDER BY montant_enchere DESC";
    private final String SELECT_BY_USER = "SELECT * FROM ENCHERES WHERE id_utilisateur = :id_utilisateur";
    private final String SELECT_HIGHEST_BID = "SELECT * FROM ENCHERES WHERE no_article = :no_article ORDER BY montant_enchere DESC LIMIT 1";
    private final String INSERT = "INSERT INTO ENCHERES (id_utilisateur, no_article, montant_enchere, date_enchere) VALUES (:id_utilisateur, :no_article, :montant_enchere, :date_enchere)";
    private final String UPDATE = "UPDATE ENCHERES SET montant_enchere = :montant_enchere, date_enchere = :date_enchere WHERE id_utilisateur = :id_utilisateur AND no_article = :no_article";
    private final String DELETE = "DELETE FROM ENCHERES WHERE id_utilisateur = :id_utilisateur AND no_article = :no_article AND montant_enchere = :montant_enchere";

    public DaoEnchereImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Enchere> getAll() {
        return jdbcTemplate.query(SELECT_ALL, new EnchereRowMapper());
    }

    @Override
    public List<Enchere> getByArticleId(Integer articleId) {
        var params = new MapSqlParameterSource();
        params.addValue("no_article", articleId);
        return jdbcTemplate.query(SELECT_BY_ARTICLE, params, new EnchereRowMapper());
    }

    @Override
    public List<Enchere> getByUserId(Integer userId) {
        var params = new MapSqlParameterSource();
        params.addValue("id_utilisateur", userId);
        return jdbcTemplate.query(SELECT_BY_USER, params, new EnchereRowMapper());
    }

    @Override
    public Enchere getHighestBid(Integer articleId) {
        var params = new MapSqlParameterSource();
        params.addValue("no_article", articleId);
        return jdbcTemplate.queryForObject(SELECT_HIGHEST_BID, params, new EnchereRowMapper());
    }

    @Override
    public void insert(Enchere enchere) {
        var params = new MapSqlParameterSource();
        params.addValue("id_utilisateur", enchere.getAcquereur().getPseudo());
        params.addValue("no_article", enchere.getArticleAVendre().getNo_article());
        params.addValue("montant_enchere", enchere.getMontant_enchere());
        params.addValue("date_enchere", enchere.getDate_enchere());
        jdbcTemplate.update(INSERT, params);
    }

    @Override
    public void update(Enchere enchere) {
        var params = new MapSqlParameterSource();
        params.addValue("id_utilisateur", enchere.getAcquereur().getPseudo());
        params.addValue("no_article", enchere.getArticleAVendre().getNo_article());
        params.addValue("montant_enchere", enchere.getMontant_enchere());
        params.addValue("date_enchere", enchere.getDate_enchere());
        jdbcTemplate.update(UPDATE, params);
    }

    @Override
    public void delete(Enchere enchere) {
        var params = new MapSqlParameterSource();
        params.addValue("id_utilisateur", enchere.getAcquereur().getPseudo());
        params.addValue("no_article", enchere.getArticleAVendre().getNo_article());
        params.addValue("montant_enchere", enchere.getMontant_enchere());
        jdbcTemplate.update(DELETE, params);
    }
}