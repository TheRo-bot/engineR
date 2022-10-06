package dev.ramar.e2.core.drawing.mod_helpers;

public class IntHelper<E extends ModHelperOwner>
{
    private E owner;

    public IntHelper(E owner) 
    {
        this.owner = owner;
    }


    public IntHelper(E owner, int i)
    {
        this(owner);
        this.with(i);
    }

    public void set(IntHelper bh)
    {
        this.value = bh.value;
    }

    /*
    Class-Field: colour
     - The colour of this rect!
    */  

    private int value = 0;
    public int get()
    {   return this.value;   }


    public E with(int val)
    {
        this.value = val;
        return this.owner;
    }

}