import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Buyer extends User implements PurchasingInterface {
    private List<CartItem> cart;

    public Buyer(String username, String email) {
        super(username, email);
        this.cart = new ArrayList<>();
    }

    @Override
    public void addToCart(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (!product.isAvailable(quantity)) {
            throw new IllegalStateException("Product is out of stock or insufficient quantity");
        }

        CartItem existingItem = cart.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            if (!product.isAvailable(newQuantity)) {
                throw new IllegalStateException("Insufficient stock for requested quantity");
            }
            existingItem.setQuantity(newQuantity);
        } else {
            cart.add(new CartItem(product, quantity));
        }
    }

    @Override
    public boolean checkout() {
        if (cart.isEmpty()) {
            return false;
        }

        List<CartItem> itemsToProcess = new ArrayList<>(cart);
        List<String> errors = new ArrayList<>();

        for (CartItem item : itemsToProcess) {
            Product product = item.getProduct();
            if (!product.isAvailable(item.getQuantity())) {
                errors.add(String.format("%s: insufficient stock (requested %d, available %d)",
                        product.getName(), item.getQuantity(), product.getQuantity()));
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalStateException("Checkout failed:\n" + String.join("\n", errors));
        }

        for (CartItem item : itemsToProcess) {
            item.getProduct().reduceQuantity(item.getQuantity());
        }

        cart.clear();
        return true;
    }

    @Override
    public List<CartItem> getCart() {
        return new ArrayList<>(cart);
    }

    @Override
    public void clearCart() {
        cart.clear();
    }

    @Override
    public double getCartTotal() {
        return cart.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public List<Product> browseProducts(List<Seller> sellers) {
        return sellers.stream()
                .flatMap(seller -> seller.getProducts().stream())
                .filter(p -> p.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public String getDashboard() {
        StringBuilder dashboard = new StringBuilder();
        dashboard.append("BUYER DASHBOARD \n");
        dashboard.append("Username: ").append(getUsername()).append("\n");
        dashboard.append("Email: ").append(getEmail()).append("\n");
        dashboard.append("Items in Cart: ").append(cart.size()).append("\n");
        dashboard.append("Cart Total: ₱").append(String.format("%.2f", getCartTotal())).append("\n\n");

        if (cart.isEmpty()) {
            dashboard.append("Cart is empty.\n");
        } else {
            dashboard.append("Cart Items:\n");
            cart.forEach(item -> dashboard.append("- ").append(item.toString()).append("\n"));
        }
        return dashboard.toString();
    }
}
