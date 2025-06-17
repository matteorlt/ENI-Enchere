package eni.ecole.enienchere.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        
        String errorMessage = "Une erreur inattendue s'est produite.";
        
        if (statusCode != null) {
            switch (statusCode) {
                case 404:
                    errorMessage = "La page demandée n'a pas été trouvée.";
                    break;
                case 403:
                    errorMessage = "Vous n'avez pas les droits nécessaires pour accéder à cette page.";
                    break;
                case 500:
                    errorMessage = "Une erreur interne s'est produite sur le serveur.";
                    break;
                default:
                    errorMessage = "Une erreur s'est produite (code " + statusCode + ").";
            }
        }
        
        if (exception != null) {
            errorMessage += " Détails : " + exception.getMessage();
        }
        
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("statusCode", statusCode);
        
        return "error";
    }
} 