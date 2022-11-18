package Controller;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MenuController {

    Parent root;
    Stage stage;
    Scene scene;
    @FXML
    Button btn_start;

    @FXML
    Label gameTimeNeeded;

    @FXML
    ImageView img_background;

    @FXML
    AnchorPane pane;

    private boolean music, sound;
    private short difficulty, fieldSize;
    private double oldWidth, oldHeight;

    private OptionsSaver saver;

    public MenuController() {
        saver = new OptionsSaver();
        OptionState state = saver.readOptions();
        if (state != null) {
            music = state.music;
            sound = state.sound;
            difficulty = state.difficulty;
            fieldSize = state.fieldSize;
        } else {
            music = false;
            sound = true;
            difficulty = 0;
            fieldSize = 0;
        }

        oldWidth = 600;
        oldHeight = 400;
    }

    @FXML
    public void switchToSceneTitle(ActionEvent event) throws IOException {
        if (sound) {
            SoundEffects.playSound("ClickSound.wav");
        }
        switchScene("TitleScreen.fxml", event);
    }

    @FXML
    public void switchToSceneOptions(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Options.fxml"));
        root = loader.load();

        OptionsController optionsController = loader.getController();
        OptionState optionState = new OptionState(music, sound, difficulty, fieldSize);
        optionsController.initializeOptions(optionState);

        if (sound) {
            SoundEffects.playSound("ClickSound.wav");
        }

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void startGame(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
        root = loader.load();

        if (sound) {
            SoundEffects.playSound("ClickSound.wav");
        }
        if (music) {
            SoundEffects.startMusic("EpicMusic.wav");
        }

        GameController gameController = loader.getController();
        int width, height, nBombs;
        switch(fieldSize) {
            case -1:
                width = 10;
                break;
            case 1:
                width = 20;
                break;
            default:
                width = 12;
                break;
        }
        height = width / 2;
        switch (difficulty) {
            case -1:
                nBombs = (width * height) / 9;
                break;
            case 1:
                nBombs = (width * height) / 5;
                break;
            default:
                nBombs = (width * height) / 7;
                break;
        }
        gameController.initialize(width, height, nBombs);
        gameController.setOptionState(new OptionState(music, sound, difficulty, fieldSize));

        saver.saveOptions(new OptionState(music, sound, difficulty, fieldSize));

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void switchScene(String FxmlFile, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(FxmlFile));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setTime(String time) {
        gameTimeNeeded.setText(time);
    }

    public void setOptions(OptionState optionState) {
        music = optionState.music;
        sound = optionState.sound;
        difficulty = optionState.difficulty;
        fieldSize = optionState.fieldSize;
    }

    @FXML
    private void resizeAll(ActionEvent event) {
        ObservableList<Node> children = pane.getChildren();
        resize(event, children.get(0));
    }

    private void resize(ActionEvent event, Node child) {
        Bounds bounds = child.getLayoutBounds();
        double currentWidth = bounds.getWidth();
        double currentHeight = bounds.getHeight();
        double currentX = child.getLayoutX();
        double currentY = child.getLayoutY();

        Window window = ((Node) event.getSource()).getScene().getWindow();
        double newWindowWidth = window.getWidth(); //-14
        double newWindowHeight = window.getHeight(); // -37

        double factorWidth = newWindowWidth/oldWidth;
        double factorHeight = newWindowHeight/oldHeight;

        double newWidth = currentWidth * factorWidth;
        double newHeight = currentHeight * factorHeight;
        double newX = currentX * factorWidth;
        double newY = currentY * factorHeight;

        child.setScaleX(factorWidth);
        System.out.println(newX);

        oldWidth = newWindowWidth;
        oldHeight = newWindowHeight;
    }
}
