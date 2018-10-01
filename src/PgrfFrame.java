import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class PgrfFrame extends JFrame {

    private static int FPS =1000/60;

    private BufferedImage img;
    static int width = 800;
    static int height = 600;
    public JPanel panel;

    public static void main(String... args) {

        PgrfFrame pgrfFrame = new PgrfFrame();
        pgrfFrame.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        pgrfFrame.init(width, height);

    }

    private void init(int width, int height) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(width, height);
        setTitle("Pocitacova grafika");
        panel = new JPanel();
        add(panel);
        setLocationRelativeTo(null);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        },100,FPS);


    }

    private void draw() {
        img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight());
        for (int i = 0; i < 100; i++) {
            img.setRGB(200 + i, 200, Color.RED.getRGB());
        }


        panel.getGraphics().drawImage(img, 0, 0, null);
        panel.paintComponents(getGraphics());

    }
}