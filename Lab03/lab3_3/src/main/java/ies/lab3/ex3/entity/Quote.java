package ies.lab3.ex3.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity 
@Table(name = "quotes")
public class Quote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotBlank(message = "Quote is mandatory")
    private String quote;

    // Define the Many-to-One relationship with Movie
    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false)  // Specifies the foreign key column for the relationship
    private Movie movie;

    // Constructors, getters, setters, and toString
    public Quote() {
    }

    public Quote(String quote, Movie movie) {
        this.quote = quote;
        this.movie = movie;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }
    
    public void setQuote(String quote) {
        this.quote = quote;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    
    @Override   
    public String toString() {
        return "Quote [id=" + id + ", quote=" + quote + ", movie=" + movie + "]";
    }
}
