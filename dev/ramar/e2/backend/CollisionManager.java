package dev.ramar.e2.backend;

import dev.ramar.e2.backend.Moment;

import dev.ramar.e2.interfaces.collision.HitManager;
import dev.ramar.e2.interfaces.collision.Hittable;

import dev.ramar.e2.structures.StaticInfo;
import dev.ramar.e2.structures.hitboxes.Group;


import java.util.*;

public class CollisionManager implements HitManager
{
	private Map<String, Group> groups = java.util.Collections.synchronizedMap(new HashMap<String, Group>());


	public CollisionManager()
	{

	}


	// sets <g> in groups using Group.getName(), ONLY IF !groups.containsKey(<name>)
	public void registerGroup(Group g)
	{
		if( groups.containsKey(g.getName()) )
			throw new IllegalArgumentException("Group '" + g.getName() + "' already exists, can't override");

		groups.put(g.getName(), g);
	}


	// ensures the group <name> exists
	public void registerGroup(String name)
	{
		if(! groups.containsKey(name) )
		{
			groups.put(name, new Group(name));
		}

	}


	public void unregisterGroup(Group g)
	{
		groups.remove(g.getName());
	}


	public Group getGroup(String name)
	{
		Group group = groups.get(name);
		if( group == null )
		{
			group = new Group(name);
			groups.put(name, group);
		}

		return group;
	}


	public List<Group> getGroups(List<String> names, boolean whitelist)
	{
		List<Group> exportGroups = new ArrayList<>();

		if( whitelist )
		{
			for( String s : names )
				if( groups.containsKey(s) )
					exportGroups.add(groups.get(s));
		}

		else
		{
			for( String s : groups.keySet() )
				if(! names.contains(s) )
					exportGroups.add(groups.get(s));
		}

		return exportGroups;
	}


	public void registerHittable(Hittable h, String groupName)
	{
		Group g = getGroup(groupName);
		g.addHittable(h);
	}


	public void unregisterHittable(Hittable h, String groupName)
	{
		groups.get(groupName).removeHittable(h);
	}


	public void clearHittable(Hittable h)
	{
		for( String s : groups.keySet() )
		{
			Group thisG = groups.get(s);

			thisG.removeHittable(h);
		}
	}

}