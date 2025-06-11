package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Utilisateur;

public interface UtilisateurService {
    static void update(Utilisateur utilisateur) {
    }

    Utilisateur consulterUtilisateurParPseudo(String pseudo);

}
