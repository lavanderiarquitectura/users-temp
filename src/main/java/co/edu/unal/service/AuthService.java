package co.edu.unal.service;

import co.edu.unal.service.LdapService;
import co.edu.unal.model.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class AuthService {

    @PersistenceContext
    EntityManager entityManager;

    String response = "";
    LdapService ldapService = new LdapService();

    public String login(User student) {

        String username = student.getUsername();
        String password = student.getPassword();

        if (ldapService.connect()) {
            if (ldapService.validateUser(username, password)) {
                response = ldapService.getData(username);
            } else {
                response = "false";
            }
        } else {
            response = "false";
        }
        return response;
    }
}