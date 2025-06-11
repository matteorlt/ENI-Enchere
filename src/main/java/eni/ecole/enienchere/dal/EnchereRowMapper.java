package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Enchere;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class EnchereRowMapper implements RowMapper<Enchere> {

    @Override
    public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
        Enchere enchere = new Enchere();
        
        // Propriétés de base
        enchere.setDate_enchere(rs.getTimestamp("date_enchere").toLocalDateTime());
        enchere.setMontant_enchere(rs.getInt("montant_enchere"));
        
        return enchere;
    }
}
