package eni.ecole.enienchere;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

/**
 * Configuration de la sécurité de l'application
 * Cette classe définit comment les utilisateurs sont authentifiés et autorisés
 *
 * TOUTES LES ROUTES SONT EN PERMIT ALL - À CONFIGURER SELON VOS BESOINS
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // ========== PAGES PUBLIQUES ==========
                        // Page d'accueil
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/contact").permitAll()
                        .requestMatchers("/mentions-legales").permitAll()
                        .requestMatchers("/a-propos").permitAll()

                        // Pages d'authentification
                        .requestMatchers("/connexion").permitAll()
                        .requestMatchers("/creer-compte").permitAll()
                        .requestMatchers("/logout").permitAll()

                        // ========== PAGES ENCHÈRES ==========
                        // Liste des enchères (page principale)
                        .requestMatchers("/enchere").permitAll()

                        // Détails d'un article
                        .requestMatchers("/details").permitAll()

                        // Soumettre une enchère
                        .requestMatchers(HttpMethod.POST, "/enchere/soumettre").authenticated()

                        // ========== GESTION UTILISATEUR ==========
                        // Profil utilisateur
                        .requestMatchers("/mon-profil").authenticated()
                        .requestMatchers("/mon-profil/modifier").authenticated()
                        .requestMatchers(HttpMethod.POST, "/mon-profil/modifier").authenticated()
                        .requestMatchers("/mon-profil/modifier-mot-de-passe").authenticated()
                        .requestMatchers(HttpMethod.POST, "/mon-profil/modifier-mot-de-passe").authenticated()
                        .requestMatchers("/profil").authenticated()

                        // Création de compte
                        .requestMatchers(HttpMethod.POST, "/creer-compte").permitAll()

                        // ========== VENTE D'ARTICLES ==========
                        // Nouvelle vente
                        .requestMatchers("/vendre").authenticated()
                        .requestMatchers(HttpMethod.POST, "/vendre").authenticated()

                        // Gestion des articles du vendeur
                        .requestMatchers("/vendeur/gestion-articles").authenticated()
                        .requestMatchers(HttpMethod.POST, "/vendeur/gestion-articles/modifier/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/vendeur/gestion-articles/supprimer/**").authenticated()

                        // ========== GESTION DES PHOTOS ==========
                        // Ajout de photos
                        .requestMatchers("/ajouter-photo").authenticated()
                        .requestMatchers(HttpMethod.POST, "/ajouter-photo").authenticated()

                        // Accès aux images
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/uploads/photos/**").permitAll()
                        .requestMatchers("/images/articles/**").permitAll()

                        // ========== PAGES ADMINISTRATEUR ==========
                        // Gestion des enchères (admin)
                        .requestMatchers("/admin/gestion-encheres").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/gestion-encheres/modifier/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/gestion-encheres/supprimer/**").hasRole("ADMIN")

                        // Mise à jour forcée des statuts (admin)
                        .requestMatchers("/admin/maj-statuts").hasRole("ADMIN")

                        // ========== PAGES SPÉCIALES ==========
                        // Accueil connecté
                        .requestMatchers("/accueil-connecte").permitAll()

                        // Page d'erreur
                        .requestMatchers("/error").permitAll()

                        // ========== RESSOURCES STATIQUES ==========
                        // CSS, JS, Images, Fonts
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/fonts/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()

                        // Well-known (pour les certificats, etc.)
                        .requestMatchers("/.well-known/**").permitAll()

                        // ========== AUTRES ROUTES ==========
                        // Toutes les autres routes (à définir si nécessaire)
                        .anyRequest().denyAll()
                )
                .formLogin(form -> form
                        .loginPage("/connexion")
                        .loginProcessingUrl("/connexion")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/connexion?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .expiredUrl("/connexion?expired")
                )
                .csrf(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/.well-known/**")
                        .addResourceLocations("classpath:/.well-known/");
                registry.addResourceHandler("/css/**")
                        .addResourceLocations("classpath:/static/css/");
                registry.addResourceHandler("/js/**")
                        .addResourceLocations("classpath:/static/js/");
                registry.addResourceHandler("/images/**")
                        .addResourceLocations("classpath:/static/images/");
                registry.addResourceHandler("/fonts/**")
                        .addResourceLocations("classpath:/static/fonts/");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Configuration de la gestion des utilisateurs avec la base de données
     * Cette méthode définit comment Spring Security va récupérer les informations des utilisateurs
     * @param dataSource La source de données (connexion à la base de données)
     * @return Un gestionnaire d'utilisateurs configuré
     */
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Requête pour récupérer les informations de l'utilisateur
        userDetailsManager.setUsersByUsernameQuery(
                "SELECT pseudo, mot_de_passe, 1, nom, prenom, email, telephone, credit, administrateur " +
                        "FROM UTILISATEURS WHERE pseudo=?"
        );

        // Requête pour récupérer les rôles de l'utilisateur
        userDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT pseudo, CASE WHEN administrateur = 1 THEN 'ROLE_ADMIN' ELSE 'ROLE_USER' END " +
                        "FROM UTILISATEURS WHERE pseudo=?"
        );

        return userDetailsManager;
    }
}

/*
 * ========== GUIDE POUR SÉCURISER LES ROUTES ==========
 *
 * Pour sécuriser une route, remplacez .permitAll() par :
 *
 * 1. .authenticated() - Nécessite d'être connecté
 * 2. .hasRole("USER") - Nécessite le rôle USER
 * 3. .hasRole("ADMIN") - Nécessite le rôle ADMIN
 * 4. .hasAnyRole("USER", "ADMIN") - Nécessite l'un des rôles
 *
 * EXEMPLES DE SÉCURISATION :
 *
 * // Pour les enchères (nécessite d'être connecté)
 * .requestMatchers(HttpMethod.POST, "/enchere/soumettre").authenticated()
 *
 * // Pour la vente (nécessite d'être connecté)
 * .requestMatchers("/vendre").authenticated()
 * .requestMatchers(HttpMethod.POST, "/vendre").authenticated()
 *
 * // Pour le profil (nécessite d'être connecté)
 * .requestMatchers("/mon-profil").authenticated()
 * .requestMatchers("/mon-profil/modifier").authenticated()
 *
 * // Pour l'administration (nécessite le rôle ADMIN)
 * .requestMatchers("/admin/**").hasRole("ADMIN")
 *
 * // Pour la gestion vendeur (nécessite d'être connecté)
 * .requestMatchers("/vendeur/**").authenticated()
 *
 * ROUTES RECOMMANDÉES À SÉCURISER :
 * - Toutes les routes /mon-profil/** (authenticated)
 * - Toutes les routes /vendre** (authenticated)
 * - Toutes les routes /vendeur/** (authenticated)
 * - Toutes les routes /admin/** (hasRole("ADMIN"))
 * - POST /enchere/soumettre (authenticated)
 * - /ajouter-photo (authenticated)
 *
 * ROUTES À LAISSER PUBLIQUES :
 * - / (page d'accueil)
 * - /enchere (liste des enchères)
 * - /details (détails d'un article)
 * - /connexion, /creer-compte
 * - /css/**, /js/**, /images/** (ressources statiques)
 * - /error
 */
