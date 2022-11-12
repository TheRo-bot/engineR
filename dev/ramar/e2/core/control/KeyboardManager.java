package dev.ramar.e2.core.control;

import dev.ramar.utils.HiddenList;

import java.util.List;

import java.util.Map;
import java.util.HashMap;

import java.util.Arrays;

public abstract class KeyboardManager
{

	public class VKeys
	{
		public static final String SHIFT = "SHIFT";
		public static final String LSHIFT = "LSHIFT";
		public static final String RSHIFT = "RSHIFT";

		public static final String ALT = "ALT";
		public static final String LALT = "LALT";
		public static final String RALT = "RALT";

		public static final String CNTRL = "CNTRL";
		public static final String LCNTRL = "LCNTRL";
		public static final String RCNTRL = "RCNTRL";


		public static final String ESCAPE = "ESCAPE";
		public static final String TAB = "TAB";
		public static final String ENTER = "ENTER";
		public static final String CAPS = "CAPS";

		public static final String PAGE_UP = "PAGE_UP";
		public static final String PAGE_DOWN = "PAGE_DOWN";

		public static final String UP = "UP";
		public static final String DOWN = "DOWN";
		public static final String LEFT = "LEFT";
		public static final String RIGHT = "RIGHT";
		public static final String SPACE = "SPACE";

		public static final String DELETE = "DELETE";
		public static final String BACKSPACE = "BACKSPACE";

		public static final String F1 = "F1";
		public static final String F2 = "F2";
		public static final String F3 = "F3";
		public static final String F4 = "F4";
		public static final String F5 = "F5";
		public static final String F6 = "F6";
		public static final String F7 = "F7";
		public static final String F8 = "F8";
		public static final String F9 = "F9";
		public static final String F10 = "F10";
		public static final String F11 = "F11";
		public static final String F12 = "F12";
		public static final String F13 = "F13";
		public static final String F14 = "F14";
		public static final String F15 = "F15";
		public static final String F16 = "F16";
		public static final String F17 = "F17";
		public static final String F18 = "F18";
		public static final String F19 = "F19";
		public static final String F20 = "F20";
		public static final String F21 = "F21";
		public static final String F22 = "F22";
		public static final String F23 = "F23";
		public static final String F24 = "F24";
	}

	private Map<String, LocalList<PressListener>> pressers = new HashMap<>();
	private Map<String, LocalList<ReleaseListener>> releasers = new HashMap<>();

	public final LocalList<KeyAdapter> adapters = new LocalList<>();

	public KeyboardManager()
	{
		this.press = new PressControl(this);
		this.release = new ReleaseControl(this);
	}

	public abstract void listen();
	public abstract void ignore();

	protected Map<String, Boolean> virtualKeyStates = new HashMap<>();

	protected boolean isVKeyDown(String vkey)
	{
		return this.virtualKeyStates.containsKey(vkey) && this.virtualKeyStates.get(vkey);
	}
	protected boolean isVKeyUp(String vkey)
	{
		return !this.virtualKeyStates.containsKey(vkey) || !this.virtualKeyStates.get(vkey);
	}

	protected void setVKey(String vkey, boolean pressed)
	{
		if( pressed )
		{
			boolean alert = this.isVKeyUp(vkey);
			this.virtualKeyStates.put(vkey, true);
			if( alert )
				this.onPress(vkey);
		}
		else
		{
			boolean alert = this.isVKeyDown(vkey);
			this.virtualKeyStates.remove(vkey);
			if( alert )
				this.onRelease(vkey);
		}
	}

	protected void onPress(String id)
	{
		boolean consumed = false;
		synchronized(this.adapters)
		{
			for( KeyAdapter adapter : this.adapters.getList() )
			{
				consumed = adapter.onPress(id);
				if( consumed )
					break;
			}
		}

		if( !consumed )
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
	}

	protected void onRelease(String id)
	{
		boolean consumed = false;
		synchronized(this.adapters)
		{
			for( KeyAdapter adapter : this.adapters.getList() )
			{
				consumed = adapter.onRelease(id);
				if( consumed )
					break;
			}
		}

		if( !consumed )
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

	}


	public interface PressListener
	{  public default void onPress(String id) {}  }

	public interface ReleaseListener
	{  public default void onRelease(String id) {}  }

	public interface KeyListener extends PressListener, ReleaseListener
	{
		// public void onRelease(String id)		
		// public void onPress(String id)
	}

	public interface KeyAdapter
	{
		// returns true if the key should be consumed
		public default boolean onRelease(String id) { return false; }
		public default boolean onPress(String id) { return false; }
	}

	public class LocalList<E> extends HiddenList
	{
		private List<E> getList()
		{  return this.list;  }
	}


	public final PressControl press;
	public final ReleaseControl release;

	public void add(KeyListener kl, String... ids)
	{
		for( String id : ids )
		{
			this.press.add(id, kl);
			this.release.add(id, kl);
		}
	}

	public void remove(KeyListener kl, String... ids)
	{
		for( String id : ids )
		{
			this.press.remove(id, kl);
			this.release.remove(id, kl);
		}
	}


	public void clear(KeyListener kl)
	{
		for( String s : this.pressers.keySet() )
		{
			LocalList<PressListener> li = this.pressers.get(s);
			li.remove(kl);
		}

		for( String s : this.releasers.keySet() )
		{
			LocalList<ReleaseListener> li = this.releasers.get(s);
			li.remove(kl);
		}
	}



	public class PressControl
	{
		private KeyboardManager kb = null;

		public PressControl(KeyboardManager kb)
		{
			this.kb = kb;
		}
		public void add(PressListener pl, String... ids)
		{
			for(String id : ids)
				this.add(id, pl);
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

		public void remove(PressListener pl, String... ids)
		{
			for(String id : ids)
				this.remove(id, pl);
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

		public void add(ReleaseListener rl, String... ids)
		{
			for(String id : ids)
				this.add(id, rl);
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

		public void remove(ReleaseListener rl, String... ids)
		{
			for(String id : ids)
				this.remove(id, rl);
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