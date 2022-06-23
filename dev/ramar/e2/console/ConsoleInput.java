package dev.ramar.e2.console;

import dev.ramar.e2.structures.Emitter;

import java.util.List;


public class ConsoleInput 
{

    public ConsoleInput() {}


    public final Emitter<OnLine> onLine = new LocalEmitter<OnLine>();

    protected void onLine(String string)
    {
        List<OnLine> li = ((LocalEmitter)onLine).getList();
        for( OnLine ol : li )
            ol.onLine(string);
    }



    public interface OnLine
    {   public void onLine(String s);  }

    public class LocalEmitter<E> extends Emitter<E>
    {
        private List<E> getList()  {  return this.list;  }
    }
}