package eni.ecole.enienchere;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.security.Security;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

//    @Bean
//    UserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//        userDetailsManager.setUsersByUsernameQuery("select pseudo, password, 1 from utilisateur where pseudo=?");
//        userDetailsManager.setAuthoritiesByUsernameQuery("select pseudo,role from roles where pseudo=?");
//        return userDetailsManager;
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth->
        {
            // pour toute les personnes non connecter
            auth.requestMatchers(HttpMethod.GET,"/").permitAll();
            auth.requestMatchers(HttpMethod.GET,"/error").permitAll();
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

            // il n'y a que les personnes connecter qui peut avoir accès
            auth.requestMatchers(HttpMethod.GET,"/cree").authenticated();
            auth.requestMatchers(HttpMethod.POST,"/cree").authenticated();
            auth.requestMatchers(HttpMethod.POST,"/photo").authenticated();
            auth.requestMatchers(HttpMethod.POST,"/acheter").authenticated();
            auth.requestMatchers(HttpMethod.GET,"/edit").authenticated();
            auth.requestMatchers(HttpMethod.POST,"/edit").authenticated();
            auth.requestMatchers(HttpMethod.GET,"/supprimer").authenticated();
            auth.requestMatchers(HttpMethod.POST,"/supprimer").authenticated();
            auth.requestMatchers(HttpMethod.GET,"/ventes").authenticated();
            auth.requestMatchers(HttpMethod.POST,"/annule").authenticated();
            auth.requestMatchers(HttpMethod.GET,"/livraison").authenticated();
            auth.requestMatchers(HttpMethod.POST,"/livraison").authenticated();
            auth.requestMatchers(HttpMethod.POST,"/profil").authenticated();
            auth.requestMatchers(HttpMethod.GET,"/profil").authenticated();


            // il n'y a que l'admin qui peut modifier les données
            auth.requestMatchers(HttpMethod.GET,"/admin").hasAnyRole("ADMIN");
            auth.requestMatchers(HttpMethod.POST,"/admin").hasAnyRole("ADMIN");

            auth.anyRequest().denyAll();
        });

        //version de pages de login par defaut du framework (spring)
        http.formLogin(Customizer.withDefaults());

        return http.build();
    }

}
