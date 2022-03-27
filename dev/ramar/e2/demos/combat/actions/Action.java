package dev.ramar.e2.demos.combat.actions;


import dev.ramar.utils.HiddenList;

public abstract class Action
{

	public Action(String name) 
	{
		this.name = name;
	}

	public String toString()
	{
		return "Action: " + this.name;
	}


	/* Class-Fields
	--===-------------
	*/

	private String name;
	public final String getName()
	{   return this.name;   }


	/*
	Class-Field: toBlock
	 - Stores a list of <Action>s, which when signalled by block(...) / unblock(...),
	   will block / unblock all the elements in that list
	*/
    public final ActionList toBlock = new ActionList();
    public class ActionList extends HiddenList<Action>
    {
	    public final void block(ActionManager... mans)
	    {
	    	for( ActionManager man : mans)
		        for( Action a : this.list )
		            man.block(Action.this, a);
	    }

	    public final void unblock(ActionManager... mans)
	    {
	    	for( ActionManager man : mans )
		        for( Action a : this.list )
		            man.unblock(Action.this, a);
	    }
    }




    /* Implementable Methods
	--===----------------------
    */

	public abstract void act(ActionManager am, Object... args);
	public void blockedAct(ActionManager am, Object... args) {}

	/* Events
	--====------
	*/

	// when there's nothing blocking this node
	public void onUnblock(ActionManager am) {}
}