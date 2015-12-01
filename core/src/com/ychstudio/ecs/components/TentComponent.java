package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TentComponent implements Component {

    public float spawnTime; // time span to spawn a new villager
    public float countDown; // counting time left to spawn a new villager
    public int maxVillagers; // max number of villagers the tent can hold
    public int currentVillagers; // current number of villagers the tent holds

    public Vector2 pos = new Vector2();

    public TentComponent(Vector2 pos) {
        this(pos, 10f, 3);
    }

    public TentComponent(Vector2 pos, float spawnTime, int maxVillagers) {
        this.spawnTime = spawnTime;
        this.countDown = spawnTime;
        this.maxVillagers = maxVillagers;
        this.currentVillagers = 0;
        this.pos.set(pos);
    }

    public boolean isFull() {
        return currentVillagers == maxVillagers;
    }

    public boolean timeUp() {
        return countDown <= 0;
    }

    public boolean canSpawn() {
        return !isFull() && timeUp();
    }

    public void resetCountDown() {
        countDown = spawnTime;
    }

}
