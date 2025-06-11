package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Enchere;

import java.util.Date;
import java.util.List;

public interface EnchereService {
    List<Enchere> getAllEncheres();
    List<Enchere> getEncheresByArticle(Integer articleId);
    List<Enchere> getEncheresByUser(Integer userId);
    Enchere getHighestBid(Integer articleId);
    void createEnchere(Enchere enchere);
    void updateEnchere(Enchere enchere);
    void deleteEnchere(Enchere enchere);
    boolean validateEnchere(Enchere enchere);

    void edit(Enchere enchere);

    List<Enchere> getByMontant(Integer montant);

    List<Enchere> getByDate_enchere(Date date_enchere);

    void delete(Integer id);
}
