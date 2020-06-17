import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
	public static void main(String[] args) {
		DbConnection dbConnection = new DbConnection();

		dbConnection.connect();

		int newId = dbConnection.insert("INSERT INTO article SET regDate = NOW(), title = '제목1', BODY = '내용1'");
		System.out.println("== 새 게시물 번호 ==");
		System.out.println(newId);

		dbConnection.close();
	}
}

class DbConnection {
	private Connection connection;

	public void connect() {
		String url = "jdbc:mysql://localhost:3306/site7?serverTimezone=UTC";
		String user = "sbsst";
		String password = "sbs123414";
		String driverName = "com.mysql.cj.jdbc.Driver";

		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.err.printf("[SQL 예외] : %s\n", e.getMessage());
		}
	}

	public int insert(String sql) {
		int id = -1;

		try {
			Statement stmt = connection.createStatement();
			stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				id = rs.getInt(1);
			}

		} catch (SQLException e) {
			System.err.printf("[SQL 예외, SQL : %s] : %s\n", sql, e.getMessage());
		}

		return id;
	}

	public void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.err.printf("[SQL 예외] : %s\n", e.getMessage());
			}
		}
	}
}
