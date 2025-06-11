package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Enchere;
import eni.ecole.enienchere.dal.DaoEnchere;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EnchereServiceImpl implements EnchereService {

    private final DaoEnchere daoEnchere;

    public EnchereServiceImpl(DaoEnchere daoEnchere) {
        this.daoEnchere = daoEnchere;
    }

    @Override
    public List<Enchere> getAllEncheres() {
        return daoEnchere.getAll();
    }

    @Override
    public List<Enchere> getEncheresByArticle(Integer articleId) {
        return daoEnchere.getByArticleId(articleId);
    }

    @Override
    public List<Enchere> getEncheresByUser(Integer userId) {
        return daoEnchere.getByUserId(userId);
    }

    @Override
    public Enchere getHighestBid(Integer articleId) {
        return daoEnchere.getHighestBid(articleId);
    }

    @Override
    public void createEnchere(Enchere enchere) {
        if (validateEnchere(enchere)) {
            daoEnchere.insert(enchere);
        } else {
            throw new IllegalArgumentException("L'enchère n'est pas valide");
        }
    }

    @Override
    public void updateEnchere(Enchere enchere) {
        if (validateEnchere(enchere)) {
            daoEnchere.update(enchere);
        } else {
            throw new IllegalArgumentException("L'enchère n'est pas valide");
        }
    }

    @Override
    public void deleteEnchere(Enchere enchere) {
        daoEnchere.delete(enchere);
    }

    @Override
    public boolean validateEnchere(Enchere enchere) {
        if (enchere == null) {
            return false;
        }

        // Vérification du montant
        if (enchere.getMontant_enchere() <= 0) {
            return false;
        }

        // Vérification de la date
        if (enchere.getDate_enchere() == null) {
            return false;
        }

        // Vérification de l'article
        if (enchere.getArticleAVendre() == null) {
            return false;
        }

        // Vérification de l'acquéreur
        if (enchere.getAcquereur() == null) {
            return false;
        }

        // Vérification que le montant est supérieur à l'enchère précédente
        Enchere highestBid = getHighestBid((int) enchere.getArticleAVendre().getNo_article());
        if (highestBid != null && enchere.getMontant_enchere() <= highestBid.getMontant_enchere()) {
            return false;
        }

        return true;
    }

    @Override
    public void edit(Enchere enchere) {
        return;
    }

    @Override
    public List<Enchere> getByMontant(Integer montant) {
        return null;
    }

    @Override
    public List<Enchere> getByDate_enchere(Date date_enchere) {
        return null;
    }

    @Override
    public void delete(Integer id) {
        return;
    }
}
