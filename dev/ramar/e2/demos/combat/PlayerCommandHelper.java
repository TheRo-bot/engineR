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



class PlayerCommandHelper
{


    static Object run(ConsoleParser cp, Object[] args, Map<String, Command> subCommands)
    {
        if( args.length > 3 )
        {
            List<Player> toProcess = new ArrayList<>();

            // -1 means all
            // 0 means none
            int selection = -1;

            // if it's 'all' then we automatically are fine
            if( ((String)args[2]).equals("all"))
                toProcess.addAll(Player.allPlayers);
            else if( ((String)args[2]).equals("none"))
                selection = 0;
            else
            {
                // attempt if it's one number
                try
                {  
                    selection = Integer.parseInt((String)args[2]);
                    if( selection < 1 || selection > Player.allPlayers.size() )
                        cp.ps.println("'debug players (none|all|<int(1:" + Player.allPlayers.size() + ")>)");
                    else
                        toProcess.add(Player.allPlayers.get(selection-1));
                }
                catch(NumberFormatException e) 
                {
                    // attempt if it's a range selector
                    String[] bits = ((String)args[2]).split(":");
                    if( bits.length == 2 )
                    {
                        int a = -1, b = -1;
                        try
                        {
                            a = Integer.parseInt(bits[0]);
                            try
                            {
                                b = Integer.parseInt(bits[1]);
                            }
                            catch(NumberFormatException f)
                            {  cp.ps.println("Second argument for range selector invalid");  }
                        }
                        catch(NumberFormatException f)
                        {  cp.ps.println("First argument for range selector invalid");  }

                        if( a != -1 && b != -1 )
                        {
                            int from = Math.min(a, b),
                                  to = Math.max(a, b);

                            for( int ii = from; ii <= to; ii++ )
                                toProcess.add(Player.allPlayers.get(ii - 1));
                        }
                    }
                    else
                        cp.ps.println("Invalid selector for 'players' command");

                } 
            }

            Object[] newArgs = new Object[args.length - 3];
            newArgs[0] = toProcess;
            int pointer = 1;
            for( int ii = 4; ii < args.length; ii++ )
            {
                newArgs[pointer] = args[ii];
                pointer++;
            }

            Command c = subCommands.get((String)args[3]);
            if( c != null )
                c.run(cp, newArgs);
            else
                cp.ps.println("unknown argument for 'players'");
        }
        return null;

    }



    public static class Modify
    {
        public static final Command command = new Command()
        {
            /*
            Method: run
             - Allows modification of the selected players through args[0]
             format: 
              debug <selector> modify <classfield> <operator> <double>
                selector: <int> | <int>:<int> | 'all'
                classfield:       'speed' |
                               'maxspeed' |
                           'acceleration' |
            */
            public Object run(ConsoleParser cp, Object[] args)
            {  
                List<Player> selected = (List<Player>)args[0];
                if( args.length > 1 )
                {
                    if( ((String)args[1]).equals("list") )
                    {
                        cp.ps.println("modify arguments:");
                        cp.ps.println("  (movement args)");
                        cp.ps.println("  - 'speed'");
                        cp.ps.println("  - 'maxspeed'");
                        cp.ps.println("  - 'acceleration'");
                        cp.ps.println("  (drawing args)");
                        cp.ps.println("  - 'size");
                        // multiple returns. in a bad way. blasphemy. i know.
                        return null;
                    }
                }

                if( args.length > 3 )
                {
                    String classField = (String)args[1];

                    Modifier mod = null;
                    Double am = null;
                    try
                    {
                        mod = Modifier.parse((String)args[2]);
                        am = Double.parseDouble((String)args[3]);
                    }
                    catch(NumberFormatException e)
                    {  cp.ps.println("a valid double must be used to modify this field"); }
                    catch(IllegalArgumentException e)
                    {  cp.ps.println("valid modifiers: '=', '+=', '-=', '/=', '*='"); }

                    if( mod != null && am != null )
                    {

                        List<String> options = parseOptions(classField);

                        if( options.size() > 1 )
                            cp.ps.println("ambiguous short-hand. conflicting: " + options);
                        else if( options.size() == 1 )
                        {

                            switch(classField)
                            {
                                case "x":
                                    for( Player p : selected )
                                        p.x = mod.process(p.x, am);
                                    break;
                                case "y":
                                    for( Player p : selected )
                                        p.y = mod.process(p.y, am);
                                    break;
                                case "speed":
                                    for( Player p : selected )
                                        p.movement_speed = mod.process(p.movement_speed, am);
                                    break;
                                case "maxspeed":
                                    for( Player p : selected )
                                        p.movement_maxSpeed = mod.process(p.movement_maxSpeed, am);
                                    break;
                                case "acceleration":
                                    for( Player p : selected )
                                        p.movement_acceleration = mod.process(p.movement_acceleration, am);
                                    break;
                                case "size":
                                    for( Player p : selected )
                                        p.draw_playerSize = mod.process(p.draw_playerSize, am);
                            }
                        }
                        else
                            cp.ps.println("invalid class-field argument for 'modify'");
                    }
                }
                else if( args.length > 1)
                {
                    List<String> options = parseOptions((String)args[1]);
                    if( options.isEmpty() )
                        cp.ps.println("invalid class-field argument for 'modify'");
                    else if( options.size() > 1 )
                        cp.ps.println("ambiguous short-hand. conflicting: " + options);
                    else
                    {
                        switch((String)args[1])
                        {
                            case "list":
                                break;
                            case "x":
                                for( Player p : selected )
                                    cp.ps.println(p.id + ".x = " + p.x);
                                break;
                            case "y":
                                for( Player p : selected )
                                    cp.ps.println(p.id + ".y = " + p.y);
                                break;
                            case "speed":
                                for( Player p : selected )
                                    cp.ps.println(p.id + ".speed = " + p.movement_speed);
                                break;
                            case "maxspeed":
                                for( Player p : selected )
                                    cp.ps.println(p.id + ".maxspeed = " + p.movement_maxSpeed);
                                break;
                            case "acceleration":
                                for( Player p : selected )
                                    cp.ps.println(p.id + ".acceleration = " + p.movement_acceleration);
                                break;
                            case "size":
                                for( Player p : selected )
                                    cp.ps.println(p.id + ".size = " + p.draw_playerSize);
                        }
                    }

                }
                return null;
            }

            private List<String> parseOptions(String classField)
            {
                List<String> options = new ArrayList<>();
                options.add("x");
                options.add("y");
                options.add("list");
                options.add("speed");
                options.add("maxspeed");
                options.add("acceleration");
                options.add("size");

                for( String option : options )
                    if( classField.equals(option) )
                    {
                        ArrayList<String> out = new ArrayList<>();
                        out.add(option);
                        return out;
                    }
                for( int ii = 0; ii < classField.length(); ii++ )
                {
                    char cFieldChar = classField.charAt(ii);
                    for( int jj = 0; jj < options.size(); jj++ )
                    {
                        String option = options.get(jj);
                        if( option.length() <= ii || option.charAt(ii) != cFieldChar )
                            options.remove(option);
                    }
                }
                return options;
            }

            public ObjectParser getParser()
            {  return null;  }

            public String describeCommand()
            {  return "Permits modification of changeable player class-fields";  }


            private enum Modifier
            {
                ASSIGN
                {
                    public double process(double in, double mod)
                    {  return mod;  }
                },
                ADD
                {
                    public double process(double in, double mod)
                    {  return in + mod;  }
                },
                SUB
                {
                    public double process(double in, double mod)
                    {  return in - mod;  }
                }, 
                DIV
                {
                    public double process(double in, double mod)
                    {  return in / mod;  }
                },
                MUL
                {
                    public double process(double in, double mod)
                    {  return in * mod;  }
                };

                public abstract double process(double in, double mod);

                public static Modifier parse(String s)
                {
                    switch(s)
                    {
                        case "=":
                            return Modifier.ASSIGN;
                        case "+=":
                            return Modifier.ADD;
                        case "-=":
                            return Modifier.SUB;
                        case "/=":
                            return Modifier.DIV;
                        case "*=":
                            return Modifier.MUL;
                        default: 
                            throw new IllegalArgumentException("Invalid modifier");
                    }
                }
            }


        };

    }


