package ComicMan;

import ComicMan.Configuration.GlobalConfiguration;

import java.io.IOException;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Comic {

    private Path file;
    private Path cover;
    private String name;
    private boolean read;

    Comic(Path comicFile) {
        file = comicFile;
        name = file.getFileName().toString().substring(0, file.getFileName().toString().lastIndexOf("."));
        findCover();
        read = false;
    }

    public Comic (Path file, String name, Path cover, boolean read) {
        this.file = file;
        this.name = name;
        this.cover = cover;
        this.read = read;
    }

    public String getName() {
        return name;
    }

    public Path getFile() {
        return file;
    }

    public Path getCover() {
        return cover;
    }

    public boolean isRead() {
        return read;
    }

    void setRead(boolean read) {
        this.read = read;
    }

    private void findCover() {
        if (file.getFileName().toString().endsWith(".cbz")) {
            try {
                ZipFile comicZip = new ZipFile(file.toString());
                ZipEntry zipEntryCover;
                Enumeration<? extends ZipEntry> entries = comicZip.entries();

                Path coverFile;
                String coverFileExtension;

                //First file in .cbz is cover
                do {
                    zipEntryCover = entries.nextElement();

                    if(zipEntryCover.isDirectory())
                        continue;

                    String file = Paths.get(zipEntryCover.getName()).getFileName().toString();
                    if (file.startsWith("01"))
                        break;
                } while(entries.hasMoreElements());

                coverFileExtension = zipEntryCover.getName().substring(zipEntryCover.getName().lastIndexOf("."));
                coverFile = GlobalConfiguration.getCoverDirectory().resolve( name + coverFileExtension );
                Files.copy(comicZip.getInputStream(zipEntryCover), coverFile, StandardCopyOption.REPLACE_EXISTING);

                cover = coverFile;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}