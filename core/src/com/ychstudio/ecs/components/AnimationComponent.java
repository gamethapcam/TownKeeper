package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;

public class AnimationComponent implements Component {

    private IntMap<Animation> animations;

    public AnimationComponent() {
        animations = new IntMap<>();
    }

    public void putAnimation(int key, Animation animation) {
        animations.put(key, animation);
    }

    public Animation getAnimation(int key) {
        return animations.get(key);
    }
}
