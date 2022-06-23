package dev.ramar.e2.rendering;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.utils.HiddenList;
import dev.ramar.utils.PairedValues;

import java.util.List;
import java.util.ArrayList;


/*
Drawable: LayerManager
 - draws Drawables in layers!
 - modular! but hardcoded :(
    - could change this one day?
       - but not now.
*/
public class LayerManager implements Drawable
{

    public LayerManager() {}



    public final LocalList bot = new LocalList();
    public final LocalList mid = new LocalList();
    public final LocalList top = new LocalList();
    public final LocalList console = new LocalList();

    public class LocalList
    {

        private List<Drawable> list = new ArrayList<>();

        private List<Drawable> getList()
        {   return this.list;   } 

        private List<PairedValues<Drawable, Boolean>> toProc = new ArrayList<>();




        public void add(Drawable e)
        {
            synchronized(this.toProc)
            {
                this.toProc.add(new PairedValues<Drawable, Boolean>(e, true));
            }
        }


        public void remove(Drawable e)
        {
            synchronized(this.toProc)
            {
                this.toProc.add(new PairedValues<Drawable, Boolean>(e, false));
            }
        }

        private void drawAt(double x, double y, ViewPort vp)
        {
            synchronized(this.toProc)
            {
                for( PairedValues<Drawable, Boolean> proc : this.toProc )
                {
                    if( proc.getV() )
                        this.list.add(proc.getK());
                    else
                        this.list.remove(proc.getK());
                }

                toProc.clear();
            }

            synchronized(this.list)
            {
                for( Drawable d : this.list )
                    d.drawAt(x, y, vp);
            }
        }
    }


    public void drawAt(double x, double y, ViewPort vp)
    {
        bot.drawAt(x, y, vp);
        mid.drawAt(x, y, vp);
        top.drawAt(x, y, vp);
        console.drawAt(0, 0, vp);
    }
}
