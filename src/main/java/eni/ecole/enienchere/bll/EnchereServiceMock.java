package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Enchere;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnchereServiceMock implements EnchereService {

    private List<Enchere> encheres = new ArrayList<>();

    @Override
    public List<Enchere> getAllEncheres() {
        return new ArrayList<>(encheres);
    }

    @Override
    public List<Enchere> getEncheresByArticle(Integer articleId) {
        return encheres.stream()
                .filter(e -> e.getArticleAVendre().getNo_article() == articleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Enchere> getEncheresByUser(Integer userId) {
        return encheres.stream()
                .filter(e -> e.getAcquereur().getPseudo().equals(String.valueOf(userId)))
                .collect(Collectors.toList());
    }

    @Override
    public Enchere getHighestBid(Integer articleId) {
        return encheres.stream()
                .filter(e -> e.getArticleAVendre().getNo_article() == articleId)
                .max((e1, e2) -> Integer.compare(e1.getMontant_enchere(), e2.getMontant_enchere()))
                .orElse(null);
    }

    @Override
    public void createEnchere(Enchere enchere) {
        if (validateEnchere(enchere)) {
            encheres.add(enchere);
        } else {
            throw new IllegalArgumentException("L'enchère n'est pas valide");
        }
    }

    @Override
    public void updateEnchere(Enchere enchere) {
        if (validateEnchere(enchere)) {
            encheres.removeIf(e -> 
                e.getArticleAVendre().getNo_article() == enchere.getArticleAVendre().getNo_article() &&
                e.getAcquereur().getPseudo().equals(enchere.getAcquereur().getPseudo())
            );
            encheres.add(enchere);
        } else {
            throw new IllegalArgumentException("L'enchère n'est pas valide");
        }
    }

    @Override
    public void deleteEnchere(Enchere enchere) {
        encheres.removeIf(e -> 
            e.getArticleAVendre().getNo_article() == enchere.getArticleAVendre().getNo_article() &&
            e.getAcquereur().getPseudo().equals(enchere.getAcquereur().getPseudo())
        );
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
