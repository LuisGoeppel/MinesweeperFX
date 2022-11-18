package Controller;

import Model.Field;
import Model.GameTime;
import Model.Tile;
import Model.TileState;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GameController {

    private GameTime gameTime;
    private Field field;
    private Timeline clock;

    private int width, height, nBombsLeft, nFieldsLeft;
    private OptionState optionState;

    private Image backside;
    private Image flag;
    private Image bomb;
    private Image bombBackside;
    private Image[] numbers;
    private ImageView[][] cardImageViews;
    private Image[][] content;

    private int cardDimensions = 25;

    Parent root;
    Stage stage;
    Scene scene;

    @FXML
    private Label bombsLeft;

    @FXML
    private Label fieldsLeft;

    @FXML
    private AnchorPane cardAnchor;

    @FXML
    private Label time;

    private String directory;

    public void initialize(int width, int height, int nBombs) {

        this.width = width;
        this.height = height;
        this.nBombsLeft = nBombs;
        this.nFieldsLeft = width * height - nBombs;

        directory = System.getProperty("user.dir") + "\\src\\main\\resources\\Images\\";

        cardDimensions = (int)(515 / (double)(width));

        backside = new Image(directory + "Tile.png");
        bomb = new Image(directory + "bomb.jpg");
        flag = new Image(directory + "TileWithFlag.png");
        bombBackside = new Image(directory + "bombBackground.png");

        numbers = new Image[7];
        numbers[0] = new Image(directory + "EmptyTile.png");
        for (int i = 1; i < 6; i++) {
            numbers[i] = new Image(directory + "Number" + i + ".png");
        }
        numbers[6] = new Image(directory + "HighNumber.png");

        cardImageViews = new ImageView[width][height];
        content = new Image[width][height];

        fieldsLeft.setText(Integer.toString(nFieldsLeft));
        bombsLeft.setText(Integer.toString(nBombsLeft));

        startClock();

        field = new Field(width, height, nBombs);

        //Create gameGrid
        GridPane gridPane = new GridPane();
        for (int i = 0; i < width; i++) {
            gridPane.addColumn(i);
        }
        for (int i = 0; i < height; i++) {
            gridPane.addRow(i);
        }

        for (int i = 0; i < width; i++) {
            for (int k = 0; k < height; k++) {

                cardImageViews[i][k] = new ImageView(backside);
                cardImageViews[i][k].setFitHeight(cardDimensions);
                cardImageViews[i][k].setFitWidth(cardDimensions);

                gridPane.add(cardImageViews[i][k], i, k);

                final int finalI = i;
                final int finalK = k;

                cardImageViews[i][k].addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if (!field.isInitialized()) {
                            field.initialize(finalI, finalK);
                            initializeImages();
                        }
                        boolean isLost = !(field.reveal(finalI, finalK));
                        updateImages();
                        if (isLost) {
                            if (optionState.sound) {
                                SoundEffects.playSound("ExplosionSound.wav");
                            }
                            if (optionState.music) {
                                SoundEffects.stopMusic();
                            }
                            revealAll();
                            delay(2000, () -> switchSceneToLoser(mouseEvent));
                        } else if (optionState.sound) {
                            SoundEffects.playSound("ClickSound.wav");
                        }
                        boolean isWon = field.isWon();
                        if (isWon) {
                            if (optionState.music) {
                                SoundEffects.stopMusic();
                            }
                            delay(1000, () -> switchSceneToWinner(mouseEvent));
                        }
                    } else {
                        if (field.isInitialized()) {
                            field.flag(finalI, finalK);
                            updateImages();
                        }
                    }
                });
            }
        }
        cardAnchor.getChildren().add(gridPane);
    }

    public void setOptionState(OptionState optionState) {
        this.optionState = optionState;
    }

    private void startClock() {
        gameTime = new GameTime();
        gameTime.start();

        clock = new Timeline(
                new KeyFrame(
                        Duration.seconds(0), (ActionEvent actionEvent) ->
                        this.time.setText(clockLayout(gameTime.getPassedGameTime()))),
                new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private String clockLayout(long time) {
        NumberFormat timeFormatter = new DecimalFormat("00");

        String seconds = timeFormatter.format(time % 60);
        String minutes = timeFormatter.format(time / 60);

        return (minutes + ":" + seconds);
    }

    private void initializeImages() {

        //Initialize images
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = field.getTileAt(x, y);

                if (tile.getIsBomb()) {
                    content[x][y] = bombBackside;
                } else {
                    int nNeighbour = Math.min(5, tile.getNeighbourBombs());
                    content[x][y] = numbers[nNeighbour];
                }
            }
        }
    }

    private void updateImages() {
        int fields = -nBombsLeft;
        int flags = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = field.getTileAt(x, y);
                if (tile.getState().equals(TileState.HIDDEN)) {
                    cardImageViews[x][y].setImage(backside);
                    fields++;
                } else if (tile.getState().equals(TileState.FLAGGED)) {
                    cardImageViews[x][y].setImage(flag);
                    flags++;
                    fields++;
                } else {
                    cardImageViews[x][y].setImage(content[x][y]);
                }
            }
        }
        fieldsLeft.setText(Integer.toString(fields));
        bombsLeft.setText(Integer.toString(Math.max(nBombsLeft - flags, 0)));
    }

    private void revealAll() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cardImageViews[x][y].setImage(content[x][y]);
            }
        }
    }

    private void switchSceneToLoser( MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoserScreen.fxml"));
            root = loader.load();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchSceneToWinner(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WinnerScreen.fxml"));
            root = loader.load();

            MenuController menuController = loader.getController();
            menuController.setTime(time.getText());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }
}
