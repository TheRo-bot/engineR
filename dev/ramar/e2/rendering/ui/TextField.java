package dev.ramar.e2.rendering.ui;

import dev.ramar.e2.rendering.drawing.stateful.*;
import dev.ramar.e2.rendering.ViewPort;


public class TextField extends Shape
{
    public double x, y;

    private TextShape inputField;
    private TextShape hintText;

    public TextField()
    {
        inputField = buildInputField();
        hintText = buildHintText();
    }


    public TextField(double x, double y)
    {
        this();
        this.x = x;
        this.y = y;

    }

    public TextField withPos(double x, double y)
    {
        this.x = x;
        this.y = y;
        return this;
    }


    protected TextShape buildInputField()
    {
        TextShape exp = new TextShape();

        exp.getMod()
            .withColour(150, 150, 150, 255)
            .withSize(16)
        ;


        return exp;
    }

    protected TextShape buildHintText()
    {
        TextShape ts = new TextShape();

        ts.getMod()
            .withColour(150, 150, 150, 255)
            .withSize(16)
        ;

        return ts;
    }


    public TextField withAlignment(double halign, double valign)
    {
        inputField.getMod().withAlignment(halign, valign);
        hintText.getMod()  .withAlignment(halign, valign);

        return this;
    }




    public TextField withInput(String t)
    {
        inputField.withText(t);
        return this;
    }

    public TextField withHint(String t)
    {
        hintText.withText(t);
        return this;
    }


    public void addInputChar(char c)
    {
        inputField.withText(inputField.getText() + c);
    }



    public void drawAt(double x, double y, ViewPort vp)
    {
        if( inputField.getText() == null || inputField.getText().isEmpty() )
            hintText.drawAt(this.x + x, this.y + y, vp);
        else
            inputField.drawAt(this.x + x, this.y + y, vp);

    }   


}