package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;

public class VillagerComponent implements Component {

    public static int IDLE = 0;
    public static int WANDER = 1;
    public static int RECRUIT = 2;
    public static int DIE = 3;

    public static float radius = 0.48f;

    private int maxHp;
    private int hp;
    public TentComponent tent;

    public VillagerComponent(int maxHp, TentComponent tent) {
        this.maxHp = maxHp;
        this.tent = tent;
        hp = maxHp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean isFullHp() {
        return hp == maxHp;
    }

    public void takeDamage(int damage) {
        hp -= damage;
    }

    public void recoverHp(int recovery) {
        hp += recovery;
    }

}
