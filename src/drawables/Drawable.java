package drawables;

import utils.Renderer;

import java.awt.*;

public interface Drawable {

    void draw(Renderer renderer);

    void modifyLastPoint(Point p);

    int getColor();

    default int getFillColor() {
        return Color.BLACK.getRGB();
    }
    default  void setColor(int color){}

}
