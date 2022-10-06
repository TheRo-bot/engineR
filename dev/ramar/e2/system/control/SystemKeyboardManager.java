package dev.ramar.e2.system.control;

import dev.ramar.e2.core.rendering.Window;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import dev.ramar.e2.core.control.KeyboardManager;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

/*
KeyboardManager: SystemKeyboardManager
 - A System hook implementation of KeyboardManager, that restricts
   usage to the window being focused. 
*/
public class SystemKeyboardManager extends KeyboardManager
{

	private static final Map<Integer, String> VKEYS = new HashMap<>();

	static
	{
		VKEYS.put(GlobalKeyEvent.VK_MENU, KeyboardManager.VKeys.ALT);
		VKEYS.put(GlobalKeyEvent.VK_LMENU, KeyboardManager.VKeys.LALT);
		VKEYS.put(GlobalKeyEvent.VK_RMENU, KeyboardManager.VKeys.RALT);

		VKEYS.put(GlobalKeyEvent.VK_SHIFT, KeyboardManager.VKeys.SHIFT);
		VKEYS.put(GlobalKeyEvent.VK_LSHIFT, KeyboardManager.VKeys.LSHIFT);
		VKEYS.put(GlobalKeyEvent.VK_RSHIFT, KeyboardManager.VKeys.RSHIFT);

		VKEYS.put(GlobalKeyEvent.VK_CONTROL, KeyboardManager.VKeys.CNTRL);
		VKEYS.put(GlobalKeyEvent.VK_LCONTROL, KeyboardManager.VKeys.LCNTRL);
		VKEYS.put(GlobalKeyEvent.VK_RCONTROL, KeyboardManager.VKeys.RCNTRL);

		VKEYS.put(GlobalKeyEvent.VK_ESCAPE, KeyboardManager.VKeys.ESCAPE);
		VKEYS.put(GlobalKeyEvent.VK_TAB, KeyboardManager.VKeys.TAB);
		VKEYS.put(GlobalKeyEvent.VK_RETURN, KeyboardManager.VKeys.ENTER);
		VKEYS.put(GlobalKeyEvent.VK_CAPITAL, KeyboardManager.VKeys.CAPS);

		VKEYS.put(GlobalKeyEvent.VK_SPACE, KeyboardManager.VKeys.SPACE);

		VKEYS.put(GlobalKeyEvent.VK_PRIOR, KeyboardManager.VKeys.PAGE_UP);
		VKEYS.put(GlobalKeyEvent.VK_NEXT, KeyboardManager.VKeys.PAGE_DOWN);

		VKEYS.put(GlobalKeyEvent.VK_UP, KeyboardManager.VKeys.UP);
		VKEYS.put(GlobalKeyEvent.VK_DOWN, KeyboardManager.VKeys.DOWN);
		VKEYS.put(GlobalKeyEvent.VK_LEFT, KeyboardManager.VKeys.LEFT);
		VKEYS.put(GlobalKeyEvent.VK_RIGHT, KeyboardManager.VKeys.RIGHT);

		VKEYS.put(GlobalKeyEvent.VK_DELETE, KeyboardManager.VKeys.DELETE);
		VKEYS.put(GlobalKeyEvent.VK_BACK, KeyboardManager.VKeys.BACKSPACE);

		VKEYS.put(GlobalKeyEvent.VK_F1, KeyboardManager.VKeys.F1);
		VKEYS.put(GlobalKeyEvent.VK_F2, KeyboardManager.VKeys.F2);
		VKEYS.put(GlobalKeyEvent.VK_F3, KeyboardManager.VKeys.F3);
		VKEYS.put(GlobalKeyEvent.VK_F4, KeyboardManager.VKeys.F4);
		VKEYS.put(GlobalKeyEvent.VK_F5, KeyboardManager.VKeys.F5);
		VKEYS.put(GlobalKeyEvent.VK_F6, KeyboardManager.VKeys.F6);
		VKEYS.put(GlobalKeyEvent.VK_F7, KeyboardManager.VKeys.F7);
		VKEYS.put(GlobalKeyEvent.VK_F8, KeyboardManager.VKeys.F8);
		VKEYS.put(GlobalKeyEvent.VK_F9, KeyboardManager.VKeys.F9);
		VKEYS.put(GlobalKeyEvent.VK_F10, KeyboardManager.VKeys.F10);
		VKEYS.put(GlobalKeyEvent.VK_F11, KeyboardManager.VKeys.F11);
		VKEYS.put(GlobalKeyEvent.VK_F12, KeyboardManager.VKeys.F12);
		VKEYS.put(GlobalKeyEvent.VK_F13, KeyboardManager.VKeys.F13);
		VKEYS.put(GlobalKeyEvent.VK_F14, KeyboardManager.VKeys.F14);
		VKEYS.put(GlobalKeyEvent.VK_F15, KeyboardManager.VKeys.F15);
		VKEYS.put(GlobalKeyEvent.VK_F16, KeyboardManager.VKeys.F16);
		VKEYS.put(GlobalKeyEvent.VK_F17, KeyboardManager.VKeys.F17);
		VKEYS.put(GlobalKeyEvent.VK_F18, KeyboardManager.VKeys.F18);
		VKEYS.put(GlobalKeyEvent.VK_F19, KeyboardManager.VKeys.F19);
		VKEYS.put(GlobalKeyEvent.VK_F20, KeyboardManager.VKeys.F20);
		VKEYS.put(GlobalKeyEvent.VK_F21, KeyboardManager.VKeys.F21);
		VKEYS.put(GlobalKeyEvent.VK_F22, KeyboardManager.VKeys.F22);
		VKEYS.put(GlobalKeyEvent.VK_F23, KeyboardManager.VKeys.F23);
		VKEYS.put(GlobalKeyEvent.VK_F24, KeyboardManager.VKeys.F24);

	}

