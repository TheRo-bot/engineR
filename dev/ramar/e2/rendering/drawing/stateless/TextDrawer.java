package dev.ramar.e2.rendering.drawing.stateless;

public abstract class TextDrawer
{

    /*
    This class needs:
     - A FontLoader
       - Some form of "Font" representation
    */

    /*
    This class should function like:
    
    drawText(String, x, y)
    drawChar(char, x, y)
    drawTextBlock(TextBlock, x, y)**
    with modifications:
        withOffset
        withColour
    
    ** a TextBlock is a class which needs to be made to describe
       sections of text that have their own modifications done 
       to draw separately and specially while looking cool and
       nice
    */


}