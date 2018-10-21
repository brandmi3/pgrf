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
        color = Color.BLACK.getRGB();

    }

    private void drawPixel(int x, int y) {
        if (x < 0 || x >= 800) return;
        ;
        if (y < 0 || y >= 600) return;
        ;
        img.setRGB(x, y, color);
    }



    public void lineDDA(Point p1, Point p2) {
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();

        int dx, dy;
        float k, g, h; // G= prirustek X, H = prirustek Y;
        dx = x2 - x1;
        dy = y2 - y1;
        k = dy / (float) dx;
        if (Math.abs(dx) > Math.abs(dy)) {
            g = 1; //jdeme po x - prirustek po 1
            h = k;
            if (x2 < x1) { // prohozeni
                x1 = x2;
                y1 = y2;
            }
        } else {
            g = 1 / k;
            h = 1;//jdeme po y - prirustek po 1
            if (y2 < y1) {// prohozeni
                x1 = x2;
                y1 = y2;
            }
        }
        float x = x1;
        float y = y1;

        for (int l = 1; l < Math.max(Math.abs(dx), Math.abs(dy)); l++) {

            drawPixel(Math.round(x), Math.round(y));
            x = x + g;
            y = y + h;
            if (Math.abs(dx) > Math.abs(dy)) {
                setColor(Color.GRAY.getRGB());
                drawPixel(Math.round(x), Math.round(y) - 1);
                drawPixel(Math.round(x), Math.round(y) + 1);
                setColor(Color.LIGHT_GRAY.getRGB());
                drawPixel(Math.round(x), Math.round(y) - 2);
                drawPixel(Math.round(x), Math.round(y) + 2);
                setColor(Color.BLACK.getRGB());
            } else {
                setColor(Color.GRAY.getRGB());
                drawPixel(Math.round(x) - 1, Math.round(y));
                drawPixel(Math.round(x) + 1, Math.round(y));
                setColor(Color.LIGHT_GRAY.getRGB());
                drawPixel(Math.round(x) - 2, Math.round(y));
                drawPixel(Math.round(x) + 2, Math.round(y));
                setColor(Color.BLACK.getRGB());
            }
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
            lineDDA(new Point((int) x0 + center.getX(), (int) y0 + center.getY()), new Point((int) x + center.getX(), (int) y + center.getY()));
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
