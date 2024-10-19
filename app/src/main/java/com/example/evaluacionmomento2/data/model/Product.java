package com.example.evaluacionmomento2.data.model;

public class Product {

    private String id;
    private String producto;
    private String precio;

    public Product() {
    }

    public Product(String producto, String precio) {
        this.producto = producto;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
