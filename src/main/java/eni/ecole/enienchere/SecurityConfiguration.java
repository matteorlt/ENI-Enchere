package eni.ecole.enienchere;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.sql.DataSource;

/**
 * Configuration de la sécurité de l'application
 * Cette classe définit comment les utilisateurs sont authentifiés et autorisés
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Configuration de la gestion des utilisateurs avec la base de données
     * Cette méthode définit comment Spring Security va récupérer les informations des utilisateurs
     * @param dataSource La source de données (connexion à la base de données)
     * @return Un gestionnaire d'utilisateurs configuré
     */
    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        // Requête pour récupérer les informations de l'utilisateur (pseudo, mot de passe, enabled)
        userDetailsManager.setUsersByUsernameQuery("select pseudo, mot_de_passe, 1 from utilisateur where pseudo=?");
        // Requête pour récupérer les rôles de l'utilisateur
        userDetailsManager.setAuthoritiesByUsernameQuery("select pseudo, 'ROLE_USER' from utilisateur where pseudo=?");
        return userDetailsManager;
    }

    /**
     * Configuration de l'encodeur de mot de passe
     * Utilise BCrypt pour le hachage sécurisé des mots de passe
     * @return Un encodeur de mot de passe BCrypt
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuration principale de la sécurité
     * Définit les règles d'accès, la gestion des sessions, etc.
     * @param http L'objet HttpSecurity à configurer
     * @return La chaîne de filtres de sécurité configurée
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//            // Configuration CSRF (Cross-Site Request Forgery)
//            // Protège contre les attaques CSRF en validant les tokens
//            .csrf(Customizer.withDefaults())
//
//            // Configuration des en-têtes de sécurité
//            .headers(headers -> headers
//                // Empêche le site d'être affiché dans une iframe (protection contre le clickjacking)
//                .frameOptions().sameOrigin()
//                // Active la protection XSS du navigateur
//                .xssProtection())
//
//            // Configuration de la gestion des sessions
//            .sessionManagement(session -> session
//                // Limite à une session active par utilisateur
//                .maximumSessions(1)
//                // Redirige vers la page de connexion si la session expire
//                .expiredUrl("/connexion?expired")
//                // Configure le timeout de session à 5 minutes (300 secondes)
//                .and()
//                .sessionFixation().newSession()
//                .invalidSessionUrl("/connexion?expired")
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .maximumSessions(1)
//                .expiredUrl("/connexion?expired"))
//
//            // Configuration de la déconnexion
//            .logout(logout -> logout
//                // URL de déconnexion
//                .logoutUrl("/logout")
//                // Page de redirection après déconnexion
//                .logoutSuccessUrl("/")
//                // Invalide la session HTTP
//                .invalidateHttpSession(true)
//                // Efface l'authentification
//                .clearAuthentication(true))
//
//            // Configuration de la gestion des erreurs
//            .exceptionHandling(exception -> exception
//                // Page d'erreur 403 (accès refusé)
//                .accessDeniedPage("/error/403")
//                // Redirection vers la page de connexion si non authentifié
//                .authenticationEntryPoint((request, response, authException) ->
//                    response.sendRedirect("/connexion")))
//
//            // Configuration "Se souvenir de moi"
//            .rememberMe(remember -> remember
//                // Clé secrète pour signer le cookie (doit être unique et sécurisée)
//                .key("ENI-Enchere-2025-SecureKey-!@#$%^&*()_+")
//                // Durée de validité du cookie (24 heures)
//                .tokenValiditySeconds(86400))

            // Configuration des autorisations
            .authorizeHttpRequests(auth -> {
                // Accès public (sans authentification)
                auth.requestMatchers(HttpMethod.GET,"/").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/error/**").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/css/**").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/images/**").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/js/**").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/enchere").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/article-detail").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/connexion/").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/inscription").permitAll();
                auth.requestMatchers(HttpMethod.POST,"/connexion/").permitAll();
                auth.requestMatchers(HttpMethod.POST,"/inscription").permitAll();
                auth.requestMatchers(HttpMethod.POST,"/profil/**").permitAll();
                // à changer en authentificated a la fin
                auth.requestMatchers(HttpMethod.POST,"/utilisateur/**").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/utilisateur/**").permitAll();
                auth.requestMatchers(HttpMethod.POST,"/utilisateur/css/**").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/utilisateur/css/**").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/details").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/details/**").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/nouvelle").permitAll();
                auth.requestMatchers(HttpMethod.POST,"/nouvelle").permitAll();
                auth.requestMatchers(HttpMethod.POST,"/enchere/soumettre").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/enchere/soumettre").permitAll();
                auth.requestMatchers(HttpMethod.POST,"/enchere/soumettre/**").permitAll();
                auth.requestMatchers(HttpMethod.GET,"/enchere/soumettre/**").permitAll();

//                // Accès authentifié (nécessite d'être connecté)
//                auth.requestMatchers(HttpMethod.GET,"/nouvelle").authenticated();
//                auth.requestMatchers(HttpMethod.POST,"/nouvelle").authenticated();
//                auth.requestMatchers(HttpMethod.POST,"/photo").authenticated();
//                auth.requestMatchers(HttpMethod.POST,"/article-detail").authenticated();
//                auth.requestMatchers(HttpMethod.GET,"/edit").authenticated();
//                auth.requestMatchers(HttpMethod.POST,"/edit").authenticated();
//                auth.requestMatchers(HttpMethod.GET,"/supprimer").authenticated();
//                auth.requestMatchers(HttpMethod.POST,"/supprimer").authenticated();
//                auth.requestMatchers(HttpMethod.GET,"/ventes").authenticated();
//                auth.requestMatchers(HttpMethod.POST,"/annule").authenticated();
//                auth.requestMatchers(HttpMethod.GET,"/livraison").authenticated();
//                auth.requestMatchers(HttpMethod.POST,"/livraison").authenticated();
//                auth.requestMatchers(HttpMethod.POST,"/profil").authenticated();
//                auth.requestMatchers(HttpMethod.GET,"/profil").authenticated();
//
//                // Accès admin (nécessite le rôle ADMIN)
//                auth.requestMatchers(HttpMethod.GET,"/admin/**").hasRole("ADMIN");
//                auth.requestMatchers(HttpMethod.POST,"/admin/**").hasRole("ADMIN");

//                // Tout autre accès est refusé
//                auth.anyRequest().denyAll();
            });
//
//            // Configuration du formulaire de connexion
//            .formLogin(form -> form
//                // Page de connexion personnalisée
//                .loginPage("/connexion")
//                // Page de redirection après connexion réussie
//                .defaultSuccessUrl("/")
//                // Permet l'accès à la page de connexion sans authentification
//                .permitAll());

        return http.build();
    }
}
