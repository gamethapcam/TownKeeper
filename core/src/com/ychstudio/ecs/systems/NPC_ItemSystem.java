package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.ychstudio.ecs.components.NPC_ItemComponent;
import com.ychstudio.ecs.components.RigidBodyComponent;

public class NPC_ItemSystem extends IteratingSystem {

    protected ComponentMapper<NPC_ItemComponent> npc_ItemComponentM = ComponentMapper.getFor(NPC_ItemComponent.class);
    protected ComponentMapper<RigidBodyComponent> rigidBodyM = ComponentMapper.getFor(RigidBodyComponent.class);
    
    public NPC_ItemSystem() {
        super(Family.all(NPC_ItemComponent.class, RigidBodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        NPC_ItemComponent npc_ItemComponent= npc_ItemComponentM.get(entity);
        RigidBodyComponent rigidBody = rigidBodyM.get(entity);
        Body body = rigidBody.body;
        
        System.out.println("npc item system working");
    }

}
