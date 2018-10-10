package ui;

import utils.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class PgrfFrame extends JFrame implements MouseMotionListener {

    private static int FPS = 1000 / 60;

    private BufferedImage img;
    static int width = 800;
    static int height = 600;
    private JPanel panel;
    private utils.Renderer renderer;
    private int coorX, coorY, seedX, seedY;
    private int clickX = 300;
    private int clickY = 300;
    private int count = 5;
    private boolean allowWrite = true;

    public static void main(String... args) {

        PgrfFrame pgrfFrame = new PgrfFrame();
        pgrfFrame.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        pgrfFrame.img.setRGB(0, 0, 500000);
        pgrfFrame.init(width, height);

    }

    private void init(int width, int height) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(width, height);
        setTitle("Pocitacova grafika");
        panel = new JPanel();
        add(panel);

        panel.addMouseMotionListener(this);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                seedX = e.getX();
                seedY = e.getY();
                System.out.println(seedX + "__-_" + seedY);
                renderer.seed(seedX, seedY);
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    count++;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    count--;
                    if (count < 3)
                        count = 3;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    allowWrite = !allowWrite;
                }
            }

         /*   @Override
            public void (KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    count++;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    count--;
                    if (count < 3)
                        count = 3;
                }

                System.out.println("f");
                ;
                super.keyReleased(e);

            }*/
        });

        setLocationRelativeTo(null);

        renderer = new Renderer(img);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 100, FPS);


    }

    private void draw() {
        img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight()); /// překreslení sceny bilou barvou
//        for (int i = 0; i < 100; i++) {
//            img.setRGB(200 + i, 200, Color.RED.getRGB());
//        }
//        renderer.lineTrivial(300, 300, coorX, coorY);
        renderer.lineDDA(clickX, clickY, coorX, coorY);
        renderer.drawPolygon(clickX, clickY, coorX, coorY, count);

        panel.getGraphics().drawImage(img, 0, 0, null);
        panel.paintComponents(getGraphics());

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (allowWrite) {
            coorX = e.getX();
            coorY = e.getY();
        }
    }
}