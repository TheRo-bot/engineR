package dev.ramar.e2.core.control;

import dev.ramar.e2.core.structures.Vec2;

import dev.ramar.utils.HiddenList;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public abstract class MouseManager
{

	protected LocalList<MoveListener> movers = new LocalList<>();
	protected LocalList<WheelListener> wheelers = new LocalList<>();
	protected Map<Integer, LocalList<PressListener>> pressers = new HashMap<>();
	protected Map<Integer, LocalList<ReleaseListener>> releasers = new HashMap<>();

	public MouseManager()
	{
		this.move = this.createMoveController();
		this.wheel = this.createWheelController();

		this.press = this.createPressController();
		this.release = this.createReleaseController();
	}

	public abstract double toRawX(double x);
	public abstract double toRawY(double y);

	public abstract void listen();
	public abstract void ignore();


	// synchronise on getting / setting data!
	public final Vec2 position = new Vec2(0);

	// sends an update to all listeners
	public void poll()
	{

	}

	// button id mapping:
	/*
	 1: left
	 2: right
	 3: middle
	 4: mouse4
	 5: mouse5
	*/
	protected void onPress(int btn, double x, double y)
	{
		synchronized(this.pressers)
		{
			if( this.pressers.containsKey(btn) )
			{
				LocalList<PressListener> toInvoke = pressers.get(btn);
				for( PressListener pl : toInvoke.getList() )
					pl.onPress(btn, x, y);
			}
		}
	}

	protected void onPress(int btn)
	{
		double x = 0.0, y = 0.0;
		synchronized(this.position)
		{
			x = this.position.getX();
			y = this.position.getY();
		}

		this.onPress(btn, x, y);
	}

	protected void onRelease(int btn, double x, double y)
	{
		synchronized(this.releasers)
		{
			if( this.releasers.containsKey(btn) )
			{
				LocalList<ReleaseListener> toInvoke = releasers.get(btn);
				for( ReleaseListener rl : toInvoke.getList() )
					rl.onRelease(btn, x, y);
			}
		}
	}

	protected void onRelease(int btn)
	{
		double x = 0.0, y = 0.0;
		synchronized(this.position)
		{
			x = this.position.getX();
			y = this.position.getY();
		}

		this.onRelease(btn, x, y);
	}


	// new position, not movement vector
	protected void onMove(double x, double y)
	{
		synchronized(this.position)
		{
			this.position.setX(x);
			this.position.setY(y);
		}

		synchronized(this.movers)
		{
			for( MoveListener ml : this.movers.getList() )
				ml.onMove(x, y);
		}
	}

	protected void onWheel(double x, double y, double power)
	{
		synchronized(this.wheelers)
		{
			for( WheelListener wl : this.wheelers.getList() )
				wl.onWheel(x, y, power);
		}
	}

	protected void onWheel(double power)
	{
		double x = 0.0, y = 0.0;
		synchronized(this.position)
		{
			x = this.position.getX();
			y = this.position.getY();
		}

		this.onWheel(x, y, power);
	}



	public interface MoveListener
	{  public default void onMove(double x, double y) { }  }

	public interface WheelListener
	{  public default void onWheel(double x, double y, double power) { }  }

	public interface PressListener
	{  public default void onPress(int button, double x, double y) { }  }

	public interface ReleaseListener
	{  public default void onRelease(int button, double x, double y) { }  }

	public interface MouseListener extends MoveListener, WheelListener, PressListener, ReleaseListener
	{
		// public void onMove(double x, double y);
		// public void onWheel(double x, double y, double power);
		// public void onPress(int button, double x, double y);
		// public void onRelease(int button, double x, double y);
	}

	public class LocalList<E> extends HiddenList
	{
		private List<E> getList()
		{  return this.list;  }
	}


	protected MoveController createMoveController()
	{  return new MoveController();  }
	public final MoveController move;

	protected WheelController createWheelController()
	{  return new WheelController();  }
	public final WheelController wheel;

	protected PressController createPressController()
	{  return new PressController();  }
	public final PressController press;

	protected ReleaseController createReleaseController()
	{  return new ReleaseController();  }
	public final ReleaseController release;


	public void add(MouseListener ml, int... btns)
	{
		this.move.add(ml);
		this.wheel.add(ml);
		for( int btn : btns )
		{
			this.press.add(btn, ml);
			this.release.add(btn, ml);
		}
	}

	public void remove(MouseListener ml, int... btns)
	{
		for( int btn : btns )
		{
			this.move.remove(ml);
			this.wheel.remove(ml);
			this.press.remove(btn, ml);
			this.release.remove(btn, ml);
		}
	}


	public class MoveController
	{
		public void add(MoveListener ml)
		{
			synchronized(MouseManager.this.movers)
			{
				MouseManager.this.movers.add(ml);
			}
		}

		public void remove(MoveListener ml)
		{
			synchronized(MouseManager.this.movers)
			{
				MouseManager.this.movers.remove(ml);
			}
		}
	}


	public class WheelController
	{
		public void add(WheelListener wl)
		{
			synchronized(MouseManager.this.wheelers)
			{
				MouseManager.this.wheelers.add(wl);
			}
		}

		public void remove(WheelListener wl)
		{
			synchronized(MouseManager.this.wheelers)
			{
				MouseManager.this.wheelers.remove(wl);
			}
		}
	}

	public class PressController
	{
		public void add(int btn, PressListener pl)
		{
			synchronized(MouseManager.this.pressers)
			{
				if( !MouseManager.this.pressers.containsKey(btn) )
					MouseManager.this.pressers.put(btn, new LocalList<PressListener>());

				LocalList<PressListener> list = MouseManager.this.pressers.get(btn);
				synchronized(list)
				{
					list.add(pl);
				}
			}
		}
		public void add(PressListener pl, int... btns)
		{
			if( btns.length == 0 )
				this.add(1, pl);

			for( int btn : btns )
				this.add(btn, pl);
		}

		public void remove(PressListener pl, int... btns)
		{
			if( btns.length == 0 )
				this.remove(1, pl);
			
			for( int btn : btns )
				this.remove(btn, pl);
		}
		public void remove(int btn, PressListener pl)
		{
			synchronized(MouseManager.this.pressers)
			{
				if( MouseManager.this.pressers.containsKey(btn) )
				{
					LocalList<PressListener> list = MouseManager.this.pressers.get(btn);
					synchronized(list)
					{
						list.remove(MouseManager.this.pressers);
					} 
				}
			}
		}
	}


	public class ReleaseController
	{
		public void add(ReleaseListener rl, int... btns)
		{
			if( btns.length == 0 )
				this.add(1, rl);
			
			for( int btn : btns )
				this.add(btn, rl);
		}

		public void add(int btn, ReleaseListener rl)
		{
			synchronized(MouseManager.this.releasers)
			{
				if( !MouseManager.this.releasers.containsKey(btn) )
					MouseManager.this.releasers.put(btn, new LocalList<ReleaseListener>());

				LocalList<ReleaseListener> list = MouseManager.this.releasers.get(btn);
				synchronized(list)
				{	
					list.add(rl);
				}
			}
		}

		public void remove(ReleaseListener rl, int... btns)
		{
			if( btns.length == 0 )
				this.remove(1, rl);
			
			for( int btn : btns )
				this.remove(btn, rl);
		}


		public void remove(int btn, ReleaseListener rl)
		{
			synchronized(MouseManager.this.releasers)
			{
				if( MouseManager.this.releasers.containsKey(btn) )
				{
					LocalList<ReleaseListener> list = MouseManager.this.releasers.get(btn);
					synchronized(list)
					{
						list.remove(MouseManager.this.pressers);
					} 
				}
			}
		}
	}
}