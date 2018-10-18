package ui;

import drawables.*;
import drawables.Point;
import utils.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PgrfFrame extends JFrame implements MouseMotionListener {
    //todo U - usecka N- n-uhelnik

    private static PgrfFrame pgrfFrame;
    private static int FPS = 1000 / 60;

    private BufferedImage img;
    static int width = 800;
    static int height = 600;
    private static JPanel panel;
    private JLabel popis = new JLabel("");
    private utils.Renderer renderer;
    private int coorX, coorY, seedX, seedY;
    private int clickX;
    private int clickY;
    private int count = 5;
    private List<drawables.Point> points = new ArrayList<>();
    private boolean firstClick;
    private DrawableType type = DrawableType.N_OBJECT;
    private NPolygon nPolygon = new NPolygon();
    private RegularPolygon regularPolygon = new RegularPolygon();
    private Point center;
    private Point radius;
    private Point distance;
    private int phase = 0;


    public static void main(String[] args) {
        pgrfFrame = new PgrfFrame();
        pgrfFrame.init();
    }

    private void init() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(width, height);
        setTitle("Pocitacova grafika");
        setLocationRelativeTo(null);
        panel = new JPanel();
        popis.setText("N- nepravidelný, R- pravidelný || vybral jsi: " + type.getPopis());
        add(panel, BorderLayout.CENTER);
        add(popis, BorderLayout.SOUTH);
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        renderer = new Renderer(img);

        panel.addMouseMotionListener(this);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (type == DrawableType.N_OBJECT) {
                    clickX = e.getX();
                    clickY = e.getY();
                    nPolygon.addPoint(new drawables.Point(clickX, clickY));
                    coorX = clickX;
                    coorY = clickY;
                    firstClick = true;
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (type == DrawableType.POLYGON) {
                    if (phase == 0) {
                        distance = new Point(0, 0);
                        radius = new Point(0, 0);
                        phase++;
                    }
                    switch (phase) {
                        case 1:
                            center = new Point(e.getX(), e.getY());
                            radius = new Point(e.getX(), e.getY());
                            phase++;
                            firstClick = true;
                            break;
                        case 2:
                            radius = new Point(e.getX(), e.getY());
                            phase++;
                            break;
                        case 3:
                            distance = new Point(e.getX(), e.getY());
                            phase = 0;
                            break;

                    }
                }
//                System.out.println(center.getX()+ "_ "+ radius.getX()+ "_" + distance.getX());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                coorX = e.getX();
                coorY = e.getY();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_N) {
                    type = DrawableType.N_OBJECT;
                    firstClick = false;
                    nPolygon.clear();
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    type = DrawableType.POLYGON;
                    firstClick = false;
                }
                popis.setText("N- nepravidelný, P- pravidelný || vybral jsi: " + type.getPopis());
            }
        });

        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 100, FPS);
    }


    public void draw() {
        img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight()); /// překreslení sceny bilou barvou


        //dynamické preslení (ukazuje kudy povede cara)
        if (type == DrawableType.N_OBJECT) {
            if (firstClick) {
                renderer.setColor(Color.GREEN.getRGB());
                renderer.lineDDA(clickX, clickY, coorX, coorY);
                renderer.lineDDA(nPolygon.getPoint(0).getX(), nPolygon.getPoint(0).getY(), coorX, coorY);
                renderer.setColor(Color.RED.getRGB());
            }
            //kresleni polygonu podle naklikanych pozic
            nPolygon.draw(renderer);
        }
        if (type == DrawableType.POLYGON) {
            if (firstClick) {
                regularPolygon.setCenter(center);
                regularPolygon.setRadius(radius);
                regularPolygon.setDistance(distance);
                regularPolygon.draw(renderer);
            }
        }
        panel.getGraphics().drawImage(img, 0, 0, null);
        panel.paintComponents(getGraphics());

    }


    @Override
    public void mouseDragged(MouseEvent e) {
        coorX = e.getX();
        coorY = e.getY();
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        if (type == DrawableType.POLYGON) {
            switch (phase) {
                case 1:
                    center = new Point(e.getX(), e.getY());
                    radius = center;
                    break;
                case 2:
                    radius = new Point(e.getX(), e.getY());
                    distance = new Point(e.getX(), e.getY());
                    break;
                case 3:
                    distance = new Point(e.getX(), e.getY());
                    break;

            }
        }
    }
}