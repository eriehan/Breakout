package BreakOut;

import javafx.scene.image.Image;

public class Ball extends Sprite {

    private int speed;
    private double vX;
    private double vY;
    private double angle;
    private int directionX;
    private int directionY;
    private double elapsedTime;
    private boolean alive;

    public Ball(Image image, int speed, int angle, double elapsedTime) {
        super(image);
        this.speed = speed;
        this.angle = angle;
        this.elapsedTime = elapsedTime;
        speedsFromAngle();
        alive = true;
    }

    @Override
    public void move() {
        this.setX(this.getX() + vX * directionX * elapsedTime);
        this.setY(this.getY() + vY * directionY * elapsedTime);
    }

    @Override
    public void collide(Sprite other) {
    }

    @Override
    public void offBoundary() {
        if(this.getX() < 0 || this.getX() > BreakOutGame.WIDTH - this.getBoundsInLocal().getWidth()) {
            reverseX();
        }
        else if(this.getY() < 0) {
            reverseY();
        }
        else if(this.getY() > BreakOutGame.HEIGHT) {
            alive = false;
        }
    }

    public void reverseX() {
        setAngle((540 - angle) % 360);
    }
    public void reverseY() {
        setAngle(360 - angle);
    }

    public void setAngle(double angle) {
        this.angle = angle;

        if(angle < 90) {
            directionX = 1; directionY = -1;
        } else if(angle < 180) {
            directionX = -1; directionY = -1;
        } else if(angle < 270) {
            directionX = -1; directionY = 1;
        } else {
            directionX = 1; directionY = 1;
        }
        speedsFromAngle();
    }

    private void speedsFromAngle() {
        vX = Math.abs(speed * Math.cos(angle));
        vY = Math.abs(speed * Math.sin(angle));
    }

    public boolean isAlive() {
        return alive;
    }
}
