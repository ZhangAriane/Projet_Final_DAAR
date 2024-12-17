package org.example.projet_final_daar.model;

import jakarta.persistence.*;


public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    String title;
    String authors;
    String image;
    String txt;



    public Livre(String title, String authors, String image, String txt) {
        this.title = title;
        this.authors = authors;
        this.image = image;
        this.txt = txt;
    }

    public Livre() {

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
