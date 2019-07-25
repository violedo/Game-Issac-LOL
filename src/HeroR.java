import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.*;


public class HeroR extends  Obj{
    private double x;
    private double y;
    private int width;
    private int height;
    private BufferedImage image;
    public int speed=4;
    public int range;
    public int dyingTime1;
    public int dyingTime2;
    public int dyingTime3;
    public int state;

    public static int power =30;
    public final static int fullcd=2000;

    public HeroR(){
        image=Main.heror;
        state=0;
    }
    public int getX() {
        return (int)x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return (int)y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public BufferedImage getImage(){return image;}
    public static int getPower(){return power;}
    public void start(int x,int y){
        this.x=x;
        this.y=y;
        range=40;
        dyingTime1=50;
        dyingTime2=0;
        dyingTime3=0;
        state=1;
    }
    public void move(){}
}
