/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

/**
 *
 * @author hotma
 */
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "articles") // Tabla en la base de datos
public class Article {

    @Id
    @SequenceGenerator(name="Article_Gen", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Article_Gen")
    private Long id;

     @Column(nullable = false) // Requerido
    private String title;

    @Column(nullable = false, length = 500) // Requerido, máximo 500 caracteres
    private String summary;
    
    @Lob
    @Column(nullable = false)
    private String content; // Nuevo campo para el contenido completo

    @Temporal(TemporalType.DATE)
    private Date publishDate;

    @Column(nullable = false) // Estado (público/privado)
    private boolean isPublic;

    @Column(nullable = false) // Inicializa vistas como 0
    private int views = 0;

    @ManyToOne // Un autor por artículo
    @JoinColumn(name = "author_id", nullable = false)
    private Customer author;

    @ManyToMany(cascade = CascadeType.PERSIST ) 
    @JoinTable(
        name = "article_topics",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private List<Topic> topics;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
    if (summary.split("\\s+").length > 20) {
        throw new IllegalArgumentException("El resumen no puede tener más de 20 palabras.");
    }
    this.summary = summary;
}
    
        // Getters y Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Customer getAuthor() {
        return author;
    }

    public void setAuthor(Customer author) {
        this.author = author;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
  
    
}
