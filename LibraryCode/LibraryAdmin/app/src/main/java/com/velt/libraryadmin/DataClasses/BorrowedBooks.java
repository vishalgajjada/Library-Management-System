package com.velt.libraryadmin.DataClasses;

public class BorrowedBooks {
    String title;
    String author;
    String category;
    String date;

    public BorrowedBooks() {

    }

    public BorrowedBooks(String title, String author, String category, String date) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
