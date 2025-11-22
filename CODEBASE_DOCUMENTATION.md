# Online Marketplace - Codebase Documentation

## 1. FILE BREAKDOWN AND PURPOSES

### User.java (Abstract Class)
**Purpose:** Base abstract class representing the blueprint for all users in the marketplace.
- Defines shared identity (username, email) for all users
- Provides validation for user data
- Declares abstract method `getDashboard()` that must be implemented by subclasses
- Provides common functionality like equals(), hashCode(), and toString()

**Key Responsibilities:**
- User data validation (username length, email format)
- Encapsulation of user identity fields
- Abstract contract for dashboard generation

---

### Seller.java (Concrete Class)
**Purpose:** Represents sellers who can upload and manage products.
- Extends `User` class
- Manages a list of products owned by the seller
- Provides functionality to upload, find, and update products
- Implements seller-specific dashboard display

**Key Responsibilities:**
- Product management (upload, list, find, update quantity)
- Display seller dashboard with product listings
- Maintain list of seller's products

---

### Buyer.java (Concrete Class)
**Purpose:** Represents buyers who can browse and purchase products.
- Extends `User` class
- Implements `PurchasingInterface` for purchasing operations
- Manages shopping cart functionality
- Provides product browsing capabilities

**Key Responsibilities:**
- Shopping cart management (add, view, clear)
- Checkout process with inventory validation
- Product browsing from all sellers
- Display buyer dashboard with cart information

---

### Product.java (Concrete Class)
**Purpose:** Represents a product in the marketplace with inventory management.
- Stores product details (name, price, quantity, seller)
- Validates product data
- Provides inventory management (increase/decrease quantity)
- Checks product availability

**Key Responsibilities:**
- Product data validation
- Inventory quantity management
- Stock availability checking
- Maintains relationship with seller

---

### CartItem.java (Concrete Class)
**Purpose:** Represents an item in a buyer's shopping cart.
- Contains a product and the quantity to purchase
- Calculates total price for the cart item
- Validates cart item data

**Key Responsibilities:**
- Store product and quantity for cart
- Calculate line item total (price × quantity)
- Validate cart item data

---

### PurchasingInterface.java (Interface)
**Purpose:** Defines the contract for purchasing operations.
- Specifies methods required for purchasing flow
- Ensures consistent purchasing behavior
- Used by `Buyer` class to implement purchasing functionality

**Key Responsibilities:**
- Define purchasing contract
- Ensure all purchasers follow same interface
- Abstract purchasing operations

---

### MarketplaceApplication.java (GUI Application)
**Purpose:** Main Swing application that provides the user interface.
- Creates and manages GUI components
- Handles user interactions
- Manages user accounts (sellers and buyers)
- Coordinates between different components
- Displays dashboards and product information

**Key Responsibilities:**
- GUI creation and management
- User account management UI
- Product listing and selection UI
- Dashboard display
- User interaction handling

---

## 2. DATA TYPES FOR EACH FIELD

### User.java
| Field | Data Type | Description |
|-------|-----------|-------------|
| `username` | `String` | User's username (min 3 characters) |
| `email` | `String` | User's email address (must contain '@') |

### Seller.java
| Field | Data Type | Description |
|-------|-----------|-------------|
| `products` | `List<Product>` | List of products owned by seller |
| (inherited) `username` | `String` | Inherited from User |
| (inherited) `email` | `String` | Inherited from User |

### Buyer.java
| Field | Data Type | Description |
|-------|-----------|-------------|
| `cart` | `List<CartItem>` | Shopping cart containing cart items |
| (inherited) `username` | `String` | Inherited from User |
| (inherited) `email` | `String` | Inherited from User |

### Product.java
| Field | Data Type | Description |
|-------|-----------|-------------|
| `name` | `String` | Product name (cannot be empty) |
| `price` | `double` | Product price (must be > 0) |
| `quantity` | `int` | Available quantity (cannot be negative) |
| `seller` | `Seller` | Reference to the seller who owns this product |

### CartItem.java
| Field | Data Type | Description |
|-------|-----------|-------------|
| `product` | `Product` | Reference to the product being purchased |
| `quantity` | `int` | Quantity to purchase (must be > 0) |

