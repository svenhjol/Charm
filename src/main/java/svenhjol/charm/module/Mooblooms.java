package svenhjol.charm.module;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.MoobloomsClient;
import svenhjol.charm.entity.MoobloomEntity;

@Module(mod = Charm.MOD_ID, client = MoobloomsClient.class)
public class Mooblooms extends CharmModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "moobloom");
    public static EntityType<MoobloomEntity> MOOBLOOM;

    @Override
    public void register() {
        MOOBLOOM = RegistryHandler.entity(ID, FabricEntityTypeBuilder
            .create(SpawnGroup.CREATURE, MoobloomEntity::new)
            .dimensions(EntityDimensions.fixed(0.9F, 1.4F))
            .trackRangeBlocks(10)
            .build());
    }
}
