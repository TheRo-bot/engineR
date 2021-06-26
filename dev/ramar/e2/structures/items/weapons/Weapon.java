package dev.ramar.e2.structures.items.weapons;


import dev.ramar.e2.structures.items.Item;

public abstract class Weapon extends Item
{
	protected void initialise()
	{

	}


	public Weapon(String name)
	{
		super(name);
		initialise();
	}



}