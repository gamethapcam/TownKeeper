package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;
import com.ychstudio.jobsys.Job;

public class NPC_ItemComponent implements Component {
    
    public static final float radius = 0.36f;
    public Job job;

    public NPC_ItemComponent(Job job) {
        this.job = job;
    }
}
