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

    private static PgrfFrame pgrfFrame;
    private static int FPS = 1000 / 30;

    private BufferedImage img;
    static int width = 800;
    static int height = 600;
    private static JPanel panel;
    private static JPanel descriptionPanel;
    private JLabel description = new JLabel("");
    private utils.Renderer renderer;
    private int coorX, coorY, seedX, seedY;
    private int clickX;
    private int clickY;
    private boolean firstClick;
    private boolean secondClick;
    private DrawableType type = DrawableType.N_OBJECT;
    private Drawable drawable;
    private List<Drawable> drawables;

    private String defaultString = "L- přímka, N- nepravidelný, P- pravidelný || vybral jsi: ";
    private boolean fillMode;


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
        drawables = new ArrayList<>();
        descriptionPanel = new JPanel(new BorderLayout());
        description.setText(defaultString + type.getPopis());
        add(panel, BorderLayout.CENTER);
        descriptionPanel.add(description, BorderLayout.NORTH);
        add(descriptionPanel, BorderLayout.SOUTH);
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        renderer = new Renderer(img);

        panel.addMouseMotionListener(this);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (type == DrawableType.N_OBJECT && !fillMode) {
                    if (!firstClick) {
                        drawable = new NPolygon();
                        drawables.add(drawable);
                    }
                    if (!((NPolygon) drawable).isDone() && !fillMode) {
                        clickX = e.getX();
                        clickY = e.getY();
                        ((NPolygon) drawable).addPoint(new drawables.Point(clickX, clickY));
                        //  coorX = clickX; todo dynamicke
                        //  coorY = clickY;
                        firstClick = true;
                    }
                }
                if (type == DrawableType.LINE && !fillMode) {
                    if (secondClick) {
                        firstClick = false;
                        secondClick = false;
                    }
                    if (!firstClick) {
                        drawable = new Line();
                        drawables.add(drawable);
                        ((Line) drawable).setP1(new Point(e.getX(), e.getY()));
                        ((Line) drawable).setP2(new Point(e.getX(), e.getY()));
                        firstClick = true;
                    } else {
                        secondClick = true;
                        drawable = null;
                    }
                    //line.setP1(p1).setP2(p2);
                }
                if (type == DrawableType.POLYGON) {
                    if (!firstClick) {
                        drawable = new RegularPolygon();
                        drawables.add(drawable);
                        ((RegularPolygon) drawable).setCenter(new Point(e.getX(), e.getY()));
                        ((RegularPolygon) drawable).setRadius(new Point(e.getX(), e.getY()));
                        ((RegularPolygon) drawable).setDistance(new Point(e.getX(), e.getY()));
                        firstClick = true;
                    } else if (!secondClick) {
                        ((RegularPolygon) drawable).setRadius(new Point(e.getX(), e.getY()));
                        ((RegularPolygon) drawable).setDistance(new Point(e.getX(), e.getY()));
                        secondClick = true;
                    } else {
                        ((RegularPolygon) drawable).setDistance(new Point(e.getX(), e.getY()));
                        System.out.println(((RegularPolygon) drawable).getCenter().getX() + ":" + ((RegularPolygon) drawable).getCenter().getY() + "  " + ((RegularPolygon) drawable).getDistance().getX() + ":" + ((RegularPolygon) drawable).getDistance().getY());
                        firstClick = false;
                        secondClick = false;
                        drawable = null;
                    }
                }
                if (fillMode) {
                    seedX = e.getX();
                    seedY = e.getY();
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
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
                firstClick = false;
                secondClick = false;
                if (e.getKeyCode() == KeyEvent.VK_N) {
                    type = DrawableType.N_OBJECT;
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    type = DrawableType.POLYGON;
                }
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    type = DrawableType.LINE;
                }
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    fillMode = !fillMode;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    finishPolygon();
                }
                description.setText(defaultString + type.getPopis());
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
        if (!fillMode) {
            img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight()); /// překreslení sceny bilou barvou
        } else {
            if (seedX != 0) {
                renderer.seedFill(coorX, coorY, img.getRGB(coorX, coorY), Color.BLUE.getRGB());
            }
        }
        for (int i = 0; i < drawables.size(); i++) {
            Drawable drawable = drawables.get(i);
            drawable.draw(renderer);
        }
        panel.getGraphics().drawImage(img, 0, 0, null);
        panel.paintComponents(getGraphics());
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (type == DrawableType.N_OBJECT) {
            if (drawable != null) {
                drawable.modifyLastPoint(new Point(e.getX(), e.getY()));
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if (type == DrawableType.LINE) {
            if (drawable != null) {
                drawable.modifyLastPoint(new Point(e.getX(), e.getY()));
            }
        }
        if (type == DrawableType.POLYGON) {
            if (drawable != null) {
                if (firstClick && !secondClick) {
                    ((RegularPolygon) drawable).setRadius(new Point(e.getX(), e.getY()));
                    ((RegularPolygon) drawable).setDistance(new Point(e.getX(), e.getY()));
                } else if(secondClick){
                    ((RegularPolygon) drawable).setDistance(new Point(e.getX(), e.getY()));
                }
            }
        }
    }

    private void finishPolygon() {
        if (drawable != null) {
            if (drawable instanceof NPolygon) {
                ((NPolygon) drawable).setDone(true);
                drawable = null;
                firstClick = false;
                secondClick = false;
            }
        }
    }
}