package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ychstudio.ecs.components.Renderer;
import com.ychstudio.ecs.components.Transform;

public class RenderSystem extends IteratingSystem {

	protected ComponentMapper<Transform> transformM = ComponentMapper.getFor(Transform.class);
	protected ComponentMapper<Renderer> rendererM = ComponentMapper.getFor(Renderer.class);

	private SpriteBatch batch;

	public RenderSystem(SpriteBatch batch) {
		super(Family.all(Transform.class, Renderer.class).get());
		this.batch = batch;
	}

	@Override
	public void update(float deltaTime) {
		batch.begin();
		super.update(deltaTime);
		batch.end();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Transform transform = transformM.get(entity);
		Renderer renderer = rendererM.get(entity);
		
		batch.draw(renderer.textureRegion, transform.x, transform.y, transform.x + renderer.getWidth() / 2f, transform.y + renderer.getHeight() / 2f, renderer.getWidth(), renderer.getHeight(), transform.sclX, transform.sclY, transform.rot, false);

	}

}
