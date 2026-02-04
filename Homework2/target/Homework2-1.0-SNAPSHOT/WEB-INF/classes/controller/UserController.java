
package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.database.DatabaseConnection;
import deim.urv.cat.homework2.model.Credentials;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("/user")
public class UserController {

    @POST
    @Path("/validate")
    @Consumes("application/json")
    @SuppressWarnings("CallToPrintStackTrace")
    public Response validateUser(Credentials credentials) {
        String query = "SELECT COUNT(*) FROM ROOT.CREDENTIALS WHERE USERNAME = ? AND PASSWORD = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, credentials.getUsername());
            statement.setString(2, credentials.getPassword());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return Response.ok().build(); // Usuario válido (HTTP 200)
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Error al validar el usuario").build(); // Error interno
        }
        
        return Response.status(Response.Status.UNAUTHORIZED).build(); // Credenciales inválidas (HTTP 401)
    }
}
