package com.pz.restapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {

    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("createdBy")
    @Expose
    private Long createdBy;

    @SerializedName("materials")
    @Expose
    private List<Material> materials;

    public Category() {
    }

    public Category(Long id, String name, Long createdBy, List<Material> materials) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.materials = materials;
    }

    public Category( String name, Long createdBy, List<Material> materials) {
        this.name = name;
        this.createdBy = createdBy;
        this.materials = materials;
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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
}
