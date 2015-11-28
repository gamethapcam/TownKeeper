package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Renderer implements Component {

	public TextureRegion textureRegion;
	
	public Renderer(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
	
	public float getWidth() {
		if (textureRegion == null) {
			return 0;
		}
		return textureRegion.getRegionWidth();
	}
	
	public float getHeight() {
		if (textureRegion == null) {
			return 0;
		}
		return textureRegion.getRegionHeight();
	}
}
