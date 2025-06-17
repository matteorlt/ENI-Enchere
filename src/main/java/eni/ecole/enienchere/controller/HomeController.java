package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
import eni.ecole.enienchere.bo.ArticleAVendre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/")
    public String getHome() {
        return "home";
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
}


