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
        if(other instanceof Paddle && this.getY() > other.getY() - getRadius() * 2 &&
                this.getY() < other.getY() - getRadius() * 2 + ITEM_SPEED * elapsedTime) {
            if(this.getX() > other.getX() - getRadius() && this.getX() < other.getX() + other.getWidth() - getRadius()) {

            }
        }
    }

    @Override
    public void offBoundary() {
        if(this.getY() > getScreenHeight()) {
            alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public double getRadius() { return getWidth() * 2/5; }

    public char getType() {return type;}

    private double getScreenHeight() {return getParent().getScene().getHeight();}
}
