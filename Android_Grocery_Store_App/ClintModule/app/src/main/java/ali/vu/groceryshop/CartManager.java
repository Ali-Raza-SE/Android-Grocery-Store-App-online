package ali.vu.groceryshop;

import java.util.ArrayList;

public class CartManager {
    private static CartManager instance;
    private ArrayList<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(CartItem cartItem) {
        cartItems.add(cartItem);
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }


    // Method to clear the cart
    public void clearCart() {
        cartItems.clear();
    }
}