### MarketplaceApplication.java
| Field | Data Type | Description |
|-------|-----------|-------------|
| `sellers` | `List<Seller>` | List of all seller accounts |
| `buyers` | `List<Buyer>` | List of all buyer accounts |
| `currentSeller` | `Seller` | Currently selected seller |
| `currentBuyer` | `Buyer` | Currently selected buyer |
| `currentUser` | `User` | Currently selected user (polymorphic reference) |
| `mainPanel` | `JPanel` | Main container panel |
| `userPanel` | `JPanel` | User management panel |
| `actionPanel` | `JPanel` | Actions panel |
| `displayPanel` | `JPanel` | Dashboard display panel |
| `displayArea` | `JTextArea` | Text area for displaying dashboards |
| `actionCardLayout` | `CardLayout` | Layout manager for action panels |
| `actionCardContainer` | `JPanel` | Container for action cards |
| `buyerProductList` | `JList<Product>` | List component for displaying products |
| `productListModel` | `DefaultListModel<Product>` | Model for product list |
| `userCardLayout` | `CardLayout` | Layout manager for user management cards |
| `userCardContainer` | `JPanel` | Container for user management cards |
| `userCombo` | `JComboBox<User>` | Dropdown for selecting users |

---

## 3. OOP CONCEPTS BREAKDOWN

### 3.1 ABSTRACTION

**Definition:** Hiding implementation details and showing only essential features.

**Implementation:**

1. **Abstract Class (User.java)**
   - `User` is declared as `abstract class`
   - Contains abstract method: `public abstract String getDashboard()`
   - Defines common structure but leaves dashboard implementation to subclasses
   - Cannot be instantiated directly: `new User()` is not allowed

2. **Interface (PurchasingInterface.java)**
   - Defines contract for purchasing operations
   - No implementation details, only method signatures
   - Forces implementing classes to provide specific behavior

**Example:**
```java
// User class - abstract blueprint
public abstract class User {
    public abstract String getDashboard();  // Must be implemented by subclasses
}

// Cannot create User directly
// User user = new User(...);  // ERROR!

// Must use concrete subclasses
Seller seller = new Seller(...);  // OK
Buyer buyer = new Buyer(...);     // OK
```

---

### 3.2 ENCAPSULATION

**Definition:** Bundling data and methods together, hiding internal implementation.

**Implementation:**

1. **Private Fields**
   - All fields are declared `private`
   - Cannot be accessed directly from outside the class
   - Must use getters/setters to access/modify

2. **Getter/Setter Methods**
   - Controlled access through public methods
   - Validation occurs in setters
   - Data integrity maintained

**Examples:**

```java
// User.java - Private fields
private String username;
private String email;

// Controlled access through getters
public String getUsername() {
    return username;
}

// Validation in setters
public void setUsername(String username) {
    validateUsername(username);  // Validation before setting
    this.username = username;
}
```

**Encapsulation in other classes:**
- `Product`: Private fields (name, price, quantity, seller) with validation
- `Seller`: Private `products` list, accessed via `getProducts()` returning copy
- `Buyer`: Private `cart` list, accessed via `getCart()` returning copy
- `CartItem`: Private product and quantity, accessed through getters

**Benefits:**
- Data validation ensures consistency
- Internal representation can change without affecting users
- Prevents invalid state (e.g., negative prices, empty usernames)

---

### 3.3 INHERITANCE

**Definition:** Mechanism where a new class inherits properties and methods from an existing class.

**Implementation:**

1. **Single Inheritance (Seller and Buyer extend User)**
   ```java
   public class Seller extends User {
       // Inherits: username, email, getUsername(), getEmail(), etc.
       // Adds: products list, uploadProduct(), getDashboard() implementation
   }
   
   public class Buyer extends User {
       // Inherits: username, email, getUsername(), getEmail(), etc.
       // Adds: cart list, purchasing methods, getDashboard() implementation
   }
   ```

2. **Constructor Chaining**
   - Subclass constructors call `super()` to initialize parent class
   ```java
   public Seller(String username, String email) {
       super(username, email);  // Calls User constructor
       this.products = new ArrayList<>();
   }
   ```

3. **Method Inheritance**
   - Subclasses inherit all public/protected methods from parent
   - Can override inherited methods (e.g., `getDashboard()`)

**Inheritance Hierarchy:**
```
        User (Abstract)
       /              \
   Seller           Buyer
```

---

### 3.4 POLYMORPHISM

**Definition:** Ability of objects to take multiple forms or behave differently based on context.

**Implementation:**

1. **Method Overriding (Runtime Polymorphism)**
   - Both `Seller` and `Buyer` override `getDashboard()` method
   - Same method name, different implementations
   - Java determines which version to call at runtime

   ```java
   // User.java
   public abstract String getDashboard();
   
   // Seller.java
   @Override
   public String getDashboard() {
       // Returns seller-specific dashboard
       return "SELLER DASHBOARD...";
   }
   
   // Buyer.java
   @Override
   public String getDashboard() {
       // Returns buyer-specific dashboard
       return "BUYER DASHBOARD...";
   }
   ```