	public SystemKeyboardManager(Window window)
	{
		this.window = window;

	}

	protected final Window window; 

	protected GlobalKeyboardHook hook = new GlobalKeyboardHook(true);

	public void shutdown()
	{
		System.out.println("keyboard shutdown");
		this.hook.shutdownHook();
	}

	@Override
	protected void setVKey(String id, boolean pressed)
	{
		if( !pressed && this.pressed.contains(id) )
			super.setVKey(id, pressed);
	}

	@Override
	protected void onPress(String id)
	{
		super.onPress(id);
		synchronized(this.pressed)
		{
			this.pressed.add(id);
		}
	}

	@Override
	protected void onRelease(String id)
	{
		super.onRelease(id);
		synchronized(this.pressed)
		{
			this.pressed.remove(id);
		}
	}


	private Set<String> pressed = new HashSet<>();

	protected GlobalKeyAdapter adapter = new GlobalKeyAdapter()
	{
		@Override 
		public void keyPressed(GlobalKeyEvent event) {
			Window wind = SystemKeyboardManager.this.window;
			if( wind != null && wind.isFocused() )
			{
				int vkey = event.getVirtualKeyCode();
				if( VKEYS.containsKey(vkey) )
					SystemKeyboardManager.this.setVKey(VKEYS.get(vkey), true);
				else
				{
					char c = event.getKeyChar();
					if( (int)c != 0 )
						SystemKeyboardManager.this.onPress("" + c);
				}
			}
		}
		
		@Override 
		public void keyReleased(GlobalKeyEvent event) {
			int vkey = event.getVirtualKeyCode();
			if( VKEYS.containsKey(vkey) )
				SystemKeyboardManager.this.setVKey(VKEYS.get(vkey), false);
			else
			{
				char c = event.getKeyChar();
				if( (int)c != 0 && pressed.contains("" + c) )
					SystemKeyboardManager.this.onRelease("" + c);
			}
		}
	};


	public void listen()
	{
		this.hook.addKeyListener(this.adapter);
	}

	public void ignore() 
	{
		this.hook.removeKeyListener(this.adapter);
	}
}