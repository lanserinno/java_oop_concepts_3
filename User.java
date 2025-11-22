import java.util.Objects;

public abstract class User {
    private String username;
    private String email;
    
    public User(String username, String email) {
        validateUsername(username);
        validateEmail(email);
        this.username = username;
        this.email = email;
    }
    
    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters");
        }
    }
    
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        validateUsername(username);
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }
    
    public abstract String getDashboard();
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
    
    @Override
    public String toString() {
        return "User{username='" + username + "', email='" + email + "'}";
    }
}

