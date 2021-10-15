package dev.ramar.e2.rendering.console;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import java.io.PrintStream;

import java.util.Random;

public class ConsoleParser
{


    public interface Command
    {
        public Object run(Object[] args);
    }

    public static ConsoleParser createParser(Console c)
    {
        ConsoleParser cp = new ConsoleParser(c);

        cp.addParser(new ObjectParser()
        {
            public Object[] parse(String s)
            {
                Object[] exp = null;
                String[] arr = s.split(" ");
                System.out.println(Arrays.toString(arr));
                if( arr[0].equals("move") )
                {
                    boolean incorrectCommand = true;
                    exp = new Object[3];
                    exp[0] = "moveConsole";
                    exp[1] = null;
                    exp[2] = 0;
                    if( arr.length > 0 )
                    {
                        if( arr[1].equals("up") 
                        ||  arr[1].equals("left")
                        ||  arr[1].equals("down")
                        ||  arr[1].equals("right")
                        )
                        {
                            exp[1] = arr[1];
                        }

                        if( arr.length > 1 )
                        {
                            try
                            {
                                int am = Integer.parseInt(arr[2]);
                                incorrectCommand = am < 1 || am > 30;
                                if(! incorrectCommand )
                                    exp[2] = am;
                            }
                            catch(NumberFormatException e) 
                            {
                                incorrectCommand = true;
                            }
                            if( incorrectCommand )
                                cp.ps.println("move (up | down | left | right) <number(1:30)>");
                        }
                    }

                }
                return exp;
            }

            public String unparse(Object[] arr)
            {
                return null;
            }
        });

        cp.addCommand("moveConsole", (Object[] args) ->
        {
            double moveHori = 0,
                   moveVert = 0;
            int moveAm = (int)args[2];

            switch((String)args[1])
            {
                case "up":
                    moveVert = -moveAm;
                    break;
                case "down":
                    moveVert =  moveAm;
                    break;
                case "left":
                    moveHori = -moveAm;
                    break;
                case "right":
                    moveHori =  moveAm;
                    break;
            }

            cp.console.withOffset(moveHori, moveVert);

            return null;

        });

        cp.addCommand("quit", (Object[] args) ->
        {
            cp.console.getViewPortRef().window.close();
            return null;  
        });

        cp.addParser(new ObjectParser()
        {
            public Object[] parse(String s)
            {
                Object[] out = null;
                String[] arr = s.split(" ");

                if( out[0].equals("test") )
                {
                    out = new Object[2];
                    out[0] = "test";
                    out[1] = new Random().nextInt();
                }

                return out;
            }

            public String unparse(Object[] arr)
            {
                String exp = "";
                for( Object o : arr )
                    exp += arr.toString() + " ";

                return exp.substring(0, exp.length() - 1);
            }
        });

        cp.addCommand("test", (Object[] args) ->
        {
            cp.ps.println("test! " + args[1]);

            return null;
        });

        return cp;
    }


    private interface ObjectParser
    {
        public Object[] parse(String s);

        public String unparse(Object[] arr);
    }


    private final Map<String, Command> commands = new HashMap<>();
    private List<ObjectParser> objectParsers = new ArrayList<>();
    private PrintStream ps;
    private Console console;


    public ConsoleParser()
    {
        this.ps = System.out;
    }

    public ConsoleParser(Console c)
    {
        this(c.getOutputStream());
        this.console = c;
    }  

    public ConsoleParser(PrintStream ps)
    {  
        this.ps = ps == null ? System.out : ps;
    }

    private void addCommand(String s, Command c)
    {
        commands.put(s, c);
    }

    private void addParser(ObjectParser op)
    {
        objectParsers.add(op);
    }


    public void parseCommand(String s)
    {
        if( s != null && !s.equals("") )
        {
            Object[] parsedCommand = null;

            for( ObjectParser parser : objectParsers )
            {
                parsedCommand = parser.parse(s);
                if( parsedCommand != null )
                    break;
            }

            boolean processed = false;
            if( parsedCommand != null )
            {
                for( String thisString : commands.keySet() )
                {
                    Command c = commands.get(thisString);
                    processed = parsedCommand[0].equals(thisString);

                    if( processed )
                    {
                        Object out = c.run(parsedCommand);
                        ps.println(out);
                        break;
                    }
                }
            }
            if(! processed )
            {
                ps.println("Unrecognised command! Try again");
            }
        }
    }
}