package ies.lab3.ex3;

import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;

@Entity 
@Table(name = "quotes")
public class Quote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @NotBlank(message = "Quote is mandatory")
    @Column(nullable = false, unique = true)
    private String quote;

    @NotBlank(message = "Movie is mandatory")
    @Column(nullable = false) 
    private String movie;

    // standard constructors / setters / getters / toString
    public Quote() {
    }

    public Quote(String quote, String movie) {
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

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }
    
    @Override   
    public String toString() {
        return "Quote [id=" + id + ", quote=" + quote + ", movie=" + movie + "]";
    }
}