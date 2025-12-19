package it.unical.fleetgo.backend.Service.Config;

import it.unical.fleetgo.backend.Persistence.Entity.Utente.CredenzialiUtente;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import it.unical.fleetgo.backend.Service.UtenteService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.sql.SQLException;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConf {

    @Autowired AdminAziendaleService adminAziendaleService;
    @Autowired UtenteService utenteService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/autenticazione/**").permitAll()
                        .requestMatchers("/dashboardAdminAziendale/**").hasRole("AdminAziendale")
                        .requestMatchers("/dashboardFleetGo/**").hasRole("FleetGo")
                        .requestMatchers("/dashboardDipendente/**").hasRole("Dipendente")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) ->
                        {response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");})
                )
                .formLogin(form -> form.loginProcessingUrl("/autenticazione/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, auth) -> {

                            String ruolo =auth.getAuthorities().iterator().next().toString();
                            String ruoloPulito= ruolo.replace("ROLE_","");

                            String targetUrl = switch (ruoloPulito) {
                                case "FleetGo" -> "/dashboardFleetGo";
                                case "AdminAziendale" -> "/dashboardAzienda";
                                case "Dipendente" -> "/dashboardDipendente";
                                default -> "/autenticazione/login";
                            };

                            HttpSession session = request.getSession();
                            CredenzialiUtente contenitoreCredenziali = null;
                            try {
                                contenitoreCredenziali = utenteService.getCredenzialiUtentiByEmail(auth.getName());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            session.setAttribute("ruolo", ruoloPulito);
                            session.setAttribute("idUtente", contenitoreCredenziali.getIdUtente());

                            if (ruoloPulito.equals("AdminAziendale")) {
                                try {
                                    session.setAttribute("idAzienda",
                                            adminAziendaleService.getIdAziendaGestita(
                                                    contenitoreCredenziali.getIdUtente()
                                            )
                                    );
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }else if (ruoloPulito.equals("Dipendente")) {
                                Integer idAziendaAssociata=null;
                                try {
                                    idAziendaAssociata= utenteService.getAziendaAssociataDipendente(contenitoreCredenziali.getIdUtente());
                                    if(idAziendaAssociata!=null){
                                        session.setAttribute("idAziendaAssociata",idAziendaAssociata);
                                    }
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write(String.format("{\"redirectUrl\": \"%s\", \"ruolo\": \"%s\"}", targetUrl, ruoloPulito));
                        })
                        .failureHandler((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Credenziali non valide");
                        })
                        .permitAll()
                )
                .logout(logout -> logout.logoutUrl("/autenticazione/logout")
                        .logoutSuccessHandler((request, response, auth) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        .permitAll()
                )
        ;
        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}