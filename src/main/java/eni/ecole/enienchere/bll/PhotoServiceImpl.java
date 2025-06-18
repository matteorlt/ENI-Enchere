package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.dal.PhotoDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoDAO photoDAO;
    
    @Value("${app.photo.max-size:5242880}") // 5MB par défaut
    private long maxFileSize;
    
    @Value("${app.photo.allowed-types:image/jpeg,image/jpg,image/png}")
    private String allowedTypes;

    public PhotoServiceImpl(PhotoDAO photoDAO) {
        this.photoDAO = photoDAO;
    }

    @Override
    public String savePhoto(MultipartFile fichier, Long articleId) {
        // Validation du fichier
        if (!validatePhotoFile(fichier)) {
            throw new IllegalArgumentException("Le fichier photo n'est pas valide");
        }
        
        // Validation de l'ID article
        if (articleId == null || articleId <= 0) {
            throw new IllegalArgumentException("L'ID de l'article n'est pas valide");
        }
        
        try {
            return photoDAO.savePhoto(fichier, articleId);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de la photo : " + e.getMessage(), e);
        }
    }

    @Override
    public void updatePhotoPath(Long articleId, String photoPath) {
        if (articleId == null || articleId <= 0) {
            throw new IllegalArgumentException("L'ID de l'article n'est pas valide");
        }
        
        if (photoPath == null || photoPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin de la photo ne peut pas être vide");
        }
        
        try {
            photoDAO.updatePhotoPath(articleId, photoPath);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du chemin de la photo : " + e.getMessage(), e);
        }
    }

    @Override
    public String getPhotoPath(Long articleId) {
        if (articleId == null || articleId <= 0) {
            throw new IllegalArgumentException("L'ID de l'article n'est pas valide");
        }
        
        try {
            return photoDAO.getPhotoPath(articleId);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du chemin de la photo : " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deletePhoto(Long articleId) {
        if (articleId == null || articleId <= 0) {
            throw new IllegalArgumentException("L'ID de l'article n'est pas valide");
        }
        
        try {
            return photoDAO.deletePhoto(articleId);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la photo : " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> getPhotoPaths(List<Long> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) {
            return List.of();
        }
        
        // Validation de tous les IDs
        for (Long id : articleIds) {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Un des IDs d'article n'est pas valide : " + id);
            }
        }
        
        try {
            return photoDAO.getPhotoPaths(articleIds);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des chemins de photos : " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasPhoto(Long articleId) {
        if (articleId == null || articleId <= 0) {
            return false;
        }
        
        try {
            return photoDAO.hasPhoto(articleId);
        } catch (Exception e) {
            // En cas d'erreur, on considère qu'il n'y a pas de photo
            return false;
        }
    }

    @Override
    public boolean validatePhotoFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        // Vérification de la taille du fichier
        if (file.getSize() > maxFileSize) {
            return false;
        }
        
        // Vérification du type MIME
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        
        List<String> allowedTypesList = Arrays.asList(allowedTypes.split(","));
        if (!allowedTypesList.contains(contentType.toLowerCase())) {
            return false;
        }
        
        // Vérification de l'extension du fichier
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return false;
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");
        
        return allowedExtensions.contains(extension);
    }
}
