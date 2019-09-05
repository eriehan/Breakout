package BreakOut;

import javafx.scene.image.Image;

public class Brick extends Sprite {

    private static final double BRICK_SPEED = 80.0;
    private static final double BRICK_WIDTH = 67.0;
    private static final double BRICK_HEIGHT = 20.0;
    private static final double MARGIN_BETWEEN_BRICKS = 1.0;
    private static final double FIRST_COLUMN = 1.0;
    private static final double FIRST_ROW = 70.0;

    private double elapsedTime;
    private double speed;
    private int directionX;
    private int brickLife;
    private boolean hit;
    private int row;
    private int col;

    public Brick(Image image, boolean isMoving, int brickLife, int row, int col, double elapsedTime) {
        super(image);

        if(isMoving) {speed = BRICK_SPEED;}
        else {speed = 0.0;}

        directionX = 1;
        this.setFitWidth(BRICK_WIDTH);
        this.setFitHeight(BRICK_HEIGHT);
        this.setX(col * (BRICK_WIDTH + MARGIN_BETWEEN_BRICKS) + FIRST_COLUMN);
        this.setY(row * (BRICK_HEIGHT + MARGIN_BETWEEN_BRICKS) + FIRST_ROW);
        this.brickLife = brickLife;
        this.elapsedTime = elapsedTime;
        this.row = row;
        this.col = col;
        hit = false;
    }

    @Override
    public void move() {
        this.setX(this.getX() + speed * directionX * elapsedTime);
    }

    @Override
    public void offBoundary() {
        if(this.getX() < 0 || this.getX() > getScreenWidth() - BRICK_WIDTH) {
            directionX *= -1;
        }
    }

    @Override
    public boolean isAlive() { return !hit; }

    public void brickHit() {
        if(brickLife>0) {
            hit = true;
            brickLife--;
        }
    }

    public int getBrickLife() {return brickLife;}
    public int getRow() {return row;}
    public int getCol() {return col;}
    public boolean isMoving() {return !(speed==0);}

    private double getScreenWidth() {return getParent().getScene().getWidth();}
}
