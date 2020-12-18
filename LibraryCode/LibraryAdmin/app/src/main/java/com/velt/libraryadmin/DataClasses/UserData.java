package com.velt.libraryadmin.DataClasses;

public class UserData {

    String userName;
    String userEmail;
    String homeAddress;
    int Books;
    String Reserve;

    public UserData() {

    }

    public UserData(String userName, String userEmail, String homeAddress, int books, String reserve) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.homeAddress = homeAddress;
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

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
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
