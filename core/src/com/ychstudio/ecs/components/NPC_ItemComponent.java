package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;
import com.ychstudio.jobsys.Job;

public class NPC_ItemComponent implements Component {
    
    public static final float radius = 0.36f;
    public Job job;
    
    private boolean taken;

    public NPC_ItemComponent(Job job) {
        this.job = job;
        taken = false;
    }
    
    public void taken() {
        taken = true;
    }
    
    public boolean isTaken() {
        return taken;
    }
}
