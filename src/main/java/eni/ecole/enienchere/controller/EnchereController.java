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
                                   @RequestParam(name = "statut", required = false) String statut,
                                   @RequestParam(name = "mesEncheres", required = false) Boolean mesEncheres,
                                   @RequestParam(name = "mesEncheresFaites", required = false) Boolean mesEncheresFaites,
                                   Principal principal) {
        var categories = categorieService.getAllCategories();
        List<ArticleAVendre> encheres;
        
        if (principal != null) {
            Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(principal.getName());
            
            if (mesEncheres != null && mesEncheres) {
                // Afficher les articles que l'utilisateur vend
                encheres = articleService.getArticlesFiltres(nom, categorie, utilisateur.getPseudo());
            } else if (mesEncheresFaites != null && mesEncheresFaites) {
                // Afficher les articles sur lesquels l'utilisateur a enchéri
                encheres = articleService.getArticlesAvecEncheresDe(utilisateur.getPseudo());
                
                // Appliquer les filtres supplémentaires si nécessaire
                if (nom != null && !nom.trim().isEmpty()) {
                    encheres = encheres.stream()
                        .filter(a -> a.getNom_article().toLowerCase().contains(nom.toLowerCase()))
                        .collect(java.util.stream.Collectors.toList());
                }
                if (categorie != null && !categorie.trim().isEmpty()) {
                    encheres = encheres.stream()
                        .filter(a -> a.getCategorie().getLibelle().equals(categorie))
                        .collect(java.util.stream.Collectors.toList());
                }
            } else {
                // Afficher toutes les enchères avec filtres
                encheres = articleService.getArticlesFiltres(nom, categorie, null);
            }
        } else {
            // Utilisateur non connecté : afficher toutes les enchères
            encheres = articleService.getArticlesFiltres(nom, categorie, null);
        }
        
        // Filtrer par statut temporel si spécifié
        if (statut != null && !statut.trim().isEmpty()) {
            Date maintenant = new Date();
            
            if ("debut".equals(statut)) {
                // Filtrer les enchères qui n'ont pas encore commencé (date de début dans le futur)
                encheres = encheres.stream()
                    .filter(a -> a.getDate_debut_enchere().after(maintenant))
                    .collect(java.util.stream.Collectors.toList());
            }
            else if ("actif".equals(statut)) {
                // Filtrer les enchères en cours (date de début passée ET date de fin future)
                encheres = encheres.stream()
                    .filter(a -> !a.getDate_debut_enchere().after(maintenant) && a.getDate_fin_enchere().after(maintenant))
                    .collect(java.util.stream.Collectors.toList());
            }
            else if ("termine".equals(statut)) {
                // Filtrer les enchères terminées (date de fin passée)
                encheres = encheres.stream()
                    .filter(a -> !a.getDate_fin_enchere().after(maintenant))
                    .collect(java.util.stream.Collectors.toList());
            }
        }
        
        model.addAttribute("encheres", encheres);
        model.addAttribute("categories", categories);
        model.addAttribute("nom", nom);
        model.addAttribute("categorie", categorie);
        model.addAttribute("statut", statut);
        model.addAttribute("mesEncheres", mesEncheres);
        model.addAttribute("mesEncheresFaites", mesEncheresFaites);
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
                model.addAttribute("creditUtilisateur", utilisateurConnecte.getCredit());
            } else {
                model.addAttribute("peutEncherir", false);
                model.addAttribute("creditUtilisateur", 0);
            }
            
            // Vérifier si l'enchère est terminée et récupérer le gagnant
            Date maintenant = new Date();
            boolean enchereTerminee = article.getDate_fin_enchere().before(maintenant);
            model.addAttribute("enchereTerminee", enchereTerminee);
            
            if (enchereTerminee) {
                // Récupérer la meilleure enchère pour connaître le gagnant
                try {
                    Enchere enchereGagnante = enchereService.getHighestBid(noArticle);
                    if (enchereGagnante != null && enchereGagnante.getAcquereur() != null) {
                        model.addAttribute("gagnant", enchereGagnante.getAcquereur());
                        model.addAttribute("prixFinal", enchereGagnante.getMontant_enchere());
                    } else {
                        // Aucune enchère, l'article n'a pas été vendu
                        model.addAttribute("gagnant", null);
                        model.addAttribute("prixFinal", null);
                    }
                } catch (Exception e) {
                    // Aucune enchère trouvée
                    model.addAttribute("gagnant", null);
                    model.addAttribute("prixFinal", null);
                }
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
            
            // Vérifier que l'utilisateur a suffisamment de crédit
            if (acquereur.getCredit() < montant) {
                redirectAttributes.addFlashAttribute("error", 
                    "Crédit insuffisant. Vous avez " + acquereur.getCredit() + " points, mais l'enchère nécessite " + montant + " points.");
                return "redirect:/details?no_article=" + noArticle;
            }
            
            // Vérifier que l'utilisateur n'a pas déjà la meilleure enchère
            if (meilleureEnchere != null && meilleureEnchere.getAcquereur() != null) {
                if (meilleureEnchere.getAcquereur().getPseudo().equals(acquereur.getPseudo())) {
                    redirectAttributes.addFlashAttribute("error", 
                        "Vous avez déjà la meilleure enchère sur cet article. Vous ne pouvez pas réenchérir.");
                    return "redirect:/details?no_article=" + noArticle;
                }
            }
            
            // Rembourser l'ancien enchérisseur s'il y en a un
            if (meilleureEnchere != null && meilleureEnchere.getAcquereur() != null) {
                String pseudoAncienEncherisseur = meilleureEnchere.getAcquereur().getPseudo();
                // Récupérer les informations complètes et à jour de l'ancien enchérisseur
                Utilisateur ancienEncherisseurComplet = utilisateurService.consulterUtilisateurParPseudo(pseudoAncienEncherisseur);
                if (ancienEncherisseurComplet != null) {
                    int nouveauCreditAncien = ancienEncherisseurComplet.getCredit() + meilleureEnchere.getMontant_enchere();
                    utilisateurService.updateCredit(pseudoAncienEncherisseur, nouveauCreditAncien);
                }
            }
            
            // Débiter le nouveau enchérisseur
            int nouveauCreditAcquereur = acquereur.getCredit() - montant;
            utilisateurService.updateCredit(acquereur.getPseudo(), nouveauCreditAcquereur);
            
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
                "Votre enchère de " + montant + " points a été enregistrée avec succès ! Votre nouveau solde : " + nouveauCreditAcquereur + " points.");
                
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Une erreur est survenue lors de l'enregistrement de votre enchère : " + e.getMessage());
        }
        
        return "redirect:/details?no_article=" + noArticle;
    }

}



