package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Categorie;
import eni.ecole.enienchere.dal.CategorieDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieServiceImpl implements CategorieService {

    private final CategorieDAO categorieDAO;

    public CategorieServiceImpl(CategorieDAO categorieDAO) {
        this.categorieDAO = categorieDAO;
    }

    @Override
    public List<Categorie> getAllCategories() {
        return categorieDAO.findAll();
    }

    @Override
    public Categorie getCategorieById(long id) {
        return categorieDAO.findById(id);
    }

    @Override
    public void createCategorie(Categorie categorie) {
        if (validateCategorie(categorie)) {
            categorieDAO.insert(categorie);
        } else {
            throw new IllegalArgumentException("La catégorie n'est pas valide");
        }
    }

    @Override
    public void updateCategorie(Categorie categorie) {
        if (validateCategorie(categorie)) {
            categorieDAO.update(categorie);
        } else {
            throw new IllegalArgumentException("La catégorie n'est pas valide");
        }
    }

    @Override
    public void deleteCategorie(long id) {
        categorieDAO.delete(id);
    }

    @Override
    public boolean validateCategorie(Categorie categorie) {
        if (categorie == null) {
            return false;
        }

        // Validation du libellé
        if (categorie.getLibelle() == null || categorie.getLibelle().trim().isEmpty()) {
            return false;
        }

        // Le libellé ne doit pas dépasser 30 caractères
        if (categorie.getLibelle().length() > 30) {
            return false;
        }

        return true;
    }
} 