package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdresseRowMapper implements RowMapper<Adresse> {
    @Override
    public Adresse mapRow(ResultSet rs, int rowNum) throws SQLException {
        var adresse = new Adresse();
        adresse.setNo_adresse(rs.getInt("no_adresse"));
        adresse.setRue(rs.getString("rue"));
        adresse.setCode_postal(rs.getString("code_postal"));
        adresse.setVille(rs.getString("ville"));
        adresse.setAdresse_eni(rs.getBoolean("adresse_eni"));

        return adresse;
    }
}
