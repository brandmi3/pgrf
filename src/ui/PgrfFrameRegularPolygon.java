package ui;

import utils.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class PgrfFrameRegularPolygon extends PgrfFrame {

    static PgrfFrameRegularPolygon regularPolygon;
    JPanel panel;
    private utils.Renderer renderer;
    private BufferedImage img;
    private int angleX, angleY, radiusX, radiusY;
    private int centerX;
    private int centerY;
    private int count = 3;
    private int phase = 0;


    public static void main(String[] args) {
        regularPolygon = new PgrfFrameRegularPolygon();
        regularPolygon.init();
    }

    private void init() {
        panel = PgrfFrame.getPanel();
        img = PgrfFrame.getImg();
        panel.addMouseMotionListener(this);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (phase == 0) {
                    angleX = 0;
                    angleX = 0;
                    radiusX = 0;
                    radiusY =0;
                    phase++;
                }
                switch (phase) {
                    case 1:
                        centerX = e.getX();
                        centerY = e.getY();
                        radiusX =centerX;
                        radiusY = centerY;
                        phase++;
                        break;
                    case 2:
                        radiusX = e.getX();
                        radiusY = e.getY();
                        phase++;
                        break;
                    case 3:
                        angleX = e.getX();
                        angleY = e.getY();
                        phase = 0;//todo
                        break;

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
        img.getGraphics().setColor(Color.BLACK);
        img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight()); /// překreslení sceny bilou barvou

        renderer.drawPolygon(centerX, centerY, radiusX, radiusY, angleX, angleY);

        panel.getGraphics().drawImage(img, 0, 0, null);
        panel.paintComponents(getGraphics());

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (phase) {
            case 1:
                centerX = e.getX();
                centerY = e.getY();
                break;
            case 2:
                radiusX = e.getX();
                radiusY = e.getY();
                angleX = radiusX;
                angleY = radiusY;
                break;
            case 3:
                angleX = e.getX();
                angleY = e.getY();
                break;

        }
    }
}