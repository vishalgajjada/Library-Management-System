package com.velt.libraryapplication.DataClasses;

public class ReserveData {

    String bookTitle;
    String userPhoneNumber;

    public ReserveData() {

    }

    public ReserveData(String bookTitle, String userPhoneNumber) {
        this.bookTitle = bookTitle;
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
}
