import java.awt.image.BufferedImage;
import java.math.*;

public class Hero extends Obj{
    private double x;
    private double y;
    private int width;
    private int height;
    private BufferedImage image;
    private BufferedImage[] images;
    private int speed;
    private double towardX;
    private double towardY;
    public boolean moving;
    private int image_count;
    private int action_count;
    private double curDir;
    public int hp;
    public static int fullHp=50000;

    public int cd;
    public int qcd;
    public int wcd;
    public int shield;

    public boolean Eon;
    public int ecd;
    public static int fullEcd=800;
    public final static int Erange=600;
    public static int ePower=30;

    public int rcd;



    public Hero(){
        x=Main.MAPWIDTH/2;
        y=Main.MAPHEIGHT/2;
        image=Main.hero_image0;
        images=new BufferedImage[]{Main.hero_image0,Main.hero_image1};
        width=image.getWidth();
        height=image.getHeight();
        speed=3;
        image_count=0;
        action_count=2;
        curDir=1.57;
        moving=false;
        hp=fullHp;
        shield=0;
        cd=0;
        qcd=0;
        wcd=0;
        ecd=0;
        Eon=false;
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
    public int getSpeed(){return speed;}
    public void setSpeed(int x){speed=x;}
    public void setTowardPos(int x,int y) {
        if (!Eon)
        {
            towardX=x;
            towardY=y;
            curDir=Math.atan2(towardY-this.y,towardX-this.x);
            if (x!=this.x||y!=this.y)
                moving=true;
        }
    }
    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public void step(){
        if (moving&&!Eon){
            image=images[image_count++/20%action_count];
            width=image.getWidth();
            height=image.getHeight();
        }
    }
    public void move(){
        if (moving&&!Eon){
            this.x+=speed*Math.cos(curDir);
            this.y+=speed*Math.sin(curDir);

            if (((x-towardX)*(x-towardX)+(y-towardY)*(y-towardY))<4&&moving)
            {
                moving=false;
                image_count=0;
                image=images[0];
            }
        }
        if (Eon){
            this.x+=speed*Math.cos(curDir);
            this.y+=speed*Math.sin(curDir);
            if (((x-towardX)*(x-towardX)+(y-towardY)*(y-towardY))<40)
                endE();
        }

    }
    public void startE(int x,int y){
        setTowardPos(x,y);
        Eon=true;
        speed=10;
        image=Main.heroE;
        width=image.getWidth();
        height=image.getHeight();
        ecd=fullEcd;
    }
    public void endE(){
        Eon=false;
        speed=3;
        image=Main.hero_image0;
        width=image.getWidth();
        height=image.getHeight();
        moving=false;
    }

}
