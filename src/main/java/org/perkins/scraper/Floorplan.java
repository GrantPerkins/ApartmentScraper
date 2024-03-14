package org.perkins.scraper;

public class Floorplan {

    public Floorplan(String name) {
        setName(name);
    }

    @Override
    public String toString() {
        return String.format("Date: %s</br>Name: %s</br>Apartment number: %s</br>Price: %s</br>Deposit: %s</br><a href=\"%s\">URL</a></br></br>", date, name, number, price, deposit, url);
    }

    public String toNewString() {
        return String.format("<b>NEW LISTING</b></br>Date: %s</br>Name: %s</br>Apartment number: %s</br>Price: %s</br>Deposit: %s</br><a href=\"%s\">URL</a></br></br>", date, name, number, price, deposit, url);
    }

    public String toKey() {
        return String.format("%s,%s", number, price);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getNumber() {
        return number;
    }

    public String getFloor() {
        return floor;
    }

    public String getUrl() {
        return url;
    }

    public String getDeposit() {
        return deposit;
    }

    private String name;
    private String price;
    private String date;
    private String number;
    private String floor;
    private String url;


    private String deposit;
}
