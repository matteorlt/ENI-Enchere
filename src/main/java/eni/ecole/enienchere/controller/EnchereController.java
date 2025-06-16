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

@Controller
public class EnchereController {

    private EnchereService enchereService;
    private ArticleService articleService;
    private final CategorieService categorieService;
    private UtilisateurService utilisateurService;

    @Autowired
    public EnchereController(ArticleService articleService, CategorieService categorieService, UtilisateurService utilisateurService) {
        this.articleService = articleService;
        this.categorieService = categorieService;
        this.utilisateurService = utilisateurService;
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
            encheres = articleService.getArticlesByVendeur(utilisateur.getPseudo());
        } else {
            encheres = articleService.getArticlesFiltres(nom, categorie);
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
    public String getDetail(@RequestParam(name = "no_article", required = true) Integer noArticle, Model model) {
        List<ArticleAVendre> articles = articleService.getArticleById(noArticle);
        if (articles != null && !articles.isEmpty()) {
            model.addAttribute("article", articles.get(0));
            return "view-detail";
        }

        return "redirect:/";
    }

    @PostMapping("/enchere/soumettre")
    public String soumettreEnchere(@RequestParam("no_article") Integer noArticle,
                                 @RequestParam("montant") Integer montant,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            ArticleAVendre article = articleService.getArticleById(noArticle).get(0);
            Utilisateur acquereur = utilisateurService.consulterUtilisateurParPseudo(principal.getName());
            
            Enchere enchere = new Enchere();
            enchere.setDate_enchere(LocalDateTime.now());
            enchere.setMontant_enchere(montant);
            enchere.setArticleAVendre(article);
            enchere.setAcquereur(acquereur);
            
            enchereService.createEnchere(enchere);
            
            redirectAttributes.addFlashAttribute("success", "Votre enchère a été enregistrée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de l'enregistrement de votre enchère.");
        }
        
        return "redirect:/details?no_article=" + noArticle;
    }

}



