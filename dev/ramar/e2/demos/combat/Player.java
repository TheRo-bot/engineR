package dev.ramar.e2.demos.combat;


import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.EngineR2;

import dev.ramar.e2.structures.Vec2;


import dev.ramar.e2.rendering.control.KeyListener;
import dev.ramar.e2.rendering.control.KeyCombo;

import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;

import dev.ramar.e2.rendering.console.Command;
import dev.ramar.e2.rendering.console.ConsoleParser;
import dev.ramar.e2.rendering.console.ObjectParser;

import dev.ramar.e2.rendering.console.commands.Debug;


import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

public class Player implements Drawable
{
    // position and velocity
    double x = 0, y = 0, xv = 0, yv = 0;

    protected int r = 255, g = 255, b = 255;

    protected List<EngineR2> ers;

    final int id;

    private static int idCounter = -1;
    static List<Player> allPlayers = new ArrayList<>();


    public Player()
    {
        idCounter++;
        id = idCounter;
        allPlayers.add(this);
    }

    public Player(double x, double y)
    {
        this();
        this.x = x;
        this.y = y;
    } 

    public void setPos(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    // up down left right
    private boolean[] directions = new boolean[]{false, false, false, false};

    protected final KeyCombo    up = new KeyCombo("up").withChar('w');

    protected final KeyCombo  down = new KeyCombo("down").withChar('s');

    protected final KeyCombo  left = new KeyCombo("left").withChar('a');

    protected final KeyCombo right = new KeyCombo("right").withChar('d');


    protected final KeyListener moveListener = new KeyListener()
    {
        public void onPress(KeyCombo kc)
        {
            processAction(kc.getName(), true);
        }

        public void onRelease(KeyCombo kc)
        {
            processAction(kc.getName(), false);
        }

        public void processAction(String name, boolean pressed)
        {
            switch(name)
            {
                case "up":
                    directions[0] = pressed;
                    break;

                case "down":
                    directions[1] = pressed;
                    break;

                case "left":
                    directions[2] = pressed;
                    break;

                case "right":
                    directions[3] = pressed;
                    break;
            }
        }
    };

    public void setdown(List<EngineR2> ers)
    {
        for( EngineR2 er : ers)
        {
            er.viewport.draw.stateless.perm.remove(drawer);

            er.viewport.window.keys.unbindPress(up, moveListener);
            er.viewport.window.keys.  unbindRel(up, moveListener);


            er.viewport.window.keys.unbindPress(down, moveListener);
            er.viewport.window.keys.  unbindRel(down, moveListener);



            er.viewport.window.keys.unbindPress(left, moveListener);
            er.viewport.window.keys.  unbindRel(left, moveListener);


            er.viewport.window.keys.unbindPress(right, moveListener);
            er.viewport.window.keys.  unbindRel(right, moveListener);            
        }

        t.interrupt();
    }

    Thread t = new Thread(() ->
    {
        try
        {
            long lastTime = System.currentTimeMillis();
            while(true)
            {
                Thread.sleep(1);
                long nowTime = System.currentTimeMillis();
                double delta = (nowTime - lastTime) / 1000.0; 
                ((Player)this).update(delta);
                lastTime = nowTime;
            }
        }   
        catch(InterruptedException e)
        {}
    });

    private static Command playerCommand = new Command()
    {
        private Map<String, Command> subCommands = new HashMap<>();

        private void list(ConsoleParser cp)
        {
            String[] lines = new String[subCommands.size()];
            int pointer = 0;
            for( String s : subCommands.keySet() )
            {
                lines[pointer] = "... players <sel> " + s;
                pointer++;
            }

            pointer = 0;

            int maxLen = 0;
            for( int ii = 0; ii < lines.length; ii++ )
                if( lines[ii].length() > maxLen )
                    maxLen = lines[ii].length();

            for( int ii = 0; ii < lines.length; ii++ )
                while(lines[ii].length() < maxLen )
                    lines[ii] += " ";

            for( int ii = 0; ii < lines.length; ii++ )
                for( String s : subCommands.keySet() )
                    if( lines[ii].contains(s) )
                        lines[ii] += " || " + subCommands.get(s).describeCommand();

            for( int ii = 0; ii < lines.length; ii++ )
                cp.ps.println(lines[ii]);

            cp.ps.println("** <sel>: 'all' or <int(1-" + allPlayers.size() + ")> or <int(1-" + allPlayers.size() + ")>:<int(1-" + allPlayers.size() + ")>");

        }



        private void setup()
        {
            subCommands.put("highlight", PlayerCommandHelper.Highlight.command);
            subCommands.put("modify", PlayerCommandHelper.Modify.command);
            subCommands.put("focus", PlayerCommandHelper.Focus.command);
        }

        // debug players 0 <option> <operator> <double>
        // debug players all ||          ||         ||
        // debug players 0 highlight || temporarily highlights that player
        // debug players 0 modify maxSpeed += 10
        private boolean firstRun = true;
        public Object run(ConsoleParser cp, Object[] args)
        {
            if( firstRun )
            {
                setup();
                firstRun = false;
            }

            System.out.println(java.util.Arrays.toString(args));
            if( args.length > 2 && ((String)args[2]).equals("list") )
                list(cp);
            else
                PlayerCommandHelper.run(cp, args, subCommands);
            return null;
        }

        public ObjectParser getParser()
        {
            return null;
        }

        public String describeCommand()
        {
            return "debug in relation to players in demos";
        }
    };



    public void setup(List<EngineR2> ers)
    {
        this.ers = ers;
        for( EngineR2 er : ers )
        {
            Debug d = (Debug)er.console.parser.getCommand("debug");
            try
            {
                d.registerCommand("players", playerCommand);
            }
            catch(IllegalArgumentException e ) {}
        }

        ers.get(0).viewport.window.onClose.add(() ->
        {
            t.interrupt();
        });
        
        t.start();

        for( EngineR2 er : ers )
        {
            er.viewport.window.keys.bindPress(up, moveListener);
            er.viewport.window.keys.  bindRel(up, moveListener);


            er.viewport.window.keys.bindPress(down, moveListener);
            er.viewport.window.keys.  bindRel(down, moveListener);



            er.viewport.window.keys.bindPress(left, moveListener);
            er.viewport.window.keys.  bindRel(left, moveListener);


            er.viewport.window.keys.bindPress(right, moveListener);
            er.viewport.window.keys.  bindRel(right, moveListener);
        }

    }

    private List<EngineR2> trackstances = new ArrayList<>();

    public void startCameraTracking(EngineR2 instance)
    {
        trackstances.add(instance);
    }

    public void stopCameraTracking(EngineR2 instance)
    {
        trackstances.remove(instance);
    }

    private boolean addedDrawers = false;

    private Drawable drawer = null;
    private void processDirection(double delta)
    {
        double angle = 0.0;
        boolean doMove = false;

        double xm = 0.0,
               ym = 0.0;

        if( directions[0] )
            ym -= movement_speed * delta;
        if( directions[1] )
            ym += movement_speed * delta;
        if( directions[2] )
            xm -= movement_speed * delta;
        if( directions[3] )
            xm += movement_speed * delta;


        // we should move movement_speed * delta in the
        // direction we're going.

        // if the distance we're travelling is more than that
        // (caused by pressing a horizontal and a vertical move
        //  key at the same time) we need to do some trig to 
        // fit everything back into place

        double hyp = Math.sqrt(Math.pow(xm, 2) + Math.pow(ym, 2));

        if( hyp > movement_speed * delta )
        {
            // since the hypotenuse isn't +- respective and is just
            // "the distance of the hypotenuse given some x angle"
            double ang = Math.acos(xm / hyp);
            // we need to flip the angle if we're going upward,
            // otherwise up+down == the same
            if( ym < 0 )
                ang *= -1;

            // here's the actual thing that stops movement from
            // exceeding our speed cap
            double dist = Math.min(hyp, movement_speed * delta);

            xm = Math.cos(ang) * dist;
            ym = Math.sin(ang) * dist;
        }


        xv += xm;
        yv += ym;
    }


    private long lastTime = -1;

    public double movement_speed = 1250;
    public double movement_maxSpeed = 150.0;
    public double movement_acceleration = 4;


    public void update(double delta)
    {
        /*
        update concept:
        {
            processAbilities(delta);
             - dive ability (blocks move, shoot ability)
             - shoot ability (whenever, here's where reloading comes in too)
             - move ability (processDirection(delta), all the movement code)
        }
        */
        processDirection(delta);

        if( Math.abs(xv) > 0.0001 || Math.abs(yv) > 0.0001 )
        {
            // once we calculate the hypotenuse we need to
            // ensure it's length is constrained

            // right now we have the 'unrestricted triangle',
            // defined by <xv> and <yv>, it isn't within the radius
            // that maxSpeed defines.

            // <xv> and <yv> define the triangle. calculate the
            // angle and hypotenuse

        
        
            double hyp = Math.sqrt(Math.pow(xv, 2) + Math.pow(yv, 2));

            double ang = Math.acos(xv / hyp);

            if( yv < 0 )
                ang *= -1;

            double dist = Math.min(movement_maxSpeed, hyp);

            // we need to use the unrestricted triangle (UT) to
            // calculate theta and hypotenuse length so we can
            // create the restricted triangle (RT) and extract
            // its adjacent and opposite lengths for a bounded
            // step.

            double xm = Math.cos(ang) * dist,
                   ym = Math.sin(ang) * dist;

            xv = xv > 0 ? Math.min(xm, xv) :
                          Math.max(xm, xv) ;

            yv = yv > 0 ? Math.min(ym, yv) :
                          Math.max(ym, yv) ;  


            xm = xm * delta * movement_acceleration;
            ym = ym * delta * movement_acceleration;

            xv -= xm;
            yv -= ym;

            x += xm;
            y += ym;


        }
        
        for( EngineR2 instance : trackstances )
        {
            instance.viewport.setCenterX(-x);
            instance.viewport.setCenterY(-y);
        }

    }

    private double round(double a)
    {
        return ((int)a * 100) / 100.0;
    }

    private double lastX = 0, lastY = 0;

    public double draw_playerSize = 60;

    public void drawAt(double xo, double yo, ViewPort vp)
    {
        // render

        vp.draw.stateless.rect.withMod()
            .withColour(r, g, b, 255)
            .withFill()
            .withOffset(xo, yo)
            .withOffset(x, y)
        ;

        vp.draw.stateless.rect.poslen(-draw_playerSize/2, -draw_playerSize/2, draw_playerSize, draw_playerSize);

        vp.draw.stateless.line.withMods(4)
            .withColour(0, 255, 255, 255)
            .withOffset(xo + x, yo + y)
        ;

        vp.draw.stateless.line.pospos(0, 0,  xv,yv);
        vp.draw.stateless.line.pospos(0, 0,  xv, 0);
        vp.draw.stateless.line.pospos(xv, 0, xv, yv);

        vp.draw.stateless.text.withMod()
            .withOffset(xo + x, yo + y)
            .withSize(10)
        ;

        double hyp = Math.sqrt(Math.pow(xv, 2) + Math.pow(yv, 2));
        double ang = Math.acos(xv / hyp);

        if( yv < 0 )
            ang *= -1;

        double roundedAng = ((int)(Math.toDegrees(ang) * 100)) / 100.0;
        vp.draw.stateless.text.pos_c(0, 10, "" + roundedAng);


    }
}   