package BreakOut;

import javafx.scene.image.Image;

public class Paddle extends Sprite {

    private int speed;
    private int directionX;
    private double elapsedTime;
    private int width;


    public Paddle(Image image, double paddleY, int paddleWidth, int speed, double elapsedTime) {
        super(image);
        this.setY(paddleY);
        this.setX(BreakOutGame.WIDTH / 2 - paddleWidth / 2);
        this.speed = speed;
        this.elapsedTime = elapsedTime;
        this.width = paddleWidth;
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
            reverse();
        }
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    private void reverse() {
        directionX *= -1;
    }
}
