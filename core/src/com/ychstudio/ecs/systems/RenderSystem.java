package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ychstudio.ecs.components.RendererComponent;
import com.ychstudio.ecs.components.TransformComponent;

public class RenderSystem extends IteratingSystem {

    protected ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);
    protected ComponentMapper<RendererComponent> rendererM = ComponentMapper.getFor(RendererComponent.class);

    private SpriteBatch batch;

    public RenderSystem(SpriteBatch batch) {
	super(Family.all(TransformComponent.class, RendererComponent.class).get());
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
	TransformComponent transform = transformM.get(entity);
	RendererComponent renderer = rendererM.get(entity);
	renderer.sprite.setPosition(transform.x - renderer.getWidth() / 2, transform.y - renderer.getHeight() / 2);
	renderer.sprite.setRotation(transform.rot);
	renderer.sprite.setScale(transform.sclX, transform.sclY);
	renderer.sprite.draw(batch);
    }

}
