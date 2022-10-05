package dev.ramar.e2.core.drawing.mod_helpers;

public class StringHelper<E extends ModHelperOwner>
{
    private E owner;

    public StringHelper(E owner) 
    {
        this.owner = owner;
    }


    public StringHelper(E owner, String s)
    {
        this(owner);
        this.with(s);
    }

    public void set(StringHelper bh)
    {
        this.value = bh.value;
    }

    /*
    Class-Field: colour
     - The colour of this rect!
    */  

    private String value = null;
    public String get()
    {   return this.value;   }


    public E with(String val)
    {
        this.value = val;
        return this.owner;
    }
}