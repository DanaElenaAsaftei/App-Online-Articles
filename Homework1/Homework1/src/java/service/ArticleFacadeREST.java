/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import authn.Credentials;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.entities.Comment;
import authn.Secured;
import com.sun.xml.messaging.saaj.util.Base64;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.core.Response;
import model.entities.Article;
import model.entities.Topic;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.BadRequestException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.HeaderParam;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;

/**
 *
 * @author hotma
 */

@Stateless
@Path("article")
public class ArticleFacadeREST extends AbstractFacade<Article> {

    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    public ArticleFacadeREST() {
        super(Article.class);
    }
    

// GET /rest/api/v1/article?topic=${topic}&author=${author}
@GET
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public List<Article> getArticles(
        @QueryParam("topic") List<Long> topicIds,
        @QueryParam("author") Long authorId) {
    // Construir la consulta dinámica basada en los parámetros.
    StringBuilder queryBuilder = new StringBuilder("SELECT a FROM Article a WHERE 1=1");

    if (topicIds != null && !topicIds.isEmpty()) {
        if (topicIds.size() > 2) {
            throw new BadRequestException("No se pueden especificar más de dos temas.");
        }
        queryBuilder.append(" AND EXISTS (SELECT t FROM a.topics t WHERE t.id IN :topicIds)");  
    }

    if (authorId != null) {
        queryBuilder.append(" AND a.author.id = :authorId");
    }

    queryBuilder.append(" ORDER BY a.views DESC");

    // Ejecutar la consulta
    TypedQuery<Article> query = em.createQuery(queryBuilder.toString(), Article.class);

    if (topicIds != null && !topicIds.isEmpty()) {
        query.setParameter("topicIds", topicIds);
    }

    if (authorId != null) {
        query.setParameter("authorId", authorId);
    }

    return query.getResultList();
}

    // GET /rest/api/v1/article/{id}
@GET
@Path("{id}")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public Response getArticle(@PathParam("id") Long id, @HeaderParam("Authorization") String header) {
    // Buscar la entidad
    Article article = em.find(Article.class, id);
         if (article == null) {
        // Devolver respuesta con código 404 y mensaje de error
        String errorMessage = "{\"error\": \"El artículo con el ID " + id + " no existe.\"}";
        return Response.status(Response.Status.NOT_FOUND)
                       .entity(errorMessage)  // Incluye el mensaje en la respuesta
                       .type(MediaType.APPLICATION_JSON)
                       .build(); 
    }

    // Comprobación si el artículo es privado
    if (!article.isPublic()) {
        if (header == null || !header.startsWith("Basic ")) {
            return Response.status(Response.Status.UNAUTHORIZED).build(); // No autenticado
        }

        String username;
        String password;

        try {
            // Descodificar la cabecera Authorization
            String auth = header.replace("Basic ", "");
            String decode = new String(DatatypeConverter.parseBase64Binary(auth), StandardCharsets.UTF_8);
            String[] credentials = decode.split(":", 2);

            if (credentials.length != 2) {
                return Response.status(Response.Status.BAD_REQUEST).build(); // Mal formato
            }

            username = credentials[0];
            password = credentials[1];
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build(); // Error de formato
        }

        // Validar las credenciales
        try {
            TypedQuery<Credentials> query = em.createNamedQuery("Credentials.findUser", Credentials.class);
            query.setParameter("username", username);
            List<Credentials> results = query.getResultList();

            if (results.isEmpty() || !results.get(0).getPassword().equals(password)) {
                return Response.status(Response.Status.FORBIDDEN).build(); // Credenciales incorrectas
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build(); // Usuario no encontrado
        }
    }

    // Incrementar el número de visualizaciones
    article.setViews(article.getViews() + 1);
    em.merge(article); // Persistir cambios en la base de datos

    return Response.ok(article).build(); // Devolver el artículo
}

        
@POST
@Secured
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public Response createArticle(Article entity) {
    
    List<String> errors = new ArrayList<>();

    // Validar campos obligatorios
    if (entity.getTitle() == null || entity.getTitle().isEmpty()) {
        errors.add("El título es obligatorio.");
    }
    if (entity.getSummary() == null || entity.getSummary().isEmpty()) {
        errors.add("El resumen es obligatorio.");
    }
    if (entity.getAuthor() == null || entity.getAuthor().getId() == null) {
        errors.add("El autor es obligatorio.");
    }
    if (entity.getTopics() == null || entity.getTopics().isEmpty()) {
        errors.add("Debe incluir al menos un tema.");
    } else if (entity.getTopics().size() > 2) {
        errors.add("El artículo no puede tener más de 2 temas.");
    }

    // Si hay errores, devolver respuesta con todos los errores acumulados
    if (!errors.isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                      .entity("{\"errors\":" + errors.toString() + "}")
                      .type(MediaType.APPLICATION_JSON)
                      .build();
    }

    // Validar si el autor existe en la base de datos
    if (em.find(model.entities.Customer.class, entity.getAuthor().getId()) == null) {
        return Response.status(Response.Status.BAD_REQUEST)
                      .entity("{\"error\":\"El autor no existe.\"}")
                      .type(MediaType.APPLICATION_JSON)
                      .build();
    }

    // Establecer la fecha de publicación
    entity.setPublishDate(new java.util.Date());
    em.persist(entity);

    // Retornar respuesta exitosa
    return Response.status(Response.Status.CREATED)
                  .entity(entity.getId())
                  .build();
}


    // DELETE /rest/api/v1/article/{id}
    @DELETE
    //@Secured
    @Path("{id}")
    public Response deleteArticle(@PathParam("id") Long id) {
        Article article = em.find(Article.class, id); //super.find(id);

        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Verificar si el usuario autenticado es el autor
        if (!article.getAuthor().isAuthorOf(id)) {
            return Response.status(Response.Status.FORBIDDEN).entity("No tienes permisos para borrar este artículo.").build();
        }

        /*super.remove(article);*/ em.remove(em.merge(article));
        return Response.noContent().build();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}