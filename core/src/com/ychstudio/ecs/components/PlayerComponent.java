package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {

    public static final int IDLE = 0;
    public static final int MOVE = 1;

    public float speed = 1f;
    public float maxSpeed = 2.2f;

}
