package com.jichao.hello;

import java.util.Date;

public class Book {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(Date publish_date) {
        this.publish_date = publish_date;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public Book(Integer id, String name, Integer price, Date publish_date, Integer author_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.publish_date = publish_date;
        this.author_id = author_id;
    }

    private Integer id;
    private String name;
    private Integer price;
    private Date publish_date;
    private Integer author_id;
}
