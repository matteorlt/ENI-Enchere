package eni.ecole.enienchere;

import eni.ecole.enienchere.bll.UtilisateurService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 * Configuration de la sécurité de l'application * Cette classe définit comment les utilisateurs sont authentifiés et autorisés
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UtilisateurService utilisateurService;

    public SecurityConfiguration(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    /**
     * Configuration de la gestion des utilisateurs avec la base de données     * Cette méthode définit comment Spring Security va récupérer les informations des utilisateurs     * @param /dataSource La source de données (connexion à la base de données)     * @return Un gestionnaire d'utilisateurs configuré
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(utilisateurService).passwordEncoder(passwordEncoder());
        return auth.build();
    }

    /**
     * Configuration de l'encodeur de mot de passe     * Utilise BCrypt pour le hachage sécurisé des mots de passe     * @return Un encodeur de mot de passe BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuration principale de la sécurité     * Définit les règles d'accès, la gestion des sessions, etc.     * @param http L'objet HttpSecurity à configurer     * @return La chaîne de filtres de sécurité configurée
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET, "/").permitAll();
                    auth.requestMatchers("/creer-compte").permitAll();
                    auth.requestMatchers("/mon-profil/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/error").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/css/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/images/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .csrf(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .formLogin(f ->
                        f.loginPage("/connexion")
                                .permitAll()
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .permitAll())
                .build();
    }
}