package com.jichao.hello;

import java.util.Date;

public class Author {
    public Author(Integer id, String last_name, String first_name, Integer price, Integer ammount, Date publish_date) {
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.price = price;
        this.amount = ammount;
        this.publish_date = publish_date;
    }


    Integer id;
    String last_name;
    String first_name;
    Integer price;
    Integer amount;
    Date publish_date;
    public String toString() {
        return "id: " + id + "," + last_name + "," + first_name + ", " + price + ", " + amount + ", " + publish_date;
    }
}
