package dev.ramar.e2.demos.combat.player.items.guns;

import dev.ramar.e2.demos.combat.player.items.guns.Gun.GunStats;
import dev.ramar.e2.demos.combat.Player;

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.demos.combat.actions.ActionManager;
import dev.ramar.e2.demos.combat.actions.ActionManager.Action;

public class RifleGun extends Gun
{


	public RifleGun(Player player)
	{
		super(player);
		this.stats = new RifleStats();
	}


	@Override
	protected void setup_actions()
	{
		super.setup_actions();
		this.actions.add(new Action()
		{
			public String getName()
			{   return "gun:shoot:start";   }

			public boolean act(ActionManager am, Object... o)
			{
			 	RifleGun.this.shootStart();
			 	return true;
			}
		});


		this.actions.add(new Action()
		{
			public String getName()
			{   return "gun:shoot:stop";   }

			public boolean act(ActionManager am, Object... o)
			{
			 	RifleGun.this.shootStop();
			 	return true;
			}
		});
	}



	public class RifleStats extends GunStats
	{
		// in a second
		public static final double DEFAULT_FIRE_RATE = 99.0;

		public double fireRate = RifleStats.DEFAULT_FIRE_RATE;
		public RifleStats withFireRate(double fireRate)
		{
			this.fireRate = fireRate;
			return this;
		}

		public RifleStats()
		{
			super();
		}
	};	


	private Updatable shooter = null;

	private void shootStart()
	{
		this.shooter = new Updatable()
		{
			private double delta = 1.0 / ((RifleStats)RifleGun.this.stats).fireRate;

			public boolean update(double delta)
			{
				boolean stop = false;
				this.delta -= delta;
				if( this.delta < 0 )
				{
					this.delta = 1.0 / ((RifleStats)RifleGun.this.stats).fireRate;
					// shoot!
					double x = Gun.viewports.get(0).window.mouse.getMouseX();
					double y = Gun.viewports.get(0).window.mouse.getMouseY();
				
					RifleGun.this.actions.blockedRun(RifleGun.this.actions.get("gun:shoot"), x, y);
				}

				return stop;
			}
		};

		DeltaUpdater.getInstance().toUpdate.add(shooter);

	}

	private void shootStop()
	{
		System.out.println("shoot stop!");
		DeltaUpdater.getInstance().toUpdate.queueRemove(this.shooter);
	}

}