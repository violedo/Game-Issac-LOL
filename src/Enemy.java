import java.awt.image.BufferedImage;
import java.math.*;

public class Enemy extends Obj{
    private double x;
    private double y;
    private int width;
    private int height;
    private BufferedImage image;
    private int speed;
    private double towardX;
    private double towardY;
    private boolean moving;
    private int image_count;
    private int action_count;
    private double curDir;
    public boolean imprisoned;
    private int imprisonedTime;
    public static int fullHp=30;
    public int hp=30;
    public int cd;
    public Enemy(){
        image=Main.enemy0;
        width=image.getWidth();
        height=image.getHeight();
        switch ((int)(Math.random()*4)){
            case 0:x=-Main.translateX;y=(Main.HEIGHT-height)*Math.random()-Main.translateY;break;
            case 1:x=Main.WIDTH-Main.translateX;y=(Main.HEIGHT-height)*Math.random()-Main.translateY;break;
            case 2:x=(Main.WIDTH-width)*Math.random()-Main.translateX;y=-Main.translateY;break;
            case 3:x=(Main.WIDTH-width)*Math.random()-Main.translateX;y=Main.HEIGHT-Main.translateY;break;
        }
        hp=fullHp;
        speed=1;
        image_count=0;
        action_count=2;
        curDir=2;
        moving=false;
        imprisoned=false;
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
    public double getCurDir(){return curDir;}
    public void setTowardPos(int x,int y) {
        towardX=x;
        towardY=y;
        curDir=Math.atan2(towardY-this.y,towardX-this.x);
        if (x!=this.x||y!=this.y)
            moving=true;
    }
    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public void step(){}
    public void move(){
        if (moving&&!imprisoned) {
            double toX = (towardX - this.x);
            double toY = (towardY - this.y);
            double distance = Math.sqrt(toX * toX + toY * toY);
            if (distance<8)
                return;
            this.x += (toX / distance * speed);
            this.y += (toY / distance * speed);
        }
    }

}
