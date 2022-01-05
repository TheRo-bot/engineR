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

import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;

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

    public KeyAdapter getAWTKeyAdapter()
    {   return adapter;   }

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

            if( !isThieveryOccurring() )
            {
                Character out = null;

                for( int k : KEYCHARS )
                    if( k == keyCode )
                        out = e.getKeyChar();

                if( out == null )
                    out = Character.toLowerCase((char)e.getKeyCode());
                // say we have a character event
                onKeyIn(out);
            }
            else
                onCharIn(e.getKeyChar());
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

    public void doEngineStuff(EngineR2 er)
    {
        Debug debug = (Debug)er.console.parser.getCommand("debug");

        debug.registerCommand("keys", new Command()
        {
            private boolean isModifierOn = false,
                              isKeyLogOn = false;

            public Object[] run(ConsoleParser cp, Object[] obj)
            {
                // obj = {debug, keys, <x>}
                // x = (modifiers | keylog)

                if( obj.length > 2 )
                {
                    String x = ((String)obj[2]).trim().toLowerCase();
                    if( x.equals("modifiers") )
                    {
                        toggleModifier(er);                        
                    }
                    else if( x.equals("keylog") )
                    {
                        toggleKeyLog(er);
                    }
                }

                return null;
            }

            public String describeCommand()
            {   return "debug stuff in relation to the AWTKeyController";   }

            private Drawable drawable = new Drawable()
            {
                public void drawAt(double xOff, double yOff, ViewPort vp)
                {
                    boolean[] states = new boolean[]
                    {
                        isLShift(), isRShift(), isLAlt(),
                        isRAlt(), isLCntrl(), isRCntrl()
                    };
                    String[] names = new String[]
                    { "lshift", "rshift", "lalt", 
                      "ralt", "lcntrl", "rcntrl" };

                    int drawCount = states.length;

                    int cellW = 40, cellH = 40;

                    int bgw = cellW * drawCount + 20;

                    int bgh = 60;

                    RectMods rm = new RectMods()
                        .withColour(150, 150, 150, 255)
                        .withFill()
                        .withOffset(vp.getLogicalWidth(), 0)
                        .withOffset(-bgw/2 - 28, bgh/2 + 10)
                    ;

                    vp.draw.stateless.rect.withTempMod(rm);

                    int tness = 4;
                    vp.draw.stateless.rect.poslen(-bgw/2, -bgh/2, bgw, bgh);
                    rm.withColour(100, 100, 100, 255);
                    vp.draw.stateless.rect.poslen(-bgw/2 + tness/2, -bgh/2 + tness/2, bgw - tness, bgh - tness);
                    vp.draw.stateless.rect.clearTempMod();


                    for( int ii = 0; ii < states.length; ii++ )
                    {
                        boolean state = states[ii];
                        String name = names[ii];
                        double xOff2 = vp.getLogicalWidth() - 55 - (ii * cellW);
                        int r = state ? 0 : 255, b = state ? 0 : 255;


                        vp.draw.stateless.text.withMod()
                            .withSize(12)
                            .withColour(r, 255, b, 255)
                            .withOffset(xOff2, cellH + 10)
                        ;

                        vp.draw.stateless.text.pos_c(0, 0, name);

                        vp.draw.stateless.rect.withMod()
                            .withOffset(xOff2, cellH)
                            .withColour(r, 255, b, 255)
                        ;
                        vp.draw.stateless.rect.poslen(-cellW/2, -cellH/2, cellW, cellH);
                    }

              

                }
            };

            private void toggleModifier(EngineR2 er)
            {
                isModifierOn = !isModifierOn;

                if( isModifierOn )
                    er.viewport.draw.stateless.perm.add(drawable);
                else
                    er.viewport.draw.stateless.perm.remove(drawable);
            }   

            private void toggleKeyLog(EngineR2 er)
            {

            }


            public ObjectParser getParser() { return null; }
        });
    }
}