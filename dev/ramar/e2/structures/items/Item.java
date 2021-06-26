package dev.ramar.e2.structures.items;


import dev.ramar.e2.interfaces.events.listeners.ItemHolder;

import dev.ramar.e2.interfaces.rendering.Renderable;
import dev.ramar.e2.interfaces.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;

public class Item implements Renderable
{
	public String name;


	public Item(String name)
	{
		this.name = name;
	}


	public void use(int type, ItemHolder ih)
	{
		switch(type)
		{
			case 0:
				mainUse(ih);
				break;
			case 1:
				secondUse(ih);
				break;
		}
	}

	public void mainUse(ItemHolder il)
	{}


	public void secondUse(ItemHolder il)
	{}



	public void render(ViewPort vp)
	{
		drawSelf(null, vp);
	}


	public void drawSelf(Vec2 p, ViewPort vp)
	{

	}

}