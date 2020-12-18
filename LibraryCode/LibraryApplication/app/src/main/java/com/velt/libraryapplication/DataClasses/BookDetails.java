package com.velt.libraryapplication.DataClasses;

public class BookDetails {

    String title;
    String author;
    String category;
    Long copies;

    public BookDetails() {

    }

    public BookDetails(String title, String author, String category, Long copies) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.copies = copies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getCopies() {
        return copies;
    }

    public void setCopies(Long copies) {
        this.copies = copies;
    }
}
