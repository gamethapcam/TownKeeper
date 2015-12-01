package com.ychstudio.builders;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.ychstudio.gamesys.GameManager;

public class WorldBuilder {

    private static final WorldBuilder instance = new WorldBuilder();

    private World world;
    private Engine engine;

    private TiledMap tiledMap;

    private WorldBuilder() {
    }

    public static WorldBuilder getInstance(World world, Engine engine) {
        instance.world = world;
        instance.engine = engine;
        return instance;
    }

    public TiledMap loadTiledMap(String mapFile) {
        tiledMap = GameManager.assetManager.get("maps/" + mapFile, TiledMap.class);
        ActorBuilder actorBuilder = ActorBuilder.getInstance(world, engine);

        MapLayers mapLayers = tiledMap.getLayers();

        // TODO: load static objects

        // TODO: load buildings

        // TODO: load animals

        // TODO: load enemies

        // TODO: load player

        MapLayer playerLayer = mapLayers.get("Player");
        MapObjects playerObjects = playerLayer.getObjects();
        Rectangle rectangle = ((RectangleMapObject) (playerObjects.get(0))).getRectangle();

        correctRectangle(rectangle);

        actorBuilder.createPlayer(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
        GameManager.playerSpawnPos.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);

        return tiledMap;
    }

    private void correctRectangle(Rectangle rectangle) {
        rectangle.x /= GameManager.PPM;
        rectangle.y /= GameManager.PPM;
        rectangle.width /= GameManager.PPM;
        rectangle.height /= GameManager.PPM;
    }

    public TiledMap geTiledMap() {
        return tiledMap;
    }

}
