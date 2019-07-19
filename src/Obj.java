import java.awt.image.BufferedImage;

abstract class Obj {

    private double x;
    private double y;
    private int width;
    private int height;
    private BufferedImage image;
    private int speed;
    private double towardX;
    private double towardY;
    private boolean moving;
    private double curDir;

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
    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public abstract void move();

}
