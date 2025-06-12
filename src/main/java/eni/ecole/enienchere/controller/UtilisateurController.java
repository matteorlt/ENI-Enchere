package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.UtilisateurService;
import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller

@SessionAttributes({"utilisateurConnecte"})
@Validated
public class UtilisateurController {


    protected final Log logger = LogFactory.getLog(getClass());

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final UtilisateurService utilisateurService;

    public UtilisateurController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UtilisateurService utilisateurService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.utilisateurService = utilisateurService;
    }


//    @GetMapping("/profil")
//    public String afficherUnUtilisateur(
//            @RequestParam("pseudo") @NotBlank String pseudo,
//            Model model,
//            @ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte) {
//
//        if (!isUtilisateurConnecte(utilisateurConnecte)) {
//            logger.warn("Tentative d'accès au profil sans être connecté");
//            return "redirect:/utilisateur/connexion";
//        }
//
//        try {
//            Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
//            if (utilisateur == null) {
//                logger.warn("Utilisateur non trouvé avec le pseudo: {}", pseudo);
//                model.addAttribute("errorMessage", "Utilisateur introuvable");
//                return "redirect:/accueil";
//            }
//
//            model.addAttribute("utilisateur", utilisateur);
//            return "view-profil";
//        } catch (Exception e) {
//            logger.error("Erreur lors de la consultation du profil pour le pseudo: {}", pseudo, e);
//            model.addAttribute("errorMessage", "Erreur lors du chargement du profil");
//            return "redirect:/accueil";
//        }
//    }

    @GetMapping("/mon-profil")
    public String afficherProfilUtilisateur(
            @RequestParam("pseudo") @NotBlank String pseudo,
            Model model,
            @ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte) {
        Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
        int no_adresse = (int) utilisateur.getAdresse().getNo_adresse();
        Adresse adresse = utilisateurService.consulterAdresseParId(no_adresse);
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("adresse", adresse);
        return "view-mon-profil";
//        if (!isUtilisateurAutorise(utilisateurConnecte, pseudo)) {
//            logger.warn("Accès non autorisé au profil pour le pseudo: {}", pseudo);
//            return "redirect:/accueil";
//        }
//
//        try {
//            Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
//            model.addAttribute("utilisateur", utilisateur);
//            return "view-mon-profil";
//        } catch (Exception e) {
//            logger.error("Erreur lors du chargement du profil utilisateur: {}", pseudo, e);
//            model.addAttribute("errorMessage", "Erreur lors du chargement du profil");
//            return "redirect:/accueil";
    }


//    @GetMapping("/mon-profil/modifier")
//    public String modifierProfilUtilisateur(
//            @RequestParam("pseudo") @NotBlank String pseudo,
//            Model model,
//            @ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte) {
//
//        if (!isUtilisateurAutorise(utilisateurConnecte, pseudo)) {
//            logger.warn("Tentative de modification non autorisée pour le pseudo: {}", pseudo);
//            return "redirect:/accueil";
//        }
//
//        try {
//            Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
//            model.addAttribute("utilisateur", utilisateur);
//            return "view-profil-modif";
//        } catch (Exception e) {
//            logger.error("Erreur lors du chargement du formulaire de modification: {}", pseudo, e);
//            return "redirect:/accueil";
//        }
//    }
//
//    @PostMapping("/mon-profil/modifier")
//    public String mettreAJourUtilisateur(
//            @RequestParam("pseudo") @NotBlank String pseudo,
//            @ModelAttribute("utilisateurConnecte") @Valid Utilisateur utilisateurConnecte,
//            BindingResult bindingResult,
//            RedirectAttributes redirectAttributes) {
//
//        if (!isUtilisateurAutorise(utilisateurConnecte, pseudo)) {
//            return "redirect:/accueil";
//        }
//
//        if (bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
//            return "redirect:/utilisateur/mon-profil/modifier?pseudo=" + pseudo;
//        }
//
//        try {
//            utilisateurService.update(utilisateurConnecte);
//            logger.info("Profil mis à jour avec succès pour l'utilisateur: {}", pseudo);
//            redirectAttributes.addFlashAttribute("successMessage", "Profil mis à jour avec succès");
//        } catch (Exception e) {
//            logger.error("Erreur lors de la mise à jour du profil: {}", pseudo, e);
//            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour");
//        }
//
//        return "redirect:/utilisateur/mon-profil?pseudo=" + pseudo;
//    }
//
//    @GetMapping("/mon-profil/modifier-mot-de-passe")
//    public String modifierMdp(
//            @RequestParam("pseudo") @NotBlank String pseudo,
//            Model model,
//            @ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte) {
//
//        if (!isUtilisateurAutorise(utilisateurConnecte, pseudo)) {
//            return "redirect:/accueil";
//        }
//
//        try {
//            Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
//            model.addAttribute("utilisateur", utilisateur);
//            return "view-profil-modif-mdp";
//        } catch (Exception e) {
//            logger.error("Erreur lors du chargement du formulaire de modification de mot de passe: {}", pseudo, e);
//            return "redirect:/accueil";
//        }
//    }
//

    /// /    @PostMapping("/mon-profil/modifier-mot-de-passe")
    /// /    public String mettreAJourMdp(
    /// /            @RequestParam("pseudo") @NotBlank String pseudo,
    /// /            @ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte,
    /// /            @RequestParam("mot_de_passe") @NotBlank @Size(min = 8) String motDePasse,
    /// /            RedirectAttributes redirectAttributes) {
    /// /
    /// /        if (!isUtilisateurAutorise(utilisateurConnecte, pseudo)) {
    /// /            return "redirect:/accueil";
    /// /        }
    /// /
    /// /        try {
    /// /            utilisateurService.updateMdp(utilisateurConnecte, motDePasse);
    /// /            logger.info("Mot de passe mis à jour avec succès pour l'utilisateur: {}", pseudo);
    /// /            redirectAttributes.addFlashAttribute("successMessage", "Mot de passe mis à jour avec succès");
    /// /        } catch (Exception e) {
    /// /            logger.error("Erreur lors de la mise à jour du mot de passe: {}", pseudo, e);
    /// /            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour du mot de passe");
    /// /        }
    /// /
    /// /        return "redirect:/utilisateur/mon-profil?pseudo=" + pseudo;
    /// /    }
