package dev.ramar.e2.control;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.utils.HiddenList;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public abstract class MouseManager
{

	protected LocalList<MoveListener> movers = new LocalList<>();
	protected Map<Integer, LocalList<PressListener>> pressers = new HashMap<>();
	protected Map<Integer, LocalList<ReleaseListener>> releasers = new HashMap<>();

	public MouseManager()
	{
		this.move = this.createMoveController();
		this.press = this.createPressController();
		this.release = this.createReleaseController();
	}

	// synchronise on getting / setting data!
	public final Vec2 mousePosition = new Vec2(0);


	protected void onPress(int buttonDex)
	{
		double x = 0.0, y = 0.0;
		synchronized(this.mousePosition)
		{
			x = this.mousePosition.getX();
			y = this.mousePosition.getY();
		}

		synchronized(this.pressers)
		{
			if( this.pressers.containsKey(buttonDex) )
			{

				LocalList<PressListener> toInvoke = pressers.get(buttonDex);
				for( PressListener pl : toInvoke.getList() )
					pl.onPress(buttonDex, x, y);
			}
		}
	}

	protected void onRelease(int buttonDex)
	{
		double x = 0.0, y = 0.0;
		synchronized(this.mousePosition)
		{
			x = this.mousePosition.getX();
			y = this.mousePosition.getY();
		}

		synchronized(this.releasers)
		{
			if( this.releasers.containsKey(buttonDex) )
			{
				LocalList<ReleaseListener> toInvoke = releasers.get(buttonDex);
				for( ReleaseListener rl : toInvoke.getList() )
					rl.onRelease(buttonDex, x, y);
			}
		}
	}


	// new position, not movement vector
	protected void onMove(double x, double y)
	{
		synchronized(this.mousePosition)
		{
			this.mousePosition.setX(x);
			this.mousePosition.setY(y);
		}

		synchronized(this.movers)
		{
			for( MoveListener ml : this.movers.getList() )
			{
				ml.onMove(x, y);
			}
		}
	}



	public interface MoveListener
	{  public void onMove(double x, double y);  }

	public interface PressListener
	{  public void onPress(int button, double x, double y);  }

	public interface ReleaseListener
	{  public void onRelease(int button, double x, double y);  }

	public interface MouseListener extends MoveListener, PressListener, ReleaseListener
	{
		// public void onMove(double x, double y);
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

	protected PressController createPressController()
	{  return new PressController();  }
	public final PressController press;

	protected ReleaseController createReleaseController()
	{  return new ReleaseController();  }
	public final ReleaseController release;



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
			for( int btn : btns )
				this.add(btn, pl);
		}

		public void remove(PressListener pl, int... btns)
		{
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