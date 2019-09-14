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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BreakOutGame extends Application {

    private static final String TITLE = "BRICKGAME BY ERIC";
    private static final double STARTING_SCENE_WIDTH = 720;
    private static final double STARING_SCENE_HEIGHT = 720;
    private static final double STARTING_SCENE_IMAGE_WIDTH = 480;
    private static final Paint STARTING_BACKGROUND = Color.WHITE;
    private static final Paint STARTING_SCENE_TEXT_COLOR = Color.CRIMSON;
    private static final double SCREEN_WIDTH = 480;
    private static final double SCREEN_HEIGHT = 690;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final Paint BACKGROUND = Color.rgb(244, 244, 248);
    private static final Paint HBOX_COLOR = Color.LIGHTBLUE;
    private static final double PADDLE_LOCATION_Y = 580;
    private static final double FIRST_QUADRANT = 90;
    private static final int ITEM_POSSIBILITY_SMALL = 20;
    private static final int ITEM_POSSIBILITY_LARGE = 35;
    public static final double MIN_Y_POS = 30;
    public static final double MAX_Y_POS = 620;
    public static final double ABILITY_Y_POS = 660;

    //resource file names
    private static final String BREAKOUT_IMAGE = "breakout.png";
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
    private static final String SLOW_POWER = "slowpower.gif";
    private static final String NEW_BALL_POWER = "newball.png";
    private static final String LEVEL_ONE_MAP = "resources/lv1.txt";
    private static final String LEVEL_TWO_MAP = "resources/lv2.txt";
    private static final String LEVEL_THREE_MAP = "resources/lv3.txt";
    private static final String LEVEL_FOUR_MAP = "resources/lv4.txt";
    private static final String LEVEL_FIVE_MAP = "resources/lv5.txt";
    private static final String EXPLANATIONS = "resources/explanations.txt";
    private static final String START_SCREEN_TEXTS = "resources/initialSceneTexts.txt";


    private Stage myStage;
    private Group myRoot = new Group();
    private Paddle myPaddle;
    private SpriteHolder spriteHolder = new SpriteHolder();
    private ArrayList<Label> labels = new ArrayList<>();
    private ArrayList<String> explanations = new ArrayList<>();
    private int explanationIdx = 0;
    private int explanationClock = 0;
    private int life;
    private boolean moreItem = false;
    private int score;
    private int level = 1;
    private int bombNum;
    private Timeline animation;

    @Override
    public void start(Stage primaryStage) throws Exception {
        myStage = primaryStage;
        makeInitialScene(STARTING_SCENE_WIDTH, STARING_SCENE_HEIGHT, STARTING_BACKGROUND);
        myStage.setTitle(TITLE);
        myStage.show();
    }

    private void makeInitialScene(double width, double height, Paint background) {
        Group root = new Group();
        Scene initialScene = new Scene(root, width, height, background);
        addLinesToInitialScreen(root, width, height);
        ImageView breakOutImage = new ImageView(stringToImage(BREAKOUT_IMAGE));
        breakOutImage.setFitWidth(STARTING_SCENE_IMAGE_WIDTH);
        breakOutImage.setX(width/2 - STARTING_SCENE_IMAGE_WIDTH/2);
        breakOutImage.setY(100);
        root.getChildren().add(breakOutImage);
        initialScene.setOnKeyPressed(keyEvent -> {
            setUpGame(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND);
            KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
            animation = new Timeline();
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.getKeyFrames().add(frame);
            animation.play();
        });
        myStage.setScene(initialScene);
    }

    private void setUpGame(double width, double height, Paint background) {
        Scene gameScene = new Scene(myRoot, width, height, background);
        while(!myRoot.getChildren().isEmpty()) {
            myRoot.getChildren().remove(myRoot.getChildren().get(0));
        }
        myRoot.getChildren().removeAll();
        explanations = readFromFile(EXPLANATIONS);

        myRoot.getChildren().addAll(statsInit(), abilityLabelsInit(), explanationLabelsInit());

        myPaddle = new Paddle(stringToImage(PADDLE_IMAGE), level, SECOND_DELAY);
        addSprite(myPaddle);
        generateLevel(1);
        life = 3;

        gameScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        gameScene.setOnKeyReleased(e -> keyReleased(e.getCode()));
        myStage.setScene(gameScene);
    }

    private void step() {
        updateSprites();
        labelUpdate();
        handleCollisions();
        removeSprites();
    }

    private void handleKeyInput(KeyCode code) {
        if (code == KeyCode.RIGHT) { myPaddle.moveRight();}
        else if (code == KeyCode.LEFT) { myPaddle.moveLeft();}
        else if (code == KeyCode.DIGIT1) { generateLevel(1);}
        else if (code == KeyCode.DIGIT2) { generateLevel(2);}
        else if (code == KeyCode.DIGIT3) { generateLevel(3);}
        else if (code == KeyCode.DIGIT4) { generateLevel(4);}
        else if (code == KeyCode.DIGIT5) { generateLevel(5);}
        else if (code == KeyCode.P) { myPaddle.stretchPaddle(true);}
        else if (code == KeyCode.W) { myPaddle.changeWarp();}
        else if (code == KeyCode.L) { life++;}
        else if (code == KeyCode.B) { bombNum += 3;}
        else if (code == KeyCode.R) { resetPositions();}
        else if (code == KeyCode.ENTER) { moveBalls();}
        else if (code == KeyCode.A) { myPaddle.changeNormalBounce();}
        else if (code == KeyCode.SPACE) { shootBomb();}
        else if (code == KeyCode.N && spriteHolder.isThereBall()) { generateBalls();}
        else if (code == KeyCode.M) { myPaddle.changeMagnetic(true);}
        else if (code == KeyCode.S) { spriteHolder.changeBallSpeed(true, level); }
        else if(code == KeyCode.Q) { spriteHolder.changeBallSpeed(false, level); }
        else if(code == KeyCode.I) { moreItem = !moreItem; }
        else if(code == KeyCode.F) { myPaddle.increaseSpeed(true); }
    }

    private void keyReleased(KeyCode code) {
        if (code == KeyCode.RIGHT || code == KeyCode.LEFT) { myPaddle.stop(); }
    }

    private void handleCollisions() {
        spriteHolder.handleCollisions();
    }

    private void addSprite(Sprite sprite) {
        spriteHolder.addSprite(sprite);
        myRoot.getChildren().add(sprite);
    }

    private void updateSprites() {
        generateItem();
        if(spriteHolder.isThereBrickToEdit()) { editBricks(); }
        if(spriteHolder.allBricksRemoved()) { generateLevel(level+1); }
        if(!spriteHolder.isThereBall()) {
            generateBall(myPaddle.getCenterX(), myPaddle.getY(), FIRST_QUADRANT/2,
                    FIRST_QUADRANT + FIRST_QUADRANT/2);
            life--;
        }
        if(life==0) {gameEnd(false, SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND);}
        spriteHolder.updateSprites();
        itemEffects(myPaddle.getItemType());
        moveBallAlongPaddle();
        myPaddle.resetType();
    }

    private void removeSprites() {
        for (Sprite sprite : spriteHolder.getAllSprites()) {
            if (!sprite.isAlive()) {
                spriteHolder.addSpriteToBeRemoved(sprite);

                if (sprite instanceof Brick) {
                    Brick brick = (Brick) sprite;
                    score += 10;
                    if (!(brick.getBrickLife() == 0)) {
                        spriteHolder.addBrickToBeEdited(new Brick(brickImage(brick.getBrickLife(), brick.isMoving()),
                                brick.isMoving(), brick.getBrickLife(), brick.getRow(), brick.getCol(), SECOND_DELAY));
                    }
                }
            }
        }
        for (Sprite sprite : spriteHolder.getSpritesToBeRemoved()) {
            myRoot.getChildren().remove(sprite);
        }
        spriteHolder.removeAllSpritesToBeRemoved();
    }

    private void generateLevel(int level) {
        if(level==6) {gameEnd(true, SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND);}
        else {
            this.level = level;
            spriteHolder.clearMap();
            myPaddle.setWidthByLevel(level);
            paddleInit();
            generateBall(myPaddle.getX() + myPaddle.getWidth() / 2, myPaddle.getY(), FIRST_QUADRANT / 2,
                    FIRST_QUADRANT + FIRST_QUADRANT / 2);
            drawMap(level);
        }
    }

    private void drawMap(int level) {
        ArrayList<String> lines = readFromFile(levelMap(level));
        ArrayList<String[]> mapLines = new ArrayList<>();

        for(String line : lines) {
            mapLines.add(line.split(" "));
        }
        if(!mapLines.isEmpty()) {
            drawMap(mapLines);
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
        ball.setX(xPos - ball.getWidth() / 2);
        ball.setY(yPos - ball.getRadius() * 2);
    }

    private void generateBrick(int row, int col, int life, boolean isMoving) {
        Brick brick = new Brick(brickImage(life, isMoving), isMoving, life, row, col, SECOND_DELAY);
        if(brick.getBrickLife()>=0) {spriteHolder.addRemovableBrick(brick);}
        addSprite(brick);
    }

    private void editBricks() {
        for(Brick brick : spriteHolder.getBricksToBeEdited()) {
            addSprite(brick);
            spriteHolder.addRemovableBrick(brick);
        }
        spriteHolder.clearBrickstoBeEdited();
    }

    private void generateBalls() {
        Ball ball = spriteHolder.getAllBalls().get(0);
        if (spriteHolder.getNumBalls() <= 3) {
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
        if (spriteHolder.getNumBalls() <= 4) {
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
        for(Ball ball : spriteHolder.getAllBalls()) {
            if(!ball.isMoving()) { ball.setX(myPaddle.getCenterX() - ball.getWidth() / 2); }
        }
    }

    // method that places paddle at the starting place
    private void paddleInit() {
        myPaddle.setX(SCREEN_WIDTH/2 - myPaddle.getWidth()/2);
        myPaddle.setY(PADDLE_LOCATION_Y);
    }

    private void resetPositions() {
        spriteHolder.clearBall();
        paddleInit();
        generateBall(myPaddle.getCenterX(), myPaddle.getY(), FIRST_QUADRANT/2,
                FIRST_QUADRANT + FIRST_QUADRANT/2);
    }

    private void moveBalls() {
        for(Ball ball : spriteHolder.getAllBalls()) {
            if(!(ball.isMoving())) { ball.startMoving(level); }
        }
    }

    private boolean allBallsMoving() {
        for(Ball ball : spriteHolder.getAllBalls()) {
            if(!(ball.isMoving())) { return false; }
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
        labels.add(new Label("Score : 0"));
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
        hbox.setTranslateY(ABILITY_Y_POS);
        return hbox;
    }

    private HBox explanationLabelsInit() {
        HBox hbox = defaultHBox();
        Label label = new Label("S -> Sets the speed of the balls slower");
        labels.add(label);
        label.setPrefHeight(40);
        hbox.getChildren().add(label);
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
        explanationUpdate();
    }

    private void generateItem() {
        for(Brick brick : spriteHolder.getRemovedBricks()) {
            generateItem(brick);
        }
        spriteHolder.clearRemovedBricks();
    }

    private void generateItem(Brick brick) {
        int ranNum = (int) (Math.random() * 100 + 1);
        int itemPos = ITEM_POSSIBILITY_SMALL;
        if(!moreItem) {itemPos = ITEM_POSSIBILITY_LARGE;}
        if(ranNum<=itemPos/7) {generateItem('L', brick);}
        else if(ranNum<=itemPos*2/7) {generateItem('P', brick); }
        else if(ranNum<=itemPos * 4/7) {generateItem('B', brick);}
        else if(ranNum<=itemPos * 6/7) {generateItem('N', brick);}
        else if(ranNum<= itemPos) {generateItem('S', brick);}
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
            case 'N' : return stringToImage(NEW_BALL_POWER);
            case 'S' : return stringToImage(SLOW_POWER);
        }
        return stringToImage(SLOW_POWER);
    }

    private void itemEffects(ArrayList<Character> types) {
        for(char type : types) {
            if(type == 'P') {myPaddle.stretchPaddle(false);}
            else if(type == 'L') {life++;}
            else if(type == 'B') {bombNum += 3;}
            else if(type == 'N') {generateBalls();}
            else if(type == 'S') {spriteHolder.changeBallSpeed(true, level);}
        }
    }

    public void explanationUpdate() {
        Label label = labels.get(7);
        if(!allBallsMoving()) {
            explanationClock = 240;
            label.setText("Press Enter Key to move ball(s)");
        }
        else if(explanationClock < 240) { explanationClock++; }
        else {
            label.setText(explanations.get(explanationIdx));
            if(explanationIdx == explanations.size() - 1) {explanationIdx = 0;}
            else {explanationIdx++;}
            explanationClock = 0;
        }
    }

    private ArrayList<String> readFromFile(String fileName) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println(fileName + " cannot be found");
        }
        return lines;
    }

    private Group addLabelToGroup(Group root, String str, Paint background, double width, double yPos, int textSize) {
        root.getChildren().add(makeLabelOnCenter(str, background, width, width*2/3, yPos, textSize));
        return root;
    }

    private Label makeLabelOnCenter(String text, Paint background, double screenWidth, double labelWidth, double yPos, int textSize) {
        Label label = new Label(text);
        label.setTextFill(background);
        label.setAlignment(Pos.CENTER);
        label.setTranslateY(yPos);
        label.setTranslateX(screenWidth/2 - labelWidth/2);
        label.setPrefWidth(labelWidth);
        label.setFont(new Font(textSize));
        return label;
    }

    private void gameEnd(boolean success, double width, double height, Paint background) {
        Group gameEndRoot = new Group();
        Scene gameEndScene = new Scene(gameEndRoot, width, height, background);
        if(!success) {addLabelToGroup(gameEndRoot, "WASTED", STARTING_SCENE_TEXT_COLOR, width, height * 0.4, 40);}
        else {addLabelToGroup(gameEndRoot, "YOU WIN!", STARTING_SCENE_TEXT_COLOR, width, height * 0.4, 40);}
        myStage.setScene(gameEndScene);
        animation.stop();
    }

    private Group addLinesToInitialScreen(Group root, double width, double height) {
        ArrayList<String> initialTexts = readFromFile(START_SCREEN_TEXTS);
        double yPos; int textSize = 11;
        for(int i=0; i<initialTexts.size(); i++) {
            if(i==0) {yPos = 0.6 * height; textSize = 20;}
            else if(i==1) {yPos = 0.65 * height; textSize = 12;}
            else if(i==2) {yPos = 0.68 * height;}
            else if(i==3) {yPos = 0.71 * height;}
            else if(i==4) {yPos = 0.73 * height;}
            else if(i==5) {yPos = 0.76 * height;}
            else if(i==6) {yPos = 0.78 * height;}
            else  {yPos = 0.81 * height;}
            root = addLabelToGroup(root, initialTexts.get(i), STARTING_SCENE_TEXT_COLOR, width, yPos, textSize);
        }
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}