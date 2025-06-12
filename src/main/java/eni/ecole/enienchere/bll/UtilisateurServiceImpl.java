package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Utilisateur;
import eni.ecole.enienchere.dal.UtilisateurDAO;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{

    UtilisateurDAO utilisateurDAO;

    public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }



    @Override
    public Utilisateur consulterUtilisateurParPseudo(String pseudo) {
        return utilisateurDAO.read(pseudo);
    }
}
