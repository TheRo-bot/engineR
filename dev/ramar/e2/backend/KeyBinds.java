package dev.ramar.e2.backend;

import dev.ramar.e2.structures.StaticInfo;


import java.util.*;

/*
Class: KeyBinds
 - Stores KeyBindings and can convert key codes to
   actions, which ActionListeners can then use to respond
*/
public class KeyBinds
{
	private static Map<Integer, String> keybinds = new HashMap<>();

	static
	{
		loadKeybinds();
	}


	private static void loadKeybinds()
	{
		keybinds.put(87, StaticInfo.Control.Actions.MOVE_UP);
		keybinds.put(65, StaticInfo.Control.Actions.MOVE_LEFT);
		keybinds.put(68, StaticInfo.Control.Actions.MOVE_RIGHT);
		keybinds.put(83, StaticInfo.Control.Actions.MOVE_DOWN);
		keybinds.put(82, StaticInfo.Control.Actions.RELOAD);
	}


	public static String getAction(int keyCode)
	{
		String action = keybinds.get(keyCode);

		// if the key isn't bound to the keyCode
		// convert to a key press correspondent to their keyboard
		if( action == null )
		{
			return String.valueOf((char)keyCode);
		}
		else
			return action;
	}


	public static String getMouseAction(int mouseCode)
	{
		String exportAction = null;
		switch(mouseCode)
		{
			case 1:
				exportAction = StaticInfo.Control.Actions.LEFT_CLICK;
				break;

			case 2:
				exportAction = StaticInfo.Control.Actions.MIDDLE_CLICK;
				break;

			case 3:
				exportAction = StaticInfo.Control.Actions.RIGHT_CLICK;
				break;
		}

		// System.out.println("getMouseAction(" + mouseCode + "): " + exportAction);
		return exportAction;
	}

}