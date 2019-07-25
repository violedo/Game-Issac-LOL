import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.*;
public class heroArrow extends Obj{
    private double x;
    private double y;
    private int width;
    private int height;
    private BufferedImage image;
    private BufferedImage[] images;
    private int speed;
    private boolean moving;
    private double curDir;
    public static int power=15;
    private Enemy target;

    public heroArrow(int x,int y,Enemy target){
        this.x=x;
        this.y=y;
        this.target=target;
        curDir=Math.atan2(target.getY()-y,target.getX()-x);
        speed=6;
        moving=true;
        image=Main.hero_arrow;
        width=image.getWidth();
        height=image.getHeight();
        power=15;
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
    public  static int getPower(){return power;}
    public void move(){
        curDir=Math.atan2(target.getY()-y,target.getX()-x);
        this.x += Math.cos(curDir)* speed;
        this.y += Math.sin(curDir)* speed;
    }
}
