package ComicMan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.awt.Desktop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class mainWindowController {
    @FXML private TilePane mainPane;

    private Library comicLib = new Library();

    @FXML private void initialize() {
        loadComics(comicLib.getAllComics());
    }

    @FXML protected void addComicFromDirActionEvent(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setTitle("Add comics from directory");
        directoryChooser.setInitialDirectory(Paths.get(System.getProperty("user.home")).toFile());

        File directory = directoryChooser.showDialog(Main.getStage());

        if(directory != null) {
            comicLib.addComicFromDir(directory.toPath());
            loadComics(comicLib.getAllComics());
        }
    }

    @FXML protected void addSingleComicActionEvent(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Add comic");
        fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home")).toFile());
        fileChooser.setSelectedExtensionFilter(
                new FileChooser.ExtensionFilter("Comic Book Zip", "*.cbz")
        );

        File file = fileChooser.showOpenDialog(Main.getStage());

        if(file != null) {
            comicLib.addComic(file.toPath());
            loadComics(comicLib.getAllComics());
        }
    }

    @FXML protected void clearLibraryActionEven(ActionEvent actionEvent) {
        comicLib.deleteAllComics();
        loadComics(comicLib.getAllComics());
    }

    private void loadComics(List<Comic> comics) {
        mainPane.getChildren().clear();

        for (Comic comic :
                comics) {
            addComicNode(comic);
        }
    }

    private void addComicNode(Comic comic) {
        Image entryThumbnail = new Image(
                "file:"+ comic.getCover().toString(),
                150,
                250,
                false,
                false,
                true
        );

        ImageView thumbnailViewer = new ImageView(entryThumbnail);
        HBox container = new HBox();

        Tooltip entryName = new Tooltip(comic.getName());
        Tooltip.install(thumbnailViewer, entryName);

        container.getChildren().add(thumbnailViewer);
        //container.setPadding(new Insets(20));

        if(comic.isRead()) {
            container.setStyle("-fx-border-color: red; -fx-border-width: 2");
        }

        thumbnailViewer.setCursor(Cursor.HAND);

        thumbnailViewer.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                openComic(comic);
            }
        });

        addComicContextMenu(thumbnailViewer, comic);

        mainPane.getChildren().add(container);
    }

    private void openComic(Comic comic) {
        if(Desktop.isDesktopSupported()) {
            Desktop de = Desktop.getDesktop();
            try {
                de.open(comic.getFile().toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addComicContextMenu(ImageView thumbnail, Comic comic){
        ContextMenu comicContext = new ContextMenu();

        MenuItem comicDeleteMenuItem = new MenuItem("Delete");
        comicDeleteMenuItem.setOnAction(event -> {
            comicLib.deleteComic(comic);
            loadComics(comicLib.getAllComics());
        });

        MenuItem comicOpenMenuItem = new MenuItem("Open");
        comicOpenMenuItem.setOnAction(event -> openComic(comic));

        comicContext.getItems().addAll(
                comicOpenMenuItem,
                comicDeleteMenuItem
        );

        if(comic.isRead()) {
            MenuItem markUnreadMenuItem = new MenuItem("Mark not read");
            markUnreadMenuItem.setOnAction(event -> {
                comicLib.updateComicReadStatus(comic, false);
                loadComics(comicLib.getAllComics());
            });

            comicContext.getItems().add(markUnreadMenuItem);
        } else {
            MenuItem markReadMenuItem = new MenuItem("Mark read");
            markReadMenuItem.setOnAction(event ->{
                comicLib.updateComicReadStatus(comic, true);
                loadComics(comicLib.getAllComics());
            });

            comicContext.getItems().add(markReadMenuItem);
        }

        thumbnail.setOnContextMenuRequested(contextMenuEvent ->
            comicContext.show(
                    thumbnail,
                    contextMenuEvent.getScreenX(),
                    contextMenuEvent.getScreenY()
            )
        );
    }
}