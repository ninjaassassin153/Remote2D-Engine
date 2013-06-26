package com.remote.remote2d.gui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.logic.Vector2D;

public class GuiTextField extends Gui {
	
	public Vector2D pos;
	public Vector2D dim;
	public int fontsize;
	public int maxLength = -1;
	public String text = "";
	public String prefix = "";
	public String defaultText = "";
	
	public TextLimiter limitToDigits = TextLimiter.FULL;
	private String hexList = "0123456789abcdef";
	
	private boolean isSelected = false;
	private boolean hasTyped = false;
	private boolean blink = true;
	private int blinkTimer = 50;
	private int backTimer = 20;
	
	
	public GuiTextField(Vector2D pos, Vector2D dim, int fontsize)
	{
		this.pos = pos;
		this.dim = dim;
		this.fontsize = fontsize;
	}

	@Override
	public void tick(int i, int j, int k, double delta) {
		hasTyped = false;
		if(pos.getColliderWithDim(dim).isPointInside(new Vector2D(i,j)) && Remote2D.getInstance().hasMouseBeenPressed())
		{
			isSelected = true;
			blink = true;
			blinkTimer = 50;
		} else if(Remote2D.getInstance().hasMouseBeenPressed())
		{
			isSelected = false;
		}
		if(blinkTimer == 0)
		{
			blinkTimer = 50;
			blink = !blink;
		}
		else
			blinkTimer--;
		
		if(isSelected)
		{
			ArrayList<Character> typedChars = Remote2D.getInstance().getLimitedKeyboardList();
			for(int x=0;x<typedChars.size();x++)
			{
				char key = typedChars.get(x);
				if((Character.isDigit(key) || limitToDigits == TextLimiter.FULL || (key == '.' && limitToDigits == TextLimiter.LIMIT_TO_FLOAT) || (hexList.contains(""+key) && limitToDigits == TextLimiter.LIMIT_TO_HEX)  || key == '\b') && (text.length() < maxLength || maxLength == -1) || key == '\b')
				{
					hasTyped = true;
					if(key == '\b' && text.length() != 0)
						text = text.substring(0, text.length() - 1);
					else if(key != '\b')
						text += key;
				}
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_BACK))
			{
				if(backTimer == 0)
				{
					if(text.length() != 0)
						text = text.substring(0, text.length() - 1);
				} else
					backTimer--;
			}else
				backTimer = 20;
			
		}
	}

	@Override
	public void render() {
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0, 0, 0, 1);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(pos.x, pos.y);
			GL11.glVertex2f(pos.x+dim.x, pos.y);
			GL11.glVertex2f(pos.x+dim.x, pos.y+dim.y);
			GL11.glVertex2f(pos.x, pos.y+dim.y);
		GL11.glEnd();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glVertex2f(pos.x, pos.y);
			GL11.glVertex2f(pos.x+dim.x, pos.y);
			GL11.glVertex2f(pos.x+dim.x, pos.y+dim.y);
			GL11.glVertex2f(pos.x, pos.y+dim.y);
			GL11.glVertex2f(pos.x, pos.y);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		String s = prefix+text;
		if(!s.equals(""))
		{
			int[] size = Fonts.get("Arial").getStringDim(s, fontsize);
			while(size[0] > dim.x-20)
			{
				s = s.substring(1, s.length());
				size = Fonts.get("Arial").getStringDim(s, fontsize);
			}
		}
		
		int yPos = pos.y+dim.y/2-Fonts.get("Arial").getStringDim(text, fontsize)[1]/2;
		Fonts.get("Arial").drawString(s+((isSelected && blink) ? "|" : "")+" ", pos.x+10, yPos, fontsize, 0xffffff);
		if(text.equals("") && !isSelected)
			Fonts.get("Arial").drawString(defaultText, pos.x+10, yPos, fontsize, 0x777777);
	}
	
	public boolean hasText()
	{
		return !text.equals("");
	}
	
	public void deselect()
	{
		isSelected = false;
	}
	
	public boolean hasTyped()
	{
		return hasTyped;
	}
	
	public boolean isSelected()
	{
		return isSelected;
	}
	
}