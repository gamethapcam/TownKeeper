package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;

public class AnimalComponent implements Component {

    public static enum Kind {
        CHICKEN, COW, SHEEP;

        public static Kind getRandomKind() {
            return Kind.values()[MathUtils.random(Kind.values().length - 1)];
        }
    }
    
    public static final int IDLE = 0;
    public static final int MOVE_UP = 1;
    public static final int MOVE_DOWN = 2;
    public static final int MOVE_LEFT = 3;
    public static final int MOVE_RIGHT = 4;

    private Kind kind;

    public static float radius = 0.48f;

    public FarmComponent farm;

    public float speed = 1f;
    public float maxSpeed = 2f;

    private float randomTimer;

    public AnimalComponent(FarmComponent farm) {
        this(farm, Kind.getRandomKind());
    }

    public AnimalComponent(FarmComponent farm, Kind kind) {
        this.farm = farm;
        this.kind = kind;

        randomTimer = MathUtils.randomTriangular(1f, 3f);
    }

    public Kind getKind() {
        return kind;
    }

    public boolean isRandomTimerUp() {
        return randomTimer <= 0;
    }

    public void updateRandomTimer(float delta) {
        randomTimer -= delta;
    }

    public void resetRandomTimer() {
        randomTimer = MathUtils.random(1f, 5f);
    }

    public void makeRandomTimerUp() {
        randomTimer = 0;
    }

}
