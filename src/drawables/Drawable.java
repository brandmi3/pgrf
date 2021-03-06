package drawables;

import utils.Renderer;

public interface Drawable {

    void draw(Renderer renderer);

    void modifyLastPoint(Point p);

}
