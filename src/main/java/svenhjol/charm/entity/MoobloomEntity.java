package svenhjol.charm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.world.World;

public class MoobloomEntity extends CowEntity {
    public MoobloomEntity(EntityType<? extends CowEntity> entityType, World world) {
        super(entityType, world);
    }

    public enum Type {
        ALLIUM,
        AZURE,
        CORNFLOWER,
        DANDELION,
        LILY_OF_THE_VALLEY,
        ORCHID,
        OXEYE_DAISY,
        POPPY,
        TULIP,
        WITHER_ROSE
    }
}
