import java.io.*;
import javazoom.jl.decoder.JavaLayerException;
import  javazoom.jl.player.*;

public class Music extends Thread {
    Player player;
    File music;
    public Music() {
        music=new File("music.mp3");
    }
    @Override
    public void run() {

        try {
            BufferedInputStream buffer=new BufferedInputStream(new FileInputStream(music));
            player=new Player(buffer);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
