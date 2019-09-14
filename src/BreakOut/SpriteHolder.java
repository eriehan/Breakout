package BreakOut;

import java.util.ArrayList;

/*This class holds the lists of sprites originally used
in the BreakOutGame.java class.*/
public class SpriteHolder {

    //Holds every single sprites existing
    private ArrayList<Sprite> sprites = new ArrayList<>();
    //Holds sprites that need to be removed in the end of the frame
    private ArrayList<Sprite> spritesToBeRemoved = new ArrayList<>();
    //Holds bricks that have to be added in the next frame
    private ArrayList<Brick> bricksToBeEdited = new ArrayList<>();
    //Holds bricks that can be removed
    private ArrayList<Brick> removableBricks = new ArrayList<>();
    //Holds bricks that had been removed. Needed this because I needed to make an item
    private ArrayList<Brick> removedBricks = new ArrayList<>();
    //Holds all the balls
    private ArrayList<Ball> balls = new ArrayList<>();

    public ArrayList<Sprite> getAllSprites() {
        return sprites;
    }

    public ArrayList<Sprite> getSpritesToBeRemoved() {
        return spritesToBeRemoved;
    }

    public boolean isThereBrickToEdit() {
        return !bricksToBeEdited.isEmpty();
    }

    public ArrayList<Ball> getAllBalls() {
        return balls;
    }

    public ArrayList<Brick> getRemovedBricks() {
        return removedBricks;
    }

    public ArrayList<Brick> getBricksToBeEdited() {
        return bricksToBeEdited;
    }

    public void updateSprites() {
        for (Sprite sprite : sprites) { sprite.update(); }
    }

    public void addSpriteToBeRemoved(Sprite sprite) {
        spritesToBeRemoved.add(sprite);
        if(sprite instanceof Ball) {
            Ball ball = (Ball) sprite;
            balls.remove(ball);
        }
        if (sprite instanceof Brick) {
            Brick brick = (Brick) sprite;
            removableBricks.remove(brick);
            if (brick.getBrickLife() == 0) {
                removedBricks.add(brick);
            }
        }
    }

    public void clearBrickstoBeEdited() {
        bricksToBeEdited.clear();
    }

    public void clearRemovedBricks() {removedBricks.clear();}

    public void addBrickToBeEdited(Brick brick) {
        bricksToBeEdited.add(brick);
    }

    public void addRemovableBrick(Brick brick) {
        removableBricks.add(brick);
    }

    public int getNumBalls() {
        return balls.size();
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
        if(sprite instanceof Ball) {
            Ball ball = (Ball) sprite;
            balls.add(ball);
        }
    }

    public void removeAllSpritesToBeRemoved() {
        sprites.removeAll(spritesToBeRemoved);
        spritesToBeRemoved.clear();
    }

    public boolean isThereBall() { return !balls.isEmpty(); }

    public boolean allBricksRemoved() { return removableBricks.isEmpty(); }

    public void handleCollisions() {
        for (Sprite spriteA : sprites) {
            for (Sprite spriteB : sprites) {
                spriteA.collide(spriteB);
            }
        }
    }

    public void clearBall() {
        spritesToBeRemoved.addAll(balls);
        balls.clear();
    }

    public void clearMap() {
        for (Sprite sprite : sprites) {
            if (!(sprite instanceof Paddle)) { addSpriteToBeRemoved(sprite); }
        }
        bricksToBeEdited.clear();
        removableBricks.clear();
    }

    public void changeBallSpeed(boolean slow, int level) {
        for(Ball ball : balls) {
            if(slow) {ball.setSpeedSlower(level);}
            else {ball.setSpeedFaster(level);}
        }
    }
}
