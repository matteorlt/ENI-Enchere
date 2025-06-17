package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import eni.ecole.enienchere.dal.AdresseDAO;
import eni.ecole.enienchere.dal.UtilisateurDAO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{

    private final UtilisateurDAO utilisateurDAO;
    private final AdresseDAO adresseDAO;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO, AdresseDAO adresseDAO, PasswordEncoder passwordEncoder) {
        this.utilisateurDAO = utilisateurDAO;
        this.adresseDAO = adresseDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Utilisateur consulterUtilisateurParPseudo(String pseudo) {
        if (pseudo == null || pseudo.trim().isEmpty()) {
            return null;
        }
        
        Utilisateur utilisateur = utilisateurDAO.read(pseudo);
        if (utilisateur == null) {
            return null;
        }
        
        if (utilisateur.getAdresse() != null) {
            Long noAdresse = utilisateur.getAdresse().getNo_adresse();
            if (noAdresse != null && noAdresse > 0) {
                Adresse adresse = adresseDAO.read(noAdresse.intValue());
                if (adresse != null) {
                    utilisateur.setAdresse(adresse);
                }
            }
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Utilisateur utilisateur = utilisateurDAO.read(username);
            if (utilisateur == null) {
                throw new UsernameNotFoundException("Utilisateur non trouvé avec le pseudo: " + username);
            }
            
            // Définir les autorisations
            String role = utilisateur.isAdministrateur() ? "ROLE_ADMIN" : "ROLE_USER";
            utilisateur.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(role)));
            
            return utilisateur;
        } catch (Exception e) {
            throw new UsernameNotFoundException("Erreur lors de la recherche de l'utilisateur: " + username, e);
        }
    }

    @Override
    public Utilisateur authentifier(String pseudo, String motDePasse) {
        Utilisateur utilisateur = utilisateurDAO.read(pseudo);
        if (utilisateur != null && passwordEncoder.matches(motDePasse, utilisateur.getMot_de_passe())) {
            return utilisateur;
        }
        return null;
    }

    @Override
    public String enregistrerUnUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.create(utilisateur);
        return utilisateur.getPseudo();
    }

    @Override
    public void enregistrerUneAdresse(Adresse adresse) {
        adresseDAO.create(adresse);
    }

}
