package utils;

import drawables.Point;
import solids.Solid;
import transforms.Mat4;
import transforms.Point3D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Transformer {

    private BufferedImage img;

    private Mat4 model;
    private Mat4 view;
    private Mat4 projection;

    public Transformer(BufferedImage img) {
        this.img = img;
    }

    //Funkce
    public void drawWireFrame(Solid solid) {

        //vykreslení matice zobrazení
        Mat4 matFinal = model.mul(view).mul(projection);

        //prvni index = první bod ,druhý indeš = druhý bod usečky (podle indexu sousedu)
        for (int i = 0; i < solid.getIndicies().size(); i += 2) {
            Point3D p1 = solid.getVerticies().get(solid.getIndicies().get(i));
            Point3D p2 = solid.getVerticies().get(solid.getIndicies().get(i + 1));

            transformEdge(matFinal, p1, p2);
        }

    }

    private void transformEdge(Mat4 matFinal, Point3D p1, Point3D p2) {
        //todo  1. vynásobit body maticí
        //      2. orez dle 'w' z bodů
        //      3. tvorba vektoru - dehomogenizace (Point3D.dehomog()) (*w)
        //      4. přepočet souřadnic na šířku a výšku okna
        //      5. drawLine() z rendereru
    }

    private void drawPixel(int x, int y, int color) {
        if (x < 0 || x >= 800) return;
        ;
        if (y < 0 || y >= 600) return;
        ;
        img.setRGB(x, y, color);
    }

    public void lineDDA(drawables.Point p1, Point p2, int color) {
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

            drawPixel(Math.round(x), Math.round(y), color);
            x = x + g;
            y = y + h;

        }

    }
}
