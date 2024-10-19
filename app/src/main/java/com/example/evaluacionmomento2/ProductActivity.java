package com.example.evaluacionmomento2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacionmomento2.data.adapter.ProductAdapter;
import com.example.evaluacionmomento2.data.dao.ProductDao;
import com.example.evaluacionmomento2.data.model.ProductModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private Button btnCrear, btnActualizar, btnLeer, btnEliminar;
    private EditText tvNombreProducto, tvPrecioProducto;
    private RecyclerView rvProducto;
    private ProductDao productDao;
    private ProductAdapter productAdapter;
    private ProductModel selectedProduct;  // Para manejar el producto seleccionado
    private String selectedProductId; // Para manejar el ID del producto seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Inicializamos las vistas
        btnCrear = findViewById(R.id.btnCrear);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnLeer = findViewById(R.id.btnLeer);  // El botón para leer la lista
        tvNombreProducto = findViewById(R.id.tvNombreProducto);
        tvPrecioProducto = findViewById(R.id.tvPrecioProducto);
        rvProducto = findViewById(R.id.rvProducto);

        // Inicializamos Firestore y el DAO
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        productDao = new ProductDao(db);

        // Configuramos el RecyclerView
        rvProducto.setLayoutManager(new LinearLayoutManager(this));

        // Manejar el botón de crear producto
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un nuevo producto y almacenarlo en Firebase
                String nombreProducto = tvNombreProducto.getText().toString();
                String precioProducto = tvPrecioProducto.getText().toString();

                if (!nombreProducto.isEmpty() && !precioProducto.isEmpty()) {
                    ProductModel product = new ProductModel();
                    product.setProducto(nombreProducto);
                    product.setPrecio(precioProducto);

                    productDao.insert(product, new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            // Limpiar los campos después de crear el producto
                            tvNombreProducto.setText("");
                            tvPrecioProducto.setText("");

                            Toast.makeText(ProductActivity.this, "Producto creado", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Manejar el botón de leer productos
        btnLeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamar a la función para cargar los productos cuando se presiona el botón
                cargarProductos();
            }
        });

        // Manejar el botón de actualizar producto
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si hay un producto seleccionado, actualizamos
                if (selectedProduct != null && selectedProductId != null) {
                    String nombreProducto = tvNombreProducto.getText().toString();
                    String precioProducto = tvPrecioProducto.getText().toString();

                    // Actualizar los datos del producto seleccionado
                    selectedProduct.setProducto(nombreProducto);
                    selectedProduct.setPrecio(precioProducto);

                    // Llamar al DAO para actualizar el producto en Firestore
                    productDao.update(selectedProductId, selectedProduct, new OnSuccessListener<Boolean>() {
                        @Override
                        public void onSuccess(Boolean isSuccess) {
                            if (isSuccess) {
                                Toast.makeText(ProductActivity.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                                // Limpiar los campos después de actualizar el producto
                                tvNombreProducto.setText("");
                                tvPrecioProducto.setText("");
                                selectedProduct = null;
                                selectedProductId = null;

                                // Recargar la lista de productos
                                cargarProductos();
                            } else {
                                Toast.makeText(ProductActivity.this, "Error al actualizar producto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void cargarProductos() {
        // Llamamos al método del DAO para obtener todos los productos
        productDao.getAllProduct(new OnSuccessListener<List<ProductModel>>() {
            @Override
            public void onSuccess(List<ProductModel> productos) {
                if (productos != null && !productos.isEmpty()) {
                    // Inicializar el adapter con la lista de productos
                    productAdapter = new ProductAdapter(productos);
                    rvProducto.setAdapter(productAdapter);

                    // Configurar el listener de clic para cada item
                    productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(ProductModel product) {
                            // Cuando se selecciona un producto, cargarlo en los EditText
                            selectedProduct = product;
                            tvNombreProducto.setText(product.getProducto());
                            tvPrecioProducto.setText(product.getPrecio());

                            // Para actualizar, necesitamos el ID del producto en Firestore
                            selectedProductId = product.getId();
                        }
                    });
                }
            }
        });
    }
}
