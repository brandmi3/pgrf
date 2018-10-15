package ui;

import com.sun.deploy.util.BlackList;
import drawables.Drawable;
import drawables.Line;
import utils.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PgrfFrameClickPolygon extends PgrfFrame {

    static PgrfFrameClickPolygon regularPolygon;
    JPanel panel;
    private utils.Renderer renderer;
    private BufferedImage img;
    private int coorX, coorY, seedX, seedY;
    private int clickX;
    private int clickY;
    private int count = 5;
    private List<Point> pointList = new ArrayList<>();
    private boolean firstClick;
    private List<Drawable> drawables;
    public static void main(String[] args) {
        ;
        regularPolygon = new PgrfFrameClickPolygon();
        regularPolygon.init();

    }

    private void init() {

        panel = PgrfFrame.getPanel();
        img = PgrfFrame.getImg();
        drawables = new ArrayList<>();
        panel.addMouseMotionListener(this);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                firstClick = true;
                clickX = e.getX();
                clickY = e.getY();
                pointList.add(new Point(clickX, coorY));
                System.out.println(clickX + " " + clickY + " " + pointList.size());
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

            }

        });


        renderer = new Renderer(img);


        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 100, PgrfFrame.getFPS());
    }

    public void draw() {
        img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight()); /// překreslení sceny bilou barvou

      //  renderer.drawPolygon(clickX, clickY, coorX, coorY, count);


        //kresleni polygonu podle naklikanych pozic
        if (pointList.size() >= 2) {

            int bef_x = pointList.get(0).x;
            int bef_y = pointList.get(0).y;
            for (int i = 1; i < pointList.size(); i++) {
                Point p = pointList.get(i);

                renderer.lineDDA(bef_x, bef_y, p.x, p.y);
                bef_x = pointList.get(i).x;
                bef_y = pointList.get(i).y;
            }
            renderer.lineDDA(bef_x, bef_y, pointList.get(0).x, pointList.get(0).y);
        }
        //dynamické preslení (ukazuje kudy povede cara)
        if (firstClick) {
            renderer.setColor(Color.GREEN.getRGB());
            renderer.lineDDA(clickX, clickY, coorX, coorY);
            renderer.lineDDA(pointList.get(0).x, pointList.get(0).y, coorX, coorY);
            renderer.setColor(Color.RED.getRGB());
        }
        panel.getGraphics().drawImage(img, 0, 0, null);
        getGraphics().setColor(Color.black);
        panel.paintComponents(getGraphics());

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        coorX = e.getX();
        coorY = e.getY();
    }
}