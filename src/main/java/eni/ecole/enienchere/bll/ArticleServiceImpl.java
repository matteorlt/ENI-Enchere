package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.ArticleAVendre;
import eni.ecole.enienchere.dal.ArticleDAO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleDAO articleDAO;

    public ArticleServiceImpl(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    @Override
    public List<ArticleAVendre> getAllArticles() {
        List<ArticleAVendre> articles = articleDAO.findAll();
        // Mettre à jour les statuts de tous les articles
        updateStatutEncheres(articles);
        return articles;
    }

    @Override
    public List<ArticleAVendre> getArticleById(Integer articleId) {
        List<ArticleAVendre> articles = articleDAO.getId(articleId);
        // Mettre à jour le statut de l'article récupéré
        updateStatutEncheres(articles);
        return articles;
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
        if (validateArticleForUpdate(article)) {
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
    public boolean deleteArticle(Integer articleId) {
        try {
            List<ArticleAVendre> articles = getArticleById(articleId);
            if (!articles.isEmpty()) {
                ArticleAVendre article = articles.get(0);
                articleDAO.delete(article);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
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

    /**
     * Validation spécifique pour la mise à jour d'un article existant
     * Ne vérifie pas que la date de début soit dans le futur
     */
    private boolean validateArticleForUpdate(ArticleAVendre article) {
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
    public List<ArticleAVendre> getArticlesFiltres(String nom, String categorie, String vendeurPseudo) {
        List<ArticleAVendre> articles;
        
        if ((nom == null || nom.isEmpty()) && (categorie == null || categorie.isEmpty()) && (vendeurPseudo == null || vendeurPseudo.isEmpty())) {
            articles = articleDAO.findAll();
        } else {
            articles = articleDAO.findByNomAndCategorieAndVendeur(nom, categorie, vendeurPseudo);
        }
        
        // Mettre à jour automatiquement le statut des enchères qui doivent commencer
        updateStatutEncheres(articles);
        
        // Retourner tous les articles (le filtrage se fait dans le contrôleur si nécessaire)
        return articles;
    }
    
    /**
     * Met à jour automatiquement le statut des enchères qui doivent commencer
     */
    private void updateStatutEncheres(List<ArticleAVendre> articles) {
        Date maintenant = new Date();
        
        for (ArticleAVendre article : articles) {
            
            // Si l'enchère était en attente (statut 0) et que sa date de début est arrivée
            if (article.getStatut() == 0 && !article.getDate_debut_enchere().after(maintenant)) {
                try {
                    articleDAO.updateStatutOnly(article.getNo_article(), 1);
                    article.setStatut(1); // Mettre à jour l'objet en mémoire aussi
                } catch (Exception e) {
                    // Log l'erreur mais continue le traitement
                    System.err.println("Erreur lors de la mise à jour du statut de l'article " + article.getNo_article() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            // Si l'enchère est terminée et toujours active
            else if (article.getStatut() == 1 && !article.getDate_fin_enchere().after(maintenant)) {
                try {
                    articleDAO.updateStatutOnly(article.getNo_article(), 2);
                    article.setStatut(2); // Mettre à jour l'objet en mémoire aussi
                } catch (Exception e) {
                    // Log l'erreur mais continue le traitement
                    System.err.println("Erreur lors de la mise à jour du statut de l'article " + article.getNo_article() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
            }
        }
    }

    @Override
    public void forcerMiseAJourStatuts() {
        List<ArticleAVendre> tousLesArticles = articleDAO.findAll();
        updateStatutEncheres(tousLesArticles);
    }

    @Override
    public List<ArticleAVendre> getArticlesAvecEncheresDe(String pseudoUtilisateur) {
        return articleDAO.findArticlesAvecEncheresDe(pseudoUtilisateur);
    }
} 