package BreakOut;

import javafx.scene.image.Image;

public class Brick extends Sprite {

    private static final double BRICK_SPEED = 80.0;
    private static final double BRICK_WIDTH = 55.0;
    private static final double BRICK_HEIGHT = 20.0;
    private static final double MARGIN_BETWEEN_BRICKS = 5.0;
    private static final double FIRST_COLUMN = 30.0;
    private static final double FIRST_ROW = 30.0;

    private double elapsedTime;
    private double speed;
    private int directionX;
    private int life;

    public Brick(Image image, boolean isMoving, int life, int row, int col, double elapsedTime) {
        super(image);

        if(isMoving) {speed = BRICK_SPEED;}
        else {speed = 0.0;}

        directionX = 1;
        this.setFitWidth(BRICK_WIDTH);
        this.setFitHeight(BRICK_HEIGHT);
        this.setX(col * (BRICK_WIDTH + MARGIN_BETWEEN_BRICKS) + FIRST_COLUMN);
        this.setY(row * (BRICK_HEIGHT + MARGIN_BETWEEN_BRICKS) + FIRST_ROW);
        this.life = life;
        this.elapsedTime = elapsedTime;
    }

    @Override
    public void move() {
        this.setX(this.getX() + speed * directionX * elapsedTime);
    }

    @Override
    public void collide(Sprite other) {
    }

    @Override
    public void offBoundary() {
        if(this.getX() < 0 || this.getX() > getScreenWidth() - BRICK_WIDTH) {
            directionX *= -1;
        }
    }

    @Override
    public boolean isAlive() { return !(life == 0); }

    @Override
    public double getWidth() {return BRICK_WIDTH;}

    @Override
    public double getHeight() {return BRICK_HEIGHT;}

    public void setLife(int life) {this.life = life;}
    public int getLife() {return life;}

    private double getScreenWidth() {return getParent().getScene().getWidth();}
}
