package dev.ramar.e2.demos.combat.player.guns;

import dev.ramar.e2.demos.combat.actions.ActionManager;
import dev.ramar.e2.demos.combat.actions.Action;

import dev.ramar.utils.HiddenList;

import java.util.List;

public class GunActions
{
	public final ActionManager manager = new ActionManager();

	public GunActions()
	{
		this.listeners = new GunActionListeners();
	}

	protected GunActions(GunActionListeners l)
	{
		this.listeners = l;
	}


	/* Class-Fields
	--===-------------
	*/


	/*
	Listener Class: GunActionsListeners
	 - Stores:
	    - aim    || AimListener
	    - reload || ReloadListener
	*/
	public GunActionListeners listeners;
	public GunActions withListeners(GunActionListeners listeners)
	{
		this.listeners = listeners;
		return this;
	}
	public static class GunActionListeners
	{
		public final LocalListener<AimListener> aim = new LocalListener<>();
		public final LocalListener<ReloadListener> reload = new LocalListener<ReloadListener>();

		public class LocalListener<E> extends HiddenList<E>
		{
			private List<E> getList()
			{   return this.list;   }


			@Override
			public void add(E e)
			{
				super.add(e);
			}
		}
	}


	/* Sub-class helper methods
	--===-------------------------
	For when the sub-classes dictate when these events are happening
	*/


	protected final void onAim(double x, double y)
	{
		List<AimListener> list = this.listeners.aim.getList();
		synchronized(list)
		{
			for( AimListener al : list )
				al.onAim(x, y);
		}
	}

	protected final void onReload()
	{
		List<ReloadListener> list = this.listeners.reload.getList();
		synchronized(list)
		{
			for( ReloadListener rl : list )
				rl.onReload();
		}

	}


	public interface AimListener
	{   public void onAim(double x, double y);   }

	public interface ReloadListener
	{   public void onReload();   }



	/*
	Action: aim
	 - aim the gun !!
	 - args:
	 	[0] = x || double
	 	[1] = y || double
	*/
	public final Action aim = new Action("gun:aim")
	{
		public void act(ActionManager am, Object... args)
		{
			if( args.length > 1 )
			{
				try
				{
					double x = (double)args[0], 
						   y = (double)args[1];

					GunActions.this.onAim(x, y);
				}
				catch(NumberFormatException e) {}
			}
		}
	};


	/*
	Action: reload
	 - reloads the gun !!
	 - args: <none>
	*/
	public final Action reload = new Action("gun:reload")
	{
		public void act(ActionManager am, Object... args)
		{
			// do some reloading shit
			GunActions.this.onReload();
		}
	};
}