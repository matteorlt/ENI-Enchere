package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import eni.ecole.enienchere.dal.AdresseDAO;
import eni.ecole.enienchere.dal.UtilisateurDAO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurDAO utilisateurDAO;
    private final AdresseDAO adresseDAO;

    public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO, AdresseDAO adresseDAO) {
        this.utilisateurDAO = utilisateurDAO;
        this.adresseDAO = adresseDAO;
    }

    @Override
    public Utilisateur consulterUtilisateurParPseudo(String pseudo) {
        var utilisateur = utilisateurDAO.read(pseudo);
        
        // Vérifier que l'utilisateur existe
        if (utilisateur == null) {
            return null;
        }
        
        // Vérifier que l'utilisateur a une adresse associée
        if (utilisateur.getAdresse() == null) {
            return utilisateur; // Retourner l'utilisateur même sans adresse
        }
        
        try {
            // Récupérer les détails complets de l'adresse
            var adresse = adresseDAO.read((int) utilisateur.getAdresse().getNo_adresse());
            if (adresse != null) {
                utilisateur.setAdresse(adresse);
            }
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer complètement
            System.err.println("Erreur lors de la récupération de l'adresse pour l'utilisateur " + pseudo + ": " + e.getMessage());
        }
        
        return utilisateur;
    }

    @Override
    public void update(Utilisateur utilisateur) {
        utilisateurDAO.update(utilisateur);
    }

    @Override
    public Adresse consulterAdresseParId(int no_adresse) {
        return adresseDAO.read(no_adresse);
    }

    @Override
    public void updateMdp(Utilisateur utilisateur, String mot_de_passe) {
        utilisateur.setMot_de_passe(mot_de_passe);
        utilisateurDAO.update(utilisateur);
    }

    @Override
    public void updateAdresse(Utilisateur utilisateur, String rue, String cp, String ville) {
        Adresse adresse = utilisateur.getAdresse();
        adresse.setRue(rue);
        adresse.setCode_postal(cp);
        adresse.setVille(ville);
        adresseDAO.update(adresse);
    }

    @Override
    public void modifUtilisateur(Utilisateur utilisateur, String nom, String prenom, String email, String telephone) {
utilisateur.setNom(nom);
utilisateur.setPrenom(prenom);
utilisateur.setEmail(email);
utilisateur.setTelephone(telephone);
        this.update(utilisateur);
    }


    @Override
    public String enregistrerUnUtilisateur(Utilisateur utilisateur) {
//        Récupère la liste des utilisateurs existants
        var utilisateurs = utilisateurDAO.readAll();
//        Vérifie un éventuel doublon
        var emailExist = utilisateurs.stream().anyMatch(u -> u.getEmail().equals(utilisateur.getEmail()));
        var pseudoExist = utilisateurs.stream().anyMatch(u -> u.getPseudo().equals(utilisateur.getPseudo()));
        if (!pseudoExist && !emailExist) {
            utilisateur.setCredit(10);
            utilisateurDAO.create(utilisateur);

        }
        if (pseudoExist) {
            throw new IllegalArgumentException("Pseudo déjà existant");
        }
        if (emailExist) {
            throw new IllegalArgumentException("Email déjà existant");
        }

        return utilisateur.getPseudo();
    }

    @Override
    public void enregistrerUneAdresse(Adresse adresse) {

        adresseDAO.create(adresse);

    }
    @Override
    public void updateCredit(String pseudo, int nouveauCredit) {
        utilisateurDAO.updateCredit(pseudo, nouveauCredit);
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var utilisateur = utilisateurDAO.read(username);

            if (utilisateur == null)
                throw new UsernameNotFoundException("User not found");

            return utilisateur;
        } catch (Exception e) {
            throw new UsernameNotFoundException("Erreur lors de la récupération de l'utilisateur: " + username, e);
        }


    }

    @Override
    public List<Adresse> getAdressesENI() {
        return adresseDAO.getAdressesENI();
    }
}
