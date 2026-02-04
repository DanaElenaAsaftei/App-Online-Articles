/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deim.urv.cat.homework2.service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import deim.urv.cat.homework2.model.Article;
import deim.urv.cat.homework2.model.Topic;
import deim.urv.cat.homework2.model.User;
import jakarta.ws.rs.core.GenericType;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Logger;

public class ArticleService {

    private static final Logger logger = Logger.getLogger(ArticleService.class.getName());
    private final String homework1BaseUrl = "http://localhost:8080/Homework1/rest/api/v1/article";

    @SuppressWarnings("LoggerStringConcat")
    public List<Article> getAllArticles() {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(homework1BaseUrl);
    try {
        String jsonResponse = target.request(MediaType.APPLICATION_JSON).get(String.class);
        logger.info("JSON recibido: " + jsonResponse);

        List<Article> articles = target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Article>>() {});
        
        logger.info("Artículos obtenidos:");
        for (Article article : articles) {
            if (article.getAuthor() == null) {
                logger.warning("El artículo con ID " + article.getId() + " no tiene un autor asignado.");
            } else {
                logger.info("Artículo ID: " + article.getId() + ", Autor: " + article.getAuthor().getFullName());
            }

            if (article.getTopics() == null || article.getTopics().isEmpty()) {
                logger.warning("El artículo con ID " + article.getId() + " no tiene tópicos asignados.");
                article.setTopics(new ArrayList<>());
            }

            // Asignar la imagen destacada
            article.setImage(assignImageForArticle(article.getId()));
        }

        client.close();
        return articles;

    } catch (Exception e) {
        logger.severe("Error al obtener artículos: " + e.getMessage());
        client.close();
        return new ArrayList<>(); // Retorna lista vacía en caso de error
    }
}


@SuppressWarnings("LoggerStringConcat")
public Article getArticleById(int id) {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(homework1BaseUrl + "/" + id);

    try {
        // Codifica las credenciales en formato "Basic username:password"
        String username = "sob"; 
        String password = "sob"; 
        String auth = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        // Realiza la solicitud con el encabezado Authorization
        Article article = target
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", auth)
            .get(Article.class);

        if (article != null) {
            logger.info("Procesando artículo con ID: " + article.getId());
            logger.info("Título: " + article.getTitle());

            // Asignar la imagen específica del artículo
            article.setImage(assignImageForArticle(article.getId()));

            // Poblar detalles adicionales
            if (article.getAuthor() == null) {
                User author = getAuthorById(article.getId());
                article.setAuthor(author);
            }

            if (article.getTopics() == null || article.getTopics().isEmpty()) {
                List<Topic> topics = getTopicsByArticleId(article.getId());
                article.setTopics(topics);
            }

            // Verificar si el contenido completo está presente, sino mostrar una advertencia
            if (article.getContent() == null || article.getContent().isEmpty()) {
                logger.warning("El artículo con ID " + id + " no tiene contenido completo asignado.");
                article.setContent("No content available for this article.");
            } else {
                logger.info("Contenido completo cargado para el artículo con ID " + id);
            }
        }

        client.close();
        return article;

    } catch (Exception e) {
        logger.severe("Error al obtener el artículo por ID: " + e.getMessage());
        client.close();
        return null;
    }
}


    @SuppressWarnings("LoggerStringConcat")
    private User getAuthorById(int authorId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/Homework1/rest/api/v1/customer/" + authorId);
        try {
            User user = target.request(MediaType.APPLICATION_JSON).get(User.class);
            client.close();
            return user;
        } catch (Exception e) {
            logger.severe("Error al obtener el autor por ID: " + e.getMessage());
            client.close();

            User errorUser = new User();
            errorUser.setFirstName("Error Fetching Author");
            return errorUser;
        }
    }



    @SuppressWarnings("LoggerStringConcat")
    private List<Topic> getTopicsByArticleId(int articleId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(homework1BaseUrl + "/" + articleId);
        try {
            Article article = target.request(MediaType.APPLICATION_JSON).get(Article.class);
            client.close();
            return article.getTopics();
        } catch (Exception e) {
            logger.severe("Error al obtener tópicos para el artículo ID: " + articleId);
            client.close();
            return new ArrayList<>();
        }
    }
    
public List<Article> getFilteredArticles(Integer authorId, Integer topicId) {
    List<Article> allArticles = getAllArticles();
    List<Article> filteredArticles = new ArrayList<>();

    for (Article article : allArticles) {
        boolean matchesAuthor = (authorId == null || (article.getAuthor() != null && article.getAuthor().getId() == authorId));
        boolean matchesTopic = (topicId == null || article.getTopics().stream()
            .anyMatch(topic -> topic.getId() == topicId)); // Usamos == para comparar tipos primitivos

        if (matchesAuthor && matchesTopic) {
            filteredArticles.add(article);
        }
    }
    return filteredArticles;
}


public List<User> getAllAuthors() {
    // Simula obtener todos los autores únicos de los artículos
    List<Article> allArticles = getAllArticles();
    return allArticles.stream()
        .map(Article::getAuthor)
        .distinct()
        .toList();
}

public List<Topic> getAllTopics() {
    // Simula obtener todos los tópicos únicos de los artículos
    List<Article> allArticles = getAllArticles();
    return allArticles.stream()
        .flatMap(article -> article.getTopics().stream())
        .distinct()
        .toList();
}

    @SuppressWarnings("LoggerStringConcat")
    private String assignImageForArticle(int articleId) {
    String imageName;
    switch (articleId) {
        case 1:
            imageName = "imagensob1.png";
            break;
        case 2:
            imageName = "imagensob2.png";
            break;
        case 3:
            imageName = "imagensob3.png";
            break;
        case 4:
            imageName = "imagensob5.png";
            break;
        case 5:
            imageName = "imagensob6.png";
            break;
        case 6:
            imageName = "imagensob7.png";
            break;    
            
        default:
            imageName = "ETSEcentrat.png"; // Imagen por defecto
    }
    logger.info("Imagen asignada para el artículo " + articleId + ": " + imageName);
    return imageName;
}


}


