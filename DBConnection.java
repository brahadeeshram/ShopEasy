package ShopEasy;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ShopEasy";
    private static final String USER = "root";  
    private static final String PASSWORD = "Rbrmng@2001";  

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

/*


CREATE DATABASE ShopEasy;
use ShopEasy;
-- Users Table


CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    address TEXT NOT NULL
);

-- Products Table
CREATE TABLE Products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL
);

-- Orders Table
CREATE TABLE Orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    total_price DECIMAL(10,2) NOT NULL,
    status ENUM('Pending', 'Completed', 'Cancelled') DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- OrderItems Table
CREATE TABLE OrderItems (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE CASCADE
);

-- Cart Table
CREATE TABLE Cart (
    user_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price DECIMAL(10,2),
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE CASCADE
);

INSERT INTO products (name, description, price, stock_quantity) 
VALUES 
('iPhone 15', 'Latest Apple smartphone with 128GB storage', 79999.99, 50),
('Samsung Galaxy S23', 'Samsung flagship with 256GB storage', 89999.99, 40),
('Dell Laptop', '15.6-inch laptop with Intel i7 processor', 119999.99, 30),
('Sony Headphones', 'Noise-canceling over-ear headphones', 1999.99, 20),
('Logitech Mouse', 'Wireless ergonomic mouse', 499.99, 100);

*/