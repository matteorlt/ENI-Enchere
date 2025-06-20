package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.UtilisateurService;
import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import eni.ecole.enienchere.dal.AdresseDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;


@Controller
@Validated
public class UtilisateurController {


    protected final Log logger = LogFactory.getLog(getClass());

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final UtilisateurService utilisateurService;

    public UtilisateurController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UtilisateurService utilisateurService, AdresseDAOImpl adresseDAOImpl) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.utilisateurService = utilisateurService;
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
            String pseudoPrincipal = authentication.getName();

            if (pseudo.equals(pseudoPrincipal)) {

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
            String pseudoPrincipal = authentication.getName();

            if (pseudo.equals(pseudoPrincipal)) {

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
            RedirectAttributes redirectAttributes,
            @ModelAttribute Utilisateur utilisateur, Principal principal) {

        var utilisateurAModifier = utilisateurService.consulterUtilisateurParPseudo(principal.getName());

        utilisateurService.modifUtilisateur(utilisateurAModifier, utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(), utilisateur.getTelephone());
        utilisateurService.updateAdresse(utilisateurAModifier, utilisateur.getAdresse().getRue(), utilisateur.getAdresse().getCode_postal(), utilisateur.getAdresse().getVille());
        redirectAttributes.addFlashAttribute("successModifProfilMessage", "Modifications effectuées avec succès");

        return "redirect:/mon-profil?pseudo=" + authentication.getName();
    }

    @GetMapping("/mon-profil/modifier-mot-de-passe")
    public String modifierMdp(
            @RequestParam("pseudo") @NotBlank String pseudo,
            Model model,
            Authentication authentication) {
        if (authentication != null) {
            String pseudoPrincipal = authentication.getName();
            
            if (pseudo.equals(pseudoPrincipal)) {
                try {
                    Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
                    model.addAttribute("utilisateur", utilisateur);
                    return "view-profil-modif-mdp";
                } catch (Exception e) {
                    logger.error("Erreur lors du chargement du formulaire de modification de mot de passe: {",e);
                    return "redirect:/";
                }
            }
        }
        return "redirect:/";
    }

       @PostMapping("/mon-profil/modifier-mot-de-passe")
        public String mettreAJourMdp(
               Authentication authentication,
               Principal principal,
               RedirectAttributes redirectAttributes,
               @RequestParam("password") @NotBlank String ancienMotDePasse,
               @RequestParam("newPassword") @NotBlank String nouveauMotDePasse,
               @RequestParam("confirmNewPassword") @NotBlank String confirmationMotDePasse) {

           var utilisateurAModifier = utilisateurService.consulterUtilisateurParPseudo(principal.getName());

           if (!nouveauMotDePasse.equals(confirmationMotDePasse)) {
               redirectAttributes.addFlashAttribute("errorConfirmMdpMessage", "Le nouveau mot de passe doit être identique à celui entré dans le champ de confirmation");
               return "view-profil-modif-mdp";
           }

           // Vérification de l'ancien mot de passe
           if (!passwordEncoder.matches(ancienMotDePasse, utilisateurAModifier.getMot_de_passe())) {
               redirectAttributes.addFlashAttribute("errorMdpMessage", "Mot de passe incorrect");
               return "view-profil-modif-mdp";
           }

           // Vérification que le nouveau mot de passe est différent de l'ancien
           if (passwordEncoder.matches(nouveauMotDePasse, utilisateurAModifier.getMot_de_passe())) {
               redirectAttributes.addFlashAttribute("errorNewMdpMessage", "Le nouveau mot de passe et l'ancien sont identiques");
               return "view-profil-modif-mdp";
           }

            try {
                // Encodage du nouveau mot de passe
                String nouveauMotDePasseEncode = passwordEncoder.encode(nouveauMotDePasse);

                // Mise à jour en base de données
                utilisateurService.updateMdp(utilisateurAModifier, nouveauMotDePasseEncode);

                redirectAttributes.addFlashAttribute("successModifMdpMessage", "Mot de passe mis à jour avec succès");

                return "redirect:/mon-profil?pseudo=" + authentication.getName();

            } catch (Exception e) {
                logger.error("Erreur lors de la mise à jour du mot de passe pour l'utilisateur: {}",e);
                redirectAttributes.addFlashAttribute("errorModifMdpMessage", "Erreur lors de la mise à jour du mot de passe");
                return "view-profil-modif-mdp";
            }
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


    @GetMapping("/creer-compte")
    public String registerGet(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "view-creer-compte";
    }

    @PostMapping("/creer-compte")
    public String formulaireCreationCompte(@ModelAttribute Utilisateur utilisateur, HttpServletRequest request, RedirectAttributes redirectAttributes, Principal principal) {

        var password = utilisateur.getMot_de_passe();

        utilisateur.setMot_de_passe(passwordEncoder.encode(password));
        try {
            utilisateurService.enregistrerUneAdresse(utilisateur.getAdresse());

            utilisateurService.enregistrerUnUtilisateur(utilisateur);
            try {
                request.login(utilisateur.getUsername(), password);
            } catch (ServletException e) {
                logger.error("Erreur, mot de passe incorrect : {",e);
                return "redirect:/register?error";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorCreationCompte",
                    e.getMessage());
            return "redirect:/creer-compte";
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


}