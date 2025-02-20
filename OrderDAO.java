package ShopEasy;




import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Place an order and update stock quantity
    public static boolean placeOrder(Order order) {
        String orderQuery = "INSERT INTO Orders (user_id, total_price, status) VALUES (?, ?, ?)";
        String orderItemQuery = "INSERT INTO OrderItems (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Insert order
            try (PreparedStatement orderStmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, order.getUserId());
                orderStmt.setDouble(2, order.getTotalPrice());
                orderStmt.setString(3, order.getStatus());
                orderStmt.executeUpdate();

                // Get generated order ID
                ResultSet rs = orderStmt.getGeneratedKeys();
                if (rs.next()) {
                    int orderId = rs.getInt(1);

                    // Insert order items and update stock
                    for (OrderItem item : order.getItems()) {
                        try (PreparedStatement itemStmt = conn.prepareStatement(orderItemQuery)) {
                            itemStmt.setInt(1, orderId);
                            itemStmt.setInt(2, item.getProductId());
                            itemStmt.setInt(3, item.getQuantity());
                            itemStmt.setDouble(4, item.getPrice());
                            itemStmt.executeUpdate();

                            // Deduct stock quantity
                            String updateStockQuery = "UPDATE Products SET stock_quantity = stock_quantity - ? WHERE id = ?";
                            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)) {
                                stockStmt.setInt(1, item.getQuantity());
                                stockStmt.setInt(2, item.getProductId());
                                stockStmt.executeUpdate();
                            }
                        }
                    }
                }
                conn.commit(); // Commit transaction
                return true;
            } catch (SQLException e) {
                conn.rollback(); 
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get order history for a user
    public static List<Order> getOrderHistory(int userId) {
        List<Order> orders = new ArrayList<>();
        String orderQuery = "SELECT * FROM Orders WHERE user_id = ?";
        String itemQuery = "SELECT * FROM OrderItems WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderQuery)) {
            orderStmt.setInt(1, userId);
            ResultSet rs = orderStmt.executeQuery();
            while (rs.next()) {
            	int user_id=rs.getInt("user_id");
                if(user_id==userId) {
                	int orderId = rs.getInt("id");
                    double totalPrice = rs.getDouble("total_price");
                    String status = rs.getString("status");

                    // Fetch order items
                    PreparedStatement itemStmt = conn.prepareStatement(itemQuery);
                    itemStmt.setInt(1, orderId);
                    ResultSet itemRs = itemStmt.executeQuery();

                    List<OrderItem> items = new ArrayList<>();
                    while (itemRs.next()) {
                        int productId = itemRs.getInt("product_id");
                        int quantity = itemRs.getInt("quantity");
                        double price = itemRs.getDouble("price");
                        items.add(new OrderItem(productId, quantity, price));
                    }

                    orders.add(new Order(orderId, userId, totalPrice, status, items));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
