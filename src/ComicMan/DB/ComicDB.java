package ComicMan.DB;

import ComicMan.Comic;
import ComicMan.Configuration.GlobalConfiguration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ComicDB {
    private static Path databaseLocation = GlobalConfiguration.getDatabaseDirectory().resolve("comics.sqlite");
    private static Connection database;

    public ComicDB() {
        connect();
    }

    private void initialize() throws SQLException {
        database = DriverManager.getConnection("jdbc:sqlite:" + databaseLocation);

        database.createStatement().execute(
                "CREATE TABLE comics(" +
                        "path PRIMARY KEY," +
                        "name," +
                        "cover_location UNIQUE," +
                        "read" +
                        ")"
        );
    }

    private void connect() {
        try {
            if(Files.notExists(databaseLocation)) {
                initialize();
            }
            else {
                database = DriverManager.getConnection("jdbc:sqlite:" + databaseLocation);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addComic(Comic comic) {
        try {
            PreparedStatement statement = database.prepareStatement(
                    "INSERT OR IGNORE INTO comics" +
                            "(path, name, cover_location, read)" +
                            "VALUES (?, ?, ?, ?)"
            );

            statement.setString(1, comic.getFile().toString());
            statement.setString(2, comic.getName());
            statement.setString(3, comic.getCover().toString());
            statement.setBoolean(4, comic.isRead());

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteComic(Comic comic) {
        try {
            PreparedStatement statement = database.prepareStatement(
                    "DELETE FROM comics WHERE path = ?"
            );

            statement.setString(1, comic.getFile().toString());

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Comic> getAllComics() {
        try {
            String query = "SELECT * from comics";
            ResultSet entries = database.createStatement().executeQuery(query);
            List<Comic> comics = new ArrayList<>();

            while(entries.next()) {
                comics.add(new Comic(
                        Paths.get(entries.getString("path")),
                        entries.getString("name"),
                        Paths.get(entries.getString("cover_location")),
                        entries.getBoolean("read")
                ));
            }

            return comics;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateRead(Comic comic) {
        try {
            PreparedStatement statement = database.prepareStatement(
                    "UPDATE comics " +
                            "SET read = ? " +
                            "WHERE path = ?"
            );

            statement.setBoolean(1, comic.isRead());
            statement.setString(2, comic.getFile().toString());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            //noinspection SqlWithoutWhere
            database.createStatement().execute(
                    "DELETE FROM comics"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
