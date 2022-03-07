package dev.ramar.e2.demos.combat;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.console.*;
import dev.ramar.e2.rendering.console.parsers.*;

import dev.ramar.utils.HiddenList;


import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class PlayerCommand implements Command
{
	/* Static Stuff
	--===-------------
	*/
    public static final ObjectParser PARSER = new StringSplitter(" "); 

    private static PlayerCommand singleton = null;
    static
    {
    	singleton = new PlayerCommand();
    }
    public static PlayerCommand getInstance()
    {   return PlayerCommand.singleton;   }


    public static final PlayerList players = new PlayerList();

    public static class PlayerList extends HiddenList<Player>
    {
    	private List<Player> getList()
    	{   return this.list;   }
    }



    SubCommand list = (ConsoleParser cp, Object... args) ->
    {
    	String singleSelection = "int(1-" + Math.max(1, PlayerCommand.this.players.size()) + ")";

    	cp.ps.println("players <selection> highlight   || highlights the selected players");
    	cp.ps.println("players <" + singleSelection + ">  focus       || focuses ");
    	cp.ps.println("players <selection> modify <..> || ");
    	cp.ps.println("players <selection> = ");
    	cp.ps.println("                      all");
    	cp.ps.println("                      " + singleSelection);
    	cp.ps.println("                      " + singleSelection + "-" + singleSelection);
    };

    SubCommand highlight = new SubCommand()
    {
		public void run(ConsoleParser cp, Object... args)
		{

            List<Player> players = (List<Player>)args[1];

            for( Player p : players )
            {
                cp.console.engine.viewport.draw.stateless.perm.add(new Drawable()
                {
                	double delta = 1.5;

                    long lastTime = -1;
                    double deltaAdd = 0.0;
                    int r = 255, g = 0, b = 0;
                    public void drawAt(double x, double y, ViewPort vp)
                    {
                        long nowTime = System.currentTimeMillis();
                        if( lastTime != -1 )
                        {
                            double delta = (nowTime - lastTime) / 1000.0;
                            deltaAdd += delta;
                        	this.delta -= delta;

                        	if( this.delta < 0 )
                        		vp.draw.stateless.perm.queueRemove(this);
                        	else if( deltaAdd > 0.125 )
                            {
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

                        vp.draw.stateless.text.pos_c(0, p.draw_playerSize * 0.90, "id: " + (p.id + 1));
                    }
                });
            }
		}

    };

    SubCommand modify = new SubCommand()
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
        public void run(ConsoleParser cp, Object... args)
        {  
        	cp.ps.println("UNIMPLEMENTED");
        }
    };

    SubCommand focus = new SubCommand()
    {
		public void run(ConsoleParser cp, Object... args)
		{
			List<Player> players = (List<Player>)args[1];
	        if( players.size() > 1 )
                cp.ps.println("only one player must be selected for focus");
            else
            {
                for( Player p : PlayerCommand.players.getList() )
                    p.stopCameraTracking(cp.console.engine);

                if( !players.isEmpty() )
                    players.get(0).startCameraTracking(cp.console.engine);
            }
		}
    };




    public PlayerCommand() {}

    /* Command Implementation
	--===------------------------
    */

    public ObjectParser getParser()
    {   return PlayerCommand.PARSER;   }

    public String describeCommand()
    {   return "Information / Modification of Players";   }


    public Object run(ConsoleParser cp, Object[] args)
    {	
    	// args[0] == name
    	// args[1] == subCommand name
    	// args[2] == selection

    	String message = "";
    	if( args.length > 1 )
    		message = (String)args[1];

    	if( !this.players.isEmpty() )
    	{
    		switch(message)
    		{
    			case "list":
		    		this.list.run(cp, null);
		    		break;
	    		case "highlight":
	    			this.highlight.run(cp, makeArgs(cp, args));
	    			break;
	    		case "modify":
	    			this.modify.run(cp, makeArgs(cp, args));
	    			break;
	    		case "focus":
	    			this.focus.run(cp, makeArgs(cp, args));
	    			break;
    		}
    	}
    	else
    		cp.ps.println("no players currently exist!");

    	return null;
    }


    /* Helper Methods
	--===---------------
    */


    /*
	Helper Method: makeArgs
	 - Converts the StringSplitted array into the subcommand array

	   (  [name, subCommand name, selection, ...]  )
	   (  convert ^ to v)
	   (  [subCommand name, List<Player>, ...]  )
    */
    private Object[] makeArgs(ConsoleParser cp, Object... args)
    {
    	Object[] out = new Object[args.length - 1];
    	out[0] = args[1];

    	out[1] = selectionOf(cp, this.players.getList(), (String)args[2]);
    	for( int ii = 2; ii < out.length; ii++ )
    		out[ii] = args[ii + 1];

    	return out;
    }


    /*
	Helper Method: selectionOf
	 - Takes in a selection (in String format) and returns the players within <toChoose> that have been selected 
    */
    private List<Player> selectionOf(ConsoleParser cp, List<Player> toChoose, String selection)
    {
    	List<Player> out = new ArrayList<>();

    	int[] range = null;
    	try
    	{
	    	range = getRange(toChoose, selection);
	    	cp.ps.println(Arrays.toString(range));
	    	if( range[0] != -1 )
	    	{
		    	for( int ii = range[0]; ii <= range[1]; ii++ )
		    		out.add(toChoose.get(ii));
	    	}
    	}
    	catch(RangeException re)
    	{   cp.ps.println(re.getMessage());   }


    	return out;
    }

    /*
	Helper Method: getRange
	 - parses <selection> into an int[2]: 
	    - [0] == min
	    - [1] == max

	 - defaults: int{0, 0}
	 - throws:
	    - RangeException || when a parsing error has occurred
    */
    private int[] getRange(List<Player> players, String selection) throws RangeException
    {
    	int[] out = new int[]{-1, -1};

    	int min = 0, max = 0;

    	// do the easiest one first
    	if( selection.equals("all") )
    	{
    		min = 0;
    		max = players.size() - 1;
    	}

    	// let's pretend it's a range, cause we can prove that easy
    	else if( selection.contains("-") )
    	{
    		String[] nums = selection.split("-");
    		try
    		{   
    			min = Integer.parseInt(nums[0]);
    			max = Integer.parseInt(nums[1]);
    		}
    		catch(NumberFormatException e) 
    		{   throw new RangeException("expected \"int(1-" + players.size() + ")-int(1-" + players.size() + "\": received int(" + selection + ")");   }

    		// we actually do have a range.. so let's make sure they're good
    		if( min <= 0 || min > max )
    			throw new RangeException("expected \"int(1-" + players.size() + ")-int(1-" + players.size() + "\": received a minimum value that's" + (min <= 0 ? " < 1" : " > " + max ));

    		if( max > players.size() )
    			throw new RangeException("expected \"int(1-" + players.size() + ")-int(1-" + players.size() + "\": received a maximum value that's > " + players.size());
    	}
    	// it's a number... or it's invalid
    	else
    	{	
	    	try
	    	{
	    		int num = Integer.parseInt(selection);

	    		// it's a number! make sure it's right
	    		if( num <= 0 || num > players.size() )
	    			throw new RangeException("expected int(1-" + players.size() + "): received a value that's " + (num > 0 ? "< 1" : "> " + players.size()));
	    	}
	    	catch(NumberFormatException e) 
	    	{
	    		// it's nothing, yuck!
	    		throw new RangeException("expected int(1-" + players.size() + "): received \"" + selection + "\"");
	    	}
    	}

    	// set the output, and we're done
    	out[0] = min;
    	out[1] = max;

    	return out;
    }

    /*
	Exception: RangeException
	 - Thrown explicitly within getRange() 
    */
    private class RangeException extends Exception
    {
    	public RangeException(String message)
    	{
    		super(message);
    	}

    	public RangeException(String message, Exception e)
    	{
    		super(message, e);
    	}
    }

}