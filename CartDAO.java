package ShopEasy;




import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    
    public static boolean addToCart(int userId, int productId, int quantity) {
    	String productQuery = "SELECT * FROM Products WHERE id = ?";
    	String query = "INSERT INTO Cart (user_id, product_id, quantity, price) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE quantity = quantity + ?";

    	try (Connection conn = DBConnection.getConnection();
    	     PreparedStatement stmt1 = conn.prepareStatement(productQuery);
    	     PreparedStatement stmt = conn.prepareStatement(query)) {
    	    
    	    
    	    stmt1.setInt(1, productId);
    	    
    	    
    	    ResultSet rs = stmt1.executeQuery();

    	    if (rs.next()) { 
    	        if (quantity > rs.getInt("stock_quantity")) {
    	            System.out.println("Out of stock");
    	            return false;
    	        }

    	        stmt.setInt(1, userId);
    	        stmt.setInt(2, productId);
    	        stmt.setInt(3, quantity);
    	        stmt.setInt(5, quantity); 

    	        stmt.setDouble(4, rs.getDouble("price")); 

    	        return stmt.executeUpdate() > 0; 
    	    } else {
    	        System.out.println("Product not found");
    	        return false;
    	    }
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	}

        return false;
    }

	public static List<OrderItem> getCartItems(int id) {
		List<OrderItem> cartItems=new ArrayList<>();
		String query="SELECT * FROM Cart where user_id= ?";
		try (Connection conn = DBConnection.getConnection();
	            PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, id);
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                cartItems.add(new OrderItem(rs.getInt("product_id"),rs.getInt("quantity"),rs.getDouble("price")));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		return cartItems;
	}
	public static void removeCart(int product_id) {
		String query="DELETE FROM Cart where product_id=?";
		try (Connection conn = DBConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)){
				stmt.setInt(1, product_id);
				int rs=stmt.executeUpdate();
				System.out.println(rs+" items removed");
		}catch (SQLException e) {
            e.printStackTrace();
        }
	}
	public static void clearTheCart() {
		String query="TRUNCATE table Cart";
		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement()) {
	            stmt.executeUpdate(query);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
}
