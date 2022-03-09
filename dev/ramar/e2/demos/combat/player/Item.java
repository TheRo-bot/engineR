package dev.ramar.e2.demos.combat.player;

import dev.ramar.e2.demos.combat.actions.ActionManager;
import dev.ramar.e2.demos.combat.actions.ActionManager.Action;

public abstract class Item
{
	public final ActionManager actions = new ActionManager();

	public Item()
	{
		this.setup_actions();
	}

	protected void setup_actions()
	{
		this.actions.add(new Action()
		{
			public String getName()
			{   return "item:use";   }

			public boolean act(ActionManager am, Object... o)
			{	
				Item.this.use();
				return true;
			}
		});

		this.actions.add(new Action()
		{
			public String getName()
			{   return "item:act";   }

			public boolean act(ActionManager am, Object... o)
			{
				String message = null;
				if( o.length > 1 && o[0] instanceof String )
					message = (String)o[0];

				Item.this.act(message, java.util.Arrays.copyOfRange(o, 1, o.length));
				return true;
			}
		});
	}

	public void blocked_use()
	{
		this.actions.blockedRun(this.actions.get("use"));
	}

	public abstract void use();

	public void blocked_act(String s, Object... args)
	{
		Object[] in = new Object[args.length + 1];

		in[0] = s;
		for( int ii = 0; ii < args.length; ii++ )
			in[ii + 1] = args[ii];
		
		this.actions.blockedRun(this.actions.get("act"), s, in);
	}

	public abstract void act(String s, Object... args);
}