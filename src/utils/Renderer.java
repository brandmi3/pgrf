package utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {
    private BufferedImage img;
    private int color;

    public Renderer(BufferedImage img) {
        this.img = img;
        color = Color.RED.getRGB();
    }

    private void drawPixel(int x, int y) {
        img.setRGB(x, y, color);
    }

    public void lineTrivial(int x1, int y1, int x2, int y2) {
        //y=kx+q;
        int dx = x1 - x2;
        int dy = y1 - y2;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (x2 < x1) {
                int c = x1;
                x1 = x2;
                x2 = c;
                c = y1;
                y1 = y2;
            }
            float k = (float) dy / (float) dx;
            for (int x = x1; x < x2; x++) {
                int y = y1 + (int) (k * (x - x1));
                drawPixel(x, y);
            }
        } else {
            if (y2 < y1) {
                int c = y1;
                y1 = y2;
                y2 = c;
                c = x1;
                x1 = x2;
            }
            float k = (float) dx / (float) dy;
            for (int y = y1; y < y2; y++) {
                int x = x1 + (int) (k * (y - y1));
                drawPixel(x, y);
            }
        }


    }
}
