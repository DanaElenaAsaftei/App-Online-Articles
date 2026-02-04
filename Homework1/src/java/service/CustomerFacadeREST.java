/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author hotma
 */
import authn.Credentials;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.entities.Customer;
import model.entities.Article;
import java.util.List;
import model.entities.CustomerDTO;

@Stateless
@Path("customer")
public class CustomerFacadeREST {

    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    // Constructor
    public CustomerFacadeREST() {
    }
//GET /rest/api/v1/customer
@GET
@Produces({MediaType.APPLICATION_JSON})
public Response getAllCustomers() {
    List<Customer> customers = em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();

    // Convertir la lista de Customer a una lista de CustomerDTO
    List<CustomerDTO> customerDTOs = customers.stream().map(customer -> {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setUsername(customer.getUsername());
        dto.setEmail(customer.getEmail());
        dto.setFullName(customer.getFullName());

        // Si el cliente tiene artículos, agregamos un enlace al último artículo publicado
        if (!customer.getArticles().isEmpty()) {
            Article latestArticle = customer.getArticles().get(customer.getArticles().size() - 1);
            dto.addLink(new CustomerDTO.Link("/article/" + latestArticle.getId(), "article"));
        }

        return dto;
    }).toList();

    return Response.ok(customerDTOs).build();
}
       
@GET
@Path("{id}")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public Response getCustomerById(@PathParam("id") Long id) {
    // Buscar el cliente en la base de datos
    Customer customer = em.find(Customer.class, id);

    // Si el cliente no existe, devolver 404
    if (customer == null) {
        return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
    }

    // Mapear la información del cliente a un DTO (sin datos sensibles)
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setId(customer.getId());
    customerDTO.setUsername(customer.getUsername());
    customerDTO.setEmail(customer.getEmail());
    customerDTO.setFullName(customer.getFullName());

    // Verificar si el cliente es autor de algún artículo y agregar enlace HATEOAS
    if (!customer.getArticles().isEmpty()) {
        Article latestArticle = customer.getArticles().get(customer.getArticles().size() - 1);
        customerDTO.addLink(new CustomerDTO.Link("/article/" + latestArticle.getId(), "latest-article"));
    }

    // Devolver la respuesta con el DTO del cliente
    return Response.ok(customerDTO).build();
}




@GET
@Path("username/{username}")
@Produces({MediaType.APPLICATION_JSON})
public Response getCustomerByUsername(@PathParam("username") String username) {
    // Busca al cliente en la base de datos
    try {
        Customer customer = em.createQuery("SELECT c FROM Customer c WHERE c.username = :username", Customer.class)
                              .setParameter("username", username)
                              .getSingleResult();

        // Mapea los datos del cliente a un DTO para evitar exponer datos sensibles
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setUsername(customer.getUsername());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setFullName(customer.getFullName());

        return Response.ok(customerDTO).build();
    } catch (Exception e) {
        // Devuelve 404 si no se encuentra al cliente
        return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
    }
}



// PUT /rest/api/v1/customer/{id}
@PUT
@Path("{id}")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public Response updateCustomer(@PathParam("id") Long id, Customer updatedCustomer) {
    Customer customer = em.find(Customer.class, id);
        
    if (customer == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Actualizar la información del cliente
    customer.setUsername(updatedCustomer.getUsername());
    customer.setFullName(updatedCustomer.getFullName());
    customer.setEmail(updatedCustomer.getEmail());
    customer.setPassword(updatedCustomer.getPassword());

    // Guardar el cliente actualizado
    em.merge(customer);

    return Response.ok(customer).build();
}
    
@POST
@Path("validate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response validateUserCredentials(Credentials credentials) {
    try {
        // Buscar el usuario en la base de datos utilizando las credenciales proporcionadas
        Credentials storedCredentials = em.createQuery(
            "SELECT c FROM Credentials c WHERE c.username = :username AND c.password = :password", Credentials.class)
            .setParameter("username", credentials.getUsername())
            .setParameter("password", credentials.getPassword())
            .getSingleResult();

        // Si las credenciales son válidas, devolver un 200 OK con true
        return Response.ok(true).build();
    } catch (Exception e) {
        // Si no se encuentran las credenciales, devolver un 401 Unauthorized con false
        return Response.status(Response.Status.UNAUTHORIZED).entity(false).build();
    }
}


}
