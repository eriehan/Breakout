package BreakOut;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Sprite extends ImageView {

    public Sprite(Image image) {
        super(image);
    }
    public abstract void move();
    public abstract void offBoundary();
    public abstract boolean isAlive();
    public void collide(Sprite other) {}
    public double getCenterX() { return getWidth() / 2 + getX(); }
    public double getCenterY() { return getHeight() / 2 + getY(); }
    public double getWidth() {
        return this.getBoundsInLocal().getWidth();
    }
    public double getHeight() {
        return this.getBoundsInLocal().getHeight();
    }
    public void update() {
        offBoundary();
        move();
    }
}