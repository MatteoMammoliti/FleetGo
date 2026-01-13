package it.unical.fleetgo.backend.Service.Config;

import it.unical.fleetgo.backend.Persistence.Entity.Utente.CredenzialiUtente;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import it.unical.fleetgo.backend.Service.AziendaService;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConf {

    @Autowired AdminAziendaleService adminAziendaleService;
    @Autowired UtenteService utenteService;
    @Autowired AziendaService aziendaService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/autenticazione/**").permitAll()
                        .requestMatchers("/sezionePubblica/**").permitAll()
                        .requestMatchers("/dashboardAdminAziendale/**").hasRole("AdminAziendale")
                        .requestMatchers("/dashboardFleetGo/**").hasRole("FleetGo")
                        .requestMatchers("/dashboardDipendente/**").hasRole("Dipendente")
                        .requestMatchers("/homeNoAzienda/**").hasRole("Dipendente")
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
                            Integer idAziendaJson = null;
                            boolean isPrimoAccesso = false;
                            Boolean aziendaAttiva = null;

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

                                    Integer idAzienda = adminAziendaleService.getIdAziendaGestita(contenitoreCredenziali.getIdUtente());

                                    session.setAttribute("idAzienda", idAzienda);
                                    idAziendaJson = idAzienda;

                                    if(adminAziendaleService.isPrimoAccesso(contenitoreCredenziali.getIdUtente())) {
                                        isPrimoAccesso = true;
                                        targetUrl = "/recuperoPassword";
                                    }

                                    if(!aziendaService.isAziendaAttiva(idAzienda)) {
                                        targetUrl = "/azienda-disabilitata";
                                        aziendaAttiva = false;
                                    } else {
                                        aziendaAttiva = true;
                                    }

                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }else if (ruoloPulito.equals("Dipendente")) {
                                try {
                                    Integer idAziendaAssociata = utenteService.getAziendaAssociataDipendente(contenitoreCredenziali.getIdUtente());

                                    if(idAziendaAssociata != null){
                                        session.setAttribute("idAziendaAssociata",idAziendaAssociata);
                                        idAziendaJson = idAziendaAssociata;
                                    } else {
                                        targetUrl = "/senza-azienda";
                                    }

                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json;charset=UTF-8");

                            Map<String, Object> parametriJson = new HashMap<>();
                            parametriJson.put("idAzienda", idAziendaJson);
                            parametriJson.put("primoAccesso", isPrimoAccesso);
                            parametriJson.put("ruolo",  ruoloPulito);
                            parametriJson.put("redirectUrl", targetUrl);
                            parametriJson.put("isAziendaAttiva", aziendaAttiva);

                            new ObjectMapper().writeValue(response.getWriter(), parametriJson);
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