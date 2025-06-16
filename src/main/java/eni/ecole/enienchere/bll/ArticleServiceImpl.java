package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.ArticleAVendre;
import eni.ecole.enienchere.dal.ArticleDAO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleDAO articleDAO;

    public ArticleServiceImpl(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    @Override
    public List<ArticleAVendre> getAllArticles() {
        return articleDAO.findAll();
    }

    @Override
    public List<ArticleAVendre> getArticleById(Integer articleId) {
        return articleDAO.getId(articleId);
    }

    @Override
    public List<ArticleAVendre> getArticlesByCategorie(Integer categorieId) {
        return articleDAO.findByCategorie(categorieId);
    }

    @Override
    public List<ArticleAVendre> getArticlesByVendeur(String vendeurPseudo) {
        return articleDAO.findByVendeur(vendeurPseudo);
    }

    @Override
    public List<ArticleAVendre> getArticlesByNom(String nom) {
        return articleDAO.findByNom(nom);
    }

    @Override
    public List<ArticleAVendre> getArticlesByPrixInitial(Integer prixInitial) {
        return articleDAO.findByPrixInitial(prixInitial);
    }

    @Override
    public void createArticle(ArticleAVendre article) {
        if (validateArticle(article)) {
            articleDAO.insert(article);
        } else {
            throw new IllegalArgumentException("L'article n'est pas valide");
        }
    }

    @Override
    public void updateArticle(ArticleAVendre article) {
        if (validateArticle(article)) {
            articleDAO.update(article);
        } else {
            throw new IllegalArgumentException("L'article n'est pas valide");
        }
    }

    @Override
    public void deleteArticle(ArticleAVendre article) {
        articleDAO.delete(article);
    }

    @Override
    public boolean validateArticle(ArticleAVendre article) {
        if (article == null) {
            return false;
        }

        // Validation du nom
        if (article.getNom_article() == null || article.getNom_article().trim().isEmpty()) {
            return false;
        }

        // Validation de la description
        if (article.getDescription() == null || article.getDescription().trim().isEmpty()) {
            return false;
        }

        // Validation des dates
        if (article.getDate_debut_enchere() == null || article.getDate_fin_enchere() == null) {
            return false;
        }

        // La date de fin doit être après la date de début
        if (article.getDate_fin_enchere().before(article.getDate_debut_enchere())) {
            return false;
        }

        // La date de début doit être dans le futur
        if (article.getDate_debut_enchere().before(new Date())) {
            return false;
        }

        // Validation des prix
        if (article.getPrix_initial() <= 0) {
            return false;
        }

        // Le prix de vente doit être supérieur ou égal au prix initial
        if (article.getPrix_vente() > 0 && article.getPrix_vente() < article.getPrix_initial()) {
            return false;
        }

        // Validation du vendeur
        if (article.getVendeur() == null) {
            return false;
        }

        // Validation de la catégorie
        if (article.getCategorie() == null) {
            return false;
        }

        return true;
    }

    @Override
    public List<ArticleAVendre> getArticlesFiltres(String nom, String categorie) {
        if ((nom == null || nom.isEmpty()) && (categorie == null || categorie.isEmpty())) {
            return articleDAO.findAll();
        } else {
            return articleDAO.findByNomAndCategorie(nom, categorie);
        }
    }
} 