package BreakOut;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Sprite extends ImageView {

    public Sprite(Image image) {
        super(image);
    }
    public abstract void move();
    public abstract void collide(Sprite other);
    public abstract void offBoundary();
    public abstract boolean isAlive();

    public void update() {
        move();
        offBoundary();
    }
}