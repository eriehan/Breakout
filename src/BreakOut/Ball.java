package BreakOut;

import javafx.scene.image.Image;

public class Ball extends Sprite {

    private static final int FIRST_QUADRANT = 90;
    private static final int SECOND_QUADRANT = 180;
    private static final int THIRD_QUADRANT = 270;
    private static final int FOURTH_QUADRANT = 360;
    private static final int ONE_AND_HALF_CYCLE = 540;
    private static final double BALL_SPEED_1 = 150;
    private static final double BALL_SPEED_2 = 200;
    private static final double BALL_SPEED_3 = 240;
    private static final double BALL_SPEED_4 = 270;
    private static final double BALL_SPEED_5 = 300;
    private static final double MIN_ANGLE = 20.0;
    private static final double MAX_ANGLE = 160.0;
    private static final double FIRST_RATIO = 0.2;
    private static final double SECOND_RATIO = 0.4;
    private static final double THIRD_RATIO = 0.6;
    private static final double FOURTH_RATIO = 0.8;
    private static final double BOUNCING_ANGLE_ADDITION = 10.0;

    private double speed;
    private double vX;
    private double vY;
    private double angle;
    private int directionX;
    private int directionY;
    private double elapsedTime;
    private boolean alive;
    private boolean turnedX = false;
    private boolean turnedY = false;
    private boolean turnedTwice = false;
    private int prevBrickRow;
    private int prevBrickCol;

    public Ball(Image image, double angle, double elapsedTime) {
        super(image);
        speed = 0;
        setAngle(angle);
        this.elapsedTime = elapsedTime;
        alive = true;
    }

    @Override
    public void move() {
        setX(getX() + vX * directionX * elapsedTime);
        setY(getY() + vY * directionY * elapsedTime);
    }

    @Override
    public void collide(Sprite other) {
        if(other instanceof Paddle) {
            collide((Paddle) other);
        }
        else if(other instanceof Brick) {
            collide((Brick) other);
        }
    }

    @Override
    public void offBoundary() {
        if(this.getX() < 0 || getX() > getScreenWidth() - getWidth()) {
            reverseX();
        } else if(this.getY() < BreakOutGame.MIN_Y_POS) {
            reverseY();
        } else  { alive = this.getY() < BreakOutGame.MAX_Y_POS; }
        hitBrickInit();
    }

    private void collide(Paddle other) {
        if(Math.abs(getCenterY() - (other.getY()-getRadius()/2)) <= vY * elapsedTime / 2) {
            if (Math.abs(getCenterX() - other.getCenterX()) <= getRadius() + other.getWidth()/2) {
                reverseY();
                if(!other.getNormalBounce()) {
                    changeAngle(other);
                }
                if(other.isMagnetic()) {
                    speed = 0;
                    vX = 0; vY = 0;
                }
            }
        }
    }

    private void collide(Brick other) {
        if(Math.abs(getCenterY() - other.getCenterY()) <= other.getHeight()/2 + getRadius()) {
            if(Math.abs(getCenterX() - other.getCenterX()) <= getRadius() + other.getWidth()/2) {
                if(turnedTwice) {threeBricksHit();}
                else if(turnedY || turnedX) {twoBricksHit(other);}
                else {bounceOnBrick(other);}

                other.brickHit();
                prevBrickRow = other.getRow();
                prevBrickCol = other.getCol();
            }
        }
    }

    private void bounceOnBrick(Brick other) {
        double prevX = getCenterX() - vX * elapsedTime * directionX;
        double prevY = getCenterY() - vY * elapsedTime * directionY;

        if(Math.abs(prevX - other.getCenterX()) < (other.getWidth()/2 + getRadius())) { reverseY(); }
        else if(Math.abs(prevY - other.getCenterY()) < (other.getHeight()/2 + getRadius())) { reverseX(); }
        else {
            if (angle <= FIRST_QUADRANT) {
                double relativeAngle = slopeBetweenTwoPoints(prevX, other.getX(), other.getY() + other.getHeight(), prevY);
                if (relativeAngle > angle && angle < SECOND_QUADRANT) { reverseY(); }
                else { reverseX(); }
            } else if (angle <= SECOND_QUADRANT) {
                double relativeAngle = slopeBetweenTwoPoints(prevX, other.getX() + other.getWidth(),
                        other.getY() + other.getHeight(), prevY);
                if (relativeAngle < angle && relativeAngle > 0) { reverseY(); }
                else { reverseX(); }
            } else if (angle <= THIRD_QUADRANT) {
                double relativeAngle = slopeBetweenTwoPoints(prevX, other.getX() + other.getWidth(), other.getY(), prevY);
                if (relativeAngle > angle) { reverseY(); }
                else { reverseX(); }
            } else {
                double relativeAngle = slopeBetweenTwoPoints(prevX, other.getX(), other.getY(), prevY);
                if (relativeAngle < angle && relativeAngle > SECOND_QUADRANT) { reverseY(); }
                else { reverseX(); }
            }
        }
    }

