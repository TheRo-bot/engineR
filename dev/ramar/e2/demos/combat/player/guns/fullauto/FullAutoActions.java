package dev.ramar.e2.demos.combat.player.guns.fullauto;

import dev.ramar.e2.demos.combat.player.guns.GunActions;
import dev.ramar.e2.demos.combat.player.guns.GunActions.GunActionListeners;


import dev.ramar.e2.demos.combat.actions.ActionManager;
import dev.ramar.e2.demos.combat.actions.Action;

import dev.ramar.utils.HiddenList;

import java.util.List;

public class FullAutoActions extends GunActions
{


	public FullAutoActions()
	{
		super();
		super.withListeners(this.listeners);
	}

	/* ClassFields
	--===------------
	*/


	public final FullAutoListeners listeners = new FullAutoListeners();


	/* 
	Class: FullAutoListeners
	 - addon to GunActionListeners, adds the Semi-Auto action listening events on top of the Gun actions
	 - Listenable Actions:
	    - shooting.start || when the gun starts shooting
	    - shooting.stop  || when the gun stops shooting
	*/
	public static class FullAutoListeners extends GunActionListeners
	{
		public final ShootingListeners shooting = new ShootingListeners();


		public static class ShootingListeners
		{
			public final LocalList<StartShootingListener> start = new LocalList<>();
			public final LocalList<StopShootingListener> stop = new LocalList<>();
		}
	}

	public static class LocalList<E> extends HiddenList<E>
	{
		private List<E> getList()
		{
			return this.list;
		}
	}



	/* Listener Interfaces
	--===--------------------
	*/



	public interface StartShootingListener
	{
		public void onStart(ActionManager am, boolean blocked);
	}

	public interface StopShootingListener
	{
		public void onStop(ActionManager am);
	}



	/* Sub-class helper methods
	--===-------------------------
	For when the sub-classes dictate when these events are happening
	*/



	protected void onStartShooting(ActionManager am, boolean blocked)
	{
		List<StartShootingListener> list = this.listeners.shooting.start.getList();

		synchronized(list)
		{
			for( StartShootingListener ssl : list )
				ssl.onStart(am, blocked);
		}
	}

	protected void onStopShooting(ActionManager am)
	{
		List<StopShootingListener> list = this.listeners.shooting.stop.getList();
		synchronized(list)
		{
			for( StopShootingListener ssl : list )
				ssl.onStop(am);
		}
	}



	/* Actions
	--===--------
	*/



	public final Action startShooting = new Action("gun:shoot:start")
	{
		public void act(ActionManager am, Object... args)
		{
			FullAutoActions.this.onStartShooting(am, false);
		}

		public void blockedAct(ActionManager am, Object... args)
		{
			FullAutoActions.this.onStartShooting(am, true);
		}
	};

	public final Action stopShooting = new Action("gun:shoot:stop")
	{
		public void act(ActionManager am, Object... args)
		{
			FullAutoActions.this.onStopShooting(am);
		}

		public boolean freeWhenSet = false;

		public void blockedAct(ActionManager am, Object... args)
		{
			this.freeWhenSet = true;
		}

		public void onUnblock()
		{
			if( this.freeWhenSet )
			{
				FullAutoActions.this.onStopShooting(null);
				this.freeWhenSet = false;
			}
		}
	};


}