package it.unical.fleetgo.backend.Service;

import it.unical.fleetgo.backend.Persistence.DAO.CredenzialiDAO;
import it.unical.fleetgo.backend.Persistence.DAO.UtenteDAO;
import it.unical.fleetgo.backend.Persistence.DBManager;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.CredenzialiUtente;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class MyUserDetailsService implements UserDetailsService {
    private final Connection connection = DBManager.getInstance().getConnection();
    private final UtenteDAO utenteDAO = new UtenteDAO(connection);
    private final CredenzialiDAO credenzialiDAO = new CredenzialiDAO(connection);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CredenzialiUtente credenzialiUtente = credenzialiDAO.getCredenzialiUtenteByEmail(email);
        if(credenzialiUtente==null){
            throw new UsernameNotFoundException(email);
        }
        String ruolo = utenteDAO.getRuoloDaId(credenzialiUtente.getIdUtente());
        if(ruolo==null){
            throw new UsernameNotFoundException("Ruolo non trovato per l'utente");
        }
        return User.builder()
                .username(email)
                .password(credenzialiUtente.getPassword())
                .roles(ruolo)
                .build();
    }
}