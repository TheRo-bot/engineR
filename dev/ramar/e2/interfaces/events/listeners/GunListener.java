package dev.ramar.e2.interfaces.events.listeners;

import dev.ramar.e2.structures.items.weapons.guns.*;

/*
Interface: GunListener
 - A more advanced callback for anything that can use a gun
 - Essentially any action a Gun has that needs more context will be here
*/
public interface GunListener extends ItemHolder
{
	public void fireShot(Bullet b);
}