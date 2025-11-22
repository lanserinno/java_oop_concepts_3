import java.util.List;

public interface PurchasingInterface {
    void addToCart(Product product, int quantity);

    boolean checkout();

    List<CartItem> getCart();

    void clearCart();

    double getCartTotal();
}
