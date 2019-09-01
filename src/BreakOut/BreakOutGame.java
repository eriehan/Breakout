package BreakOut;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class BreakOutGame extends Application {

    public static final String TITLE = "BRICKGAME BY ERIC";
    public static final int WIDTH = 480;
    public static final int HEIGHT = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;

    private Scene myScene;
    private Group myRoot = new Group();
    private ArrayList<Sprite> sprites = new ArrayList<>();
    private int ballNum = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        myScene = setupGame(WIDTH, HEIGHT, BACKGROUND);
        primaryStage.setScene(myScene);
        primaryStage.setTitle(TITLE);
        primaryStage.show();

        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupGame (int width, int height, Paint background) {

        Scene scene = new Scene(myRoot, width, height, background);

        return scene;
    }

    private void step(double elapsedTime) {

    }

    private void updateSprites() {
        for(Sprite sprite : sprites) {
            sprite.update();
        }
    }

    private void removeSprites() {
        for(Sprite sprite : sprites) {
            if(!sprite.isAlive()) {
                myRoot.getChildren().remove(sprite);
                sprites.remove(sprite);
                if(sprite instanceof Ball) {
                    ballNum--;
                }
            }
        }
    }

    private void generateBall(double xPos, double yPos) {

        ballNum++;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
