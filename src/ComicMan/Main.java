package ComicMan;

import ComicMan.Configuration.GlobalConfiguration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;

public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        Scene primaryScene = new Scene(root, 1200, 900);

        primaryScene.getStylesheets().addAll(getClass().getResource("mainWindow.css").toExternalForm());

        primaryStage.setTitle("ComicMan");
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    @Override
    public void init() {
        if(Files.notExists(GlobalConfiguration.getCoverDirectory())) {
            try {
                Files.createDirectories(GlobalConfiguration.getCoverDirectory());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(Files.notExists(GlobalConfiguration.getDatabaseDirectory())) {
            try {
                Files.createDirectories(GlobalConfiguration.getDatabaseDirectory());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    static Stage getStage() {
        return stage;
    }
}
