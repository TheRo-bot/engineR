
package dev.ramar.e2.rendering.control;
import dev.ramar.e2.structures.Vec2;

import java.util.List;

import dev.ramar.utils.HiddenList;

/*
NOTE:
 x and y should be in coordinate space of the game window
*/
public abstract class MouseController
{   

    public final LocalList<OnMousePress> onPress = new LocalList<>();
    public final LocalList<OnMouseRelease> onRelease = new LocalList<>();

    protected MouseController() {}

    protected final void onPress(int button, double x, double y)
    {
        List<OnMousePress> li = this.onPress.getList();

        for( OnMousePress omp : li )
            omp.mousePressed(button, x, y);
    }

    protected final void onRelease(int button, double x, double y)
    {
        List<OnMouseRelease> li = this.onRelease.getList();

        for( OnMouseRelease omr : li )
            omr.mouseReleased(button, x, y);
    }



    public abstract double getMouseX();
    public abstract double getMouseY();

    public abstract Vec2 getUpdatingVec();

    public interface OnMousePress
    {   public void mousePressed(int bID, double x, double y);   }

    public interface OnMouseRelease
    {   public void mouseReleased(int bID, double x, double y);   }



    public class LocalList<E> extends HiddenList<E>
    {
        private List<E> getList()
        {   return this.list;   }
    }
}