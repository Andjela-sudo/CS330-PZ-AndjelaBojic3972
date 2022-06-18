package com.pz.restapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("createdBy")
    @Expose
     private User createdBy;

    @SerializedName("items")
    @Expose
    private java.util.List<Item> items;

    public List() {
    }

    public List(Long id, String name, User createdBy, java.util.List<Item> items) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.items = items;
    }
    public List( String name, User createdBy, java.util.List<Item> items) {
        this.name = name;
        this.createdBy = createdBy;
        this.items = items;
    }

    @Override
    public String toString() {
        return "List{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdBy=" + createdBy +
                ", items=" + items +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public java.util.List<Item> getItems() {
        return items;
    }

    public void setItems(java.util.List<Item> items) {
        this.items = items;
    }
}
