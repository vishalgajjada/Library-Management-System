package com.velt.libraryapplication.DataClasses;

public class UserData {

    String userName;
    String userEmail;
    String phoneNumber;
    int Books;
    String Reserve;

    public UserData() {

    }

    public UserData(String userName, String userEmail, String phoneNumber, int books, String reserve) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.phoneNumber = phoneNumber;
        Books = books;
        Reserve = reserve;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getBooks() {
        return Books;
    }

    public void setBooks(int books) {
        Books = books;
    }

    public String getReserve() {
        return Reserve;
    }

    public void setReserve(String reserve) {
        Reserve = reserve;
    }
}
