package ShopEasy;

import java.sql.SQLException;

public class ErrorHandler {
    public static String handleSQLException(SQLException e) {
        // Log the error and return a user-friendly message
        e.printStackTrace();
        return "Database error occurred!";
    }
}

