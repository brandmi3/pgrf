package ui;

import solids.Solid;
import transforms.Camera;
import utils.Transformer;

import javax.swing.*;
import javax.swing.text.html.HTML;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PgrfWiredFrame extends JFrame {

    private static PgrfWiredFrame wiredFrame;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int FPS = 1000 / 30;

    private static JPanel panel;

    private BufferedImage img;
    private Transformer transformer;
    private List<Solid> solids;
    private Camera camera;

    public static void main(String[] args) {
        wiredFrame = new PgrfWiredFrame();
        wiredFrame.init(WIDTH, HEIGHT);
    }

    private void init(int width, int height) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setTitle("Pocitacova grafika - wired");
        setLocationRelativeTo(null);

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        transformer = new Transformer(img);
        solids = new ArrayList<>();
        camera = new Camera();
        panel = new JPanel();
        add(panel);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });
        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 0, FPS);
    }

    public void draw() {
        img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight());

        for (Solid solid : solids) {
            transformer.drawWireFrame(solid);
        }

        panel.getGraphics().drawImage(img, 0, 0, null);
        panel.paintComponents(getGraphics());

    }


}
