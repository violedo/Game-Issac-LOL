import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;

public class Main extends JPanel{
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public static  int MAPWIDTH;
    public static  int MAPHEIGHT;
    public static int translateX;
    public static int translateY;
    private int state = 1;
    private static final int START = 0;
    private static final int RUNNING = 1;
    private static final int PAUSE = 2;
    private static final int GAME_OVER = 3;
    public static int interval = 1000 / 100; // in ms
    private Timer timer;
    private int MouseX;
    private int MouseY;
    private int enemyCount;
    public byte score;
    public int level=1;
    public int newEnemyTime=200;
    public static int bestRecord;

    public static BufferedImage hero_image0;
    public static BufferedImage hero_image1;
    public static BufferedImage hero_arrow;
    public static BufferedImage background;
    public static BufferedImage hero_Q;
    public static BufferedImage hero_QBoom;
    public static BufferedImage enemy0;
    public static BufferedImage cai;
    public static BufferedImage enemyA;
    public static BufferedImage hero_W;
    public static BufferedImage heroE;
    public static BufferedImage heror;
    public static FileReader in=null;
    public static FileWriter out=null;

    private Hero hero=new Hero();
    Set<heroArrow> heroarrows = new HashSet<>();
    Set<HeroQ> heroQs = new HashSet<>();
    HeroW heroW=new HeroW();
    HeroR heroR=new HeroR();

    Set<Enemy> enemies=new HashSet<>();
    Set<EnemyA> enemyAs=new HashSet<>();
    Music music;

