package com.example.evaluacionmomento2.data.dao;

import android.util.Log;

import com.example.evaluacionmomento2.data.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDao {

    private static final String TAG = "ProductDao";
    private static final String COLLECTION_NAME = "productos";

    private final FirebaseFirestore db;


    public ProductDao(FirebaseFirestore db) {
        this.db = db;
    }

    public void insert(Product product, OnSuccessListener<String> listener) {
        Map<String, Object> productData = new HashMap<>();
        productData.put("producto", product.getProducto());
        productData.put("precio", product.getPrecio());


        db.collection(COLLECTION_NAME)
                .add(productData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "onSuccess: " + documentReference.getId());
                    listener.onSuccess(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "onFailure: ", e);
                    listener.onSuccess(null);
                });
    }


    public void update(String id, Product product, OnSuccessListener<Boolean> listener) {
        Map<String, Object> productData = new HashMap<>();
        productData.put("producto", product.getProducto());
        productData.put("precio", product.getPrecio());


        db.collection(COLLECTION_NAME)
                .document(id)
                .update(productData)
                .addOnSuccessListener(unused -> listener.onSuccess(true))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "onFailure: ", e);
                    listener.onSuccess(false);
                });
    }
    public void getById(String id, OnSuccessListener<Product> listener) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Product product = document.toObject(Product.class);
                            listener.onSuccess(product);
                        } else {
                            listener.onSuccess(null);
                        }
                    } else {
                        Log.e(TAG, "onComplete: ", task.getException());
                        listener.onSuccess(null);
                    }
                });
    }
    public void getAllProduct(OnSuccessListener<List<Product>> listener) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Product> facturaList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Product factura = documentSnapshot.toObject(Product.class);
                        facturaList.add(factura);
                    }
                    Log.d(TAG, "Productos cargados: " + facturaList.size());
                    listener.onSuccess(facturaList);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al cargar productos", e);
                    listener.onSuccess(null);
                });
    }

    public void delete(String id, OnSuccessListener<Boolean> listener) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .delete()
                .addOnSuccessListener(unused -> listener.onSuccess(true))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "onFailure: ", e);
                    listener.onSuccess(false);
                });
    }
}
