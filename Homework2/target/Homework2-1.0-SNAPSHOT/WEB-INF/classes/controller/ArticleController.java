/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.Article;
import deim.urv.cat.homework2.model.User;
import deim.urv.cat.homework2.model.Topic;
import deim.urv.cat.homework2.service.ArticleService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.logging.Logger;

@Controller
@Path("/articles")
public class ArticleController {

    private static final Logger logger = Logger.getLogger(ArticleController.class.getName());

    @Inject
    private Models models;

    @Inject
    private ArticleService articleService;
    
    @Inject
    private HttpServletRequest request;

    // Listar artículos con filtros
@GET
@SuppressWarnings("LoggerStringConcat")
public String listArticles(@QueryParam("author") Integer authorId, @QueryParam("topic") Integer topicId) {
    try {
        logger.info("Accediendo a listArticles...");
        // Obtener todos los artículos filtrados
        List<Article> articles = articleService.getFilteredArticles(authorId, topicId);

        logger.info("Artículos después del filtrado: " + articles.size());

        // Obtener lista de autores y tópicos para los filtros
        List<User> authors = articleService.getAllAuthors();
        List<Topic> topics = articleService.getAllTopics();

        // Pasar datos al modelo
        models.put("articles", articles);
        models.put("authors", authors);
        models.put("topics", topics);

        return "articles.jsp"; // Redirigir a la vista JSP
    } catch (Exception e) {
        logger.severe("Error en el controlador listArticles: " + e.getMessage());
        return "Error500.jsp"; // Página genérica para manejar errores
    }
}

@GET
@Path("/{id}")
@SuppressWarnings("LoggerStringConcat")
public String getArticleDetails(@PathParam("id") Integer articleId) {
    try {
        logger.info("Obteniendo detalles para el artículo con ID: " + articleId);
        // Obtener el artículo por su ID
        Article article = articleService.getArticleById(articleId);

        if (article == null) {
            // Si el artículo no existe, redirigir a una página 404
            return "404.jsp";
        }

        // Verificar si el artículo es privado
        if (!article.isPublic()) {
            // Verificar si el usuario está autenticado
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("username") == null) {
                // Guardar la URL del artículo solicitado para redirigir después de login
                session = request.getSession(true);
                session.setAttribute("redirectAfterLogin", "/Web/articles/" + articleId);

                // Redirigir a la página de login
                return "login.jsp";
            }
        }

        // Pasar el artículo al modelo para renderizar en JSP
        models.put("article", article);

        // Redirigir a la página de detalles del artículo
        return "articleDetail.jsp";
    } catch (Exception e) {
        Logger.getLogger(ArticleController.class.getName()).severe("Error al obtener detalles del artículo: " + e.getMessage());
        return "Error500.jsp"; // Página genérica para errores
    }
}



    
    
}




