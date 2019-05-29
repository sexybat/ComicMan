package ComicMan;

import ComicMan.Configuration.GlobalConfiguration;
import ComicMan.DB.ComicDB;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class Library {

    private ComicDB comicDatabase = new ComicDB();

    void addComic(Path comicLocation) {
        comicDatabase.addComic(new Comic(comicLocation));
    }

    void addComicFromDir(Path directory) {
        for (Path comic :
                Objects.requireNonNull(scrapeForFiles(directory))) {
            addComic(comic);
        }
    }

    void deleteComic(Comic comicToDelete) {
        try {
            comicDatabase.deleteComic(comicToDelete);
            Files.delete(comicToDelete.getCover());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<Comic> getAllComics() {
        return comicDatabase.getAllComics();
    }

    private List<Path> scrapeForFiles(Path directoryToRead) {
        if (Files.isReadable(directoryToRead) && Files.isDirectory(directoryToRead)) {
            List<Path> entriesFound = new ArrayList<>();

            try {

                for (Path entry :
                        Files.newDirectoryStream(directoryToRead)) {

                    if (Files.isDirectory(entry)) {
                        List<Path> tempList = scrapeForFiles(entry);

                        if (tempList != null) {
                            entriesFound.addAll(tempList);
                        }

                    } else if (Files.isRegularFile(entry)) {
                        entriesFound.add(entry);
                    }
                }

                Collections.sort(entriesFound);

                return entriesFound;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    void updateComicReadStatus(Comic comic, boolean read) {
        comic.setRead(read);
        comicDatabase.updateRead(comic);
    }

    void deleteAllComics() {
        try {
            comicDatabase.deleteAll();
            clearCovers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearCovers() throws IOException {
        for (Path file :
                Files.newDirectoryStream(GlobalConfiguration.getCoverDirectory())) {
            Files.delete(file);
        }
    }
}
