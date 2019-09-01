package BreakOut;

import javafx.scene.image.Image;

public class Bomb extends Sprite {
    private boolean alive;
    private double elapsedTime;
    public static final double BOMB_SPEED = 3.0;

    public Bomb(Image image, double elapsedTime) {
        super(image);
        this.elapsedTime = elapsedTime;
        alive = true;
    }

    @Override
    public void move() {
        this.setY(this.getY() - BOMB_SPEED * elapsedTime);
    }

    @Override
    public void collide(Sprite other) {

    }

    @Override
    public void offBoundary() {
        if(this.getY() < 0) {
            alive = false;
        }
    }

    @Override
    public boolean isAlive() {
        return false;
    }
}
