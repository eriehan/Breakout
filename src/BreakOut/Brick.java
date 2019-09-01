package BreakOut;

import javafx.scene.image.Image;

public class Brick extends Sprite {
    private boolean alive;
    private double elapsedTime;
    private boolean isMoving;
    private int speed;
    private int directionX;
    private int width;
    private int height;
    private int life;
    public static final double BRICK_SPEED = 2.0;

    public Brick(Image image) {
        super(image);
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
        if(this.getX() < 0 || this.getX() > BreakOutGame.WIDTH - width) {
            directionX *= -1;
        }
    }

    @Override
    public boolean isAlive() {
        return !(life == 0);
    }
}
