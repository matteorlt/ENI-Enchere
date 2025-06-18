package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Categorie;
import java.util.List;

public interface CategorieDAO {
    List<Categorie> findAll();
    Categorie findById(long id);
    void insert(Categorie categorie);
    void update(Categorie categorie);
    void delete(long id);
} 