package ali.vu.groceryshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<CartItem> cartItems;

    public CartAdapter(CartActivity cartActivity, ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView productNameTextView;
        private TextView quantityTextView;
        private TextView totalPriceTextView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
        }

        public void bind(CartItem cartItem) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            double productPrice = Double.parseDouble(product.getPrice());
            double totalPrice = productPrice * quantity;


            productNameTextView.setText(product.getTitle());
            quantityTextView.setText("Quantity: " + quantity);
            totalPriceTextView.setText("Total Price: " + String.format("%.2f", totalPrice));
        }
    }
}
