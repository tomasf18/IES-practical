package ies.lab3.ex3;

import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;

@Entity 
@Table(name = "movies")
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title is mandatory")
    @Column(nullable = false) 
    private String title;
    
    @NotBlank(message = "Year is mandatory")
    @Column(nullable = false)
    private String year;

    // standard constructors / setters / getters / toString
    public Movie() {
    }

    public Movie(String title, String year) {
        this.title = title;
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override   
    public String toString() {
        return "Movie [id=" + id + ", title=" + title + ", year=" + year + "]";
    }
}