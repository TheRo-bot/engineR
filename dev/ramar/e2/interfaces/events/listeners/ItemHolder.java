package dev.ramar.e2.interfaces.events.listeners;

import dev.ramar.e2.structures.items.Item;

/*
Interface: ItemHolder
 - Small Interface for callbacks with using an item
*/
public interface ItemHolder
{
	public void itemUseCallback(Item i);
}