package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
import eni.ecole.enienchere.bll.PhotoService;
import eni.ecole.enienchere.bo.ArticleAVendre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class AjoutPhotoController {

    private final ArticleService articleService;
    private final PhotoService photoService;

    @Autowired
    public AjoutPhotoController(ArticleService articleService, PhotoService photoService) {
        this.articleService = articleService;
        this.photoService = photoService;
    }

    @GetMapping("/accueil-connecte")
    public String accueilConnecte() {
        return "accueilConnecte";
    }

    @GetMapping("/ajouter-photo")
    public String afficherAjoutPhoto(Model model, Principal principal) {
        if (principal != null) {
            // Récupérer les articles de l'utilisateur connecté
            List<ArticleAVendre> articlesUtilisateur = articleService.getArticlesByVendeur(principal.getName());
            model.addAttribute("articles", articlesUtilisateur);
        }
        model.addAttribute("participant", "coach_toto");
        return "view-ajout-photo";
    }

    @PostMapping("/ajouter-photo")
    public String enregistrerPhoto(@RequestParam("photo") MultipartFile fichier, 
                                 @RequestParam("articleId") Long articleId,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal) {
        try {
            // Vérifier que l'utilisateur est connecté
            if (principal == null) {
                redirectAttributes.addFlashAttribute("erreur", "Vous devez être connecté pour ajouter une photo");
                return "redirect:/connexion";
            }

            // Validation du fichier
            if (!photoService.validatePhotoFile(fichier)) {
                redirectAttributes.addFlashAttribute("erreur", "Le fichier photo n'est pas valide");
                return "redirect:/ajouter-photo";
            }
            
            // Vérifier que l'article existe et appartient à l'utilisateur
            List<ArticleAVendre> articles = articleService.getArticleById(articleId.intValue());
            if (articles.isEmpty()) {
                redirectAttributes.addFlashAttribute("erreur", "Article non trouvé");
                return "redirect:/ajouter-photo";
            }
            
            ArticleAVendre article = articles.get(0);
            if (!article.getVendeur().getPseudo().equals(principal.getName())) {
                redirectAttributes.addFlashAttribute("erreur", "Vous ne pouvez ajouter une photo qu'à vos propres articles");
                return "redirect:/ajouter-photo";
            }
            
            // Sauvegarder la photo
            String photoPath = photoService.savePhoto(fichier, articleId);
            
            if (photoPath != null) {
                redirectAttributes.addFlashAttribute("succes", "Photo ajoutée avec succès");
                return "redirect:/enchere";
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Erreur lors de l'ajout de la photo");
                return "redirect:/ajouter-photo";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erreur", "Erreur lors de l'ajout de la photo : " + e.getMessage());
            return "redirect:/ajouter-photo";
        }
    }
}
