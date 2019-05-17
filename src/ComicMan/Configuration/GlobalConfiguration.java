package ComicMan.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GlobalConfiguration {

    private static Path applicationDirectory = Paths.get(System.getProperty("user.home"), ".comicman");
    private static Path databaseDirectory = applicationDirectory.resolve("database");
    private static Path coverDirectory = applicationDirectory.resolve("covers");

    public static Path getDatabaseDirectory() {
        return databaseDirectory;
    }

    public static Path getCoverDirectory() {
        return coverDirectory;
    }

    public Path getApplicationDirectory() {
        return applicationDirectory;
    }
}
