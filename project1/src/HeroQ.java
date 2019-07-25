import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.*;


public class HeroQ extends  Obj{
    private double x;
    private double y;
    private double startX;
    private double startY;
    private int width;
    private int height;
    private BufferedImage image;
    private int speed;
    private double towardX;
    private double towardY;
    private boolean moving;
    private int range;
    private double curDir;
    public int dyingTime;
    public static int power =30;
    public final static int fullcd=500;

    public HeroQ(int x,int y,int towardX,int towardY){
        this.x=x;
        this.y=y;
        startX=x;
        startY=y;
        this.towardX=towardX;
        this.towardY=towardY;
        speed=5;
        range=500;
        moving=true;
        int toX=towardX-x;
        int toY=towardY-y;
        curDir=Math.atan2(toY,toX);
        image=Main.hero_Q;
        width=image.getWidth();
        height=image.getHeight();
        power=30;
    }
    public  double getCurDir(){return curDir;}
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
    public boolean outOfBounds(){
        return x<-width-Main.translateX||x>Main.WIDTH-Main.translateX||y<-height-Main.translateY||y>Main.HEIGHT-Main.translateY;
    }
    public boolean isMoving(){return moving;}
    public static int getPower(){return power;}
    public void stop(){
        moving=false;
        image=Main.hero_QBoom;
        width=image.getWidth();
        height=image.getHeight();
        dyingTime=100;
    }
    public void move(){
        if (moving)
        {
            this.x += Math.cos(curDir)* speed;
            this.y += Math.sin(curDir)* speed;
        }
        if (((towardX-x)*(towardX-x)+(towardY-y)*(towardY-y)<10||(startX-x)*(startX-x)+(startY-y)*(startY-y)>range*range)&&moving)
        {
            stop();
        }
    }
}
