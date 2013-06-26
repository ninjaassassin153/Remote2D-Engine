package com.remote.remote2d.gui.editor.inspector;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Animation;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.gui.GuiTextField;
import com.remote.remote2d.gui.TextLimiter;
import com.remote.remote2d.logic.Vector2D;

public class GuiEditorInspectorSectionAnimation extends GuiEditorInspectorSection {
	
	GuiTextField textField;

	public GuiEditorInspectorSectionAnimation(String name, Vector2D pos, int width) {
		super(name, pos, width);
		textField = new GuiTextField(pos.add(new Vector2D(10,20)), new Vector2D(width-20,20), 20);
	}

	@Override
	public int getHeight() {
		return 40;
	}

	@Override
	public Object getData() {
		return Remote2D.getInstance().artLoader.getAnimation(textField.text);
	}

	@Override
	public void initSection() {
		
	}

	@Override
	public void tick(int i, int j, int k, double delta) {
		textField.tick(i, j, k, delta);
	}

	@Override
	public void render() {
		Fonts.get("Arial").drawString(name, pos.x, pos.y, 20, 0xffffff);
		textField.render();
	}
	
	@Override
	public void setData(Object o) {
		if(o instanceof Animation)
		{
			textField.text = ((Animation)o).getPath();
		}
	}

	@Override
	public void deselect() {
		textField.deselect();
	}

	@Override
	public boolean isSelected() {
		return textField.isSelected();
	}

}