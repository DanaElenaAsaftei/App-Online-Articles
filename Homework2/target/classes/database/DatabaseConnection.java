package deim.urv.cat.homework2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final String URL = "jdbc:derby://localhost:1527/homework1";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    @SuppressWarnings("LoggerStringConcat")
    public static Connection getConnection() throws SQLException {
        LOGGER.info("Intentando establecer conexión con la base de datos...");

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                LOGGER.info("Conexión establecida con éxito.");
                return connection;
            } else {
                throw new SQLException("La conexión devuelta es nula.");
            }
        } catch (SQLException e) {
            LOGGER.severe("Error al conectar con la base de datos: " + e.getMessage());
            throw e; // Re-lanzar la excepción para que sea manejada en la capa superior
        }
    }
}

