package com.example.app;
import java.io.Serializable;

public class Book  implements Serializable{
    private int id;
    private String title;
    private String author;
    private String description;

    //Constructor vacío
    public Book() {}

    //Constructor Completo
    public Book (int id, String title, String author, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
    }


    //Getters y Setters
    public int getId() {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getAuthor () {
        return author;
    }

    public void setAuthor (String author) {
        this.author = author;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    @Override
    public String toString () {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
