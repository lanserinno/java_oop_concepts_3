import java.util.Objects;

public class Product {
    private String name;
    private double price;
    private int quantity;
    private Seller seller;

    public Product(String name, double price, int quantity, Seller seller) {
        validateName(name);
        validatePrice(price);
        validateInitialQuantity(quantity);
        validateSeller(seller);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.seller = seller;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
    }

    private void validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (Double.isInfinite(price) || Double.isNaN(price)) {
            throw new IllegalArgumentException("Invalid price value");
        }
    }

    private void validateInitialQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    private void validateSeller(Seller seller) {
        if (seller == null) {
            throw new IllegalArgumentException("Product must have a seller");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        validatePrice(price);
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public Seller getSeller() {
        return seller;
    }

    public boolean isAvailable(int requestedQuantity) {
        return quantity >= requestedQuantity && requestedQuantity > 0;
    }

    public void reduceQuantity(int amount) {
        if (!isAvailable(amount)) {
            throw new IllegalStateException("Insufficient stock");
        }
        this.quantity -= amount;
    }

    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.quantity += amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Product product = (Product) obj;
        return Double.compare(product.price, price) == 0 &&
                Objects.equals(name, product.name) &&
                Objects.equals(seller, product.seller);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, seller);
    }

    @Override
    public String toString() {
        return "Product{name='" + name + "', price=" + price + ", quantity=" + quantity + "}";
    }
}
