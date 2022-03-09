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

import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;
import dev.ramar.e2.demos.combat.actions.*;
import dev.ramar.e2.demos.combat.actions.ActionManager.Action;
import dev.ramar.e2.demos.combat.player.*;

import dev.ramar.e2.rendering.control.KeyCombo.Directionality;
import dev.ramar.e2.rendering.control.MouseController.MouseListener;

import dev.ramar.e2.demos.combat.player.items.guns.*;


import dev.ramar.utils.PairedValues;

import dev.ramar.utils.nodes.Node;
import dev.ramar.utils.HiddenList;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import java.util.Set;
import java.util.TreeSet;

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

    public final ActionManager actions = new ActionManager();


    public Player()
    {
        idCounter++;
        id = idCounter;

        PlayerCommand.players.add(this);

        this.inventory.setHotBarSlots(new RifleGun(this));
        this.inventory.selectHotBar(0);
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

    public void setX(double x)
    {
        this.x = x;
    }

    public void modX(double x)
    {
        this.x += x;
    }

    public double getX() { return x; }

    public void setY(double y)
    {
        this.y = y;
    }

    public void modY(double y)
    {
        this.y += y;
    }

    public double getY() { return y; }

    public void setXV(double x)
    {
        this.xv = x;
    }

    public void modXV(double x)
    {
        this.xv += x;
    }

    public double getXV() 
    {    return this.xv;   }

    public void setYV(double y)
    {
        this.yv = y;
    }

    public void modYV(double y)
    {
        this.yv += y;
    }

    public double getYV() { return yv; }



    public final VecList vecs = new VecList();

    public interface VecModifier
    {
        public double modify(double input);
    }

    public class VecList extends HiddenList<PairedValues<Vec2, VecModifier>> 
    {
        public Vec2 create()
        {
            return this.create(null);
        }

        public Vec2 create(VecModifier vm)
        {
            Vec2 move = new Vec2();

            this.add(new PairedValues<>(move, vm));
            return move;
        }

        public PairedValues<Vec2, VecModifier> find(VecModifier vm)
        {
            for( PairedValues<Vec2, VecModifier> pv : this.list )
                if( pv.getV().equals(vm) )
                    return pv;

            return null;
        }

        public PairedValues<Vec2, VecModifier> find(Vec2 v2)
        {
            for( PairedValues<Vec2, VecModifier> pv : this.list )
                if( pv.getK().equals(v2) )
                    return pv;

            return null;
        }

        public void remove(VecModifier vm)
        {
            this.remove(this.find(vm));
        }

        public void remove(Vec2 v2)
        {
            this.remove(this.find(v2));
        }

        private List<PairedValues<Vec2, VecModifier>> getList()
        {
            return this.list;
        }
    }

    protected final KeyCombo    up = new KeyCombo("up").withChar('w');

    protected final KeyCombo  down = new KeyCombo("down").withChar('s');

    protected final KeyCombo  left = new KeyCombo("left").withChar('a');

    protected final KeyCombo right = new KeyCombo("right").withChar('d');

    protected final KeyCombo moveToPoint = new KeyCombo("to-point").withChar('b');

    protected final KeyCombo dodge = new KeyCombo("dodge").withTShift(Directionality.LEFT);

    protected final KeyListener moveListener = new KeyListener()
    {
        public void onPress(KeyCombo kc)
        {
            actions.blockedRun(actions.get("movement"), 
                     new Object[]{kc.getName(), true});
        }

        public void onRelease(KeyCombo kc)
        {
            actions.blockedRun(actions.get("movement"),
                     new Object[]{kc.getName(), false});
        }
    };

    protected final KeyListener dodgeListener = new KeyListener()
    {
        boolean acting = false;
        public void onPress(KeyCombo kc)
        {
            if( !acting )
            {
                acting = true;
                actions.blockedRun(actions.get("dodge"));
            }
        }

        public void onRelease(KeyCombo kc)
        {
            if( acting )
            {
                acting = false;
                // ((DodgeAction)actions.get("dodge")).stop();
            }
        }
    };

    private Inventory inventory = new Inventory()
        .withHotBarSlots(4)
    ;

    protected final KeyListener inventoryKeyListener = new KeyListener()
    {

        public void onPress(KeyCombo kc)
        {
            switch(kc.getName())
            {
                case "reload":
                    Item i = inventory.getHeldItem();
                    if( i != null && i instanceof Gun )
                    {
                        // Player.this.actions.blockedRun(g.actions.reload);
                        i.actions.get("reload").act(i.actions);
                    }
                    break;
            }
        }

        public void onRelease(KeyCombo kc)
        {
            
        }

    };

    protected final MouseListener inventoryMouseListener = new MouseListener()
    {

        public void mousePressed(int button, double x, double y)
        {
            if( button == 1 )
            {
                Item i = inventory.getHeldItem();
                if( i != null )
                {
                    Action a = i.actions.get("gun:shoot:start");
                    if( !a.toBlock.contains(Player.this.actions.get("dodge")) )
                        a.toBlock.add(Player.this.actions.get("dodge"));
                    Player.this.actions.blockedRun(a, x, y);
                }

            }
        }

        public void mouseReleased(int button, double x, double y)
        {
            if( button == 1 )
            {
                Item i = inventory.getHeldItem();

                if( i != null )
                {
                    Action a = i.actions.get("gun:shoot:stop");
                    if( a != null )
                        a.act(i.actions, x, y);
                }
            }
        }

    };

    public void setdown(List<EngineR2> ers)
    {
        actionsSetdown(ers);
        for( EngineR2 er : ers)
        {
            Gun.viewports.remove(er.viewport);
            er.viewport.draw.stateless.perm.remove(drawer);

            er.viewport.window.keys.unbindPress(up, moveListener);
            er.viewport.window.keys.  unbindRel(up, moveListener);


            er.viewport.window.keys.unbindPress(down, moveListener);
            er.viewport.window.keys.  unbindRel(down, moveListener);

            er.viewport.window.keys.unbindPress(dodge, dodgeListener);
            er.viewport.window.keys.  unbindRel(dodge, dodgeListener);

            er.viewport.window.keys.unbindPress(left, moveListener);
            er.viewport.window.keys.  unbindRel(left, moveListener);


            er.viewport.window.keys.unbindPress(right, moveListener);
            er.viewport.window.keys.  unbindRel(right, moveListener); 

            er.viewport.window.mouse.onPress.remove(this.inventoryMouseListener);
            er.viewport.window.mouse.onRelease.remove(this.inventoryMouseListener);
        }
    }


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


    private Updatable updateCycle = (double delta) ->
    {
        boolean stop = false;
        // Strategy for: arbitrarily moving the player around the world each update
        //  - accumulate a vector which consists of Vec2 being modified by their VecModifier in <vecs>
        //  

        double thisXV = 0.0,
               thisYV = 0.0;

        for( PairedValues<Vec2, VecModifier> vs : Player.this.vecs.getList() )
        {
            Vec2 v = vs.getK();
            double _x = v.getX() * delta,
                   _y = v.getY() * delta;

            if( vs.getV() != null )
            {
                _x = vs.getV().modify(_x);
                _y = vs.getV().modify(_y);
            }
            v.take(_x, _y);

            thisXV += _x;
            thisYV += _y;
        }

        Player.this.xv = thisXV;
        Player.this.yv = thisYV;

        x += thisXV;
        y += thisYV;


        for( EngineR2 instance : Player.this.trackstances )
        {
            instance.viewport.setCenterX(-x);
            instance.viewport.setCenterY(-y);
        }

        return stop;
    };

    public void setup(List<EngineR2> ers)
    {
        actionsSetup(ers);    
        this.ers = ers;
        for( EngineR2 er : ers )
        {
            Gun.viewports.add(er.viewport);
            Debug d = (Debug)er.console.parser.getCommand("debug");
            try
            {
                d.registerCommand("players", playerCommand);
            }
            catch(IllegalArgumentException e ) {}
        }


        
        if( !DeltaUpdater.getInstance().toUpdate.contains(this.updateCycle) )
            DeltaUpdater.getInstance().toUpdate.add(this.updateCycle);

        for( EngineR2 er : ers )
        {
            er.viewport.window.keys.bindPress(up, moveListener);
            er.viewport.window.keys.  bindRel(up, moveListener);


            er.viewport.window.keys.bindPress(down, moveListener);
            er.viewport.window.keys.  bindRel(down, moveListener);


            er.viewport.window.keys.bindPress(dodge, dodgeListener);
            er.viewport.window.keys.  bindRel(dodge, dodgeListener);

            er.viewport.window.keys.bindPress(left, moveListener);
            er.viewport.window.keys.  bindRel(left, moveListener);


            er.viewport.window.keys.bindPress(right, moveListener);
            er.viewport.window.keys.  bindRel(right, moveListener);

            er.viewport.window.mouse.onPress.add(this.inventoryMouseListener);
            er.viewport.window.mouse.onRelease.add(this.inventoryMouseListener);
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

    public double movement_speed = 1250;
    public double movement_maxSpeed = 150.0;
    public double movement_acceleration = 4;


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

        vp.draw.stateless.line.withTempMod()
            .withColour(0, 255, 255, 255)
            .withOffset(xo + x, yo + y)
        ;


        double lx = 0.0,
               ly = 0.0,
               sumX = 0.0,
               sumY = 0.0;

        Vec2 last = new Vec2(0, 0);
        for( PairedValues<Vec2, VecModifier> vs : vecs.getList() )
        {
            double x = vs.getK().getX();
            double y = vs.getK().getY();
            if( vs.getV() != null )
            {
                vs.getV().modify(x);
                vs.getV().modify(y);
            }

            sumX += x;
            sumY += y;

            vp.draw.stateless.line.pospos(lx, ly, x, y);

            lx = x;
            ly = y;
        }
        vp.draw.stateless.line.activeMod()
            .withColour(255, 0, 0, 255)
        ;
        vp.draw.stateless.line.pospos(0, 0, sumX, sumY);

        vp.draw.stateless.line.clearTempMod();
        // vp.draw.stateless.line.pospos(0, 0,  xv,yv);
        // vp.draw.stateless.line.pospos(0, 0,  xv, 0);
        // vp.draw.stateless.line.pospos(xv, 0, xv, yv);

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

    public void actionsSetdown(List<EngineR2> ers)
    {
        actions.clean();
    }


    public void actionsSetup(List<EngineR2> ers)
    {


        final MovementAction movement = new MovementAction(actions, this); 
        actions.add(movement);

        final WaypointMoveAction wma = new WaypointMoveAction(this);
        wma.toBlock.add(movement);

        actions.add(wma);

        final DodgeAction da = new DodgeAction(this);
        da.toBlock.add(movement);
        da.toBlock.add(da);

        actions.add(da);


        final EngineR2 er1 = ers.get(0);
        er1.viewport.window.keys.bindPress(moveToPoint, (KeyCombo k) ->
        {
            actions.blockedRun(wma, new Object[]{er1.viewport.window.mouse.getMouseX(), er1.viewport.window.mouse.getMouseY()});
        });
    }
}   