    /*
    Command: Focus
     - allows the switching of focus to a specific Player
    */
    public static class Focus
    {
        public static final Command command = new Command()
        {
            public Object run(ConsoleParser cp, Object[] args)
            {
                List<Player> players = (List<Player>)args[0];

                if( players.size() > 1 )
                    cp.ps.println("only one player must be selected for focus");
                else
                {
                    for( Player p : Player.allPlayers )
                        p.stopCameraTracking(cp.console.engine);

                    if( !players.isEmpty() )
                        players.get(0).startCameraTracking(cp.console.engine);
                }

                return null;
            }


            public ObjectParser getParser() { return null; }

            public String describeCommand()
            {  return "focuses the viewport to one Player"; }
        };
    }


    public static class Highlight
    {
        public static final Command command = new Command()
        {


            /*
            Method: run
             - Highlight's run command
             - highlights the selected Players stored in args[0]
            */
            public Object run(ConsoleParser cp, Object[] args)
            {
                List<Player> players = (List<Player>)args[0];

                for( Player p : players )
                {
                    cp.console.engine.viewport.draw.stateless.perm.add(new Drawable()
                    {
                        long lastTime = -1;
                        double deltaAdd = 0.0;
                        int deltaTimes = 0;
                        int r = 255, g = 0, b = 0;
                        public void drawAt(double x, double y, ViewPort vp)
                        {
                            long nowTime = System.currentTimeMillis();
                            if( lastTime != -1 )
                            {
                                double delta = (nowTime - lastTime) / 1000.0;
                                deltaAdd += delta;
                                if( deltaAdd > 0.125 )
                                {
                                    deltaTimes++;
                                    if( deltaTimes > 10 )
                                        vp.draw.stateless.perm.queueRemove(this);

                                    deltaAdd = 0.0;
                                    if( r == 255 )
                                    {
                                        g = 255;
                                        r = 0;
                                    }
                                    else if( g == 255 )
                                    {
                                        b = 255;
                                        g = 0;
                                    }
                                    else if( b == 255 )
                                    {
                                        r = 255;
                                        b = 0;
                                    }
                                }
                            }

                            lastTime = nowTime;
                            vp.draw.stateless.rect.withMod()
                                .withColour(r, g, b, 255)
                                .withOffset(x, y)
                                .withOffset(p.x, p.y)
                            ;

                            vp.draw.stateless.rect.poslen(p.draw_playerSize * -0.75, p.draw_playerSize * -0.75, p.draw_playerSize * 1.5, p.draw_playerSize * 1.5);

                            vp.draw.stateless.text.withMod()
                                .withColour(r, g, b, 255)
                                .withOffset(x + p.x, y + p.y)
                                .withSize(11) 
                            ;   

                            vp.draw.stateless.text.pos_c(0, p.draw_playerSize * 0.90, "id: " + p.id);
                        }
                    });
                }
                return null;
            }

            public ObjectParser getParser()
            {  return null;  }

            public String describeCommand()
            {  return "highlights the selected Player/s";  }
        };


    }

}