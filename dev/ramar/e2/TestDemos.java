package dev.ramar.e2;


public class TestDemos
{
    EngineR2 instance;

    String currDemo;

    public TestDemos(EngineR2 instance)
    {
        this.instance = instance;
    }

    public void stopDemo()
    {
        switch(currDemo)
        {
            case "combat":
                stop_combat();
                break;

            case "explore":
                stop_explore();
                break;

            case "cutscene":
                stop_cutscene();
                break;
        }
    }



    


    public void demo_combat()
    {
        if( currDemo == null )
        {
            currDemo = "combat";

        }
        else
            instance.console.out.println("'" + currDemo + "' already playing! please use 'demo stop' to stop current demo");

    }

    public void stop_combat()
    {
        if( currDemo.equals("combat") )
        {
            currDemo = null;
        }
        else
            instance.console.out.println("Combat demo not running, can't stop it!");
    }









    public void demo_explore()
    {
        if( currDemo == null )
        {
            currDemo = "explore";
            
        }
    }

    public void stop_explore()
    {
        if( currDemo.equals("explore") )
        {
            currDemo = null;
        }
        else
            instance.console.out.println("Explore demo not running, can't stop it!");
    }










    public void demo_cutscene()
    {
        if( currDemo == null )
        {
            currDemo = "cutscene";
            
        }
    }

    public void stop_cutscene()
    {
        if( currDemo.equals("cutscene") )
        {
            currDemo = null;
        }
        else
            instance.console.out.println("Cutscene demo not running, can't stop it!");
    }
}