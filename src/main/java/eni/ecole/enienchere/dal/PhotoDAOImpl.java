package eni.ecole.enienchere.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Repository
public class PhotoDAOImpl implements PhotoDAO {

    private final JdbcTemplate jdbcTemplate;
    
    @Value("${app.upload.dir:uploads/photos}")
    private String uploadDir;

    @Autowired
    public PhotoDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String savePhoto(MultipartFile file, Long articleId) {
        if (file.isEmpty()) {
            return null;
        }

        InputStream inputStream = null;
        try {
            // Créer le répertoire s'il n'existe pas
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Générer un nom unique pour le fichier
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : "";
            String filename = "article_" + articleId + "_" + UUID.randomUUID().toString() + extension;
            
            Path filePath = uploadPath.resolve(filename);
            
            // Utiliser InputStream pour éviter les problèmes de fichiers temporaires
            inputStream = file.getInputStream();
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            
            String photoPath = uploadDir + "/" + filename;
            
            // Mettre à jour la base de données
            updatePhotoPath(articleId, photoPath);
            
            return photoPath;
            
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de la photo", e);
        } finally {
            // S'assurer que l'InputStream est fermé
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Log l'erreur mais ne pas interrompre le traitement
                    System.err.println("Erreur lors de la fermeture de l'InputStream: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void updatePhotoPath(Long articleId, String photoPath) {
        String sql = "UPDATE ARTICLES_A_VENDRE SET photo = ? WHERE no_article = ?";
        jdbcTemplate.update(sql, photoPath, articleId);
    }

    @Override
    public String getPhotoPath(Long articleId) {
        String sql = "SELECT photo FROM ARTICLES_A_VENDRE WHERE no_article = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, articleId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deletePhoto(Long articleId) {
        try {
            // Récupérer le chemin de la photo actuelle
            String photoPath = getPhotoPath(articleId);
            
            if (photoPath != null && !photoPath.trim().isEmpty()) {
                // Supprimer le fichier physique
                try {
                    Path filePath = Paths.get(photoPath);
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                } catch (IOException e) {
                    System.err.println("Erreur lors de la suppression du fichier photo: " + e.getMessage());
                }
                
                // Mettre à jour la base de données
                String sql = "UPDATE ARTICLES_A_VENDRE SET photo = NULL WHERE no_article = ?";
                int rowsAffected = jdbcTemplate.update(sql, articleId);
                return rowsAffected > 0;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> getPhotoPaths(List<Long> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) {
            return List.of();
        }
        
        StringBuilder sql = new StringBuilder("SELECT photo FROM ARTICLES_A_VENDRE WHERE no_article IN (");
        for (int i = 0; i < articleIds.size(); i++) {
            if (i > 0) sql.append(",");
            sql.append("?");
        }
        sql.append(") AND photo IS NOT NULL");
        
        return jdbcTemplate.queryForList(sql.toString(), String.class, articleIds.toArray());
    }

    @Override
    public boolean hasPhoto(Long articleId) {
        String sql = "SELECT COUNT(*) FROM ARTICLES_A_VENDRE WHERE no_article = ? AND photo IS NOT NULL AND photo != ''";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, articleId);
        return count != null && count > 0;
    }
}
