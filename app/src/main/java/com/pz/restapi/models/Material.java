package com.pz.restapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Material {
    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("price")
    @Expose
    private Integer price;

    @SerializedName("createdBy")
    @Expose
    private Long createdBy;

    @SerializedName("updatedBy")
    @Expose
    private Long updatedBy;

    @SerializedName("categoryId")
    @Expose
    private Long categoryId;

    @SerializedName("photo")
    @Expose
    private String photo;


    public Material() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Material(Long id, String title, String description, Integer price, Long createdBy, Long updatedBy, Long categoryId, String photo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.categoryId = categoryId;
        this.photo = photo;
    }

    public Material( String title, String description, Integer price, Long createdBy, Long updatedBy, Long categoryId, String photo) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.categoryId = categoryId;
        this.photo = photo;
    }
}
