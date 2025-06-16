package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public class AjoutPhotoController {

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
    public class PhotoController {

        @Autowired
        private PhotoService photoService;


        @GetMapping("/accueil-connecte")
        public String accueilConnecte() {
        return "accueilConnecte";
        }

        @GetMapping("/ajouter-photo")
        public String afficherAjoutPhoto(Model model) {
            model.addAttribute("participant", "coach_toto");
            return "ajouterPhoto";
        }

        @PostMapping("/ajouter-photo")
        public String EnregistrerPhoto (@RequestParam("photo")MultipartFile fichier){
        try{ photoService.enregistrerPhoto(fichier);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/ajouter-photo?erreur";

}

}

}