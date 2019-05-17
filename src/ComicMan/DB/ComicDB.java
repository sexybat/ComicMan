package ComicMan.DB;

import ComicMan.Comic;
import ComicMan.Configuration.GlobalConfiguration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComicDB {
    private Path databaseLocation = GlobalConfiguration.getDatabaseDirectory().resolve("comics.sqlite");
    private Connection database;

    private void initialize() throws SQLException {
        database = DriverManager.getConnection("jdbc:sqlite:" + databaseLocation);

        database.createStatement().execute(
                "CREATE TABLE comics(" +
                        "path PRIMARY KEY," +
                        "name," +
                        "cover_location UNIQUE" +
                        ")"
        );
    }

    public void connect() {
        try{
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
            database.createStatement().execute(
                    String.format("INSERT OR IGNORE INTO comics (path, name, cover_location) VALUES ( '%s','%s','%s')",
                            comic.getFile(),
                            comic.getName(),
                            comic.getCover())
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Comic> getComics() {
        try {
            String query = "SELECT path, name, cover_location from comics";
            ResultSet entries = database.createStatement().executeQuery(query);
            List<Comic> comics = new ArrayList<>();

            while(entries.next()) {
                comics.add(new Comic(
                        Paths.get(entries.getString("path")),
                        entries.getString("name"),
                        Paths.get(entries.getString("cover_location"))
                ));
            }

            return comics;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
