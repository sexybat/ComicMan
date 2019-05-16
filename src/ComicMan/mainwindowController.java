package ComicMan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import java.nio.file.Path;
import java.util.List;

public class mainwindowController {
    @FXML
    private TilePane mainPane;

    private Library comicLib = new Library();

    @FXML protected void choosePathButtonAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Path chosenDirectory = directoryChooser.showDialog(Main.getStage()).toPath();

        comicLib.addComicFromDir(chosenDirectory);
        showComics(comicLib.getAllComics());
    }


    private void showComics(List<Comic> comics) {
        for (Comic comic :
                comics) {
            Image entryThumbnail = new Image("file:"+ comic.getCover().toString(),200,0,
                                    true,false,true);
            ImageView thumbnailViewer = new ImageView(entryThumbnail);
            HBox container = new HBox();

            Tooltip entryName = new Tooltip(comic.getName());
            Tooltip.install(thumbnailViewer, entryName);

            container.getChildren().add(thumbnailViewer);
            container.setPadding(new Insets(10,10,10,10));

            mainPane.getChildren().add(container);
        }
    }
}
