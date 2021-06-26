package dev.ramar.e2.interfaces.collision;


import dev.ramar.e2.structures.hitboxes.Group;

import java.util.List;

/*
Interface: HitManager
 - An Interface for something which manages Collision
*/
public interface HitManager
{
	public void registerGroup(Group g);

	public void registerGroup(String name);

	public void unregisterGroup(Group g);

	public Group getGroup(String name);

	public List<Group> getGroups(List<String> names, boolean whitelist);

	public void registerHittable(Hittable h, String groupName);

	public void unregisterHittable(Hittable h, String groupName);

	public void clearHittable(Hittable h);


}