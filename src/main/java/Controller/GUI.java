package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        File iconFile = new File("src/main/resources/Images/bomb.jpg");
        Image icon = new Image(iconFile.toURI().toString());

        Parent gameScreen = FXMLLoader.load(getClass().getResource("TitleScreen.fxml"));
        Scene titleScreen = new Scene(gameScreen);
        Stage stage = new Stage();

        stage.setTitle("Minesweeper");
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setScene(titleScreen);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}