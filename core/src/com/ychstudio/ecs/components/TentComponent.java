package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TentComponent implements Component {

    public float spawnTime; // time span to spawn a new citizen
    public float countDown; // counting time left to spawn a new citizen
    public int maxCitizens; // max number of citizens the tent can hold
    public int currentCitizens; // current number of citizens the tent holds

    public Vector2 pos = new Vector2();

    public TentComponent(Vector2 pos) {
        this(pos, 10f, 3);
    }

    public TentComponent(Vector2 pos, float spawnTime, int maxCitizens) {
        this.spawnTime = spawnTime;
        this.countDown = spawnTime;
        this.maxCitizens = maxCitizens;
        this.currentCitizens = 0;
        this.pos.set(pos);
    }

    public boolean isFull() {
        return currentCitizens == maxCitizens;
    }

}
