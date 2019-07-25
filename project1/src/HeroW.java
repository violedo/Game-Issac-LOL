import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.*;

public class HeroW {
    public static int power=100;
    public int dyingTime;
    private BufferedImage image;
    private int width;
    private int height;
    public final static int fullcd=800;
    public final static int fullDyingTime=300;
    public boolean on;

    public HeroW(){
        dyingTime=300;
        image=Main.hero_W;
        on=false;
        width=image.getWidth();
        height=image.getHeight();
    }
    public BufferedImage getImage(){return image;}
    public static int getPower(){return power;}
    public  int getWidth(){return width;}
    public  int getHeight(){return height;}
}
