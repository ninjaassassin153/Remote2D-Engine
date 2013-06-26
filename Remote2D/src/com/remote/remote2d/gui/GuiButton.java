package com.remote.remote2d.gui;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.logic.Vector2D;

public class GuiButton extends Gui {
	
	/**
	 * The ID number of the button.  This is used in GuiMenu's actionPerformed(GuiButton)
	 * to identify the button. 
	 */
	public int id;
	/**
	 * The state of the button.  0 = disabled, 1 = idle, and 2/3 = selected
	 */
	int selectState = 1;
	int selectColor = 2;
	Vector2D pos;
	Vector2D dim;
	public String text;
	Texture tex;
	
	/**
	 * A basic button class.  Click it, and something happens.  Buttons are built into
	 * the GUI class with a hook structure.
	 */
	public GuiButton(int id, Vector2D pos, Vector2D dim, String text)
	{
		this.id = id;
		this.pos = pos;
		this.dim = dim;
		this.text = text;
		
		tex = Remote2D.getInstance().artLoader.getTexture("/res/gui/controls.png");
	}
	
	public void render()
	{
		renderControlElement(tex,pos,dim,selectState,0);
		
		int[] texdim = Fonts.get("Arial").getStringDim(text, 20);
		Fonts.get("Arial").drawString(text, pos.x+dim.x/2-texdim[0]/2, pos.y+dim.y/2-texdim[1]/2, 20, 0x000000);
	}
	
	public GuiButton setDisabled(boolean disabled)
	{
		if(disabled == (selectState==0))
			return this;
		if(disabled)
			selectState = 0;
		else
			selectState = 1;
		
		return this;
	}
	
	public boolean getDisabled()
	{
		return selectState == 0;
	}
	
	public static void renderControlElement(Texture tex, Vector2D pos, Vector2D dim, int color, int size)
	{
		Vector2D[] coords = getControlImageCoords(color,size);
		
		renderTextureWithCoords(
				tex,
				pos,
				new Vector2D(5,dim.y),
				coords[0],
				coords[1]);
		renderTextureWithCoords(
				tex,
				new Vector2D(pos.x+5,pos.y),
				new Vector2D(dim.x-10,dim.y),
				coords[2],
				coords[3]);
		renderTextureWithCoords(
				tex,
				new Vector2D(pos.x+dim.x-5,pos.y),
				new Vector2D(5,dim.y),
				coords[4],
				coords[5]);
	}
	
	public static Vector2D[] getControlImageCoords(int color, int size)
	{
		Vector2D[] coords = new Vector2D[6];
		
		int posX = color*40;
		int posY = 0;
		
		if(size != 0)
		{
			posX += 20;
			if(size == 2)
				posY = 20;
		}
		
		int dimY = 40;
		if(size == 0)
			dimY = 40;
		else if(size == 1)
			dimY = 20;
		else if(size == 2)
			dimY = 10;
		
		coords[0] = new Vector2D(posX,posY);
		coords[1] = new Vector2D(5,dimY);
		
		coords[2] = new Vector2D(posX+5,posY);
		coords[3] = new Vector2D(10,dimY);
		
		coords[4] = new Vector2D(posX+15,posY);
		coords[5] = new Vector2D(5,dimY);
		
		return coords;
	}
	
	public void tick(int i, int j, int k, double delta)
	{
		if(i > pos.x && j > pos.y && i < pos.x+dim.x && j < pos.y+dim.y && selectState != 0)
		{
			selectState = selectColor;
		} else if(selectState != 0)
		{
			selectState = 1;
		}
	}

}