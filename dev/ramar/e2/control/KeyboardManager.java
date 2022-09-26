package dev.ramar.e2.control;

import dev.ramar.utils.HiddenList;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public abstract class KeyboardManager
{

	private Map<String, LocalList<PressListener>> pressers = new HashMap<>();
	private Map<String, LocalList<ReleaseListener>> releasers = new HashMap<>();

	public KeyboardManager()
	{
		this.press = new PressControl(this);
		this.release = new ReleaseControl(this);
	}

	protected void onKeyPressed(String id)
	{
		synchronized(this.pressers)
		{
			if( this.pressers.containsKey(id) )
			{
				LocalList<PressListener> toInvoke = this.pressers.get(id);
				for(PressListener pl : toInvoke.getList() )
					pl.onPress(id);
			}
		}
	}

	protected void onKeyReleased(String id)
	{
		synchronized(this.releasers)
		{
			if( this.releasers.containsKey(id) )
			{
				LocalList<ReleaseListener> toInvoke = this.releasers.get(id);
				for(ReleaseListener pl : toInvoke.getList() )
					pl.onRelease(id);
			}
		}
	}


	public interface PressListener
	{  public void onPress(String id);  }

	public interface ReleaseListener
	{  public void onRelease(String id);  }


	public class LocalList<E> extends HiddenList
	{
		private List<E> getList()
		{  return this.list;  }
	}



	public final PressControl press;
	public final ReleaseControl release;




	public class PressControl
	{
		private KeyboardManager kb = null;

		public PressControl(KeyboardManager kb)
		{
			this.kb = kb;
		}

		public void add(String id, PressListener pl)
		{
			if( this.kb != null )
			{
				if( !this.kb.pressers.containsKey(id) )
					this.kb.pressers.put(id, new LocalList<PressListener>());

				LocalList<PressListener> presser = this.kb.pressers.get(id);
				presser.add(pl);
			}
		}

		public void remove(String id, PressListener pl)
		{
			if( this.kb != null && this.kb.pressers.containsKey(id) )
			{
				LocalList<PressListener> presser = this.kb.pressers.get(id);
				if( presser != null )
					presser.remove(pl);
			}
		}
	}
	public class ReleaseControl
	{
		private KeyboardManager kb = null;

		public ReleaseControl(KeyboardManager kb)
		{
			this.kb = kb;
		}

		public void add(String id, ReleaseListener rl)
		{
			if( this.kb != null )
			{
				if( !this.kb.releasers.containsKey(id) )
					this.kb.releasers.put(id, new LocalList<ReleaseListener>());

				LocalList<ReleaseListener> releaser = this.kb.releasers.get(id);
				releaser.add(rl);
			}
		}

		public void remove(String id, ReleaseListener rl)
		{
			if( this.kb != null && this.kb.releasers.containsKey(id) )
			{
				LocalList<ReleaseListener> releaser = this.kb.releasers.get(id);
				if( releaser != null )
					releaser.remove(rl);
			}
		}
	}
}