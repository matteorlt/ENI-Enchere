package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Enchere;

import java.util.List;

public interface DaoEnchere {
    List<Enchere> getAll();
    List<Enchere> getByArticleId(Integer articleId);
    List<Enchere> getByUserId(Integer userId);
    void insert(Enchere enchere);
    void update(Enchere enchere);
    void delete(Enchere enchere);
    Enchere getHighestBid(Integer articleId);
}
