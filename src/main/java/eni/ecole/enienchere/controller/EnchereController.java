package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
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

    @Autowired
    public EnchereController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/encheres")
    public String afficherEncheres(Model model,
                                   @RequestParam(name = "nom", required = false) String nom,
                                   @RequestParam(name = "categorie", required = false) String categorie) {
        List<String> categories = List.of("Ameublement", "Informatique", "Sports&Loisirs", "Vêtements");
        List<ArticleAVendre> encheres = articleService.getArticlesFiltres(nom, categorie);
        model.addAttribute("encheres", encheres);
        model.addAttribute("categories", categories);
        model.addAttribute("nom", nom);
        model.addAttribute("categorie", categorie);
        model.addAttribute("currentDate", java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return "view-enchere";
    }

    @GetMapping("/enchere")
    public String ListEnchereController(Model model) {
        List<ArticleAVendre> encheres = articleService.getAllArticles();
        model.addAttribute("encheres", encheres);
        return "view-enchere";
    }

}



