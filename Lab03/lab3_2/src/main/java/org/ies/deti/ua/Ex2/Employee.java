package org.ies.deti.ua.Ex2;

import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;

@Entity 
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false) 
    private String name;
    
    // If the client does not provide an email, the server will return a VALIDATION error
    @NotBlank(message = "Email is mandatory")
    // But if the line above was not present, the following error would be thrown to the terminal:
    // ERROR 189314 --- [nio-8080-exec-1] o.h.engine.jdbc.spi.SqlExceptionHelper   : Column 'email' cannot be null
    // And this because of the line below (nullable = false)
    @Column(nullable = false, unique = true)
    private String email;

    // standard constructors / setters / getters / toString
    public Employee() {
    }

    public Employee(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override   
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", email=" + email + "]";
    }
}