import java.util.ArrayList;
import java.util.List;

public class Seller extends User {
    private List<Product> products;

    public Seller(String username, String email) {
        super(username, email);
        this.products = new ArrayList<>();
    }

    public void uploadProduct(String name, double price, int quantity) {
        Product product = new Product(name, price, quantity, this);
        products.add(product);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public Product findProduct(String name) {
        return products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void updateProductQuantity(String productName, int newQuantity) {
        Product product = findProduct(productName);
        if (product != null) {
            product.setQuantity(newQuantity);
        }
    }

    @Override
    public String getDashboard() {
        StringBuilder dashboard = new StringBuilder();
        dashboard.append("SELLER DASHBOARD \n");
        dashboard.append("Username: ").append(getUsername()).append("\n");
        dashboard.append("Email: ").append(getEmail()).append("\n");
        dashboard.append("Total Products Listed: ").append(products.size()).append("\n\n");

        if (products.isEmpty()) {
            dashboard.append("No products listed yet.\n");
        } else {
            dashboard.append("Products:\n");
            products.forEach(p -> {
                dashboard.append(String.format("- %s: ₱%.2f (Qty: %d)\n",
                        p.getName(), p.getPrice(), p.getQuantity()));
            });
        }
        return dashboard.toString();
    }
}
