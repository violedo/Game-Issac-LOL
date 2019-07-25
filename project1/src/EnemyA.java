import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.*;
public class EnemyA extends Obj{
    private double x;
    private double y;
    private int width;
    private int height;
    private BufferedImage image;
    private BufferedImage[] images;
    private int speed;
    private double towardX;
    private double towardY;
    private boolean moving;
    private int image_count;
    private int action_count;
    private double curDir;
    public static int power=10;

    public EnemyA(int x,int y,int towardX,int towardY){
        this.x=x;
        this.y=y;
        this.towardX=towardX;
        this.towardY=towardY;
        speed=3;
        moving=true;
        int toX=towardX-x;
        int toY=towardY-y;
        curDir=Math.atan2(toY,toX);
        image=Main.enemyA;
        width=image.getWidth();
        height=image.getHeight();
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
    public int getPower(){return power;}
    public void move(){
        this.x += Math.cos(curDir)* speed;
        this.y += Math.sin(curDir)* speed;
    }
}