2. **Polymorphic References**
   - `User` reference can point to `Seller` or `Buyer` objects
   - Same reference type, different object types
   - Method calls resolve to correct implementation at runtime

   ```java
   // MarketplaceApplication.java
   private User currentUser;  // Can hold Seller or Buyer
   
   // Later in code:
   currentUser = new Seller(...);  // User reference points to Seller
   currentUser = new Buyer(...);   // Same reference points to Buyer
   
   // Polymorphic method call
   String dashboard = currentUser.getDashboard();  
   // Calls Seller.getDashboard() or Buyer.getDashboard() based on actual object
   ```

3. **Instanceof Checks**
   - Runtime type checking to determine actual object type
   ```java
   if (currentUser instanceof Seller) {
       currentSeller = (Seller) currentUser;  // Downcast
   } else if (currentUser instanceof Buyer) {
       currentBuyer = (Buyer) currentUser;    // Downcast
   }
   ```

**Polymorphism Examples:**
```java
// Same interface, different behavior
User user1 = new Seller("John", "john@email.com");
User user2 = new Buyer("Alice", "alice@email.com");

System.out.println(user1.getDashboard());  
// Output: "SELLER DASHBOARD..."

System.out.println(user2.getDashboard());  
// Output: "BUYER DASHBOARD..."

// Same method call, different results based on actual object type
```

---

### 3.5 INTERFACE

**Definition:** Contract that defines what methods a class must implement.

**Implementation:**

1. **Interface Declaration (PurchasingInterface.java)**
   ```java
   public interface PurchasingInterface {
       void addToCart(Product product, int quantity);
       boolean checkout();
       List<CartItem> getCart();
       void clearCart();
       double getCartTotal();
   }
   ```

2. **Interface Implementation (Buyer.java)**
   ```java
   public class Buyer extends User implements PurchasingInterface {
       // Must implement all methods from PurchasingInterface
       
       @Override
       public void addToCart(Product product, int quantity) { ... }
       
       @Override
       public boolean checkout() { ... }
       
       @Override
       public List<CartItem> getCart() { ... }
       
       @Override
       public void clearCart() { ... }
       
       @Override
       public double getCartTotal() { ... }
   }
   ```

3. **Multiple Type Support**
   - `Buyer` is both a `User` (inheritance) and a `PurchasingInterface` (interface)
   - Can be used polymorphically as either type

**Interface Benefits:**
- Defines contract without implementation details
- Allows multiple classes to follow same interface
- Enables loose coupling between components
- Supports "programming to interface" principle

---

## 4. OOP CONCEPTS SUMMARY TABLE

| OOP Concept | Where Used | Example |
|-------------|------------|---------|
| **Abstraction** | `User` abstract class, `PurchasingInterface` | Abstract `getDashboard()` method |
| **Encapsulation** | All classes | Private fields with getters/setters + validation |
| **Inheritance** | `Seller extends User`, `Buyer extends User` | Subclasses inherit username, email fields |
| **Polymorphism** | Method overriding, polymorphic references | Different `getDashboard()` implementations, `User currentUser` can be Seller/Buyer |
| **Interface** | `Buyer implements PurchasingInterface` | Defines purchasing contract for buyers |

---

## 5. DATA VALIDATION EXAMPLES

### User.java
- **Username**: Must not be empty, minimum 3 characters
- **Email**: Must not be empty, must contain '@' character

### Product.java
- **Name**: Cannot be null or empty
- **Price**: Must be > 0, cannot be infinite or NaN
- **Quantity** (initial): Must be > 0
- **Quantity** (update): Cannot be negative (can be 0 for out of stock)
- **Seller**: Cannot be null

### CartItem.java
- **Product**: Cannot be null
- **Quantity**: Must be > 0

### Buyer.java
- **addToCart()**: Validates product not null, quantity > 0, sufficient stock
- **checkout()**: Validates all cart items have sufficient stock before processing

---

## 6. MEMORY MANAGEMENT FEATURES

1. **Defensive Copies**: `getProducts()` and `getCart()` return new ArrayList copies
2. **Stream API**: Efficient filtering and processing (e.g., `browseProducts()`)
3. **Appropriate Collections**: ArrayList for dynamic lists, minimal memory footprint
4. **Validation**: Prevents invalid objects from being created, reducing errors

---

This documentation provides a complete overview of the codebase structure, data types, and OOP implementation.

