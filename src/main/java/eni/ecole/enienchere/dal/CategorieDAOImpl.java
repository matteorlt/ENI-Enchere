package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Categorie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategorieDAOImpl implements CategorieDAO {

    private final JdbcTemplate jdbcTemplate;
    private final CategorieRowMapper categorieRowMapper;

    public CategorieDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.categorieRowMapper = new CategorieRowMapper();
    }

    @Override
    public List<Categorie> findAll() {
        String sql = "SELECT * FROM CATEGORIES";
        return jdbcTemplate.query(sql, categorieRowMapper);
    }

    @Override
    public Categorie findById(long id) {
        String sql = "SELECT * FROM CATEGORIES WHERE no_categorie = ?";
        return jdbcTemplate.queryForObject(sql, categorieRowMapper, id);
    }

    @Override
    public void insert(Categorie categorie) {
        String sql = "INSERT INTO CATEGORIES (libelle) VALUES (?)";
        jdbcTemplate.update(sql, categorie.getLibelle());
    }

    @Override
    public void update(Categorie categorie) {
        String sql = "UPDATE CATEGORIES SET libelle = ? WHERE no_categorie = ?";
        jdbcTemplate.update(sql, categorie.getLibelle(), categorie.getNo_categorie());
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM CATEGORIES WHERE no_categorie = ?";
        jdbcTemplate.update(sql, id);
    }
}

class CategorieRowMapper implements RowMapper<Categorie> {
    @Override
    public Categorie mapRow(ResultSet rs, int rowNum) throws SQLException {
        Categorie categorie = new Categorie();
        categorie.setNo_categorie(rs.getLong("no_categorie"));
        categorie.setLibelle(rs.getString("libelle"));
        return categorie;
    }
} 