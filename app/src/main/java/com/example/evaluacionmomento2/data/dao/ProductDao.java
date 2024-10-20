package com.example.evaluacionmomento2.data.dao;

import android.util.Log;

import com.example.evaluacionmomento2.data.model.ProductModel;
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

    public void insert(ProductModel product, OnSuccessListener<String> listener) {
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


    public void update(String id, ProductModel product, OnSuccessListener<Boolean> listener) {
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
    public void getById(String id, OnSuccessListener<ProductModel> listener) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ProductModel product = document.toObject(ProductModel.class);
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
    public void getAllProduct(OnSuccessListener<List<ProductModel>> listener) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ProductModel> productList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ProductModel product = documentSnapshot.toObject(ProductModel.class);
                        if (product != null) {
                            product.setId(documentSnapshot.getId());  // Asignamos el ID del documento al modelo
                            productList.add(product);
                        }
                    }
                    Log.d(TAG, "Productos cargados: " + productList.size());
                    listener.onSuccess(productList);
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
                .addOnSuccessListener(aVoid -> listener.onSuccess(true))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al eliminar el producto", e);
                    listener.onSuccess(false);
                });
    }

}
