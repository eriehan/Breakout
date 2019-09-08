package BreakOut;

import javafx.scene.image.Image;

public class Item extends Sprite {

    private static final double ITEM_SPEED = 80.0;
    private static final double ITEM_WIDTH = 20.0;

    private boolean alive;
    private double elapsedTime;
    private char type;

    public Item(Image image, char type, double elapsedTime) {
        super(image);
        setFitHeight(ITEM_WIDTH);
        setFitWidth(ITEM_WIDTH);
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
        if(other instanceof Paddle && other.intersects(getBoundsInLocal())) {
            alive = false;
        }
    }

    @Override
    public void offBoundary() {
        if(this.getCenterY() > BreakOutGame.MAX_Y_POS) {
            alive = false;
        }
    }

    public boolean isAlive() { return alive; }

    public char getType() {return type;}
}
