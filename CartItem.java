import java.util.Objects;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        CartItem cartItem = (CartItem) obj;
        return Objects.equals(product, cartItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }

    @Override
    public String toString() {
        return String.format("%s x%d = ₱%.2f", product.getName(), quantity, getTotalPrice());
    }
}
