package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

@Path("/profile")
public class ProfileController {

    @Inject
    private UserService userService; // Inyección del servicio de usuario

    @GET
    public Response getUserProfile(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not logged in").build();
        }

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not logged in").build();
        }

        try {
            // Usar el UserService para obtener los datos del usuario
            User user = userService.findUserByUsername(username);

            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }

            // Guardar los datos del usuario en la sesión
            session.setAttribute("profile_id", user.getId());
            session.setAttribute("profile_name", user.getFullName());
            session.setAttribute("profile_email", user.getEmail());
            session.setAttribute("username", user.getUsername());

            // Redirigir a la vista del perfil
            return Response.seeOther(java.net.URI.create(request.getContextPath() + "/profile")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving user data: " + e.getMessage()).build();
        }
    }
}
