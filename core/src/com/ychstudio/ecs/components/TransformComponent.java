package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component {
	
	public float x, y;
	public float rot;
	
	public float sclX, sclY;
	
	public TransformComponent() {
		this(0, 0, 0);
	}
	
	public TransformComponent(float x, float y, float rot) {
		this(x, y, rot, 1f, 1f);
	}
	
	public TransformComponent(float x, float y, float rot, float sclX, float sclY) {
		this.x = x;
		this.y = y;
		this.rot = rot;
		this.sclX = sclX;
		this.sclY = sclY;
	}
	
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setPos(Vector2 pos) {
		x = pos.x;
		y = pos.y;
	}
	
	
}
