package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.ArticleAVendre;

import java.util.List;

public interface ArticleDAO {
    List<ArticleAVendre> getId(Integer articleId);
    void insert(ArticleAVendre article);
    void update(ArticleAVendre article);
    void delete(ArticleAVendre article);
    List<ArticleAVendre> findAll();
    List<ArticleAVendre> findByCategorie(Integer categorieId);
    List<ArticleAVendre> findByVendeur(String vendeurPseudo);
    List<ArticleAVendre> findByNom(String nom);
    List<ArticleAVendre> findByPrixInitial(Integer prixInitial);
    List<ArticleAVendre> findByNomAndCategorie(String nom, String categorie);
    List<ArticleAVendre> findByNomAndCategorieAndVendeur(String nom, String categorie, String vendeurPseudo);
}
