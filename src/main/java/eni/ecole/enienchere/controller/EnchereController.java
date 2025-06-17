package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
import eni.ecole.enienchere.bll.CategorieService;
import eni.ecole.enienchere.bll.EnchereService;
import eni.ecole.enienchere.bll.UtilisateurService;
import eni.ecole.enienchere.bo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

@Controller
public class EnchereController {

    private EnchereService enchereService;
    private ArticleService articleService;
    private final CategorieService categorieService;
    private UtilisateurService utilisateurService;

    @Autowired
    public EnchereController(ArticleService articleService, CategorieService categorieService, UtilisateurService utilisateurService, EnchereService enchereService) {
        this.articleService = articleService;
        this.categorieService = categorieService;
        this.utilisateurService = utilisateurService;
        this.enchereService = enchereService;
    }

    @GetMapping("/enchere")
    public String afficherEncheres(Model model,
                                   @RequestParam(name = "nom", required = false) String nom,
                                   @RequestParam(name = "categorie", required = false) String categorie,
                                   @RequestParam(name = "mesEncheres", required = false) Boolean mesEncheres,
                                   Principal principal) {
        var categories = categorieService.getAllCategories();
        List<ArticleAVendre> encheres;
        
        if (mesEncheres != null && mesEncheres && principal != null) {
            Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(principal.getName());
            encheres = articleService.getArticlesFiltres(nom, categorie, utilisateur.getPseudo());
        } else {
            encheres = articleService.getArticlesFiltres(nom, categorie, null);
        }
        
        model.addAttribute("encheres", encheres);
        model.addAttribute("categories", categories);
        model.addAttribute("nom", nom);
        model.addAttribute("categorie", categorie);
        model.addAttribute("mesEncheres", mesEncheres);
        model.addAttribute("currentDate", java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return "view-enchere";
    }

    @GetMapping("/details")
    public String getDetail(@RequestParam(name = "no_article", required = true) Integer noArticle, Model model, Principal principal) {
        List<ArticleAVendre> articles = articleService.getArticleById(noArticle);
        if (articles != null && !articles.isEmpty()) {
            ArticleAVendre article = articles.get(0);
            model.addAttribute("article", article);
            
            // Formatage de la date pour JavaScript
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String dateFinFormatted = sdf.format(article.getDate_fin_enchere());
            model.addAttribute("dateFinFormatted", dateFinFormatted);
            
            // Récupérer la meilleure enchère actuelle
            try {
                Enchere meilleureEnchere = enchereService.getHighestBid(noArticle);
                if (meilleureEnchere != null) {
                    model.addAttribute("meilleureOffre", meilleureEnchere.getMontant_enchere());
                    model.addAttribute("montantMinimum", meilleureEnchere.getMontant_enchere() + 1);
                } else {
                    model.addAttribute("meilleureOffre", article.getPrix_initial());
                    model.addAttribute("montantMinimum", article.getPrix_initial() + 1);
                }
            } catch (Exception e) {
                model.addAttribute("meilleureOffre", article.getPrix_initial());
                model.addAttribute("montantMinimum", article.getPrix_initial() + 1);
            }
            
            // Vérifier si l'utilisateur peut enchérir
            if (principal != null) {
                Utilisateur utilisateurConnecte = utilisateurService.consulterUtilisateurParPseudo(principal.getName());
                boolean peutEncherir = !article.getVendeur().getPseudo().equals(utilisateurConnecte.getPseudo());
                model.addAttribute("peutEncherir", peutEncherir);
            } else {
                model.addAttribute("peutEncherir", false);
            }
            
            return "view-detail";
        }
        return "redirect:/";
    }

    @PostMapping("/enchere/soumettre")
    public String soumettreEnchere(@RequestParam("no_article") Integer noArticle,
                                 @RequestParam("montant_propose") Integer montant,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Vous devez être connecté pour enchérir.");
            return "redirect:/details?no_article=" + noArticle;
        }
        
        try {
            // Récupérer l'article
            List<ArticleAVendre> articles = articleService.getArticleById(noArticle);
            if (articles == null || articles.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Article non trouvé.");
                return "redirect:/enchere";
            }
            
            ArticleAVendre article = articles.get(0);
            Utilisateur acquereur = utilisateurService.consulterUtilisateurParPseudo(principal.getName());
            
            // Vérifier que l'utilisateur n'est pas le vendeur
            if (article.getVendeur().getPseudo().equals(acquereur.getPseudo())) {
                redirectAttributes.addFlashAttribute("error", "Vous ne pouvez pas enchérir sur votre propre article.");
                return "redirect:/details?no_article=" + noArticle;
            }
            
            // Vérifier que l'enchère n'est pas terminée
            Date maintenant = new Date();
            if (article.getDate_fin_enchere().before(maintenant)) {
                redirectAttributes.addFlashAttribute("error", "Cette enchère est terminée.");
                return "redirect:/details?no_article=" + noArticle;
            }
            
            // Vérifier le montant minimum
            Enchere meilleureEnchere = null;
            try {
                meilleureEnchere = enchereService.getHighestBid(noArticle);
            } catch (Exception e) {
                // Pas d'enchère existante
            }
            
            int montantMinimum;
            if (meilleureEnchere != null) {
                montantMinimum = meilleureEnchere.getMontant_enchere() + 1;
            } else {
                montantMinimum = article.getPrix_initial() + 1;
            }
            
            if (montant < montantMinimum) {
                redirectAttributes.addFlashAttribute("error", 
                    "Votre enchère doit être d'au moins " + montantMinimum + " points.");
                return "redirect:/details?no_article=" + noArticle;
            }
            
            // Créer l'enchère
            Enchere enchere = new Enchere();
            enchere.setDate_enchere(LocalDateTime.now());
            enchere.setMontant_enchere(montant);
            enchere.setArticleAVendre(article);
            enchere.setAcquereur(acquereur);
            
            enchereService.createEnchere(enchere);
            
            // Mettre à jour le prix de vente de l'article
            article.setPrix_vente(montant);
            articleService.updateArticle(article);
            
            redirectAttributes.addFlashAttribute("success", 
                "Votre enchère de " + montant + " points a été enregistrée avec succès !");
                
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Une erreur est survenue lors de l'enregistrement de votre enchère : " + e.getMessage());
        }
        
        return "redirect:/details?no_article=" + noArticle;
    }

}



