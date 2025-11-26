package it.unical.fleetgo.backend.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static DBManager instance;
    private Connection connection;
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    static {
        try {
            String url = System.getenv("DATABASE_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            boolean credenzialiValide = url != null && user != null && password != null;
            if (credenzialiValide) {
                DB_URL = url;
                DB_USER = user;
                DB_PASSWORD = password;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DBManager(){}

    public static DBManager getInstance(){
        if(instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection(){
        try {
            if(connection == null) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la connessione al database", e);
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new RuntimeException("Errore chiusura connessione DB", e);
            }
        }
    }
}