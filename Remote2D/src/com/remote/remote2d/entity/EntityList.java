package com.remote.remote2d.entity;

import java.util.ArrayList;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.entity.component.Component;

public class EntityList {
	
	/**
	 * The actual entity list.
	 */
	private ArrayList<Entity> entityList;
	
	/**
	 * A big list of all of the Entities that are available.  When spawning an Entity into
	 * the world, you need to add it to the entity list if you expect it to get tick()ed
	 * or render()ed.
	 */
	
	public EntityList()
	{
		 entityList = new ArrayList<Entity>();
	}
	
	public void addEntityToList(Entity e)
	{
		entityList.add(e);
		e.spawnEntityInWorld();
	}
	
	public void removeEntityFromList(Entity e)
	{
		for(int i=0;i<entityList.size();i++)
		{
			if(entityList.get(i).equals(e))
			{
				entityList.remove(i);
			}
		}
	}
	
	public void reloadTextures()
	{
		for(int i=0;i<entityList.size();i++)
		{
			entityList.get(i).reloadTextures();
		}
	}
	
	public void render(boolean editor)
	{
		for(int i=0;i<entityList.size();i++)
		{
			ArrayList<Component> components = entityList.get(i).getComponents();
			for(int x=0;x<components.size();x++)
				components.get(x).renderBefore(editor);
			entityList.get(i).render(editor);
			for(int x=components.size()-1;x>=0;x--)
				components.get(x).renderAfter(editor);
		}
	}
	
	public void tick(int i, int j, int k, double delta)
	{
		for(int x=0;x<entityList.size();x++)
		{
			if(!entityList.get(x).isStatic)
			{
				ArrayList<Component> components = entityList.get(x).getComponents();
				for(int y=0;y<components.size();y++)
					components.get(y).tick(i, j, k, delta);
			}
		}
	}
	
	public void set(int x, Entity e)
	{
		entityList.set(x, e);
	}
	
	public int size()
	{
		return entityList.size();
	}
	
	public int indexOf(Entity e)
	{
		return entityList.indexOf(e);
	}
	
	public Entity get(int index)
	{
		return entityList.get(index);
	}
	
	public EntityList clone()
	{
		EntityList newList = new EntityList();
		for(int x=0;x<entityList.size();x++)
		{
			newList.addEntityToList(entityList.get(x).clone());
		}
		return newList;
	}

	public void clear() {
		entityList.clear();
	}
	
}