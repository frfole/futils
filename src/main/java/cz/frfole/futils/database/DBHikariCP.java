package cz.frfole.futils.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import cz.frfole.futils.Futils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class DBHikariCP {
    private final HikariConfig config = new HikariConfig();
    private final HikariDataSource dataSource;
    private Connection conn;

    /**
     * Construct {@link DBHikariCP} from config.yml
     *
     * @param futils the {@link Futils} instance
     */
    public DBHikariCP(Futils futils) {
        YamlConfiguration conf = futils.getCM().getConfig("config").getConfig();
        this.config.setJdbcUrl("jdbc:mysql://" + conf.getString("mysql.ip")
                + ":" + conf.getString("mysql.port")
                + "/" + conf.getString("mysql.database") + "?useSSL=false");
        this.config.setUsername(conf.getString("mysql.username"));
        this.config.setPassword(conf.getString("mysql.password"));
        this.config.addDataSourceProperty("cachePrepStmts", "true");
        this.config.addDataSourceProperty("prepStmtCacheSize", "250");
        this.config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.dataSource = new HikariDataSource(this.config);
    }

    /**
     * Opens connection
     *
     * @throws SQLException if database access error occurs
     */
    public void openConnection() throws SQLException {
        this.conn = this.dataSource.getConnection();
    }

    /**
     * Close connection
     *
     * @throws SQLException if database access error occurs
     */
    public void closeConnection() throws SQLException {
        this.conn.close();
    }

    /**
     * Returns {@link #conn}
     *
     * @return {@link #conn}
     */
    public Connection getConnection() {
        return this.conn;
    }
}
