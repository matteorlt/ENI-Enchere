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


    @GetMapping("/profil")
    public String afficherUnUtilisateur(
            @RequestParam("pseudo") @NotBlank String pseudo,
            Model model,
            Authentication authentication) {

        // Vérifier que l'utilisateur est connecté
        if (authentication == null) {
            logger.warn("Tentative d'accès au profil sans être connecté");
            return "redirect:/connexion";
        }

        try {
            Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
            if (utilisateur == null) {
                logger.warn("Utilisateur non trouvé avec le pseudo: " + pseudo);
                model.addAttribute("errorMessage", "Utilisateur introuvable");
                return "redirect:/";
            }

            model.addAttribute("utilisateur", utilisateur);
            return "view-profil";
        } catch (Exception e) {
            logger.error("Erreur lors de la consultation du profil pour le pseudo: " + pseudo, e);
            model.addAttribute("errorMessage", "Erreur lors du chargement du profil");
            return "redirect:/";
        }
    }

    @GetMapping("/mon-profil")
    public String afficherProfilUtilisateur(
            @RequestParam("pseudo") @NotBlank String pseudo,
            Model model, Authentication authentication) {

        if (authentication != null) {
            // Utiliser getName() qui retourne le username (pseudo)
            String pseudoConnecte = authentication.getName();
            
            // Vérifier que l'utilisateur connecté accède à son propre profil
            if (pseudo.equals(pseudoConnecte)) {
                try {
                    Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
                    int no_adresse = (int) utilisateur.getAdresse().getNo_adresse();
                    Adresse adresse = utilisateurService.consulterAdresseParId(no_adresse);
                    model.addAttribute("utilisateur", utilisateur);
                    model.addAttribute("adresse", adresse);
                    return "view-mon-profil";
                } catch (Exception e) {
                    logger.error("Erreur lors du chargement du profil pour: " + pseudo, e);
                    return "redirect:/";
                }
            }
        }
        return "redirect:/";
    }


    @GetMapping("/mon-profil/modifier")
    public String modifierProfilUtilisateur(
            @RequestParam("pseudo") @NotBlank String pseudo,
            Model model,
            Authentication authentication) {

        if (authentication != null) {
            String pseudoConnecte = authentication.getName();
            
            if (pseudo.equals(pseudoConnecte)) {
                try {
                    Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
                    int no_adresse = (int) utilisateur.getAdresse().getNo_adresse();
                    Adresse adresse = utilisateurService.consulterAdresseParId(no_adresse);
                    model.addAttribute("utilisateur", utilisateur);
                    model.addAttribute("adresse", adresse);
                    return "view-profil-modif";
                } catch (Exception e) {
                    logger.error("Erreur lors du chargement du formulaire de modification pour: " + pseudo, e);
                    return "redirect:/";
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping("/mon-profil/modifier")
    public String mettreAJourUtilisateur(

            Authentication authentication,
            @ModelAttribute Utilisateur utilisateur, Principal principal) {

        var utilisateurAModifier = utilisateurService.consulterUtilisateurParPseudo(principal.getName());


        utilisateurService.modifUtilisateur(utilisateurAModifier, utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(), utilisateur.getTelephone());


        return "redirect:/mon-profil?pseudo=" + authentication.getName();
    }

    @GetMapping("/mon-profil/modifier-mot-de-passe")
    public String modifierMdp(
            @RequestParam("pseudo") @NotBlank String pseudo,
            Model model,
            Authentication authentication) {
        if (authentication != null) {
            String pseudoConnecte = authentication.getName();
            
            if (pseudo.equals(pseudoConnecte)) {
                try {
                    Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
                    model.addAttribute("utilisateur", utilisateur);
                    return "view-profil-modif-mdp";
                } catch (Exception e) {
                    logger.error("Erreur lors du chargement du formulaire de modification de mot de passe pour: " + pseudo, e);
                    return "redirect:/";
                }
            }
        }
        return "redirect:/";
    }


       @PostMapping("/mon-profil/modifier-mot-de-passe")
        public String mettreAJourMdp(
               Authentication authentication,
               @ModelAttribute Utilisateur utilisateur, Principal principal,
               RedirectAttributes redirectAttributes,
               @RequestParam("newPassword") @NotBlank String nouveauMotDePasse) {

            try {
                String nouveauMotDePasseEncode = passwordEncoder.encode(nouveauMotDePasse);
                var utilisateurAModifier = utilisateurService.consulterUtilisateurParPseudo(principal.getName());
                utilisateurService.updateMdp(utilisateurAModifier, nouveauMotDePasseEncode);
                redirectAttributes.addFlashAttribute("successMessage", "Mot de passe mis à jour avec succès");

            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour du mot de passe");
            }

            return "redirect:/mon-profil?pseudo=" + authentication.getName();
        }

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
    String login() {
        logger.info("Affichage du formulaire login");


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
    public String formulaireCreationCompte(@ModelAttribute Utilisateur utilisateur, HttpServletRequest request, Principal principal) {

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
