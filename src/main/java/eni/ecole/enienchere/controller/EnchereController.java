package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
import eni.ecole.enienchere.bll.CategorieService;
import eni.ecole.enienchere.bll.EnchereService;
import eni.ecole.enienchere.bo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    public EnchereController(ArticleService articleService, CategorieService categorieService) {
        this.articleService = articleService;
        this.categorieService = categorieService;
    }

    @GetMapping("/enchere")
    public String afficherEncheres(Model model,
                                   @RequestParam(name = "nom", required = false) String nom,
                                   @RequestParam(name = "categorie", required = false) String categorie) {
        var categories = categorieService.getAllCategories();
        System.out.println("Catégories récupérées : " + categories);
        List<ArticleAVendre> encheres = articleService.getArticlesFiltres(nom, categorie);
        model.addAttribute("encheres", encheres);
        model.addAttribute("categories", categories);
        model.addAttribute("nom", nom);
        model.addAttribute("categorie", categorie);
        model.addAttribute("currentDate", java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return "view-enchere";
    }

}



