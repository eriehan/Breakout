package BreakOut;


import javafx.scene.image.Image;

public class Paddle extends Sprite {

    private static final int PADDLE_NORMAL_LENGTH = 80;
    private static final int PADDLE_SHORT_LENGTH = 60;
    private static final int PADDLE_HEIGHT = 15;
    private static final double STRETCH_WIDTH = 1.5;
    private static final double CONTRACT_WIDTH = 2.25;


    private int speed;
    private double elapsedTime;
    private double width;
    private boolean warp = false;
    private boolean normalBounce = false;
    private boolean magnetic = false;
    private int stretched;
    private String itemType = "";

    public Paddle(Image image, int level, double elapsedTime) {
        super(image);
        setWidthByLevel(level);
        setFitWidth(width);
        this.elapsedTime = elapsedTime;
        this.setFitHeight(PADDLE_HEIGHT);
        speed = 0;
    }

    @Override
    public void move() { this.setX(this.getX() + speed * elapsedTime); }

    @Override
    public void collide(Sprite other) {
        if(other instanceof Item) { itemType += " " + ((Item) other).getType(); }
    }

    @Override
    public void offBoundary() {
        double screenWidth = getScreenWidth();
        if(!warp) {
            if (this.getX() < 0) { this.setX(0); }
            else if (this.getX() > screenWidth - width) { this.setX(screenWidth - width); }
        }
        else {
            if(this.getX() < 0 - width) { this.setX(screenWidth); }
            else if(this.getX() > screenWidth) { this.setX(0 - width); }
        }
    }

    @Override
    public boolean isAlive() { return true; }

    public void setWidthByLevel(int level) {
        stretched = 0;
        if(level <= 3) { width = PADDLE_NORMAL_LENGTH;}
        else { width = PADDLE_SHORT_LENGTH;}
        setFitWidth(width);
    }

    public void setSpeed(int speed) { this.speed = speed; }

    public void changeWarp() { this.warp = !warp; }
    public boolean getWarp() {return warp;}

    public void stretchPaddle(boolean cheat) {
        if(stretched <= 1) {
            width *= STRETCH_WIDTH; stretched++;
        } else if(stretched == 2) {
            if(cheat) {contractPaddle();}
        }
        setFitWidth(width);
    }

    public void contractPaddle() {
        if(stretched == 1) { width /= STRETCH_WIDTH; }
        else if(stretched == 2) { width /= CONTRACT_WIDTH;}
        this.setFitWidth(width);
        stretched = 0;
    }

    public void resetType() {itemType = "";}
    public void changeNormalBounce() {normalBounce = !normalBounce;}
    public boolean getNormalBounce() {return normalBounce;}
    public boolean isMagnetic() {return magnetic;}
    public void changeMagnetic(boolean cheat) {
        if(cheat) {magnetic = !magnetic;}
        else {magnetic = true;}
    }

    public String getItemType() {return itemType;}
    private double getScreenWidth() {return getParent().getScene().getWidth();}
}
