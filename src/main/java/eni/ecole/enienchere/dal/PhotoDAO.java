package eni.ecole.enienchere.dal;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoDAO {

    String savePhoto(MultipartFile file, Long articleId);

    void updatePhotoPath(Long articleId, String photoPath);

    String getPhotoPath(Long articleId);

    boolean deletePhoto(Long articleId);

    List<String> getPhotoPaths(List<Long> articleIds);

    boolean hasPhoto(Long articleId);
}
