package ShopEasy;


import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    static int userId=1;
    static int orderId=1;
    
    public static void main(String[] args) {
        System.out.println("Welcome to ShopEasy E-Commerce Backend System");
        
        while (true) {
            
            System.out.println("1. Register a new user");
            System.out.println("2. Login a user");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            if(choice==1) {
            	registerUser();
            }else if(choice==2) {
            	User loggedInUser=loginUser();
            	if(loggedInUser!=null) {
            		while(true) {
            			showMenu();
            			handleUserChoice(loggedInUser);            			
            		}
            	}
            }
        }
    }

    public static void showMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("1. View all products");
        System.out.println("2. View product details");
        System.out.println("3. Add product to cart");
        System.out.println("4. View cart");
        System.out.println("5. Place an order");
        System.out.println("6. View order history");
        System.out.println("7. Exit");
    }

    public static void handleUserChoice(User loggedInUser) {
    	int choice=scanner.nextInt();
    	scanner.nextLine();
        switch (choice) {
            case 1:
                viewProducts();
                break;
            case 2:
                viewProductDetails();
                break;
            case 3:
                addToCart(loggedInUser);
                break;
            case 4:
                viewCart(loggedInUser);
                break;
            case 5:
                placeOrder(loggedInUser);
                break;
            case 6:
                viewOrderHistory(loggedInUser);
                break;
            case 7:
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public static void registerUser() {
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        System.out.println("Enter your address:");
        String address = scanner.nextLine();
        
        User user = new User(userId++, name, email, password, address);
        boolean success = UserDAO.registerUser(user);
        
        if (success) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("User registration failed.");
        }
    }

    public static User loginUser() {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        
        User user = UserDAO.loginUser(email, password);
        
        if (user != null) {
            System.out.println("Login successful! Your id : " + user.getId());
        } else {
            System.out.println("Invalid credentials.");
        }
        return user;
    }

    public static void viewProducts() {
        List<Product> products = ProductDAO.getAllProducts();
        
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            for (Product product : products) {
                System.out.println("ID: " + product.getId() + ", Name: " + product.getName() + ", Price: " + product.getPrice());
            }
        }
    }

    public static void viewProductDetails() {
        System.out.println("Enter product ID to view details:");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        
        Product product = ProductDAO.getProductById(productId);
        
        if (product != null) {
            System.out.println("Product ID: " + product.getId());
            System.out.println("Name: " + product.getName());
            System.out.println("Description: " + product.getDescription());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Stock Quantity: " + product.getStockQuantity());
        } else {
            System.out.println("Product not found.");
        }
    }

    public static void addToCart(User loggedInUser) {
        System.out.println("Enter product ID to add to cart:");
        int productId = scanner.nextInt();
        System.out.println("Enter quantity:");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        
        boolean success = CartDAO.addToCart(loggedInUser.getId(), productId, quantity);
        
        if (success) {
            System.out.println("Product added to cart.");
        } else {
            System.out.println("Failed to add product to cart.");
        }
    }

    public static void viewCart(User loggedInUser) {
        
        List<OrderItem> OrderItems = CartDAO.getCartItems(loggedInUser.getId());
        
        if (OrderItems.isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            for (OrderItem item : OrderItems) {
                System.out.println("Product ID: " + item.getProductId() + ", Quantity: " + item.getQuantity());
            }
        }
        System.out.println("press 1 to remove a items");
        System.out.println("press 0 to go to menu");
        int choice=scanner.nextInt();
        scanner.nextLine();
        if(choice==1) {
        	System.out.print("Enter the product ID to be remove : ");
        	int productId=scanner.nextInt();
        	CartDAO.removeCart(productId);
        }
    }

    public static void placeOrder(User loggedInUser) {
       
        List<OrderItem> OrderItems = CartDAO.getCartItems(loggedInUser.getId());
        
        if (OrderItems.isEmpty()) {
            System.out.println("Cart is empty, cannot place order.");
            return;
        }
        
        double totalPrice = 0;
        for (OrderItem item : OrderItems) {
            Product product = ProductDAO.getProductById(item.getProductId());
            totalPrice += product.getPrice() * item.getQuantity();
        }
        
        Order order = new Order(++orderId, loggedInUser.getId(), totalPrice, "Pending", OrderItems); // Creating order object
        boolean success = OrderDAO.placeOrder(order);
        
        if (success) {
        	CartDAO.clearTheCart();
            System.out.println("Order placed successfully!");
        } else {
            System.out.println("Failed to place order.");
        }
    }

    public static void viewOrderHistory(User loggedInUser) {
        
        List<Order> orders = OrderDAO.getOrderHistory(loggedInUser.getId());
        
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getId() + ", Total Price: " + order.getTotalPrice() + ", Status: " + order.getStatus());
                for (OrderItem item : order.getItems()) {
                    System.out.println("Product ID: " + item.getProductId() + ", Quantity: " + item.getQuantity() + ", Price: " + item.getPrice());
                }
            }
        }
    }
}

