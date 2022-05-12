package dev.ramar.e2.structures;

import dev.ramar.utils.HiddenList;

import java.util.List;

public class ObservableVec2 extends Vec2
{


    public ObservableVec2() {
        super();
    }

    public ObservableVec2(double x, double y)
    {
        super(x, y);
    }


    public final Listeners listeners = new Listeners();

    public static class Listeners
    {
        private Listeners() {}
        public final LocalList<XListeners> onX = new LocalList<XListeners>();
        public final LocalList<YListeners> onY = new LocalList<YListeners>();
    }

    public static class LocalList<E> extends HiddenList<E>
    {

        private List<E> getList()
        {   return this.list;   }
    }

    public interface XListeners
    {
        public void onXChange(double x);
    }

    public interface YListeners
    {
        public void onYChange(double y);
    }

    private void onX(double x) 
    {
        for( XListeners xl : this.listeners.onX.getList() )
            xl.onXChange(x);
    }

    private void onY(double y)
    {
        for( YListeners yl : this.listeners.onY.getList() )
            yl.onYChange(y);
    }



    @Override
    public void setX(double x)
    {
        double old = this.x;
        super.setX(x);

        if( old != this.x )
            this.onX(this.x);
    }


    @Override
    public void setY(double y)
    {
        double old =this.y;
        super.setY(y);

        if( old != this.y )
            this.onY(this.y);
    }

}