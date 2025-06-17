package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
import eni.ecole.enienchere.bll.PhotoService;
import eni.ecole.enienchere.bll.UtilisateurService;
import eni.ecole.enienchere.bo.ArticleAVendre;
import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class AjoutPhotoController {

    private final ArticleService articleService;
    private final PhotoService photoService;
    private final UtilisateurService utilisateurService;

    @Autowired
    public AjoutPhotoController(ArticleService articleService, PhotoService photoService, UtilisateurService utilisateurService) {
        this.articleService = articleService;
        this.photoService = photoService;
        this.utilisateurService = utilisateurService;
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

    @GetMapping("/admin/gestion-encheres")
    public String afficherGestionEncheres(Model model, 
                                          @RequestParam(value = "recherche", required = false) String recherche,
                                          @RequestParam(value = "vendeur", required = false) String vendeur,
                                          @RequestParam(value = "statut", required = false) String statut,
                                          Principal principal) {
        if (principal == null) {
            return "redirect:/connexion";
        }

        try {
            // Vérifier si l'utilisateur est administrateur
            Utilisateur utilisateur = utilisateurService.getUtilisateurByPseudo(principal.getName());
            if (utilisateur == null || !utilisateur.isAdministrateur()) {
                return "redirect:/enchere?erreur=acces_refuse";
            }

            List<ArticleAVendre> articles;
            
            // Récupérer TOUS les articles selon les filtres
            if (recherche != null && !recherche.trim().isEmpty()) {
                articles = articleService.getArticlesByNom(recherche);
            } else if (vendeur != null && !vendeur.trim().isEmpty()) {
                articles = articleService.getArticlesByVendeur(vendeur);
            } else {
                // Récupérer TOUS les articles de la plateforme
                articles = articleService.getAllArticles();
            }
            
            // Filtrer par statut si spécifié
            if (statut != null && !statut.trim().isEmpty()) {
                if ("actif".equals(statut)) {
                    articles = articles.stream()
                        .filter(a -> a.getStatut() == 1)
                        .collect(java.util.stream.Collectors.toList());
                }
                else if ("termine".equals(statut)) {
                    articles = articles.stream()
                        .filter(a -> a.getStatut() == 0)
                        .collect(java.util.stream.Collectors.toList());
                }
                else if ("debut".equals(statut)) {
                    // Filtrer les enchères qui n'ont pas encore commencé (date de début dans le futur)
                    java.util.Date maintenant = new java.util.Date();
                    articles = articles.stream()
                        .filter(a -> a.getDate_debut_enchere().after(maintenant))
                        .collect(java.util.stream.Collectors.toList());
                }
            }
            
            model.addAttribute("articles", articles);
            model.addAttribute("recherche", recherche);
            model.addAttribute("vendeur", vendeur);
            model.addAttribute("statut", statut);
            model.addAttribute("isAdmin", true);
            
            return "view-gestion-encheres";
        } catch (Exception e) {
            model.addAttribute("erreur", "Erreur lors du chargement des enchères : " + e.getMessage());
            return "view-gestion-encheres";
        }
    }

    @PostMapping("/admin/gestion-encheres/modifier/{articleId}")
    public String modifierEnchere(@PathVariable Long articleId,
                                  @RequestParam(value = "nom", required = false) String nom,
                                  @RequestParam(value = "description", required = false) String description,
                                  @RequestParam(value = "prix", required = false) Integer prix,
                                  @RequestParam(value = "statut", required = false) Integer statut,
                                  @RequestParam(value = "photo", required = false) MultipartFile photo,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal) {
        if (principal == null) {
            return "redirect:/connexion";
        }

        try {
            // Vérifier si l'utilisateur est administrateur
            Utilisateur utilisateur = utilisateurService.getUtilisateurByPseudo(principal.getName());
            if (utilisateur == null || !utilisateur.isAdministrateur()) {
                redirectAttributes.addFlashAttribute("erreur", "Accès refusé - Droits administrateur requis");
                return "redirect:/enchere";
            }

            // Vérifier que l'article existe
            List<ArticleAVendre> articles = articleService.getArticleById(articleId.intValue());
            if (articles.isEmpty()) {
                redirectAttributes.addFlashAttribute("erreur", "Article non trouvé");
                return "redirect:/admin/gestion-encheres";
            }

            ArticleAVendre article = articles.get(0);
            boolean modificationEffectuee = false;

            // Modifier les informations de l'article
            if (nom != null && !nom.trim().isEmpty() && !nom.equals(article.getNom_article())) {
                article.setNom_article(nom.trim());
                modificationEffectuee = true;
            }

            if (description != null && !description.trim().isEmpty() && !description.equals(article.getDescription())) {
                article.setDescription(description.trim());
                modificationEffectuee = true;
            }

            if (prix != null && prix > 0 && !prix.equals(article.getPrix_initial())) {
                article.setPrix_initial(prix);
                modificationEffectuee = true;
            }

            if (statut != null && (statut == 0 || statut == 1) && statut != article.getStatut()) {
                article.setStatut(statut);
                modificationEffectuee = true;
            }

            // Sauvegarder les modifications de l'article
            if (modificationEffectuee) {
                articleService.updateArticle(article);
            }

            // Gérer la photo si fournie
            if (photo != null && !photo.isEmpty()) {
                if (photoService.validatePhotoFile(photo)) {
                    // Supprimer l'ancienne photo
                    photoService.deletePhoto(articleId);
                    // Ajouter la nouvelle photo
                    String photoPath = photoService.savePhoto(photo, articleId);
                    if (photoPath != null) {
                        modificationEffectuee = true;
                    }
                } else {
                    redirectAttributes.addFlashAttribute("erreur", "Fichier photo non valide");
                    return "redirect:/admin/gestion-encheres";
                }
            }

            if (modificationEffectuee) {
                redirectAttributes.addFlashAttribute("succes", 
                    "Enchère modifiée avec succès : " + article.getNom_article() + 
                    " (Vendeur: " + article.getVendeur().getPseudo() + ")");
            } else {
                redirectAttributes.addFlashAttribute("info", "Aucune modification détectée");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", "Erreur lors de la modification : " + e.getMessage());
        }

        return "redirect:/admin/gestion-encheres";
    }

    @PostMapping("/admin/gestion-encheres/supprimer/{articleId}")
    public String supprimerEnchere(@PathVariable Long articleId,
                                   RedirectAttributes redirectAttributes,
                                   Principal principal) {
        if (principal == null) {
            return "redirect:/connexion";
        }

        try {
            // Vérifier si l'utilisateur est administrateur
            Utilisateur utilisateur = utilisateurService.getUtilisateurByPseudo(principal.getName());
            if (utilisateur == null || !utilisateur.isAdministrateur()) {
                redirectAttributes.addFlashAttribute("erreur", "Accès refusé - Droits administrateur requis");
                return "redirect:/enchere";
            }

            // Vérifier que l'article existe
            List<ArticleAVendre> articles = articleService.getArticleById(articleId.intValue());
            if (articles.isEmpty()) {
                redirectAttributes.addFlashAttribute("erreur", "Article non trouvé");
                return "redirect:/admin/gestion-encheres";
            }

            ArticleAVendre article = articles.get(0);

            // Supprimer la photo associée
            photoService.deletePhoto(articleId);

            // Supprimer l'article
            if (articleService.deleteArticle(articleId.intValue())) {
                redirectAttributes.addFlashAttribute("succes", 
                    "Enchère supprimée avec succès : " + article.getNom_article() + 
                    " (Vendeur: " + article.getVendeur().getPseudo() + ")");
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Erreur lors de la suppression de l'enchère");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", "Erreur lors de la suppression : " + e.getMessage());
        }

        return "redirect:/admin/gestion-encheres";
    }
}
