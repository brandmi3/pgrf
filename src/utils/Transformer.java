package utils;

import drawables.Point;
import solids.Solid;
import transforms.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class Transformer {

    private BufferedImage img;

    private Mat4 model = new Mat4Identity();
    private Mat4 view = new Mat4Identity();
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

            transformEdge(matFinal, p1, p2, solid.getColorByEdge(i / 2));
        }

    }

    private void transformEdge(Mat4 mat, Point3D p1, Point3D p2, int color) {
        //todo  1. vynásobit body maticí
        p1 = p1.mul(mat);
        p2 = p2.mul(mat);

        //      2. orez dle 'w' z bodů
        if (p1.getW() <= 0 && p2.getW() <= 0) return; // není v zorném poli

        //      3. tvorba vektoru - dehomogenizace (Point3D.dehomog()) (*w)
        Optional<Vec3D> o1 = p1.dehomog(); //vydělení W
        Optional<Vec3D> o2 = p2.dehomog(); //vydělení W

        if (!o1.isPresent() || !o2.isPresent())
            return;

        Vec3D v1 = o1.get();
        Vec3D v2 = o2.get();

        //      4. přepočet souřadnic na šířku a výšku okna - původně -1,1 -> 0,1
        v1 = v1.mul(new Vec3D(1, 1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D(
                        0.5 * (img.getWidth() - 1), //0-799
                        0.5 * (img.getHeight() - 1),
                        1));

        v2 = v2.mul(new Vec3D(1, 1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D(
                        0.5 * (img.getWidth() - 1),
                        0.5 * (img.getHeight() - 1),
                        1));
        //      5. drawLine() z rendereru,
        lineDDA(new Point((int) v1.getX(), ((int) v1.getY())), new Point((int) v2.getX(), ((int) v2.getY())), color);

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

    public Mat4 getModel() {
        return model;
    }

    public Transformer setModel(Mat4 model) {
        this.model = model;
        return this;
    }

    public Mat4 getView() {
        return view;
    }

    public Transformer setView(Mat4 view) {
        this.view = view;
        return this;
    }

    public Mat4 getProjection() {
        return projection;
    }

    public Transformer setProjection(Mat4 projection) {
        this.projection = projection;
        return this;
    }
}
