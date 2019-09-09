package BreakOut;

import javafx.scene.image.Image;

public class Bomb extends Sprite {
    private static final double BOMB_SPEED = 200.0;
    private static final double BOMB_IMAGE_HEIGHT = 20.0;

    private boolean alive;
    private double elapsedTime;

    public Bomb(Image image, double elapsedTime, Paddle paddle) {
        super(image);
        setFitHeight(BOMB_IMAGE_HEIGHT);
        setFitWidth(BOMB_IMAGE_HEIGHT);
        this.setX(paddle.getCenterX() - getWidth()/2);
        this.setY(paddle.getY() - getRadius());
        this.elapsedTime = elapsedTime;
        alive = true;
    }

    @Override
    public void move() {
        this.setY(this.getY() - BOMB_SPEED * elapsedTime);
    }

    @Override
    public void collide(Sprite other) {
        if(alive) {
            if (other instanceof Ball) {
                collide((Ball) other);
            } else if (other instanceof Brick) {
                collide((Brick) other);
            }
        }
    }

    @Override
    public void offBoundary() { alive = this.getY() > BreakOutGame.MIN_Y_POS; }

    @Override
    public boolean isAlive() { return alive; }

    private void collide(Ball other) {
        if(Math.abs(getCenterX() - other.getCenterX()) <= other.getRadius() + getRadius()) {
            if(Math.abs(getCenterY() - other.getCenterY()) <= other.getRadius() + getRadius()) {
                other.setAlive(false);
                alive = false;
            }
        }
    }

    private void collide(Brick other) {
        if(Math.abs(getCenterY() - other.getCenterY()) <= other.getHeight()/2 + getRadius()) {
            if (Math.abs(getCenterX() - other.getCenterX()) <= other.getWidth() / 2 + getRadius()) {
                other.brickHit();
                alive = false;
            }
        }
    }

    public double getRadius() {return getHeight()/4;}
}