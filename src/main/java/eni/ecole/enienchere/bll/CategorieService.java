package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Categorie;
import java.util.List;

public interface CategorieService {
    List<Categorie> getAllCategories();
    Categorie getCategorieById(long id);
    void createCategorie(Categorie categorie);
    void updateCategorie(Categorie categorie);
    void deleteCategorie(long id);
    boolean validateCategorie(Categorie categorie);
} 