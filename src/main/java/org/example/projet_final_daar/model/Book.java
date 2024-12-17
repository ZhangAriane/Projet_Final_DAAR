package org.example.projet_final_daar.model;

import jakarta.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 1000)
    String title;
    @Column(length = 1000)
    String authors;
    @Column(length = 1000)
    String image;
    @Column(length = 1000)
    String txt;



    public Book(String title, String authors, String image, String txt) {
        this.title = title;
        this.authors = authors;
        this.image = image;
        this.txt = txt;
    }

    public Book() {

    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {return title;}

    public String getAuthors() {return authors;}

    public String getImage() {return image;}

    public String getTxt() {return txt;}

    public void setTitle(String title) {this.title = title;}

    public void setAuthors(String authors) {this.authors = authors;}

    public void setImage(String image) {this.image = image;}

    public void setTxt(String txt) {this.txt = txt;}


}
