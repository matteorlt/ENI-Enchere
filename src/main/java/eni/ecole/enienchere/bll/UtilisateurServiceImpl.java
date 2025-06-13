package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import eni.ecole.enienchere.dal.AdresseDAO;
import eni.ecole.enienchere.dal.UtilisateurDAO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{

    UtilisateurDAO utilisateurDAO;
    AdresseDAO adresseDAO;

    public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO, AdresseDAO adresseDAO) {
        this.utilisateurDAO = utilisateurDAO;
        this.adresseDAO = adresseDAO;
    }

    @Override
    public Utilisateur consulterUtilisateurParPseudo(String pseudo) {
        var utilisateur = utilisateurDAO.read(pseudo);
        var adresse = adresseDAO.read((int)utilisateur.getAdresse().getNo_adresse());
        utilisateur.setAdresse(adresse);


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
    public Utilisateur authentifier(String pseudo, String motDePasse) {
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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


}
