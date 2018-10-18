package utils;

import drawables.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Robot;
import java.awt.AWTException;

public class Renderer {
    private BufferedImage img;
    private int color;

    public Renderer(BufferedImage img) {
        this.img = img;
        color = Color.RED.getRGB();
    }

    private void drawPixel(int x, int y) {
        if (x < 0 || x >= 800) return;
        ;
        if (y < 0 || y >= 600) return;
        ;
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

    public void lineDDA(int x1, int y1, int x2, int y2) {
        int dx, dy;
        float k, g, h; // G= prirustek X, H = prirustek Y;
        dx = x2 - x1;
        dy = y2 - y1;
        k = dy / (float) dx;
        if (Math.abs(dx) > Math.abs(dy)) {
            g = 1; //jdeme po x - prirustek po 1
            h = k;
            if (x2 < x1) { // prohozeni
                int temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
        } else {
            g = 1 / k;
            h = 1;//jdeme po y - prirustek po 1
            if (y2 < y1) {// prohozeni
                int temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
        }
        float x = x1;
        float y = y1;

        for (int l = 1; l < Math.max(Math.abs(dx), Math.abs(dy)); l++) {

            drawPixel(Math.round(x), Math.round(y));
            x = x + g;
            y = y + h;
        }

    }


    public void drawPolygon(Point center, Point radius, Point distance) {


        double x0 = radius.getX() - center.getX();
        double y0 = radius.getY() - center.getY();
        double circleRadius = 2 * Math.PI;
        int count = (int) Math.sqrt(Math.pow(Math.abs(distance.getX() - radius.getX()), 2) + Math.pow(Math.abs(distance.getY() - radius.getY()), 2)) / 15; // /15 aby to bylo pomalejsi meneni
        if (count < 3)
            count = 3;
        double step = circleRadius / (double) count;
        for (double i = 0; i < circleRadius; i += step) {
            double x = x0 * Math.cos(step) + y0 * Math.sin(step);
            double y = y0 * Math.cos(step) - x0 * Math.sin(step);
            lineDDA((int) x0 + center.getX(), (int) y0 + center.getY(), (int) x + center.getX(), (int) y + center.getY());
            x0 = x;
            y0 = y;
        }


    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
