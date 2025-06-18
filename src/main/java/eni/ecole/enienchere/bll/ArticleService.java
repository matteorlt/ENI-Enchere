package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.ArticleAVendre;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ArticleService {
    // Méthodes de récupération
    List<ArticleAVendre> getAllArticles();
    List<ArticleAVendre> getArticleById(Integer articleId);
    List<ArticleAVendre> getArticlesByCategorie(Integer categorieId);
    List<ArticleAVendre> getArticlesByVendeur(String vendeurPseudo);
    List<ArticleAVendre> getArticlesByNom(String nom);
    List<ArticleAVendre> getArticlesByPrixInitial(Integer prixInitial);
    List<ArticleAVendre> getArticlesFiltres(String nom, String categorie, String vendeurPseudo);
    List<ArticleAVendre> getArticlesAvecEncheresDe(String pseudoUtilisateur);
    
    // Méthodes de gestion
    void createArticle(ArticleAVendre article);
    void updateArticle(ArticleAVendre article);
    void deleteArticle(ArticleAVendre article);
    boolean deleteArticle(Integer articleId);
    
    // Méthodes de validation
    boolean validateArticle(ArticleAVendre article);
} 