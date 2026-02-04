package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.controller.UserForm;
import deim.urv.cat.homework2.model.Credentials;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.client.Entity;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {
    private final WebTarget webTarget;
    private final jakarta.ws.rs.client.Client client;
    private static final String BASE_URI = "http://localhost:8080/UserService/rest/api";

    // Declaración del logger
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    
    public UserServiceImpl() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("user");
    }
    
    @Override
    public User findUserByEmail(String email){
        Response response = webTarget.path(email)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (response.getStatus() == 200) {
            return response.readEntity(User.class);
        }
        return null;
    }

    @Override
    public boolean addUser(UserForm user) {
       Response response = webTarget.request(MediaType.APPLICATION_JSON)
               .post(Entity.entity(user, MediaType.APPLICATION_JSON), 
                    Response.class);
       return response.getStatus() == 201;
    }

@Override
public boolean isValidUser(String username, String password) {
    // Crear cliente HTTP
    jakarta.ws.rs.client.Client client = jakarta.ws.rs.client.ClientBuilder.newClient();
    WebTarget target = client.target("http://localhost:8080/Homework1/rest/api/v1/customer/validate");

    // Crear objeto Credentials
    Credentials credentials = new Credentials();
    credentials.setUsername(username);
    credentials.setPassword(password);

    // Realizar la solicitud POST
    Response response = target.request(MediaType.APPLICATION_JSON)
                              .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));

    // Analizar la respuesta
    return response.getStatus() == 200;
}

@Override
@SuppressWarnings("LoggerStringConcat")
public User findUserByUsername(String username) {
    // Crear cliente HTTP
    WebTarget target = client.target("http://localhost:8080/Homework1/rest/api/v1/customer/username/" + username);

    try {
        // Realizar la solicitud GET
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        // Verificar el código de respuesta
        if (response.getStatus() == 200) {
            return response.readEntity(User.class); // Leer la respuesta como un objeto User
        } else {
            LOGGER.warning("Usuario no encontrado para el username: " + username);
            return null;
        }
    } catch (Exception e) {
        LOGGER.severe("Error al obtener usuario por username: " + e.getMessage());
        return null;
    }
}


}

