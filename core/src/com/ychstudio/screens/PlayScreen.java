package com.ychstudio.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ychstudio.TownKeeper;
import com.ychstudio.builders.WorldBuilder;
import com.ychstudio.ecs.systems.AnimationSystem;
import com.ychstudio.ecs.systems.PhysicsSystem;
import com.ychstudio.ecs.systems.PlayerSystem;
import com.ychstudio.ecs.systems.RenderSystem;
import com.ychstudio.ecs.systems.StateSystem;

import gamesys.GameManager;

public class PlayScreen implements Screen {

    private final TownKeeper game;
    private final SpriteBatch batch;

    private World world;
    private Engine engine;

    private FitViewport viewport;
    private OrthographicCamera camera;

    private Box2DDebugRenderer box2dDebugRenderer;
    private boolean showBox2DDebugRenderer;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    public PlayScreen(TownKeeper game) {
	this.game = game;
	this.batch = game.getBatch();
    }

    @Override
    public void show() {

	camera = new OrthographicCamera();
	viewport = new FitViewport(12f, 9f, camera);

	world = new World(new Vector2(), true);
	engine = new Engine();

	engine.addSystem(new PlayerSystem());
	engine.addSystem(new PhysicsSystem());
	engine.addSystem(new StateSystem());
	engine.addSystem(new AnimationSystem());
	engine.addSystem(new RenderSystem(batch));

	box2dDebugRenderer = new Box2DDebugRenderer();
	showBox2DDebugRenderer = true;

	WorldBuilder worldBuilder = WorldBuilder.getInstance(world, engine);
	tiledMap = worldBuilder.loadTiledMap("map1.tmx");
	tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / GameManager.PPM, batch);

	camera.translate(GameManager.playerPos);

    }

    @Override
    public void render(float delta) {
	handleInput();

	Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	batch.setProjectionMatrix(camera.combined);
	tiledMapRenderer.setView(camera);
	tiledMapRenderer.render();

	world.step(Math.min(delta, 1 / 60f), 8, 3);
	engine.update(delta);

	if (showBox2DDebugRenderer) {
	    box2dDebugRenderer.render(world, camera.combined);
	}
    }

    private void handleInput() {

	if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
	    showBox2DDebugRenderer = !showBox2DDebugRenderer;
	}

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
	tiledMapRenderer.dispose();
	box2dDebugRenderer.dispose();
    }

}
