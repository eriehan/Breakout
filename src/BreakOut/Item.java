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

    //Cannot do alive = !(other instanceof Paddle && other.intersects(getBoundsInLocal))
    //as the item can sometimes disappear when it is not in contact with other sprites (when the item is off the boundary of the screen)
    @Override
    public void collide(Sprite other) {
        if(other instanceof Paddle && other.intersects(getBoundsInLocal())) { alive = false; }
    }

    @Override
    public void offBoundary() { alive = this.getCenterY() <= BreakOutGame.MAX_Y_POS; }

    public boolean isAlive() { return alive; }
    public char getType() {return type;}
}