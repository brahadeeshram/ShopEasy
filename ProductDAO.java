package ShopEasy;





import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Get all products
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name"),
                        rs.getString("description"), rs.getDouble("price"), rs.getInt("stock_quantity")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

	public static Product getProductById(int productId) {
		String query="SELECT * FROM Products where id = ?";
		try (Connection conn = DBConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, productId);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return new Product(rs.getInt(1),rs.getString("name"), rs.getString("description"), rs.getDouble("price"), rs.getInt("stock_quantity"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		return null;
	}
}

