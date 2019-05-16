package ComicMan;

import ComicMan.DB.ComicDB;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Library {

    private ComicDB comicDatabase = new ComicDB();

    public Library() {
        comicDatabase.connect();
    }

    public void addComic(Path comicLocation){
        comicDatabase.addComic(new Comic(comicLocation));
    }

    public void addComicFromDir(Path directory){
        for (Path comic :
                Objects.requireNonNull(scrapeForFiles(directory))) {
            addComic(comic);
        }
    }

    public List<Comic> getAllComics(){
        return comicDatabase.getComics();
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
}
