package com.example.magucho.Modelo;

public class Libro {

    private String photo;
    private String tittle;
    private String cost;
    private String gender;
    private String year;

    public  Libro(){}
    public Libro(String photo, String tittle, String cost, String gender, String year) {
        this.photo = photo;
        this.tittle = tittle;
        this.cost = cost;
        this.gender = gender;
        this.year = year;
    }
    public Libro( String tittle, String cost, String gender, String year) {
        this.tittle = tittle;
        this.cost = cost;
        this.gender = gender;
        this.year = year;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
