package carlbot.database;

import java.sql.*;

public class Database{

    public Database(String subProtocolName, String path, String user, String password) throws SQLException {
        System.out.println("Connecting to database...");
        connection = DriverManager.getConnection("jdbc:" + subProtocolName + ":" + path, user, password);
        System.out.println("Connected to database.");
    }
    private Connection connection;

    public boolean executeQuery(String query) throws SQLException  {
        Statement statement = connection.createStatement();
        boolean result = statement.execute(query);
        statement.close();
        return result;
    }

    public QueryResult getQueryResult(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return new QueryResult(statement, resultSet);
    }

    public String escape(String text) {
        return text.replaceAll("'", "\\'");
    }
}
