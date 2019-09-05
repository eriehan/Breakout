package BreakOut;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
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
    private static final double SCREEN_HEIGHT = 650;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final Paint BACKGROUND = Color.rgb(244, 244, 248);
    private static final Paint HBOX_COLOR = Color.LIGHTBLUE;
    private static final int PADDLE_SPEED = 200;
    private static final double PADDLE_LOCATION_Y = 580;
    private static final double FIRST_QUADRANT = 90;
    private static final int ITEM_POSSIBILITY_SMALL = 30;
    private static final int ITEM_POSSIBILITY_LARGE = 35;
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
    private static final String BOMB_IMAGE = "bomb.png";
    private static final String LIFE_POWER = "lifepower.gif";
    private static final String BOMB_POWER = "bombpower.gif";
    private static final String PADDLE_POWER = "paddlelength.gif";
    private static final String LEVEL_ONE_MAP = "resources/lv1.txt";
    private static final String LEVEL_TWO_MAP = "resources/lv2.txt";
    private static final String LEVEL_THREE_MAP = "resources/lv3.txt";
    private static final String LEVEL_FOUR_MAP = "resources/lv4.txt";
    private static final String LEVEL_FIVE_MAP = "resources/lv5.txt";
    public static final double MIN_Y_POS = 30;
    public static final double MAX_Y_POS = 620;

    private Scene myScene;
    private Group myRoot = new Group();
    private Paddle myPaddle;
    private ArrayList<Sprite> sprites = new ArrayList<>();
    private ArrayList<Sprite> spritesToBeRemoved = new ArrayList<>();
    private ArrayList<Brick> bricksToBeEdited = new ArrayList<>();
    private ArrayList<Brick> removableBricks = new ArrayList<>();
    private ArrayList<Brick> removedBricks = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Label> labels = new ArrayList<>();
    private int life;
    private int score;
    private int level = 1;
    private int bombNum;
    private KeyFrame frame;
    private Timeline animation;

    @Override
    public void start(Stage primaryStage) throws Exception {
        myScene = setupGame(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND);
        HBox hbox = statsInit();
        HBox hbox2 = abilityLabelsInit();
        myRoot.getChildren().add(hbox);
        myRoot.getChildren().add(hbox2);

        primaryStage.setScene(myScene);
        primaryStage.setTitle(TITLE);
        primaryStage.show();

        frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupGame(double width, double height, Paint background) {
        Scene scene = new Scene(myRoot, width, height, background);

        myPaddle = new Paddle(stringToImage(PADDLE_IMAGE), level, SECOND_DELAY);
        addSprite(myPaddle);
        generateLevel(1);
        life = 3;

        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        scene.setOnKeyReleased(e -> keyReleased(e.getCode()));
        return scene;
    }

    private void step() {
        updateSprites();
        labelUpdate();
        handleCollisions();
        removeSprites();
    }

    private void handleKeyInput(KeyCode code) {
        if (code == KeyCode.RIGHT) {
            myPaddle.setSpeed(PADDLE_SPEED);
        } else if (code == KeyCode.LEFT) {
            myPaddle.setSpeed(PADDLE_SPEED * (-1));
        } else if (code == KeyCode.DIGIT1) {
            generateLevel(1);
        } else if (code == KeyCode.DIGIT2) {
            generateLevel(2);
        } else if (code == KeyCode.DIGIT3) {
            generateLevel(3);
        } else if (code == KeyCode.DIGIT4) {
            generateLevel(4);
        } else if (code == KeyCode.DIGIT5) {
            generateLevel(5);
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
            moveBalls();
        } else if (code == KeyCode.Z) {
            animation.stop();
        } else if(code == KeyCode.X) {
            animation.play();
        } else if(code == KeyCode.A) {
            myPaddle.changeNormalBounce();
        } else if(code == KeyCode.SPACE) {
            shootBomb();
        } else if(code == KeyCode.N && !balls.isEmpty()) {
            generateBalls();
        } else if(code == KeyCode.M) {
            myPaddle.changeMagnetic(true);
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
        generateItem();
        if(!bricksToBeEdited.isEmpty()) {
            editBricks();
        }
        if(removableBricks.size() == 0) {
            generateLevel(level+1);
        }
        if(balls.isEmpty()) {
            generateBall(myPaddle.getCenterX(), myPaddle.getY(), FIRST_QUADRANT/2,
                    FIRST_QUADRANT + FIRST_QUADRANT/2);
            life--;
        }
        for (Sprite sprite : sprites) {
            sprite.update();
        }
        itemEffects(myPaddle.getItemType());
        moveBallAlongPaddle();
        myPaddle.resetType();
    }

    private void removeSprites() {
        for (Sprite sprite : sprites) {
            if (!sprite.isAlive()) {
                if (sprite instanceof Ball) {
                    Ball ball = (Ball) sprite;
                    balls.remove(ball);
                }
                if (sprite instanceof Brick) {
                    Brick brick = (Brick) sprite;
                    removableBricks.remove(brick);
                    if (!(brick.getBrickLife() == 0)) {
                        bricksToBeEdited.add(new Brick(brickImage(brick.getBrickLife(), brick.isMoving()),
                                brick.isMoving(), brick.getBrickLife(), brick.getRow(), brick.getCol(), SECOND_DELAY));
                    }
                    else {removedBricks.add(brick);}
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
        this.level = level;
        clearMap();
        myPaddle.setWidthByLevel(level);
        paddleInit();
        generateBall(myPaddle.getX() + myPaddle.getWidth() / 2, myPaddle.getY(), FIRST_QUADRANT/2,
                FIRST_QUADRANT + FIRST_QUADRANT/2);
        drawMap(level);
    }

    private void drawMap(int level) {
        ArrayList<String[]> lines = new ArrayList<>();
        try {
            File file = new File(levelMap(level));
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] strings = line.split(" ");
                lines.add(strings);
            }
        } catch (IOException e) {
            System.out.println(levelMap(level) + " cannot be found");
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

    private void generateBall(double xPos, double yPos, double angle1, double angle2) {
        double angle = Math.random() * (angle2 - angle1) + angle1;
        Ball ball = new Ball(stringToImage(BALL_IMAGE), angle, SECOND_DELAY);
        addSprite(ball);
        balls.add(ball);
        ball.setX(xPos - ball.getWidth() / 2);
        ball.setY(yPos - ball.getRadius() * 2);
    }

    private void generateBrick(int row, int col, int life, boolean isMoving) {
        Brick brick = new Brick(brickImage(life, isMoving), isMoving, life, row, col, SECOND_DELAY);
        if(brick.getBrickLife()>=0) {removableBricks.add(brick);}
        addSprite(brick);
    }

    private void editBricks() {
        for(Brick brick : bricksToBeEdited) {
            addSprite(brick);
            removableBricks.add(brick);
        }
        bricksToBeEdited.clear();
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
        removableBricks.clear();
        bricksToBeEdited.clear();
    }

    private void clearBall() {
        spritesToBeRemoved.addAll(balls);
        balls.clear();
    }

    private void generateBalls() {
        Ball ball = balls.get(0);
        if (balls.size() <= 3) {
            if (ball.getAngle() < FIRST_QUADRANT) {
                generateBall(ball.getX() + ball.getWidth() / 2, ball.getY() + ball.getRadius() * 2,
                        ball.getAngle(), FIRST_QUADRANT * 1.5);
            } else if(ball.getAngle() < FIRST_QUADRANT * 2){
                generateBall(ball.getX() + ball.getWidth() / 2, ball.getY() + ball.getRadius() * 2,
                        FIRST_QUADRANT * 0.5, ball.getAngle());
            } else {
                generateBall(ball.getX() + ball.getWidth() / 2, ball.getY() + ball.getRadius() * 2,
                        FIRST_QUADRANT * 0.5, FIRST_QUADRANT * 1.5);
            }
        }
        if (balls.size() <= 4) {
            if (!ball.isMoving() || ball.getAngle() > FIRST_QUADRANT * 2) {
                generateBall(ball.getX() + ball.getWidth() / 2, ball.getY() + ball.getRadius() * 2,
                        FIRST_QUADRANT * 0.5, FIRST_QUADRANT * 1.5);
            } else {
                generateBall(ball.getX() + ball.getWidth() / 2, ball.getY() + ball.getRadius() * 2,
                        FIRST_QUADRANT * 2.5, FIRST_QUADRANT * 3.5);
            }
        }
        moveBalls();
    }

    //when the ball(s) is supposed to be on the paddle, the ball will move along the paddle when
    //LEFT or RIGHT key is pressed.
    private void moveBallAlongPaddle() {
        for(Ball ball : balls) {
            if(!ball.isMoving()) {
                ball.setX(myPaddle.getCenterX() - ball.getWidth() / 2);
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
        generateBall(myPaddle.getCenterX(), myPaddle.getY(), FIRST_QUADRANT/2,
                FIRST_QUADRANT + FIRST_QUADRANT/2);
    }

    private void moveBalls() {
        for(Ball ball : balls) {
            if(!(ball.isMoving())) {
                ball.startMoving(level);
            }
        }
    }

    private boolean allBallsMoving() {
        for(Ball ball : balls) {
            if(!(ball.isMoving())) {
                return false;
            }
        }
        return true;
    }

    private void shootBomb() {
        if(bombNum > 0 && allBallsMoving()) {
            Bomb bomb = new Bomb(stringToImage(BOMB_IMAGE), SECOND_DELAY, myPaddle);
            addSprite(bomb);
            bombNum--;
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

    private HBox statsInit() {
        labels.clear();
        HBox hbox = defaultHBox();
        labels.add(new Label("Life : 3"));
        labels.add(new Label("Score : 0 "));
        labels.add(new Label("Bombs : 0"));
        labels.add(new Label("Level : 1"));

        for(Label label : labels) {
            label.setPrefHeight(30);
            hbox.getChildren().add(label);
        }
        return hbox;
    }

    private HBox abilityLabelsInit() {
        HBox hbox = defaultHBox();
        labels.add(new Label("Warp: off"));
        labels.add(new Label("Bounce differently: on"));
        labels.add(new Label("Magnetic: off"));

        for(int i=4; i<labels.size(); i++) {
            labels.get(i).setPrefHeight(30);
            hbox.getChildren().add(labels.get(i));
        }
        hbox.setTranslateY(MAX_Y_POS);
        return hbox;
    }

    private HBox defaultHBox() {
        HBox hbox = new HBox();
        BackgroundFill fill = new BackgroundFill(HBOX_COLOR, CornerRadii.EMPTY, Insets.EMPTY);
        hbox.setBackground(new Background(fill));
        hbox.setPrefWidth(SCREEN_WIDTH);
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    private void labelUpdate() {
        labels.get(0).setText("Life: " + life);
        labels.get(1).setText("Score: " + score);
        labels.get(2).setText("Bombs : " + bombNum);
        labels.get(3).setText("Level : " + level);
        if(myPaddle.getWarp()) {labels.get(4).setText("Warp: on");}
        else {labels.get(4).setText("Warp: off");}

        if(myPaddle.getNormalBounce()) {labels.get(5).setText("Bounce differently: off");}
        else {labels.get(5).setText("Bounce differently: on");}

        if(myPaddle.isMagnetic()) {labels.get(6).setText("Magnetic: on");}
        else {labels.get(6).setText("Magnetic: off");}
    }

    private void generateItem() {
        for(Brick brick : removedBricks) {
            generateItem(brick);
        }
        removedBricks.clear();
    }

    private void generateItem(Brick brick) {
        int ranNum = (int) (Math.random() * 100 + 1);
        if(ranNum<=ITEM_POSSIBILITY_SMALL/6) {generateItem('L', brick);}
        else if(ranNum<=ITEM_POSSIBILITY_SMALL /2) {generateItem('P', brick); }
        else if(ranNum<=ITEM_POSSIBILITY_SMALL) {generateItem('B', brick);}
    }

    private void generateItem(char type, Brick brick) {
        Item item = new Item(itemImage(type), type, SECOND_DELAY);
        addSprite(item);
        item.setX(brick.getCenterX() - item.getWidth()/2);
        item.setY(brick.getCenterY() - item.getHeight()/2);
    }

    private Image itemImage(char type) {
        switch(type) {
            case 'L' : return stringToImage(LIFE_POWER);
            case 'P' : return stringToImage(PADDLE_POWER);
            case 'B' : return stringToImage(BOMB_POWER);
        }
        return stringToImage(BOMB_POWER);
    }

    private void itemEffects(String type) {

        if(type!=null) {
            String[] effects = type.split(" ");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
