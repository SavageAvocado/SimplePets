package simplepets.brainsynder.sql;

import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.debug.DebugLevel;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.CompletableFuture;

/**
 * This class was provided by {@link https://github.com/Thatsmusic99}
 * From the resource {@link https://github.com/Niestrat99/AT-Rewritten}
 * Fat chance, i didn't use MysqlDataSource lmao
 */
public abstract class SQLManager {

    protected String tablePrefix;
    private final String databaseName;
    protected volatile boolean usingSqlite;

    public SQLManager() {
        this(false);
    }

    public SQLManager(boolean forceSqlite) {
        tablePrefix = ConfigOption.INSTANCE.MYSQL_TABLE.getValue();
        if (!tablePrefix.matches("^[_A-Za-z0-9]+$")) {
            SimplePets.getDebugLogger().debug(DebugLevel.WARNING, "Table prefix " + tablePrefix + " is not alphanumeric. Using simplepets...", true);
            tablePrefix = "simplepets";
        }
        boolean enabled = ConfigOption.INSTANCE.MYSQL_ENABLED.getValue();
        databaseName = ConfigOption.INSTANCE.MYSQL_DATABASE.getValue();

        if (forceSqlite || !enabled) {
            //Debug.debug(DebugLevel.DEBUG, getClass().getSimpleName()+" Using SQLite - forced", true);
            usingSqlite = true;
        } else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                //Debug.debug(DebugLevel.DEBUG, getClass().getSimpleName()+" Error using SQLite", true);
                usingSqlite = true;
            }
        }

        // This ended up being missed in the code leading SQLSyntaxErrorException
        createTable();
    }

    // Forgot about thread safety and got told off for it in AT
    public Connection implementConnection() {
        Connection connection = null;
        if (usingSqlite) {
            connection = loadSqlite();
        } else {
            StringBuilder url = new StringBuilder();
            url.append("jdbc:mysql://").append(ConfigOption.INSTANCE.MYSQL_HOST.getValue()).append(":")
                    .append(ConfigOption.INSTANCE.MYSQL_PORT.getValue()).append("/").append(databaseName);
            url.append("?useSSL=").append(ConfigOption.INSTANCE.MYSQL_SSL.getValue());
            url.append("&autoReconnect=true");

            try {
                connection = DriverManager.getConnection(url.toString(),
                        ConfigOption.INSTANCE.MYSQL_USERNAME.getValue(), ConfigOption.INSTANCE.MYSQL_PASSWORD.getValue());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return connection;
    }

    public String getTable(String suffix) {
        if (usingSqlite) return tablePrefix + suffix;
        return databaseName + "." + (tablePrefix + suffix);
    }

    private Connection loadSqlite() {
        // Load JDBC
        try {
            Class.forName("org.sqlite.JDBC");
            File file = new File(PetCore.getInstance().getDataFolder(), "storage.db");
            if (!file.exists()) file.createNewFile();
            return DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //SHOW COLUMNS FROM `tbl_name`; // Lists columns
    public CompletableFuture<Boolean> hasColumn(String table, String column) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = implementConnection()) {
                DatabaseMetaData md2 = connection.getMetaData();
                ResultSet rsTables = md2.getColumns(null, null, tablePrefix + table, column);
                return rsTables.next();
            } catch (Exception e) {
                SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Unable to check if '" + column + "' exists in the database");
                return false;
            }
        }, PetCore.getInstance().async).thenApplyAsync(result -> result, PetCore.getInstance().sync);
    }

    public void modifyColumn(String table, String column, String type) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = implementConnection()) {
                Statement statement = connection.createStatement();
                statement.executeUpdate("ALTER TABLE `" + tablePrefix + table + "` MODIFY COLUMN " + column + " " + type + " NOT NULL");
            } catch (SQLException throwables) {
                SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Unable to add '" + column + "' to the database");
            }
        }, PetCore.getInstance().async);
    }

    public void addColumn(String table, String column, String type) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = implementConnection()) {
                Statement statement = connection.createStatement();
                statement.executeUpdate("ALTER TABLE `" + tablePrefix + table + "` ADD " + column + " " + type + " NOT NULL");
            } catch (SQLException throwables) {
                SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Unable to add '" + column + "' to the database");
            }
        }, PetCore.getInstance().async);
    }

    public abstract void createTable();

    public abstract void transferOldData();

    public String getStupidAutoIncrementThing() {
        return usingSqlite ? "AUTOINCREMENT" : "AUTO_INCREMENT";
    }

    public String getStupidTextThing() {
        return usingSqlite ? "TEXT" : "LONGTEXT";
    }

    public boolean isUsingSqlite() {
        return usingSqlite;
    }
}