//
    @GetMapping("/connexion")
    public String connexion() {
        return "view-connexion";
    }


    /// /    @PostMapping("/supprimer")
    /// /    public String supprimerCompte(
    /// /            @RequestParam("pseudo") @NotBlank String pseudo,
    /// /            @ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte,
    /// /            SessionStatus status,
    /// /            RedirectAttributes redirectAttributes) {
    /// /
    /// /        if (!isUtilisateurAutorise(utilisateurConnecte, pseudo)) {
    /// /            return "redirect:/accueil";
    /// /        }
    /// /
    /// /        try {
    /// /            utilisateurService.supprimerUtilisateur(pseudo);
    /// /            status.setComplete(); // Terminer la session après suppression
    /// /            logger.info("Compte supprimé avec succès pour l'utilisateur: {}", pseudo);
    /// /            redirectAttributes.addFlashAttribute("successMessage", "Votre compte a été supprimé avec succès");
    /// /            return "redirect:/accueil";
    /// /        } catch (Exception e) {
    /// /            logger.error("Erreur lors de la suppression du compte: {}", pseudo, e);
    /// /            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression du compte");
    /// /            return "redirect:/utilisateur/mon-profil?pseudo=" + pseudo;
    /// /        }
    /// /    }

    @PostMapping("/creer-compte")
    public String formulaireCreationCompte(@ModelAttribute Utilisateur utilisateur, HttpServletRequest request) {
        var password = utilisateur.getPassword();
        utilisateur.setMot_de_passe(passwordEncoder.encode(password));
        utilisateurService.enregistrerUnUtilisateur(utilisateur);

        try {
            request.login(utilisateur.getUsername(), password);
        } catch (ServletException e) {
            // Gérer l'erreur (par exemple, mot de passe incorrect)
            return "redirect:/register?error";
        }

        // 2. Authentifier automatiquement l'utilisateur
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        utilisateur.getUsername(),
                        password // la c'est le mdp en clair qu'il nous faut
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        return "redirect:/";
    }

    @GetMapping("/creer-compte")
    public String registerGet(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "redirect:/creer-compte";
    }

}



////    @PostMapping("/creer-compte/enregistrer")
////    public String enregistrerCompte(
////            @ModelAttribute("utilisateur") @Valid Utilisateur utilisateur,
////            BindingResult result,
////            Model model,
////            RedirectAttributes redirectAttributes) {
////
////        // Vérification de l'unicité de l'email
////        if (utilisateurService.existeParEmail(utilisateur.getEmail())) {
////            result.rejectValue("email", "email.exists", "Cet email est déjà utilisé");
////        }
////
////        // Vérification de l'unicité du pseudo
////        if (utilisateurService.existeParPseudo(utilisateur.getPseudo())) {
////            result.rejectValue("pseudo", "pseudo.exists", "Ce pseudo est déjà utilisé");
////        }
////
////        if (result.hasErrors()) {
////            model.addAttribute("utilisateur", utilisateur);
////            return "view-creer-compte";
////        }
////
////        try {
////            utilisateurService.enregistrerUtilisateur(utilisateur);
////            logger.info("Nouveau compte créé avec succès pour l'utilisateur: {}", utilisateur.getPseudo());
////            redirectAttributes.addFlashAttribute("successMessage", "Compte créé avec succès ! Vous pouvez maintenant vous connecter.");
////            return "redirect:/utilisateur/connexion";
////        } catch (Exception e) {
////            logger.error("Erreur lors de la création du compte pour: {}", utilisateur.getPseudo(), e);
////            model.addAttribute("errorMessage", "Erreur lors de la création du compte");
////            model.addAttribute("utilisateur", utilisateur);
////            return "view-creer-compte";
////        }
//    }
//
//    // Méthodes utilitaires privées
//    private boolean isUtilisateurConnecte(Utilisateur utilisateur) {
//        return utilisateur != null && utilisateur.getPseudo() != null && !utilisateur.getPseudo().trim().isEmpty();
//    }
//
//    private boolean isUtilisateurAutorise(Utilisateur utilisateurConnecte, String pseudo) {
//        return isUtilisateurConnecte(utilisateurConnecte) && pseudo.equals(utilisateurConnecte.getPseudo());
//    }
//
//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception e, RedirectAttributes redirectAttributes) {
//        logger.error("Erreur non gérée dans UtilisateurController", e);
//        redirectAttributes.addFlashAttribute("errorMessage", "Une erreur inattendue s'est produite");
//        return "redirect:/accueil";
//    }
