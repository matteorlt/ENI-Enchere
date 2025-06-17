package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.ArticleAVendre;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

    String savePhoto(MultipartFile fichier, Long articleId);

    void updatePhotoPath(Long articleId, String photoPath);

    String getPhotoPath(Long articleId);

    boolean deletePhoto(Long articleId);

    List<String> getPhotoPaths(List<Long> articleIds);

    boolean hasPhoto(Long articleId);

    boolean validatePhotoFile(MultipartFile file);
}
