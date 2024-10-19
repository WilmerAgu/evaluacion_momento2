package com.example.evaluacionmomento2.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacionmomento2.R;
import com.example.evaluacionmomento2.data.model.ProductModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductModel> product;
    private OnItemClickListener listener; // Listener para los clics

    public ProductAdapter(List<ProductModel> product) {
        this.product = product;
    }

    // Interfaz para manejar el evento de clic
    public interface OnItemClickListener {
        void onItemClick(ProductModel product);
    }

    // Método para asignar el listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductModel product = this.product.get(position);

        holder.producto.setText(product.getProducto());
        holder.precio.setText(product.getPrecio());

        // Manejar el clic en cada item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product); // Pasar el producto seleccionado
            }
        });
    }



    @Override
    public int getItemCount() {
        return product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView producto;
        public TextView precio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            producto = itemView.findViewById(R.id.tvListarProducto);
            precio = itemView.findViewById(R.id.tvListarPrecio);
        }
    }
}