    private void twoBricksHit(Brick other) {
        if(prevBrickCol == other.getCol() && turnedY) {
            reverseX();
            reverseY();
        } else if(prevBrickRow == other.getRow() && turnedX) {
            reverseX();
            reverseY();
        } else if(prevBrickRow != other.getRow() && prevBrickCol != other.getCol()) {
            if(turnedY) {reverseX();}
            else if(turnedX) {reverseY();}
        }
        turnedTwice = true;
    }

    private void threeBricksHit() {
        if(!turnedX) {reverseX();}
        else if(!turnedY) {reverseY();}
    }

    public void reverseX() {
        setAngle((ONE_AND_HALF_CYCLE - angle) % FOURTH_QUADRANT);
        turnedX = true;
    }
    public void reverseY() {
        setAngle(FOURTH_QUADRANT - angle);
        turnedY = true;
    }

    public void setAngle(double angle) {
        this.angle = angle;

        if(angle < FIRST_QUADRANT) {
            directionX = 1; directionY = -1;
        } else if(angle < SECOND_QUADRANT) {
            directionX = -1; directionY = -1;
        } else if(angle < THIRD_QUADRANT) {
            directionX = -1; directionY = 1;
        } else {
            directionX = 1; directionY = 1;
        }
        speedsFromAngle(angle);
    }

    private double levelSpeed(int level) {
        if(level==1) {return BALL_SPEED_1;}
        else if(level==2) {return BALL_SPEED_2;}
        else if(level==3) {return BALL_SPEED_3;}
        else if(level==4) {return BALL_SPEED_4;}
        else {return BALL_SPEED_5;}
    }

    private void speedsFromAngle(double angle) {
        vX = Math.abs(speed * Math.cos(angleRadian()));
        vY = Math.abs(speed * Math.sin(angleRadian()));
    }

    public boolean isMoving() {return !(speed==0);}
    public boolean isAlive() { return alive; }
    public double getRadius() { return getWidth()*2/5; }
    public double getAngle() {return angle;}

    public void startMoving(int level) {
        speed = levelSpeed(level);
        speedsFromAngle(angle);
    }

    private double angleRadian() {return Math.toRadians(angle);}
    private double slopeBetweenTwoPoints(double x1, double x2, double y1, double y2) {
        double degree = Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1)));
        if(degree > 0) {return degree;}
        else {return FOURTH_QUADRANT + degree;}
    }

    public void setAlive(boolean alive) {this.alive = alive;}

    public void hitBrickInit() {turnedX = false; turnedY = false; turnedTwice = false;}

    private void changeAngle(Paddle other) {
        double ratio = (getCenterX() - other.getX()) / other.getWidth();

        if(ratio < FIRST_RATIO) {setAngle(angle + 2 * BOUNCING_ANGLE_ADDITION);}
        else if(ratio < SECOND_RATIO) {setAngle(angle + BOUNCING_ANGLE_ADDITION);}
        else if(ratio>THIRD_RATIO && ratio<FOURTH_RATIO) {setAngle(angle - BOUNCING_ANGLE_ADDITION);}
        else if(ratio > FOURTH_RATIO) {setAngle(angle - 2 * BOUNCING_ANGLE_ADDITION);}

        if(angle < MIN_ANGLE) {setAngle(MIN_ANGLE);}
        else if(angle > MAX_ANGLE) {setAngle(MAX_ANGLE);}
    }

    private double getScreenWidth() { return getParent().getScene().getWidth();}
}
