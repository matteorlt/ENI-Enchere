package eni.ecole.enienchere.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/images")
public class PhotoController {

    @Value("${app.upload.dir:uploads/photos}")
    private String uploadDir;

    @GetMapping("/photos/{filename:.+}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Resource resource = new FileSystemResource(filePath);

            if (resource.exists() && resource.isReadable()) {
                // Déterminer le type MIME
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/articles/{articleId}")
    public ResponseEntity<Resource> getPhotoByArticleId(@PathVariable Long articleId) {
        try {
            // Rechercher les fichiers qui commencent par "article_{articleId}_"
            Path uploadPath = Paths.get(uploadDir);
            if (Files.exists(uploadPath)) {
                String prefix = "article_" + articleId + "_";
                Path photoFile = Files.list(uploadPath)
                        .filter(path -> path.getFileName().toString().startsWith(prefix))
                        .findFirst()
                        .orElse(null);

                if (photoFile != null && Files.exists(photoFile)) {
                    Resource resource = new FileSystemResource(photoFile);
                    String contentType = Files.probeContentType(photoFile);
                    if (contentType == null) {
                        contentType = "application/octet-stream";
                    }

                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + photoFile.getFileName() + "\"")
                            .body(resource);
                }
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 