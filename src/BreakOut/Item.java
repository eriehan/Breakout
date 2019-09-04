package BreakOut;

import javafx.scene.image.Image;

public class Item extends Sprite {
    private boolean alive;
    private double elapsedTime;
    private char type;
    public static final double ITEM_SPEED = 80.0;

    public Item(Image image, char type, double elapsedTime) {
        super(image);
        this.type = type;
        this.elapsedTime = elapsedTime;
        alive = true;
    }

    @Override
    public void move() {
        this.setY(this.getY() + ITEM_SPEED * elapsedTime);
    }

    @Override
    public void collide(Sprite other) {
        alive = other instanceof Paddle && this.intersects(other.getBoundsInLocal());
    }

    @Override
    public void offBoundary() { alive = this.getY() <= BreakOutGame.MAX_Y_POS; }

    public boolean isAlive() {return alive; }

    public char getType() {return type;}

    private double getScreenHeight() {return getParent().getScene().getHeight();}
}
