package com.ychstudio.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ychstudio.TownKeeper;
import com.ychstudio.ecs.systems.PhysicsSystem;
import com.ychstudio.ecs.systems.RenderSystem;
import com.ychstudio.ecs.systems.StateSystem;

public class PlayScreen implements Screen {

	private final TownKeeper game;
	private final SpriteBatch batch;

	private World world;
	private Engine engine;

	private FitViewport viewport;
	private OrthographicCamera camera;

	public PlayScreen(TownKeeper game) {
		this.game = game;
		this.batch = game.getBatch();
	}

	@Override
	public void show() {

		camera = new OrthographicCamera();
		viewport = new FitViewport(20f, 15f, camera);

		world = new World(new Vector2(0, 9.8f), true);
		engine = new Engine();
		
		engine.addSystem(new PhysicsSystem());
		engine.addSystem(new StateSystem());
		engine.addSystem(new RenderSystem(batch));

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);

		world.step(Math.min(delta, 1 / 60f), 8, 3);
		engine.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		world.dispose();
	}

}
