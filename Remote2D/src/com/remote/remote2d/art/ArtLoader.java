package com.remote.remote2d.art;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.io.R2DFileManager;
import com.remote.remote2d.io.R2DFileSaver;
import com.remote.remote2d.io.R2DType;
import com.remote.remote2d.io.R2DTypeCollection;
import com.remote.remote2d.io.R2DTypeString;
import com.remote.remote2d.particles.ParticleSystem;
import com.remote.remote2d.world.Map;

/**
 * Loads textures for you, and also caches them in case a texture is used a lot.
 * @author Flafla2
 *
 */
public class ArtLoader {
	
	private HashMap<String,Animation> animList;
	
	public ArtLoader()
	{
		animList = new HashMap<String,Animation>();
	}
	
	public Animation getAnimation(String s)
	{
		if(!R2DExists(s))
			return null;
		if(!animList.containsKey(s))
		{
			Log.debug("New animation added to list: "+s);
			Animation animation = new Animation(s);
			animList.put(s, animation);
		}
		return animList.get(s);
	}
	
	public boolean textureExists(String s)
	{
		String x = Remote2D.getJarPath().getPath().toString()+s;
		File f = new File(x);

		if(f.exists() && f.isFile() && f.getName().endsWith(".png"))
			return true;
		else
			return false;
	}
	
	public boolean R2DExists(String s)
	{
		String x = Remote2D.getJarPath().getPath().toString()+s;
		File f = new File(x);

		if(f.exists() && f.isFile() && (f.getName().endsWith(Entity.getExtension()) || f.getName().endsWith(Animation.getExtension()) || f.getName().endsWith(ParticleSystem.getExtension()) || f.getName().endsWith(Map.getExtension())))
			return true;
		else
			return false;
	}
	
}
