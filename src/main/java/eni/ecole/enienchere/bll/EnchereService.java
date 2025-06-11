package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Enchere;

import java.util.List;

public interface EnchereService {
    List<Enchere> getAll();
    List<Enchere> getByArticleId(Integer articleId);

}

//public interface MoutonService {
//    Mouton getById(long id);
//
//    List<Mouton> getAll();
//
//    void save(Mouton mouton);
//
//    List<Paturage> getPaturages();
//    Paturage getPaturageById(long id);
//}

//public interface DaoEnchere {
//    List<Enchere> getAll();
//    List<Enchere> getByArticleId(Integer articleId);
//    List<Enchere> getByUserId(Integer userId);
//    void insert(Enchere enchere);
//    void update(Enchere enchere);
//    void delete(Enchere enchere);
//    Enchere getHighestBid(Integer articleId);
//}