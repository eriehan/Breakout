package BreakOut;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BreakOutGame extends Application {

    private static final String TITLE = "BRICKGAME BY ERIC";
    private static final double SCREEN_WIDTH = 480;
    private static final double SCREEN_HEIGHT = 600;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final Paint BACKGROUND = Color.rgb(244, 244, 248);
    private static final int PADDLE_SPEED = 200;
    private static final double PADDLE_LOCATION_Y = 550;
    private static final String PADDLE_IMAGE = "paddle.gif";
    private static final String BALL_IMAGE = "ball.gif";
    private static final String BRICK_IMAGE_1 = "brick1.gif";
    private static final String BRICK_IMAGE_2 = "brick2.gif";
    private static final String BRICK_IMAGE_3 = "brick3.gif";
    private static final String BRICK_IMAGE_4 = "brick4.gif";
    private static final String BRICK_IMAGE_5 = "brick5.gif";
    private static final String BRICK_IMAGE_6 = "brick6.gif";
    private static final String BRICK_IMAGE_P = "brick8.gif";
    private static final String BRICK_IMAGE_M = "brick10.gif";
    private static final String LEVEL_ONE_MAP = "lv1.txt";
    private static final String LEVEL_TWO_MAP = "lv2.txt";
    private static final String LEVEL_THREE_MAP = "lv3.txt";
    private static final String LEVEL_FOUR_MAP = "lv4.txt";
    private static final String LEVEL_FIVE_MAP = "lv5.txt";


    private Scene myScene;
    private Group myRoot = new Group();
    private Paddle myPaddle;
    private ArrayList<Sprite> sprites = new ArrayList<>();
    private ArrayList<Sprite> spritesToBeRemoved = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();
    private int life = 3;
    private int level = 1;
    private int bombNum = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        myScene = setupGame(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND);
        primaryStage.setScene(myScene);
        primaryStage.setTitle(TITLE);
        primaryStage.show();

        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupGame(double width, double height, Paint background) {
        Scene scene = new Scene(myRoot, width, height, background);

        myPaddle = new Paddle(stringToImage(PADDLE_IMAGE), level, SECOND_DELAY);
        addSprite(myPaddle);
        generateLevel(1);

        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        scene.setOnKeyReleased(e -> keyReleased(e.getCode()));

        return scene;
    }

    private void step(double elapsedTime) {
        updateSprites();
        handleCollisions();
        removeSprites();
    }

    private void handleKeyInput(KeyCode code) {
        if (code == KeyCode.RIGHT) {
            myPaddle.setSpeed(PADDLE_SPEED);
        } else if (code == KeyCode.LEFT) {
            myPaddle.setSpeed(PADDLE_SPEED * (-1));
        } else if (code == KeyCode.DIGIT1) {
            level = 1;
            generateLevel(level);
        } else if (code == KeyCode.DIGIT2) {
            level = 2;
            generateLevel(level);
        } else if (code == KeyCode.DIGIT3) {
            level = 3;
            generateLevel(level);
        } else if (code == KeyCode.DIGIT4) {
            level = 4;
            generateLevel(level);
        } else if (code == KeyCode.DIGIT5) {
            level = 5;
            generateLevel(level);
        } else if (code == KeyCode.P) {
            myPaddle.stretchPaddle(true);
        } else if (code == KeyCode.W) {
            myPaddle.changeWarp();
        } else if (code == KeyCode.L) {
            life++;
        } else if (code == KeyCode.B) {
            bombNum += 3;
        } else if (code == KeyCode.R) {
            resetPositions();
        } else if (code == KeyCode.ENTER) {
            enterKeyToMoveBall();
        }
    }

    private void keyReleased(KeyCode code) {
        if (code == KeyCode.RIGHT || code == KeyCode.LEFT) {
            myPaddle.setSpeed(0);
        }
    }

    private void handleCollisions() {
        for (Sprite spriteA : sprites) {
            for (Sprite spriteB : sprites) {
                spriteA.collide(spriteB);
            }
        }
    }

    private void addSprite(Sprite sprite) {
        sprites.add(sprite);
        myRoot.getChildren().add(sprite);
    }

    private void updateSprites() {
        if(balls.isEmpty()) {
            generateBall(myPaddle.getX() + myPaddle.getWidth()/2, myPaddle.getY(), level);
        }
        for (Sprite sprite : sprites) {
            sprite.update();
        }
        moveBallAlongPaddle();
    }

    private void removeSprites() {
        for (Sprite sprite : sprites) {
            if (!sprite.isAlive()) {
                if (sprite instanceof Ball) {
                    Ball ball = (Ball) sprite;
                    balls.remove(ball);
                }

                spritesToBeRemoved.add(sprite);
            }
        }
        for (Sprite sprite : spritesToBeRemoved) {
            myRoot.getChildren().remove(sprite);
            sprites.remove(sprite);
        }
        spritesToBeRemoved.clear();
    }

    private void generateLevel(int level) {
        clearMap();
        myPaddle.setWidthByLevel(level);
        paddleInit();
        generateBall(myPaddle.getX() + myPaddle.getWidth() / 2, myPaddle.getY(), level);
        drawMap(level);
    }

    private void drawMap(int level) {
        ArrayList<String[]> lines = new ArrayList<>();
        try {
            File file = new File(levelMap(level));
            System.out.println(file.exists());
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] strings = line.split(" ");
                lines.add(strings);
            }
        } catch (IOException e) {
            System.out.println(levelMap(level) + "cannot be found");
        }

        if(!lines.isEmpty()) {
            drawMap(lines);
        }
    }

    private void drawMap(ArrayList<String[]> lines) {
        int row = 0;
        int col = 0;
        for(String[] line : lines) {
            for(String str : line) {
                if(str.equals("1")) {generateBrick(row, col, 1, false);}
                if(str.equals("2")) {generateBrick(row, col, 2, false);}
                if(str.equals("3")) {generateBrick(row, col, 3, false);}
                if(str.equals("4")) {generateBrick(row, col, 4, false);}
                if(str.equals("5")) {generateBrick(row, col, 5, false);}
                if(str.equals("6")) {generateBrick(row, col, 6, false);}
                if(str.equals("P")) {generateBrick(row, col, -1, false);}
                if(str.equals("M")) {generateBrick(row, col, 1, true);}
                col++;
            }
            col = 0; row++;
        }
    }

    private void generateBall(double xPos, double yPos, int level) {
        double angle = Math.random() * 90 + 45;
        Ball ball = new Ball(stringToImage(BALL_IMAGE), level, angle, SECOND_DELAY);
        addSprite(ball);
        balls.add(ball);
        ball.setX(xPos - ball.getWidth() / 2);
        ball.setY(yPos - ball.getRadius() * 2);
    }

    private void generateBrick(int row, int col, int life, boolean isMoving) {
        Brick brick = new Brick(brickImage(life, isMoving), isMoving, life, row, col, SECOND_DELAY);
        addSprite(brick);
    }

    private void clearMap() {
        for (Sprite sprite : sprites) {
            if (!(sprite instanceof Paddle)) {
                spritesToBeRemoved.add(sprite);
            }
            if(sprite instanceof Ball) {
                Ball ball = (Ball) sprite;
                balls.remove(ball);
            }
        }
    }

    private void clearBall() {
        spritesToBeRemoved.addAll(balls);
        balls.clear();
    }

    private void moveBallAlongPaddle() {
        for(Ball ball : balls) {
            if(!ball.isMoving()) {
                ball.setX(myPaddle.getX() + myPaddle.getWidth()/2 - ball.getWidth() / 2);
            }
        }
    }

    // method that places paddle at the starting place
    private void paddleInit() {
        myPaddle.setX(SCREEN_WIDTH/2 - myPaddle.getWidth()/2);
        myPaddle.setY(PADDLE_LOCATION_Y);
    }

    private void resetPositions() {
        clearBall();
        paddleInit();
        generateBall(myPaddle.getX() + myPaddle.getWidth()/2, myPaddle.getY(), level);
    }

    private void enterKeyToMoveBall() {
        for(Ball ball : balls) {
            if(!(ball.isMoving())) {
                ball.startMoving();
            }
        }
    }

    private Image stringToImage(String str) {
        return new Image(this.getClass().getClassLoader().getResourceAsStream(str));
    }

    private String levelMap(int level) {
        switch(level) {
            case 1: return LEVEL_ONE_MAP;
            case 2: return LEVEL_TWO_MAP;
            case 3: return LEVEL_THREE_MAP;
            case 4: return LEVEL_FOUR_MAP;
        }
        return LEVEL_FIVE_MAP;
    }

    private Image brickImage(int life, boolean isMoving) {
        if(isMoving) {return stringToImage(BRICK_IMAGE_M);}
        switch(life) {
            case 1: return stringToImage(BRICK_IMAGE_1);
            case 2: return stringToImage(BRICK_IMAGE_2);
            case 3: return stringToImage(BRICK_IMAGE_3);
            case 4: return stringToImage(BRICK_IMAGE_4);
            case 5: return stringToImage(BRICK_IMAGE_5);
            case 6: return stringToImage(BRICK_IMAGE_6);
        }
        return stringToImage(BRICK_IMAGE_P);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
