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
    private static int FPS = 1000 / 60;

    private BufferedImage img;
    static int width = 800;
    static int height = 600;
    private static JPanel panel;
    private static JPanel descriptionPanel;
    private JLabel description = new JLabel("");
    private JLabel area = new JLabel("");
    private utils.Renderer renderer;
    private int coorX, coorY, seedX, seedY;
    private int clickX;
    private int clickY;
    private boolean firstClick;
    private boolean secondClick;
    private DrawableType type = DrawableType.N_OBJECT;
    private NPolygon nPolygon = new NPolygon();
    private RegularPolygon regularPolygon = new RegularPolygon();
    private List<Line> lines = new ArrayList<>();
    private Line line;
    private Point p1;
    private Point p2;
    private Point distance;
    private int phase = 0;
    private String defaultString = "L- přímka, N- nepravidelný, P- pravidelný || vybral jsi: ";
    private String defaultAreaString = "Obsah (čáry se nesmí překrývat a nesmí býv v zákrytu od středu): ";
    private boolean fillMode;
//todo migrace do drawables

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
        descriptionPanel = new JPanel(new BorderLayout());
        description.setText(defaultString + type.getPopis());
        add(panel, BorderLayout.CENTER);
        descriptionPanel.add(description, BorderLayout.NORTH);
        descriptionPanel.add(area, BorderLayout.SOUTH);
        add(descriptionPanel, BorderLayout.SOUTH);
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        renderer = new Renderer(img);

        panel.addMouseMotionListener(this);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (type == DrawableType.N_OBJECT && !fillMode) {
                    System.out.println("");
                    clickX = e.getX();
                    clickY = e.getY();
                    nPolygon.addPoint(new drawables.Point(clickX, clickY));
                    coorX = clickX;
                    coorY = clickY;
                    firstClick = true;
                }

                if (fillMode) {
                    seedX = e.getX();
                    seedY = e.getY();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (type == DrawableType.LINE) {

                    if (secondClick) {
                        firstClick = false;
                        secondClick = false;
                    }
                    if (!firstClick) {
                        line = new Line();
                        lines.add(line);
                        line.setP1(new Point(e.getX(), e.getY()));
                        line.setP2(new Point(e.getX(), e.getY()));
                        firstClick = true;
                    } else {
                        secondClick = true;

                        line = null;

                    }
                    //line.setP1(p1).setP2(p2);
                }
                if (type == DrawableType.POLYGON) {
                    if (phase == 0) {
                        distance = new Point(0, 0);
                        p2 = new Point(0, 0);
                        phase++;
                    }
                    switch (phase) {
                        case 1:
                            p1 = new Point(e.getX(), e.getY());
                            p2 = new Point(e.getX(), e.getY());
                            phase++;
                            firstClick = true;
                            break;
                        case 2:
                            p2 = new Point(e.getX(), e.getY());
                            phase++;
                            break;
                        case 3:
                            distance = new Point(e.getX(), e.getY());
                            phase = 0;
                            break;

                    }
                }

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
                    area.setText(defaultAreaString);
                    firstClick = false;
                    nPolygon.clear();
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    type = DrawableType.POLYGON;
                    area.setText("");
                    firstClick = false;
                    phase = 0;
                }
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    type = DrawableType.LINE;
                    area.setText("");
                    firstClick = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    fillMode = !fillMode;
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
            if (seedX != 0){
                System.out.println(seedX +" " + seedY);
                renderer.seedFill(coorX, coorY, img.getRGB(coorX, coorY), Color.BLUE.getRGB());

            }
        }

        //dynamické preslení (ukazuje kudy povede cara)
        if (type == DrawableType.N_OBJECT) {
            if (firstClick) {
                renderer.lineDDA(new Point(clickX, clickY), new Point(coorX, coorY));
                renderer.lineDDA(new Point(nPolygon.getPoint(0).getX(), nPolygon.getPoint(0).getY()), new Point(coorX, coorY));
            }
            //kresleni polygonu podle naklikanych pozic
            nPolygon.draw(renderer);
            area.setText(defaultAreaString + nPolygon.calculateArea() + " px2");
            ;
        }
        //Regular
        if (type == DrawableType.POLYGON) {
            if (firstClick) {
                regularPolygon.setCenter(p1);
                regularPolygon.setRadius(p2);
                regularPolygon.setDistance(distance);
                regularPolygon.draw(renderer);
            }
        }
        //Line
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i) != null)
                lines.get(i).draw(renderer);
        }
//        if (type == DrawableType.LINE) {
//            if (firstClick) {
//                if (!secondClick)
//                    p2 = new Point(coorX, coorY);
//                line.draw(renderer);
//            }
//        }
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


        if (type == DrawableType.LINE) {
            if (line != null) {

                line.modifyLastPoint(new Point(e.getX(), e.getY()));
                System.out.println(line.getP2().getX());
            }

        }
        if (type == DrawableType.POLYGON) {
            switch (phase) {
                case 1:
                    p1 = new Point(e.getX(), e.getY());
                    p2 = p1;
                    break;
                case 2:
                    p2 = new Point(e.getX(), e.getY());
                    distance = new Point(e.getX(), e.getY());
                    break;
                case 3:
                    distance = new Point(e.getX(), e.getY());
                    break;

            }
        }
    }
}