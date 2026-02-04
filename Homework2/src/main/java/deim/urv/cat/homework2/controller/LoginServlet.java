package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.service.UserService;
import deim.urv.cat.homework2.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/Web/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl(); // Instancia de UserService

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirige al JSP de login
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Llama al método isValidUser del servicio
            if (userService.isValidUser(username, password)) { // Cambiado aquí
                // Crear sesión y almacenar el usuario autenticado
                HttpSession session = request.getSession(true);
                session.setAttribute("username", username);

                // Redirigir al artículo solicitado o a la lista de artículos
                String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
                if (redirectAfterLogin != null) {
                    session.removeAttribute("redirectAfterLogin");
                    response.sendRedirect(request.getContextPath() + redirectAfterLogin);
                } else {
                    response.sendRedirect(request.getContextPath() + "/Web/articles");
                }
            } else {
                // Redirigir al login con un mensaje de error
                response.sendRedirect(request.getContextPath() + "/Web/login?error=1");
            }
        } catch (Exception e) {
            throw new ServletException("Error al autenticar usuario", e);
        }
    }
}

