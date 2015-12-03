package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ychstudio.ai.Node;

public class VillagerComponent implements Component {

    public static final int IDLE = 0;
    public static final int WANDER = 1;
    public static final int RECRUIT = 2;
    public static final int DIE = 3;

    public static float radius = 0.48f;

    public TentComponent tent;

    public float speed = 1f;
    public float maxSpeed = 2f;

    private float randomTimer;

    public Vector2 targetPos;
    public Vector2 targetDir;

    // current moving-toward position of A* path finding node
    public Node pathNode;

    public VillagerComponent(TentComponent tent) {
        this.tent = tent;

        randomTimer = MathUtils.random(1f, 5f);
        targetPos = new Vector2(tent.pos);
        targetDir = new Vector2();
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

    public void makeRandomTimerForever() {
        randomTimer = Float.MAX_VALUE;
    }

}
