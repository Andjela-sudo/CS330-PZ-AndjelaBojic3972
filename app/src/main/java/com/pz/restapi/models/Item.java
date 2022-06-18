package com.pz.restapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;

    @SerializedName("listId")
    @Expose
    private Long listId;

    @SerializedName("material")
    @Expose
    private Material material;

    @SerializedName("kolicina")
    @Expose
    private Integer kolicina;

    public Item() {
    }

    public Item(Long id, Long listId, Material material, Integer kolicina) {
        this.id = id;
        this.listId = listId;
        this.material = material;
        this.kolicina = kolicina;
    }

    public Item( Long listId, Material material, Integer kolicina) {
        this.listId = listId;
        this.material = material;
        this.kolicina = kolicina;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", listId=" + listId +
                ", material title=" + material.getTitle() +
                ", kolicina=" + kolicina +
                '}';
    }
}
