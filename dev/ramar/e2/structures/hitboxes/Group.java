package dev.ramar.e2.structures.hitboxes;


import dev.ramar.e2.interfaces.collision.HitManager;
import dev.ramar.e2.interfaces.collision.Hittable;

import dev.ramar.e2.structures.StaticInfo;

import dev.ramar.e2.backend.Moment;

import java.util.*;

public class Group
{

	private String name;

	private List<Hittable> members = new ArrayList<>();

	// white/black list for collideable groups
	private List<String> list = new ArrayList<>();
	private boolean whitelist = true;


	public Group(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return name;
	}

	public List<Hittable> getMembers()
	{
		return members;
	}

	public Hittable getNMember(int n)
	{
		return members.get(n);
	}

	public boolean areNamesWhitelist()
	{
		return whitelist;
	}

	public List<String> getGroupNames()
	{
		return list;
	}

	public String getName()
	{
		return name;
	}

	public void addHittable(Hittable h)
	{
		members.add(h);
	}

	public void removeHittable(Hittable h)
	{
		members.remove(h);
		clearAllCollisionsFromHittable(h);
	}

	public void setWhitelist()
	{
		whitelist = true;
	}

	public void setBlacklist()
	{
		whitelist = false;
	}


	public void addGroupToList(String name)
	{
		list.add(name);
	}

	// set this up here so we don't constantly re-create memory
	// when we update
	private Hittable[] smallBigTest = new Hittable[2]; 


	/*
	Method: doCollision (no args)
	 - Does collision for every member inside this Group
	*/
	public void doCollision()
	{
		HitManager hm = StaticInfo.Objects.getHitManager();

		if( !list.isEmpty() )
		{
			int ii = 0;
			while(ii < members.size())
			{
				Hittable hitter = getNMember(ii);
				for( Group g : hm.getGroups(list, whitelist) )
				{
					int jj = 0;
					while(jj < g.members.size() )
					{
						Hittable target = g.getNMember(jj);
						if( hitter.collides(target) )
						{
							hitter.onCollision(target);
							target.onCollision(hitter);

						}
						else
						{
							target.onNoCollision(hitter);
							hitter.onNoCollision(target);
						}
						jj++;
					}					
				}
				ii++;
			}
		}
	}


	/*
	Method: doCollision (Hittable)
	 - Does collision for the given member <collider>
	*/
	public void doCollision(Hittable collider)
	{
		HitManager hm = StaticInfo.Objects.getHitManager();

		boolean colliderCollided = false;
		for( Group g : hm.getGroups(list, whitelist) )
		{
			int ii = 0;
			while(ii < g.members.size())
			{
				Hittable h = g.getNMember(ii);
				boolean collides = collider.collides(h);
				if(! colliderCollided )
					colliderCollided = collides;

				if( collides )
				{
					collider.onCollision(h);
					h.onCollision(collider);

				}
				else
				{
					h.onNoCollision(collider);
					collider.onNoCollision(h);
				}
				ii++;
			}
		}
	}

	private void clearAllCollisionsFromHittable(Hittable collider)
	{
		HitManager hm = StaticInfo.Objects.getHitManager();

		boolean colliderCollided = false;
		for( Group g : hm.getGroups(list, whitelist) )
		{
			int ii = 0;
			while(ii < g.members.size())
			{
				Hittable h = g.getNMember(ii);
				
				h.onNoCollision(collider);

				ii++;
			}
		}
	}


}