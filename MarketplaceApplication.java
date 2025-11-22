import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceApplication extends JFrame {
    private List<Seller> sellers;
    private List<Buyer> buyers;
    private Seller currentSeller;
    private Buyer currentBuyer;
    private User currentUser;

    private JPanel mainPanel;
    private JPanel userPanel;
    private JPanel actionPanel;
    private JPanel displayPanel;
    private JTextArea displayArea;
    private CardLayout actionCardLayout;
    private JPanel actionCardContainer;
    private JList<Product> buyerProductList;
    private DefaultListModel<Product> productListModel;
    private CardLayout userCardLayout;
    private JPanel userCardContainer;
    private JComboBox<User> userCombo;

    public MarketplaceApplication() {
        sellers = new ArrayList<>();
        buyers = new ArrayList<>();

        setTitle("Online Marketplace");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createComponents();
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void createComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        userPanel = createUserPanel();
        actionPanel = createActionPanel();
        displayPanel = createDisplayPanel();

        mainPanel.add(userPanel, BorderLayout.NORTH);
        mainPanel.add(actionPanel, BorderLayout.CENTER);
        mainPanel.add(displayPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("User Management"));

        userCardLayout = new CardLayout();
        userCardContainer = new JPanel(userCardLayout);

        JPanel mainMenuPanel = createMainMenuPanel();
        JPanel createAccountPanel = createCreateAccountPanel();
        JPanel chooseAccountPanel = createChooseAccountPanel();

        userCardContainer.add(mainMenuPanel, "MAIN");
        userCardContainer.add(createAccountPanel, "CREATE");
        userCardContainer.add(chooseAccountPanel, "CHOOSE");

        userCardLayout.show(userCardContainer, "MAIN");

        panel.add(userCardContainer, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton createAccountBtn = new JButton("Create Account");
        JButton chooseAccountBtn = new JButton("Choose Account");

        createAccountBtn.setPreferredSize(new Dimension(200, 40));
        chooseAccountBtn.setPreferredSize(new Dimension(200, 40));

        createAccountBtn.addActionListener(e -> userCardLayout.show(userCardContainer, "CREATE"));
        chooseAccountBtn.addActionListener(e -> {
            if (userCombo != null) {
                refreshUserCombo(userCombo);
            }
            userCardLayout.show(userCardContainer, "CHOOSE");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createAccountBtn, gbc);
        gbc.gridy = 1;
        panel.add(chooseAccountBtn, gbc);

        return panel;
    }

    private JPanel createCreateAccountPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel typeLabel = new JLabel("User Type:");
        JComboBox<String> typeCombo = new JComboBox<>(new String[] { "Seller", "Buyer" });
        JLabel nameLabel = new JLabel("Username:");
        JTextField nameField = new JTextField(20);
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JButton createBtn = new JButton("Create Account");
        JButton backBtn = new JButton("Back");

        createBtn.addActionListener(e -> {
            try {
                String type = (String) typeCombo.getSelectedItem();
                String username = nameField.getText().trim();
                String email = emailField.getText().trim();

                User user;
                if ("Seller".equals(type)) {
                    user = new Seller(username, email);
                    sellers.add((Seller) user);
                } else {
                    user = new Buyer(username, email);
                    buyers.add((Buyer) user);
                }

                if (userCombo != null) {
                    refreshUserCombo(userCombo);
                }
                showMessage("Account created: " + username);
                nameField.setText("");
                emailField.setText("");
                userCardLayout.show(userCardContainer, "MAIN");
            } catch (Exception ex) {
                showError("Error creating account: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> userCardLayout.show(userCardContainer, "MAIN"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(typeLabel, gbc);
        gbc.gridx = 1;
        panel.add(typeCombo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createBtn, gbc);
        gbc.gridy = 4;
        panel.add(backBtn, gbc);

        return panel;
    }

    private JPanel createChooseAccountPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        userCombo = new JComboBox<>();
        userCombo.setPreferredSize(new Dimension(250, 25));
        userCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    User user = (User) value;
                    String role = user instanceof Seller ? " (Seller)" : " (Buyer)";
                    setText(user.getUsername() + role);
                }
                return this;
            }
        });

        JButton selectBtn = new JButton("Select Account");
        JButton backBtn = new JButton("Back");

        selectBtn.addActionListener(e -> {
            User selected = (User) userCombo.getSelectedItem();
            if (selected != null) {
                currentUser = selected;
                if (selected instanceof Seller) {
                    currentSeller = (Seller) selected;
                    currentBuyer = null;
                } else if (selected instanceof Buyer) {
                    currentBuyer = (Buyer) selected;
                    currentSeller = null;
                }
                updateDisplay();
                showMessage("Selected account: " + selected.getUsername());
                userCardLayout.show(userCardContainer, "MAIN");
            }
        });

        backBtn.addActionListener(e -> userCardLayout.show(userCardContainer, "MAIN"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Select Account:"), gbc);
        gbc.gridy = 1;
        panel.add(userCombo, gbc);
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(selectBtn, gbc);
        gbc.gridy = 3;
        panel.add(backBtn, gbc);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Actions"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel sellerPanel = createSellerPanel();
        JPanel buyerPanel = createBuyerPanel();

        actionCardLayout = new CardLayout();
        actionCardContainer = new JPanel(actionCardLayout);
        actionCardContainer.add(sellerPanel, "SELLER");
        actionCardContainer.add(buyerPanel, "BUYER");
        actionCardContainer.add(new JPanel(), "EMPTY");

        actionCardLayout.show(actionCardContainer, "EMPTY");

        panel.add(actionCardContainer, gbc);

        return panel;
    }

    private JPanel createSellerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Seller Actions"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField(15);
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(10);
        JLabel qtyLabel = new JLabel("Quantity:");
        JTextField qtyField = new JTextField(10);
        JButton uploadBtn = new JButton("Create Product");

        uploadBtn.addActionListener(e -> {
            try {
                if (currentSeller == null) {
                    showError("Please select a seller first");
                    return;
                }

                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int quantity = Integer.parseInt(qtyField.getText().trim());

                currentSeller.uploadProduct(name, price, quantity);
                showMessage("Product uploaded: " + name);
                nameField.setText("");
                priceField.setText("");
                qtyField.setText("");
                refreshProductList();
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Invalid number format");
            } catch (Exception ex) {
                showError("Error uploading product: " + ex.getMessage());
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        gbc.gridx = 2;
        panel.add(priceLabel, gbc);
        gbc.gridx = 3;
        panel.add(priceField, gbc);
        gbc.gridx = 4;
        panel.add(qtyLabel, gbc);
        gbc.gridx = 5;
        panel.add(qtyField, gbc);
        gbc.gridx = 6;
        panel.add(uploadBtn, gbc);

        return panel;
    }

    private JPanel createBuyerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Buyer Actions"));

        productListModel = new DefaultListModel<>();
        buyerProductList = new JList<>(productListModel);
        buyerProductList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        buyerProductList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Product) {
                    Product product = (Product) value;
                    String text = String.format("%-30s ₱%10.2f  Qty: %-5d  by %s",
                            product.getName(), product.getPrice(),
                            product.getQuantity(), product.getSeller().getUsername());
                    setText(text);
                    setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                }
                return this;
            }
        });

        JScrollPane listScrollPane = new JScrollPane(buyerProductList);
        listScrollPane.setPreferredSize(new Dimension(600, 150));
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Available Products"));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel qtyLabel = new JLabel("Quantity:");
        JTextField qtyField = new JTextField(5);
        qtyField.setText("1");
        JButton addToCartBtn = new JButton("Add to Cart");
        JButton checkoutBtn = new JButton("Checkout");
        JButton clearCartBtn = new JButton("Clear Cart");

        addToCartBtn.addActionListener(e -> {
            try {
                if (currentBuyer == null) {
                    showError("Please select a buyer first");
                    return;
                }

                Product product = buyerProductList.getSelectedValue();
                if (product == null) {
                    showError("Please select a product from the list");
                    return;
                }

                int quantity = Integer.parseInt(qtyField.getText().trim());
                currentBuyer.addToCart(product, quantity);
                showMessage("Added to cart: " + product.getName() + " x" + quantity);
                qtyField.setText("1");
                refreshProductList();
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Invalid quantity format");
            } catch (Exception ex) {
                showError("Error adding to cart: " + ex.getMessage());
            }
        });

        checkoutBtn.addActionListener(e -> {
            try {
                if (currentBuyer == null) {
                    showError("Please select a buyer first");
                    return;
                }

                if (currentBuyer.getCart().isEmpty()) {
                    showError("Cart is empty");
                    return;
                }

                double total = currentBuyer.getCartTotal();
                currentBuyer.checkout();
                showMessage("Checkout successful! Total: ₱" + String.format("%.2f", total));
                refreshProductList();
                updateDisplay();
            } catch (Exception ex) {
                showError("Checkout failed: " + ex.getMessage());
            }
        });

        clearCartBtn.addActionListener(e -> {
            if (currentBuyer != null) {
                currentBuyer.clearCart();
                showMessage("Cart cleared");
                updateDisplay();
            }
        });

        controlPanel.add(qtyLabel);
        controlPanel.add(qtyField);
        controlPanel.add(addToCartBtn);
        controlPanel.add(checkoutBtn);
        controlPanel.add(clearCartBtn);

        panel.add(listScrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);

        refreshProductList();

        return panel;
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dashboard & Information"));

        displayArea = new JTextArea(15, 70);
        displayArea.setEditable(false);
        displayArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayArea);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void refreshUserCombo(JComboBox<User> combo) {
        if (combo == null)
            return;
        combo.removeAllItems();
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(sellers);
        allUsers.addAll(buyers);
        allUsers.forEach(combo::addItem);
    }

    private void refreshProductList() {
        if (productListModel == null)
            return;
        productListModel.clear();
        List<Product> allProducts = new ArrayList<>();
        sellers.forEach(seller -> allProducts.addAll(seller.getProducts()));
        allProducts.stream()
                .filter(p -> p.getQuantity() > 0)
                .forEach(productListModel::addElement);
    }

    private void updateDisplay() {
        if (displayArea == null)
            return;

        StringBuilder display = new StringBuilder();

        if (currentUser != null) {
            display.append(currentUser.getDashboard()).append("\n");

            if (currentBuyer != null) {
                display.append("\nAVAILABLE PRODUCTS\n");
                List<Product> products = currentBuyer.browseProducts(sellers);
                if (products.isEmpty()) {
                    display.append("No products available.\n");
                } else {
                    products.forEach(p -> {
                        display.append(String.format("- %s by %s: ₱%.2f (Qty: %d)\n",
                                p.getName(), p.getSeller().getUsername(), p.getPrice(), p.getQuantity()));
                    });
                }
            }
        }

        displayArea.setText(display.toString());

        if (actionCardLayout != null && actionCardContainer != null) {
            if (currentUser instanceof Seller) {
                actionCardLayout.show(actionCardContainer, "SELLER");
            } else if (currentUser instanceof Buyer) {
                actionCardLayout.show(actionCardContainer, "BUYER");
                refreshProductList();
            } else {
                actionCardLayout.show(actionCardContainer, "EMPTY");
            }
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            MarketplaceApplication app = new MarketplaceApplication();
            app.setVisible(true);
        });
    }
}
