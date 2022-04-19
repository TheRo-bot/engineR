package dev.ramar.e2.rendering.awt.control;


import dev.ramar.e2.rendering.control.KeyController;

import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import dev.ramar.e2.EngineR2;
import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.console.Console;
import dev.ramar.e2.rendering.console.ConsoleParser;
import dev.ramar.e2.rendering.console.Command;
import dev.ramar.e2.rendering.console.ObjectParser;
import dev.ramar.e2.rendering.console.commands.Debug;

public class AWTKeyController extends KeyController
{
    private static final int SHIFT = 16,
                             CNTRL = 17,
                             ALT   = 18;


    private AWTViewPort vp;


    public AWTKeyController withViewPort(AWTViewPort vp)
    {
        this.vp = vp;
        ((AWTWindow)vp.window).getCanvas().addKeyListener(adapter);
        return this;
    }


    // functionality:
    /*
    - process key inputs (only when focused on window):
      - send key inputs to any active thieves
      - if no thieves, test KeyCombos and send listening triggers
        if they are active
    */


    private KeyAdapter adapter = new KeyAdapter()
    {
        private boolean isLeft(int keyLocation)
        {   return keyLocation == KeyEvent.KEY_LOCATION_LEFT;    }

        private boolean isRight(int keyLocation)
        {   return keyLocation == KeyEvent.KEY_LOCATION_RIGHT;   }


        private void updateMods(KeyEvent e, boolean pressing)
        {
            boolean  isLeft =  isLeft(e.getKeyLocation()),
                    isRight = isRight(e.getKeyLocation());

            /*
            KNOWN BUG: shift "stickiness"
             how to reproduce:
              - hold down both shift keys, the first released shift
                will be "stuck" pressed until individually pressed
                again
             reasoning:
              - for some reason, the KeyAdapter doesn't seem to get
                a keyReleased() invocation when both shifts are pressed
                and one is realeased.
            */

            /*
            KNOWN BUG: unresponsive alt keys
             how to reproduce:
              - spam any alt key, the responsiveness of the key will
                not be as precise as other characters. this can be
                visualised through the console command 'debug keys modifiers'
             reasoning:
              - i'm not entirely sure, but it seems again that the keyRelased()
                event isn't called when it should for some reason. feels like
                it's on some clock based checking weirdness for some reason.
            */
            if( isLeft )
            {
                if( e.getExtendedKeyCode() == SHIFT )
                    setLShift(pressing);
                if( e.getKeyCode() == ALT )
                      setLAlt(pressing);
                if( e.getKeyCode() == CNTRL )
                    setLCntrl(pressing);
            }

            if( isRight )
            {
                if( e.getKeyCode() == SHIFT )
                    setRShift(pressing);
                if( e.getKeyCode() == ALT )
                      setRAlt(pressing);
                if( e.getKeyCode() == CNTRL )
                    setRCntrl(pressing);                
            }
        }


        private final int[] KEYCHARS = new int[]{222, 192};

        @Override
        public void keyPressed(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            // System.out.println("press: " + e.getKeyCode() + " -- " + (char)e.getKeyCode() + " -- " + e.getKeyChar());
            // System.out.println("       " + (Character.toLowerCase((char)e.getKeyCode())) );


            // update KeyController's active modifiers
            updateMods(e, true);

            Character out = null;

            for( int k : KEYCHARS )
                if( k == keyCode )
                    out = e.getKeyChar();

            if( out == null )
                out = Character.toLowerCase((char)e.getKeyCode());
            // say we have a character event
            onKeyIn(out);
        }


        @Override
        public void keyReleased(KeyEvent e)
        {
            int keyCode = e.getKeyCode();

            // update KeyController's active modifiers
            updateMods(e, false);
            Character out = null;

            for( int k : KEYCHARS )
                if( k == keyCode )
                    out = e.getKeyChar();

            if( out == null )
                out = Character.toLowerCase((char)e.getKeyCode());
            // say we have a character event
            onKeyOut(out);
        }
    };



    /* Console Commands
    -====-----------------
    */

    public void doEngineStuff(EngineR2 er) {}
}