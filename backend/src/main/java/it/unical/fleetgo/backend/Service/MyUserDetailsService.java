package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Persistence.DAO.CredenzialiDAO;
import it.unical.fleetgo.backend.Persistence.DAO.UtenteDAO;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.CredenzialiUtente;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired private DataSource dataSource;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {

        try(Connection connection = this.dataSource.getConnection()) {
            UtenteDAO utenteDAO = new UtenteDAO(connection);
            CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);

            CredenzialiUtente credenzialiUtente = credenzialiDAO.getCredenzialiUtenteByEmail(email);

            if(credenzialiUtente == null) {
                throw new UsernameNotFoundException("Utente non trovato");
            }

            String ruolo = utenteDAO.getRuoloDaId(credenzialiUtente.getIdUtente());
            if(ruolo == null){
                throw new UsernameNotFoundException("Ruolo non trovato per l'utente");
            }

            return User.builder()
                    .username(email)
                    .password(credenzialiUtente.getPassword())
                    .roles(ruolo)
                    .build();
        }
    }
}