package BreakOut;

import javafx.scene.image.Image;

public class Ball extends Sprite {

    private static final int FIRST_QUADRANT = 90;
    private static final int SECOND_QUADRANT = 180;
    private static final int THIRD_QUADRANT = 270;
    private static final int FOURTH_QUADRANT = 360;
    private static final int ONE_AND_HALF_CYCLE = 540;
    public static final double BALL_SPEED_1 = 150;
    public static final double BALL_SPEED_2 = 200;
    public static final double BALL_SPEED_3 = 240;
    public static final double BALL_SPEED_4 = 270;
    public static final double BALL_SPEED_5 = 300;

    private double speed;
    private double vX;
    private double vY;
    private double angle;
    private int directionX;
    private int directionY;
    private double elapsedTime;
    private boolean alive;
    private int level;

    public Ball(Image image, int level, double angle, double elapsedTime) {
        super(image);
        speed = 0;
        setAngle(angle);
        this.elapsedTime = elapsedTime;
        this.level = level;
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
        } else if(this.getY() < 0) {
            reverseY();
        } else if(this.getY() > getScreenHeight()) {
            alive = false;
        }
    }

    public void collide(Paddle other) {
        if(this.getY() >= other.getY() - getRadius() * 2 && this.getY() < other.getY() - getRadius() * 2 + vY * elapsedTime) {
            if (this.getX() > other.getX() - getRadius() &&
                    this.getX() < other.getX() + other.getWidth() - getRadius()) {
                reverseY();
            }
        }
    }

    public void collide(Brick other) {
        if(getY() > other.getY() - getRadius() * 2 && getY() < other.getY() + other.getHeight()) {
            if(getX() > other.getX() - getRadius() && getX() < other.getX() + other.getWidth() - getRadius()) {
                bounceOnBrick(other);
                other.setLife(other.getLife() - 1);
            }
        }
    }

    private void bounceOnBrick(Brick other) {
        double prevX = getX() - vX;
        double prevY = getY() + vY;

        System.out.println(prevX + ", " + prevY + ", " + angle);
        System.out.println(getX() + ", " + getY());

        double relativeAngle = 0.0;

        if(angle <= FIRST_QUADRANT) {
            relativeAngle = Math.toDegrees(Math.atan2(prevY - (other.getY() + other.getHeight() + getRadius()),
                    other.getX() - getRadius() - prevX));
            System.out.println((other.getY() + other.getHeight() + getRadius()) + ", " + (other.getX() - getRadius()));
            System.out.println(relativeAngle);

            if(relativeAngle > angle) {
                reverseY();
            } else { reverseX(); }
        }

        else if(angle <= SECOND_QUADRANT) {
            relativeAngle = Math.toDegrees(Math.atan2(prevY - (other.getY() + other.getHeight() + getRadius()),
                    other.getX() + other.getWidth() + getRadius() - prevX));
            System.out.println((other.getY() + other.getHeight() + getRadius()) + ", " + (other.getX() + other.getWidth() + getRadius()));
            System.out.println(relativeAngle);

            if(relativeAngle < angle && relativeAngle > 0) {
                reverseY();
            } else { reverseX(); }
        }

        else if(angle <= THIRD_QUADRANT) {
            relativeAngle = Math.toDegrees(Math.atan2((other.getY() - getRadius()) - prevY,
                    prevX - (other.getX() + other.getWidth() + getRadius())));
            System.out.println((other.getY() - getRadius()) + ", " + (other.getX() + other.getWidth() + getRadius()));
            System.out.println(relativeAngle);

            if(relativeAngle > angle - SECOND_QUADRANT) {
                reverseY();
            } else { reverseX(); }
        }
        else {
            relativeAngle = Math.toDegrees(Math.atan2((other.getY() - getRadius()) - prevY,
                    prevX - (other.getX() - getRadius())));
            System.out.println((other.getY() - getRadius()) + ", " + (other.getX() - getRadius()));
            System.out.println(relativeAngle);

            if (relativeAngle < angle - SECOND_QUADRANT && relativeAngle > 0) {
                reverseY();
            } else { reverseX(); }
        }
    }

    public void reverseX() {
        setAngle((ONE_AND_HALF_CYCLE - angle) % FOURTH_QUADRANT);
    }
    public void reverseY() {
        setAngle(FOURTH_QUADRANT - angle);
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
        vX = Math.abs(speed * Math.cos(Math.toRadians(angle)));
        vY = Math.abs(speed * Math.sin(Math.toRadians(angle)));
    }

    public boolean isMoving() {return !(speed==0);}

    public boolean isAlive() {
        return alive;
    }

    public double getRadius() {
        return getWidth()*2/5;
    }

    public void startMoving() {
        this.speed = levelSpeed(level);
        speedsFromAngle(angle);
    }

    private double getScreenWidth() { return getParent().getScene().getWidth();}
    private double getScreenHeight() { return getParent().getScene().getHeight();}
}