    static {
        try {
            background=ImageIO.read(Main.class.getResource("background.jpg"));
            MAPWIDTH=background.getWidth();
            MAPHEIGHT=background.getHeight();
            hero_image0=ImageIO.read(Main.class.getResource("hero0.png"));
            hero_image1=ImageIO.read(Main.class.getResource("hero1.png"));
            hero_arrow=ImageIO.read(Main.class.getResource("hero_arrow4.png"));
            hero_Q=ImageIO.read(Main.class.getResource("hero_Q.png"));
            hero_QBoom=ImageIO.read(Main.class.getResource("heroQBoom.png"));
            enemy0=ImageIO.read(Main.class.getResource("enemy0.png"));
            cai=ImageIO.read(Main.class.getResource("cai.png"));
            enemyA=ImageIO.read(Main.class.getResource("enemyA.png"));
            hero_W=ImageIO.read(Main.class.getResource("W.png"));
            heroE=ImageIO.read(Main.class.getResource("heroE.png"));
            heror=ImageIO.read(Main.class.getResource("heroR.png"));

            in=new FileReader("record.txt");
            bestRecord=in.read();
            in.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game");
        Main game = new Main(); // 面板对象
        frame.add(game); // 将面板添加到JFrame中
        frame.setSize(WIDTH, HEIGHT); // 设置大小
        frame.setAlwaysOnTop(true); // 设置其总在最上
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 默认关闭操作
        // frame.setIconImage(new ImageIcon("images/icon.jpg").getImage()); // 设置窗体的图标
        frame.setLocationRelativeTo(null); // 设置窗体初始位置
        frame.setVisible(true); // 尽快调用paint
        game.enemyCount=0;
        game.score=0;
        game.translateX=-MAPWIDTH/2+WIDTH/2;
        game.translateY=-MAPHEIGHT/2+HEIGHT/2;

        game.action(); // 启动执行
    }
    public void action() {


        music=new Music();
        music.start();
        MouseAdapter l = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) { // 鼠标移动
                if (state == RUNNING) { // 运行状态下移动英雄机--随鼠标位置
                    MouseX=e.getX()-translateX;
                    MouseY=e.getY()-translateY;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) { // 鼠标进入
                if (state == PAUSE) { // 暂停状态下运行
                    state = RUNNING;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) { // 鼠标退出
                if (state == RUNNING) { // 游戏未结束，则设置其为暂停
                    state = PAUSE;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) { // 鼠标点击
                if(e.getButton() == e.BUTTON1){
                    if (hero.cd<=0){
                        for (Enemy h:enemies){
                            if ((MouseX-h.getX())*(MouseX-h.getX())+(MouseY-h.getY())*(MouseY-h.getY())<1600
                                    &&(MouseX-hero.getX())*(MouseX-hero.getX())+(MouseY-hero.getY())*(MouseY-hero.getY())<250000)
                            {
                                heroArrow x=new heroArrow(hero.getX()+hero.getWidth()/2,hero.getY()+hero.getHeight()/2,h);
                                heroarrows.add(x);
                                hero.cd=100;
                                break;
                            }
                        }
                    }
                }

                if(e.getButton() == e.BUTTON3){
                    hero.setTowardPos(MouseX,MouseY);
                }

            }
        };
        this.addMouseListener(l); // 处理鼠标点击操作
        this.addMouseMotionListener(l); // 处理鼠标滑动操作
        this.requestFocus();
        KeyAdapter keyAdapter=new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyReleased(KeyEvent e){
                if (e.getKeyCode()==KeyEvent.VK_Q&&hero.qcd<=0)
                {
                    HeroQ heroQ=new HeroQ(hero.getX(),hero.getY(),MouseX,MouseY);
                    heroQs.add(heroQ);
                    hero.qcd=HeroQ.fullcd;
                }
                else if (e.getKeyCode()==KeyEvent.VK_W&&hero.wcd<=0&&heroW.on==false)
                {
                    hero.wcd=HeroW.fullcd;
                    hero.shield+=HeroW.getPower();
                    heroW.on=true;
                    heroW.dyingTime=HeroW.fullDyingTime;
                }
                else if (e.getKeyCode()==KeyEvent.VK_E&&hero.ecd<=0&&hero.Eon==false)
                {
                    int ToX=MouseX-hero.getX();
                    int ToY=MouseY-hero.getY();
                    if (ToX*ToX+ToY*ToY>Hero.Erange*Hero.Erange)
                    {
                        hero.startE((int)(hero.getX()+Hero.Erange*Math.cos(Math.atan2(ToY,ToX))),(int)(hero.getY()+Hero.Erange*Math.sin(Math.atan2(ToY,ToX))));
                    }
                    else hero.startE(MouseX,MouseY);
                }
                else if (e.getKeyCode()==KeyEvent.VK_R)
                if (e.getKeyCode()==KeyEvent.VK_R&&hero.ecd<=0&&hero.rcd<=0){
                    heroR.start(hero.getX(),hero.getY());
                    hero.rcd=HeroR.fullcd;
                }
                else if (e.getKeyCode()==KeyEvent.VK_S) {
                    hero.moving = false;
                    hero.setImage(hero_image0);
                }
            }

            @Override
            public void keyPressed(KeyEvent e){}
        };
        this.addKeyListener(keyAdapter);

        timer=new Timer();
        timer.schedule(new TimerTask(){
           @Override
           public void run(){
               if (state==RUNNING)
               {
                   produceEnemiesAction();
                   cdAction();
                   AttackAction();
                   move();
                   moveMapAction();
                   step();
                   outOfBoundsAction();
                   dyingAction();
               }
               repaint();
           }
        },interval,interval);

    }

    public void move(){
        hero.move();
        for (Enemy e:enemies){
            e.setTowardPos(hero.getX(),hero.getY());
            e.move();
        }
        for (heroArrow h:heroarrows){
            h.move();
        }
        for (HeroQ h:heroQs){
            h.move();
        }
        for (EnemyA e:enemyAs){
            e.move();
        }
    }
    public void step(){
        hero.step();
    }
    @Override
    public void paint(Graphics g) {
        switch (state){
            case RUNNING:
                g.translate(translateX,translateY);
                g.drawImage(background, 0, 0, null);
                paintHeroR(g);
                paintHero(g);
                paintHeroArrow(g);
                paintHeroQ(g);
                paintHeroW(g);
                paintEnemies(g);
                paintEnemyAs(g);
                paintScore(g);
                paintHeroBoard(g);
                g.translate(-translateX,-translateY);
                break;
            case GAME_OVER:
                g.translate(translateX,translateY);
                g.drawImage(background, 0, 0, null);
                if (score>bestRecord)
                {
                    g.setFont(new Font("TimesRoman",Font.PLAIN,80));
                    g.drawString("新记录什么的，侥幸罢了",200-translateX,100-translateY);
                    try {
                        out=new FileWriter("record.txt");
                        out.write(score);
                        out.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else g.drawImage(cai,400-translateX,20-translateY,null);
                g.translate(-translateX,-translateY);
        }


    }
    public void outOfBoundsAction(){
        List<heroArrow> removeArrows = new ArrayList<>();
        for (heroArrow h:heroarrows){
            if (h.outOfBounds()) {
                removeArrows.add(h);
            }
        }
        heroarrows.removeAll(removeArrows);

        List<HeroQ> removeQs = new ArrayList<>();
        for (HeroQ h:heroQs){
            if (h.outOfBounds()) {
                removeQs.add(h);
            }
        }
        heroQs.removeAll(removeQs);

        List<EnemyA> removeEnemyAs = new ArrayList<>();
        for (EnemyA h:enemyAs){
            if (h.outOfBounds()) {
                removeEnemyAs.add(h);
            }
        }
        enemyAs.removeAll(removeEnemyAs);

    }

    public void dyingAction(){
        heroQDying();
        enemiesDying();
        heroDying();
        heroWDying();
        heroRDying();
    }


    public void produceEnemiesAction(){
        if (enemyCount++%newEnemyTime==0)
        {
            Enemy enemy=new Enemy();
            enemies.add(enemy);
            enemyCount=1;
        }
    }

    public void AttackAction(){
        for (Enemy e:enemies){
            if (--e.cd<=0&&!e.imprisoned){
                EnemyA a=new EnemyA(e.getX(),e.getY(),hero.getX(),hero.getY());
                enemyAs.add(a);
                e.cd=300;
            }
        }
    }
    public void moveMapAction(){
        if (MouseX+translateX<30&&translateX<-10)
        {
            translateX+=10;
            MouseX-=10;
        }
        if (MouseY+translateY<30&&translateY<-10)
        {
            translateY+=10;
            MouseY-=10;
        }
        if (MouseX+translateX>WIDTH-30&&translateX>-MAPWIDTH+10+WIDTH)
        {
            translateX-=10;
            MouseX+=10;
        }
        if (MouseY+translateY>HEIGHT-60&&translateY>-MAPHEIGHT+10+HEIGHT)
        {
            translateY-=10;
            MouseY+=10;
        }
    }
    public void cdAction(){
        if (hero.cd>0)
            --hero.cd;
        if (hero.qcd>0)
            --hero.qcd;
        if (hero.wcd>0)
            --hero.wcd;
        if (hero.ecd>0)
            --hero.ecd;
        if (hero.rcd>0)
            --hero.rcd;
    }


    private void paintHero(Graphics g){
        if (g instanceof Graphics2D)
        {
            ((Graphics2D) g).rotate(hero.getCurDir(),hero.getX(),hero.getY());
            g.drawImage(hero.getImage(),hero.getX()-hero.getWidth()/2,hero.getY()-hero.getHeight()/2,null);
            ((Graphics2D) g).rotate(-hero.getCurDir(),hero.getX(),hero.getY());
        }
        g.setColor(new Color(50,50,50));
        g.fillRect(hero.getX()-hero.getWidth()/2-20,hero.getY()-hero.getHeight(),100,10);
        g.setColor(new Color(0,255,0));
        g.fillRect(hero.getX()-hero.getWidth()/2-20,hero.getY()-hero.getHeight(),hero.hp*100/(Hero.fullHp+hero.shield),10);
        g.setColor(new Color(200,200,200));
        g.fillRect(hero.getX()-hero.getWidth()/2+hero.hp*100/(Hero.fullHp+hero.shield)-20,hero.getY()-hero.getHeight(),hero.shield*100/(Hero.fullHp+hero.shield),10);

    }
    private void paintHeroArrow(Graphics g){
        if (g instanceof Graphics2D)
        {
            for (heroArrow h:heroarrows){
                ((Graphics2D) g).rotate(h.getCurDir(),h.getX(),h.getY());
                g.drawImage(h.getImage(),h.getX()-h.getWidth()/2,h.getY()-h.getHeight()/2,null);
                ((Graphics2D) g).rotate(-h.getCurDir(),h.getX(),h.getY());
            }
        }

    }
    private void paintHeroQ(Graphics g){
        if (g instanceof Graphics2D)
        {
            for (HeroQ h:heroQs){
                ((Graphics2D) g).rotate(h.getCurDir(),h.getX(),h.getY());
                g.drawImage(h.getImage(),h.getX()-h.getWidth()/2,h.getY()-h.getHeight()/2,null);
                ((Graphics2D) g).rotate(-h.getCurDir(),h.getX(),h.getY());
            }
        }

    }
    private void paintHeroW(Graphics g){
        if (heroW.on==true)
        {
            g.drawImage(heroW.getImage(),hero.getX()-heroW.getWidth()/2,hero.getY()-heroW.getHeight()/2,null);
        }
    }
    private void paintEnemies(Graphics g){
        if (g instanceof Graphics2D)
        {
            for (Enemy h:enemies){
                ((Graphics2D) g).rotate(h.getCurDir(),h.getX(),h.getY());
                g.drawImage(h.getImage(),h.getX()-h.getWidth()/2,h.getY()-h.getHeight()/2,null);
                ((Graphics2D) g).rotate(-h.getCurDir(),h.getX(),h.getY());
            }
        }

    }
    private void paintEnemyAs(Graphics g){
        if (g instanceof Graphics2D)
        {
            for (EnemyA h:enemyAs){
                ((Graphics2D) g).rotate(h.getCurDir(),h.getX(),h.getY());
                g.drawImage(h.getImage(),h.getX()-h.getWidth()/2,h.getY()-h.getHeight()/2,null);
                ((Graphics2D) g).rotate(-h.getCurDir(),h.getX(),h.getY());
            }
        }

    }
    private void paintScore(Graphics g){
        g.setFont(new Font("TimesRoman",Font.PLAIN,50));
        g.drawString("score: "+score,950-translateX,50-translateY);
        g.drawString("level:"+level,950-translateX,100-translateY);

        g.drawString("Record:"+bestRecord,450-translateX,100-translateY);
    }
    private void paintHeroBoard(Graphics g){
        g.setColor(new Color(255,255,255));
        g.fillRect(0-translateX,600-translateY,190,200);
        g.fillRect(200-translateX,680-translateY,80,80);
        g.fillRect(300-translateX,680-translateY,80,80);
        g.fillRect(400-translateX,680-translateY,80,80);
        g.fillRect(500-translateX,680-translateY,80,80);

        g.setColor(new Color(0,0,0));
        g.setFont(new Font("TimesRoman",Font.PLAIN,20));
        g.drawString("生命值:"+hero.hp+"/"+Hero.fullHp,20-translateX,640-translateY);
        g.drawString("攻速:"+1.0,20-translateX,670-translateY);
        g.drawString("攻击力:"+heroArrow.getPower(),20-translateX,700-translateY);
        g.drawString("移速:"+hero.getSpeed(),20-translateX,730-translateY);
        g.setFont(new Font("TimesRoman",Font.PLAIN,40));
        g.drawString("Q",220-translateX,710-translateY);
        g.drawString("W",320-translateX,710-translateY);
        g.drawString("E",420-translateX,710-translateY);
        g.drawString("R",520-translateX,710-translateY);
        g.setFont(new Font("TimesRoman",Font.PLAIN,20));
        g.drawString("cd:"+(int)(hero.qcd/100),220-translateX,750-translateY);
        g.drawString("伤害:"+HeroQ.getPower(),210-translateX,725-translateY);
        g.drawString("cd:"+(int)(hero.wcd/100),320-translateX,750-translateY);
        g.drawString("护盾:"+heroW.getPower(),305-translateX,725-translateY);
        g.drawString("cd:"+(int)(hero.ecd/100),420-translateX,750-translateY);
        g.drawString("咸鱼突进",405-translateX,725-translateY);
        g.drawString("cd:"+(int)(hero.rcd/100),520-translateX,750-translateY);
        g.drawString("禁锢",505-translateX,725-translateY);


    }
    private void paintHeroR(Graphics g){
        if (heroR.state!=0){
            g.drawImage(heroR.getImage(),heroR.getX()-heroR.range,heroR.getY()-heroR.range,heroR.range*2,heroR.range*2,null);
        }
    }

    private void heroQDying(){
        List<HeroQ> removeQs = new ArrayList<>();

        for (HeroQ h:heroQs){
            if (!h.isMoving())
            {
                --h.dyingTime;
                if (h.dyingTime==0) {
                    removeQs.add(h);
                }
            }
        }
        heroQs.removeAll(removeQs);
    }
    private void enemiesDying(){
        List<Enemy> removeEnemies=new ArrayList<>();
        List<heroArrow> removeArrows=new ArrayList<>();
        for (Enemy e:enemies){
            for (HeroQ h:heroQs){
                if (collide(e,h))
                {
                    e.hp-=h.getPower();
                    if (h.isMoving()){
                        h.stop();
                    }
                }
            }
            for (heroArrow h:heroarrows){
                if (collide(e,h)){
                    e.hp-=h.getPower();
                    removeArrows.add(h);
                }
            }
            if (collide(e,heroR))
            {
                e.hp-=heroR.getPower();
                e.imprisoned=true;
            }
            else e.imprisoned=false;
            heroarrows.removeAll(removeArrows);
            if (e.hp<=0)
            {
                removeEnemies.add(e);
                ++score;
                if (score==Math.sqrt(level*level*level)*5)
                    levelUp();
            }
        }
        enemies.removeAll(removeEnemies);
    }
    private void heroDying(){
        List<EnemyA> removeEnemyAs=new ArrayList<>();
        for (EnemyA a:enemyAs){
            if (collide(a,hero))
            {
                removeEnemyAs.add(a);
                if (hero.shield>a.getPower())
                    hero.shield-=a.getPower();
                else {
                    hero.hp-=a.getPower()-hero.shield;
                    hero.shield=0;
                }
            }
        }
        enemyAs.removeAll(removeEnemyAs);
        List<Enemy> removeEnemies=new ArrayList<>();
        if (!hero.Eon)
        {
            for (Enemy e:enemies){
                if (collide(e,hero))
                {
                    if (hero.shield>50)
                        hero.shield-=50;
                    else {
                        hero.hp-=50-hero.shield;
                        hero.shield=0;
                    }
                    removeEnemies.add(e);
                }
            }
        }
        else {
            for (Enemy e:enemies){
                if (collide(e,hero))
                {
                    e.hp-=hero.ePower;
                }
                if (e.hp<=0)
                {
                    ++score;
                    removeEnemies.add(e);
                    if (score%10==0)
                        levelUp();
                }
            }
        }
        enemies.removeAll(removeEnemies);
        if (hero.shield==0&&heroW.on==true)
        {
            heroW.dyingTime=0;
            heroW.on=false;
        }
        if (hero.hp<=0)
        {
            state= GAME_OVER;
        }
    }
    private void heroWDying(){
        if (heroW.on)
            --heroW.dyingTime;
        if (heroW.dyingTime<=0)
        {
            heroW.on=false;
            hero.shield=0;
        }
    }
    private void heroRDying(){
        switch (heroR.state){
            case 0:break;
            case 1:
                --heroR.dyingTime1;
                if (heroR.dyingTime1<=0)
                {
                    heroR.state=2;
                    heroR.dyingTime2=45;
                }
                break;
            case 2:
                --heroR.dyingTime2;
                heroR.range+=heroR.speed;
                if (heroR.dyingTime2<=0)
                {
                    heroR.state=3;
                    heroR.dyingTime3=200;
                }
                break;
            case 3:
                --heroR.dyingTime3;
                if (heroR.dyingTime3<=0)
                {
                    heroR.state=0;
                    heroR.range=0;
                }
                break;
        }
    }

    private boolean collide(Obj a,Obj b){
        if (b instanceof HeroR){
            if (((HeroR) b).state==0)
                return false;
            if ((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY())<((HeroR) b).range*((HeroR) b).range)
                return true;
            return false;
        }

        if ((a.getX()+a.getWidth()/2>b.getX()-b.getWidth()/2&&a.getX()-a.getWidth()<b.getX()+b.getWidth()/2)
                &&(a.getY()+a.getHeight()/2>b.getY()-b.getHeight()/2&&a.getY()-a.getHeight()/2<b.getY()+b.getHeight()/2))
            return true;
        else return false;
    }
    private void levelUp(){
        hero.fullHp+=100;
        hero.hp+=100;
        heroArrow.power+=8;
        HeroQ.power+=15;
        Hero.ePower+=8;
        HeroW.power+=30;
        HeroR.power+=20;
        newEnemyTime*=0.9;
        Enemy.fullHp+=16;
        EnemyA.power+=6;
        ++level;
    }

}