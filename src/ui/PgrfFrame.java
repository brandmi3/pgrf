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
    private ControlPanel controlPanel;


    static int width = 1000;
    static int height = 600;
    private static JPanel panel;
    private utils.Renderer renderer;
    private int seedX, seedY;
    private int clickX;
    private int clickY;
    private int seedColor;
    private boolean firstClick;
    private boolean secondClick;
    private boolean fillWithPattern;
    private Drawable drawable;
    private NPolygon polCutter;
    private NPolygon clippedPol;
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
        //setResizable(false);
        setTitle("Pocitacova grafika");
        setLocationRelativeTo(null);

        panel = new JPanel();

        drawables = new ArrayList<>();
        polCutter = new NPolygon();
        polCutter.getPoints().add(new Point(50, 50));
        polCutter.getPoints().add(new Point(100, 500));
        polCutter.getPoints().add(new Point(300, 250));

        clippedPol = new NPolygon();
        seedColor = Color.BLUE.getRGB();
        drawables.add(polCutter);
        //  drawables.add(clippedPol);
        img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        renderer = new Renderer(img);

        controlPanel = new ControlPanel(pgrfFrame);
        controlPanel.revalidate();
        add(controlPanel, BorderLayout.WEST);
        add(panel, BorderLayout.CENTER);
        revalidate();

        /* *** Listeners *** */
        panel.addMouseMotionListener(this);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                panel.requestFocusInWindow();
                super.mouseReleased(e);
                if (!fillMode) {
                    if (!firstClick) {
                        drawable = new NPolygon();
                        drawables.add(drawable);
                    }
                    if (!((NPolygon) drawable).isDone() && !fillMode) {
                        clickX = e.getX();
                        clickY = e.getY();
                        ((NPolygon) drawable).addPoint(new drawables.Point(clickX, clickY));
                        firstClick = true;
                    }
                }
                if (fillMode) {
                    seedX = e.getX();
                    seedY = e.getY();

                    if (fillWithPattern) {
                        renderer.seedFillPattern(seedX, seedY, Color.RED.getRGB(), Color.GREEN.getRGB(), Color.PINK.getRGB());
                    } else {
                        renderer.seedFill(seedX, seedY, img.getRGB(seedX, seedY), seedColor);
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                System.out.println("2");
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    System.out.println("F");
                    fillMode = !fillMode;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("enter");
                    finishPolygon();
                }
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
            Clipper clipper = new Clipper(polCutter);
            for (int i = 1; i < drawables.size(); i++) {
                if (((NPolygon) drawables.get(i)).getNumberOfPoints() > 2) {
                    System.out.println("POCET: " + ((NPolygon) drawables.get(i)).getNumberOfPoints());
                    NPolygon clippedPol = new NPolygon(clipper.clipPoly((NPolygon) drawables.get(i)));
                    if (clippedPol.getPoints().size() > 2)
                        renderer.scanLine(clippedPol.getPoints(), Color.BLACK.getRGB(), Color.BLACK.getRGB());
                }
            }
        } else {
            if (seedX != 0) {
//                renderer.seedFill(seedX, seedY, img.getRGB(seedX, seedY), seedColor);
                //             renderer.seedFillPattern(seedX, seedY, Color.RED.getRGB());
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
        if (drawable != null & !fillMode) {
            drawable.modifyLastPoint(new Point(e.getX(), e.getY()));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    private void finishPolygon() {
        if (drawable != null) {
            if (drawable instanceof NPolygon) {
                ((NPolygon) drawable).setDone(true);
                drawable = null;
                controlPanel.setDrawables(drawables);
                firstClick = false;
                secondClick = false;
            }
        }
    }

    public boolean isFillMode() {
        return fillMode;
    }

    public PgrfFrame setFillMode(boolean fillMode) {
        this.fillMode = fillMode;
        return this;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public PgrfFrame setRenderer(Renderer renderer) {
        this.renderer = renderer;
        return this;
    }

    public int getSeedColor() {
        return seedColor;
    }

    public PgrfFrame setSeedColor(int seedColor) {
        this.seedColor = seedColor;
        return this;
    }

    public boolean isFillWithPattern() {
        return fillWithPattern;
    }

    public PgrfFrame setFillWithPattern(boolean fillWithPattern) {
        this.fillWithPattern = fillWithPattern;
        return this;
    }
}