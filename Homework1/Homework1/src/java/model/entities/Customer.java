/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

import jakarta.persistence.*;
import java.util.List;
import model.entities.Article;
import java.util.ArrayList;
import jakarta.json.bind.annotation.JsonbTransient;

@Entity
@Table(name = "customers") // Nombre de la tabla en la base de datos
public class Customer {

    @Id
    @SequenceGenerator(name = "Customer_Gen", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Customer_Gen")
    private Long id;

    @Column(nullable = false, unique = true) // Requerido y único
    private String username;

    @JsonbTransient
    //@Column(name = "PASSWORD") // Requerido
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String fullName;
    
    @JsonbTransient
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles; // Lista de artículos que el usuario ha creado

    // Constructor vacío
    public Customer() {
    }

    // Constructor con parámetros
    public Customer(String username, String password, String email/*String fullName*/) {
        this.username = username;
        this.password = password;
        this.email = email;
       // this.fullName = fullName;
    }
    


    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("El correo electrónico no es válido.");
        }
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo no puede estar vacío.");
        }
        this.fullName = fullName;
    }
    @JsonbTransient
    public List<Article> getArticles() {
        return articles;
    }
    

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    // Método auxiliar para agregar un artículo al usuario
    public void addArticle(Article article) {
        articles.add(article);
        article.setAuthor(this);
    }

    // Método auxiliar para eliminar un artículo del usuario
    public void removeArticle(Article article) {
        articles.remove(article);
        article.setAuthor(null);
        
    }
    
      public boolean isAuthorOf(Long articleId) {
        return articles.stream().anyMatch(article -> article.getId().equals(articleId));
    }
      
     @Transient
     private List<Link> links = new ArrayList<>();
     
      // Métodos para gestionar los enlaces HATEOAS
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addLink(Link link) {
        this.links.add(link);
    }

    // Clase interna para representar un enlace HATEOAS
    public static class Link {
        private String href;
        private String rel;

        public Link(String href, String rel) {
            this.href = href;
            this.rel = rel;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getRel() {
            return rel;
        }

        public void setRel(String rel) {
            this.rel = rel;
        }
    }
}
