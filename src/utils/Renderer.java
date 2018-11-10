package utils;

import drawables.Edge;
import drawables.Line;
import drawables.NPolygon;
import drawables.Point;


import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private BufferedImage img;
    private int color;
    private static final int[][] PATTERN = {
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
    };

    public Renderer(BufferedImage img) {
        this.img = img;
        color = Color.BLACK.getRGB();


    }

    private void drawPixel(int x, int y, int color) {
        if (x < 0 || x >= 800) return;
        ;
        if (y < 0 || y >= 600) return;
        ;
        img.setRGB(x, y, color);
    }

    private void drawPixel(int x, int y) {
        drawPixel(x, y, color);
    }

    public void lineTrivial(int x1, int y1, int x2, int y2) {
        // y = kx + q
        int dx = x1 - x2;
        int dy = y1 - y2;

        if (Math.abs(dx) > Math.abs(dy)) {
            // řídící osa x
            // float k ... q
            // drawPixel(x, y)
            if (x1 > x2) {
                int p = x1;
                x1 = x2;
                x2 = p;

                y1 = y2;

            }
            float k = (float) dy / (float) dx;
            for (int x = x1; x < x2; x++) {
                int y = y1 + (int) (k * (x - x1));
                drawPixel(x, y);
            }
        } else {
            // řídící osa y
            if (y2 < y1) {
                int p = x1;
                x1 = x2;

                p = y1;
                y1 = y2;
                y2 = p;

            }
            float k = (float) dx / (float) dy;
            for (int y = y1; y < y2; y++) {
                int x = x1 + (int) (k * (y - y1));
                drawPixel(x, y);
            }
        }
    }

    public void lineDDA(Point p1, Point p2, int color) {
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();
        float k, g, h;
        int dy = y2 - y1;
        int dx = x2 - x1;
        k = dy / (float) dx;

        //určení řídící osy
        if (Math.abs(dx) > Math.abs(dy)) {
            g = 1;
            h = k;
            if (x1 > x2) { // prohození
                int temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
        } else {
            g = 1 / k;
            h = 1;
            if (y1 > y2) {//prohozeni
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
        int max = Math.max(Math.abs(dx), Math.abs(dy));

        for (int i = 0; i <= max; i++) {
            drawPixel(Math.round(x), Math.round(y), color);
            x += g;
            y += h;
        }
    }

    public void drawPolygon(Point center, Point radius, Point distance, int color) {


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
            lineDDA(new Point((int) x0 + center.getX(), (int) y0 + center.getY()), new Point((int) x + center.getX(), (int) y + center.getY()), color);
            x0 = x;
            y0 = y;
        }
    }

    public void seedFill(int x, int y, int oldColor, int newColor) {

        if (oldColor == img.getRGB(x, y)) {


            drawPixel(x, y, newColor);
            if (x < img.getWidth() - 1 && x > 1 && y < img.getHeight() - 1 && y > 1) {
                if (img.getRGB(x + 1, y) != newColor)
                    seedFill(x + 1, y, oldColor, newColor);
                if (img.getRGB(x - 1, y) != newColor)
                    seedFill(x - 1, y, oldColor, newColor);
                if (img.getRGB(x, y + 1) != newColor)
                    seedFill(x, y + 1, oldColor, newColor);
                if (img.getRGB(x, y - 1) != newColor)
                    seedFill(x, y - 1, oldColor, newColor);
            }
        }

    }

    public void seedFillPattern(int x, int y, int borderColor, int firstColor, int secondColor) {

        if (borderColor != img.getRGB(x, y)) {

            int indexX = (Math.abs(x) % 10);
            int indexY = (Math.abs(y) % 10);

            int fillValue = PATTERN[indexY][indexX];

            if (fillValue == 1) {
                drawPixel(x, y, firstColor);
            } else if (fillValue == 0) {
                drawPixel(x, y, secondColor);
            }
            if (x < img.getWidth() - 1 && x > 1 && y < img.getHeight() - 1 && y > 1) {
                if (img.getRGB(x + 1, y) != borderColor && img.getRGB(x + 1, y) != firstColor && img.getRGB(x + 1, y) != secondColor)
                    seedFillPattern(x + 1, y, borderColor, firstColor, secondColor);
                if (img.getRGB(x - 1, y) != borderColor && img.getRGB(x - 1, y) != firstColor && img.getRGB(x - 1, y) != secondColor)
                    seedFillPattern(x - 1, y, borderColor, firstColor, secondColor);
                if (img.getRGB(x, y + 1) != borderColor && img.getRGB(x, y + 1) != firstColor && img.getRGB(x, y + 1) != secondColor)
                    seedFillPattern(x, y + 1, borderColor, firstColor, secondColor);
                if (img.getRGB(x, y - 1) != borderColor && img.getRGB(x, y - 1) != firstColor && img.getRGB(x, y - 1) != secondColor)
                    seedFillPattern(x, y - 1, borderColor, firstColor, secondColor);
            }
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void scanLine(List<Point> points, int borderColor, int fillColor) {


        List<Edge> edges = new ArrayList();

        int prevX = points.get(points.size() - 1).getX();
        int prevY = points.get(points.size() - 1).getY();

        for (int i = 0; i < points.size(); i++) {
            if (prevY != points.get(i).getY()) {
                Edge e = new Edge(new Point(prevX, prevY), new Point(points.get(i).getX(), points.get(i).getY()));
                e.order();
                edges.add(e);
                prevX = points.get(i).getX();
                prevY = points.get(i).getY();
            }
        }

        int minY = points.get(0).getY();
        int maxY = minY;

        for (int i = 0; i < edges.size(); i++) {
            minY = edges.get(i).yMin(minY);
            maxY = edges.get(i).yMax(maxY);
        }

        for (int y = minY; y <= maxY; y++) {

            List<Integer> intersections = new ArrayList<>();

            for (int i = 0; i < edges.size(); i++) {
                Edge e = edges.get(i);

                if (e.isIntersection(y)) {
                    int x = e.findX(y);
                    intersections.add(x);
                }
            }
            Collections.sort(intersections);
            if (intersections.size() >= 2) {
                for (int i = 0; i < intersections.size(); i++) {
                }
                for (int i = 0; i < intersections.size(); i += 2) {
                    lineDDA(new Point(intersections.get(i) + 2, y), new Point(intersections.get(i + 1), y), fillColor);
                }
            }

        }

    }


}
