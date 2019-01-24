package urjc.isi.pruebasSparkJava;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Injector {
	
	private static Connection c;
	
	public Injector(String name){
		try {
			if(c!=null) return;
		
			c = DriverManager.getConnection(name);
			c.setAutoCommit(false);
		}catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}

	public static String select(String table, String film) {
		String sql = "SELECT * FROM " + table + " WHERE film=?";
    	String result = new String();
    	
    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		pstmt.setString(1, film);
    		ResultSet rs = pstmt.executeQuery();
    		// Commit after query is executed
    		c.commit();
    		while (rs.next()) {
    			// read the result set
    			result += "film = " + rs.getString("film") + "\n";
    			System.out.println("film = "+rs.getString("film") + "\n");

    			result += "actor = " + rs.getString("actor") + "\n";
    			System.out.println("actor = "+rs.getString("actor")+"\n");
    		}
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
	}
	
	
	public void close() {
        try {
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
	
}
