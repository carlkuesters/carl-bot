package carlbot.database;

import java.sql.*;

public class Database{

    public Database(String subProtocolName, String path, String user, String password) {
        this.subProtocolName = subProtocolName;
        this.path = path;
        this.user = user;
        this.password = password;
    }
    private String subProtocolName;
    private String path;
    private String user;
    private String password;
    private Connection connection;

    private void reconnectIfClosed() throws SQLException {
        if (connection.isClosed()) {
            System.out.println("Connection closed, trying to reconnect.");
            connect();
        }
    }

    public void connect() throws SQLException {
        System.out.println("Connecting to database...");
        connection = DriverManager.getConnection("jdbc:" + subProtocolName + ":" + path, user, password);
        System.out.println("Connected to database.");
    }

    public boolean executeQuery(String query) throws SQLException  {
        reconnectIfClosed();
        Statement statement = connection.createStatement();
        boolean result = statement.execute(query);
        statement.close();
        return result;
    }

    public QueryResult getQueryResult(String query) throws SQLException {
        reconnectIfClosed();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return new QueryResult(statement, resultSet);
    }

    public String escape(String text) {
        return text.replaceAll("'", "\\'");
    }
}
