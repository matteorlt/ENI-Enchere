package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Utilisateur;

import java.util.List;

public interface UtilisateurDAO {

String create(Utilisateur utilisateur);
Utilisateur read(String pseudo);
List<Utilisateur> readAll();
void update(Utilisateur utilisateur);
void delete(String pseudo);

}
