package BreakOut;

import javafx.scene.image.Image;

public class Item extends Sprite {
    private boolean alive;
    private double elapsedTime;
    public static final double ITEM_SPEED = 2.0;

    public Item(Image image, double elapsedTime) {
        super(image);
        this.elapsedTime = elapsedTime;
        alive = true;
    }

    @Override
    public void move() {
        this.setY(this.getY() + ITEM_SPEED * elapsedTime);
    }

    @Override
    public void collide(Sprite other) {

    }

    @Override
    public void offBoundary() {
        if(this.getY() > BreakOutGame.HEIGHT) {
            alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }
}
