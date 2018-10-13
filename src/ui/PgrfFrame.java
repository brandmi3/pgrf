package ui;

import utils.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class PgrfFrame extends JFrame implements MouseMotionListener {

    protected static int FPS = 1000 / 60;

    protected static BufferedImage img;
    static int width = 800;
    static int height = 600;
    protected static JPanel panel;

    public PgrfFrame() throws HeadlessException {
        this.init();
    }

    private void init() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(width, height);
        setTitle("Pocitacova grafika");
        setLocationRelativeTo(null);
        panel = new JPanel();
        add(panel);
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public static int getFPS() {
        return FPS;
    }

    public static void setFPS(int FPS) {
        PgrfFrame.FPS = FPS;
    }

    public static BufferedImage getImg() {
        return img;
    }

    public static void setImg(BufferedImage img) {
        PgrfFrame.img = img;
    }

    public static JPanel getPanel() {
        return panel;
    }

    public static void setPanel(JPanel panel) {
        PgrfFrame.panel = panel;
    }

    public void draw() {};



    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {



    }
}