package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.UtilisateurService;
import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import eni.ecole.enienchere.dal.AdresseDAOImpl;
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

import java.security.Principal;


@Controller

@SessionAttributes({"utilisateurConnecte"})
@Validated
public class UtilisateurController {


    protected final Log logger = LogFactory.getLog(getClass());

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final UtilisateurService utilisateurService;
    private final AdresseDAOImpl adresseDAOImpl;

    public UtilisateurController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UtilisateurService utilisateurService, AdresseDAOImpl adresseDAOImpl) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.utilisateurService = utilisateurService;
        this.adresseDAOImpl = adresseDAOImpl;
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
            Model model) {
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

    @GetMapping("/mon-profil/modifier")
    public String modifierProfil(
            @RequestParam("pseudo") @NotBlank String pseudo,
            Model model,
            Authentication authentication) {

        if(authentication!=null) {
            var principal = authentication.getPrincipal();

            if (principal instanceof Utilisateur && pseudo.equals(((Utilisateur) principal).getPseudo())) {

                try {
                    Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
                    int no_adresse = (int) utilisateur.getAdresse().getNo_adresse();
                    Adresse adresse = utilisateurService.consulterAdresseParId(no_adresse);
                    model.addAttribute("utilisateur", utilisateur);
                    model.addAttribute("adresse", adresse);
                    return "view-profil-modif";
                } catch (Exception e) {
                    logger.error("Erreur lors du chargement du formulaire de modification: {}");
                    return "redirect:/accueil";
                }
            }
        }
        return "redirect:/accueil";
    }

    @PostMapping("/mon-profil/modifier")
    public String mettreAJourProfil(
            @RequestParam("pseudo") @NotBlank String pseudo,
            @RequestParam("nom") @NotBlank String nom,
            @RequestParam("prenom") @NotBlank String prenom,
            @RequestParam("email") @NotBlank String email,
            @RequestParam("telephone") String telephone,
            @RequestParam("rue") @NotBlank String rue,
            @RequestParam("cp") @NotBlank String cp,
            @RequestParam("ville") @NotBlank String ville,

            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (authentication == null) {
            return "redirect:/";
        }

        var principal = authentication.getPrincipal();

        if (!(principal instanceof Utilisateur) || !pseudo.equals(((Utilisateur) principal).getPseudo())) {
            return "redirect:/";
        }

        Utilisateur utilisateur = (Utilisateur) principal;


        try {
// Mise à jour en base de données
            utilisateurService.updateAdresse(utilisateur.getAdresse(),rue, cp, ville);
            utilisateurService.modifUtilisateur(utilisateur, nom, prenom, email, telephone);

            logger.info("Mot de passe mis à jour avec succès pour l'utilisateur: {}");
            redirectAttributes.addFlashAttribute("successMessage", "Profil mis à jour avec succès");

            return "redirect:/mon-profil?pseudo=" + pseudo;

        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du mot de passe pour l'utilisateur: {}");
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("errorMessage", "Erreur lors de la mise à jour du profil");
            return "view-profil-modif";
        }
    }


    @GetMapping("/mon-profil/modifier-mot-de-passe")
    public String modifierMdp(
            @RequestParam("pseudo") @NotBlank String pseudo,
            Model model,
            Authentication authentication) {

        if(authentication!=null) {
            var principal = authentication.getPrincipal();

            if (principal instanceof Utilisateur && pseudo.equals(((Utilisateur) principal).getPseudo())) {

                try {
                    model.addAttribute("utilisateur", principal);
                    return "view-profil-modif-mdp";
                } catch (Exception e) {
                    logger.error("Erreur lors du chargement du formulaire de modification de mot de passe: {}");
                    return "redirect:/";
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping("/mon-profil/modifier-mot-de-passe")
    public String mettreAJourMdp(
            @RequestParam("pseudo") @NotBlank String pseudo,
            @RequestParam("password") @NotBlank String ancienMotDePasse,
            @RequestParam("newPassword") @NotBlank String nouveauMotDePasse,
            @RequestParam("confirmNewPassword") @NotBlank String confirmationMotDePasse,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (authentication == null) {
            return "redirect:/";
        }

        var principal = authentication.getPrincipal();

        if (!(principal instanceof Utilisateur) || !pseudo.equals(((Utilisateur) principal).getPseudo())) {
            return "redirect:/";
        }

        Utilisateur utilisateur = (Utilisateur) principal;

        if (!nouveauMotDePasse.equals(confirmationMotDePasse)) {
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("errorMessage", "Les nouveaux mots de passe ne correspondent pas");
            return "view-profil-modif-mdp";
        }

        // Vérification de l'ancien mot de passe
        if (!passwordEncoder.matches(ancienMotDePasse, utilisateur.getMot_de_passe())) {
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("errorMessage", "L'ancien mot de passe est incorrect");
            return "view-profil-modif-mdp";
        }

        // Vérification que le nouveau mot de passe est différent de l'ancien
        if (passwordEncoder.matches(nouveauMotDePasse, utilisateur.getMot_de_passe())) {
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("errorMessage", "Le nouveau mot de passe doit être différent de l'ancien");
            return "view-profil-modif-mdp";
        }

        try {
            // Encodage du nouveau mot de passe
            String nouveauMotDePasseEncode = passwordEncoder.encode(nouveauMotDePasse);

            // Mise à jour en base de données
            utilisateurService.updateMdp(utilisateur.getPseudo(), nouveauMotDePasseEncode);

            logger.info("Mot de passe mis à jour avec succès pour l'utilisateur: {}");
            redirectAttributes.addFlashAttribute("successMessage", "Mot de passe mis à jour avec succès");

            return "redirect:/mon-profil?pseudo=" + pseudo;

        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du mot de passe pour l'utilisateur: {}");
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("errorMessage", "Erreur lors de la mise à jour du mot de passe");
            return "view-profil-modif-mdp";
        }
    }
    @GetMapping("/connexion")
    String login(Model model) {
        logger.info("Affichage du formulaire login");
        model.addAttribute("utilisateur", new Utilisateur());

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
        var password = utilisateur.getMot_de_passe();

        utilisateur.setMot_de_passe(passwordEncoder.encode(password));


        utilisateurService.enregistrerUneAdresse(utilisateur.getAdresse());

//        var no_adresse = newAdresse.getNo_adresse();
//        utilisateur.

//        utilisateur.getAdresse().setNo_adresse(adresseEnregistrer.getNo_adresse());
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
        return "view-creer-compte";
    }

}


/// /    @PostMapping("/creer-compte/enregistrer")
/// /    public String enregistrerCompte(
/// /            @ModelAttribute("utilisateur") @Valid Utilisateur utilisateur,
/// /            BindingResult result,
/// /            Model model,
/// /            RedirectAttributes redirectAttributes) {
/// /
/// /        // Vérification de l'unicité de l'email
/// /        if (utilisateurService.existeParEmail(utilisateur.getEmail())) {
/// /            result.rejectValue("email", "email.exists", "Cet email est déjà utilisé");
/// /        }
/// /
/// /        // Vérification de l'unicité du pseudo
/// /        if (utilisateurService.existeParPseudo(utilisateur.getPseudo())) {
/// /            result.rejectValue("pseudo", "pseudo.exists", "Ce pseudo est déjà utilisé");
/// /        }
/// /
/// /        if (result.hasErrors()) {
/// /            model.addAttribute("utilisateur", utilisateur);
/// /            return "view-creer-compte";
/// /        }
/// /
/// /        try {
/// /            utilisateurService.enregistrerUtilisateur(utilisateur);
/// /            logger.info("Nouveau compte créé avec succès pour l'utilisateur: {}", utilisateur.getPseudo());
/// /            redirectAttributes.addFlashAttribute("successMessage", "Compte créé avec succès ! Vous pouvez maintenant vous connecter.");
/// /            return "redirect:/utilisateur/connexion";
/// /        } catch (Exception e) {
/// /            logger.error("Erreur lors de la création du compte pour: {}", utilisateur.getPseudo(), e);
/// /            model.addAttribute("errorMessage", "Erreur lors de la création du compte");
/// /            model.addAttribute("utilisateur", utilisateur);
/// /            return "view-creer-compte";
/// /        }
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
