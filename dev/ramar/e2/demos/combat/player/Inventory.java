package dev.ramar.e2.demos.combat.player;

import dev.ramar.e2.demos.combat.actions.ActionManager.Action;

public class Inventory
{

	private Item[] hotbar = null;
	private int selection = -1;

	public Inventory()
	{
		
	}


	/* Accessors
	--===----------
	*/

	public Item getHeldItem()
	{
		Item out = null;

		if( this.hotbar != null && this.selection != -1 )
			out = this.hotbar[this.selection];

		return out;
	}


	/* Mutators
	--===---------
	*/

	public void setHotBarSlots(Item... items)
	{
		this.hotbar = items;
	}

	public void selectHotBar(int dex)
	{
		if( dex < 0 || dex > hotbar.length )
			throw new IndexOutOfBoundsException(dex + " for range (0-" + hotbar.length + ")");

		this.selection = dex;
	}

	public void setHotBarSlots(int size)
	{
		Item[] newBar = new Item[size];

		if( this.hotbar != null )
			for( int ii = 0; ii < newBar.length; ii++ )
				if( this.hotbar.length < ii )
					newBar[ii] = this.hotbar[ii];

		this.hotbar = newBar;
	}

	public Inventory withHotBarSlots(int size)
	{
		this.setHotBarSlots(size);
		return this;
	}
}