package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;

public class OptionsController {

    @FXML
    CheckBox box_hard;
    @FXML
    CheckBox box_normal;
    @FXML
    CheckBox box_easy;
    @FXML
    CheckBox box_large;
    @FXML
    CheckBox box_medium;
    @FXML
    CheckBox box_small;
    @FXML
    CheckBox box_music;
    @FXML
    CheckBox box_sound;

    Parent root;
    Stage stage;
    Scene scene;

    public void initializeOptions(OptionState state) {
        box_music.setSelected(state.music);
        box_sound.setSelected(state.sound);
        switch(state.difficulty) {
            case -1:
                box_easy.setSelected(true);
                break;
            case 0:
                box_normal.setSelected(true);
                break;
            case 1:
                box_hard.setSelected(true);
        }
        switch(state.fieldSize) {
            case -1:
                box_small.setSelected(true);
                break;
            case 0:
                box_medium.setSelected(true);
                break;
            case 1:
                box_large.setSelected(true);
        }
    }

    @FXML
    public void playSound(ActionEvent event) {
        if (box_sound.isSelected()) {
            SoundEffects.playSound("ClickSound.wav");
        }
    }

    @FXML
    public void saveAndSwitchToTitle(ActionEvent event) throws IOException {
        boolean music, sound;
        short difficulty, fieldSize;
        music = box_music.isSelected();
        sound = box_sound.isSelected();
        if (box_hard.isSelected()) {
            difficulty = 1;
        } else if (box_easy.isSelected()) {
            difficulty = -1;
        } else {
            difficulty = 0;
        }
        if (box_large.isSelected()) {
            fieldSize = 1;
        } else if (box_small.isSelected()) {
            fieldSize = -1;
        } else {
            fieldSize = 0;
        }

        if (sound) {
            SoundEffects.playSound("ClickSound.wav");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("TitleScreen.fxml"));
        root = loader.load();

        MenuController menuController = loader.getController();
        OptionState optionState = new OptionState(music, sound, difficulty, fieldSize);
        menuController.setOptions(optionState);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void regulate(ActionEvent event) {
        Object source = event.getSource();
        if (source.equals(box_medium)) {
            box_medium.setSelected(true);
            box_large.setSelected(false);
            box_small.setSelected(false);
        }
        if (source.equals(box_large)) {
            box_medium.setSelected(false);
            box_large.setSelected(true);
            box_small.setSelected(false);
        }
        if (source.equals(box_small)) {
            box_medium.setSelected(false);
            box_large.setSelected(false);
            box_small.setSelected(true);
        }
        if (source.equals(box_normal)) {
            box_normal.setSelected(true);
            box_hard.setSelected(false);
            box_easy.setSelected(false);
        }
        if (source.equals(box_hard)) {
            box_normal.setSelected(false);
            box_hard.setSelected(true);
            box_easy.setSelected(false);
        }
        if (source.equals(box_easy)) {
            box_normal.setSelected(false);
            box_hard.setSelected(false);
            box_easy.setSelected(true);
        }
        if (box_sound.isSelected()) {
            SoundEffects.playSound("ClickSound.wav");
        }
    }
}
