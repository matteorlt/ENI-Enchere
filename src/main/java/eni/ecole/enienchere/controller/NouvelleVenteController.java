package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
import eni.ecole.enienchere.bll.CategorieService;
import eni.ecole.enienchere.bo.ArticleAVendre;
import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/vendre")public class NouvelleVenteController {

    private final ArticleService articleService;
    private final CategorieService categorieService;

    @Autowired

    public NouvelleVenteController(ArticleService articleService, CategorieService categorieService) {
        this.articleService = articleService;
    }   this.categorieService = categorieService;    }

@GetMapping
public String afficherFormulaire(Model model) {

    model.addAttribute("categories", categorieService.getAllCategories());
    return "view-nouvelle-vente";    }

@PostMapping
public String creerVente(@RequestParam String nomArticle,
                         @RequestParam String description,
                         @RequestParam int categorieId, @RequestParam int miseAPrix,
                         @RequestParam LocalDateTime dateDebut,
                         @RequestParam LocalDateTime dateFin, HttpSession session, RedirectAttributes redirectAttributes) {

    Utilisateur vendeur = (Utilisateur) session.getAttribute("user");

    if (vendeur == null) {
        redirectAttributes.addFlashAttribute("error", "Vous devez être connecté pour vendre un article");
        return "redirect:/login";        }

    try {            ArticleAVendre article = new ArticleAVendre();

        article.setNomArticle(nomArticle);
        article.setDescription(description);
        article.setPrixInitial(miseAPrix);
        article.setDateDebutEncheres(dateDebut);
        article.setDateFinEncheres(dateFin);
        article.setVendeur(vendeur);
        article.setCategorie(new Categorie(categorieId));

        article.setStatut(0); // Non débuté
        // articleService.validateArticle(article);
        // articleService.saveArticle(article);
        // redirectAttributes.addFlashAttribute("success", "Votre article a été mis en vente avec succès");
        //
        // return "redirect:/articles";        } catch (IllegalArgumentException e) {
        // redirectAttributes.addFlashAttribute("error", e.getMessage());            return "redirect:/vendre";
        // }    }}





