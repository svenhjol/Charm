package svenhjol.charm.module;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.ItemHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.EndermitePowderClient;
import svenhjol.charm.entity.EndermitePowderEntity;
import svenhjol.charm.event.EntityDropsCallback;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.item.EndermitePowderItem;

@Module(mod = Charm.MOD_ID, client = EndermitePowderClient.class, description = "Endermites drop endermite powder that can be used to locate an End City.")
public class EndermitePowder extends CharmModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "endermite_powder");
    public static EntityType<EndermitePowderEntity> ENTITY;
    public static EndermitePowderItem ENDERMITE_POWDER;

    public static double lootingBoost = 0.3D;

    @Config(name = "Maximum drops", description = "Maximum endermite powder dropped when endermite is killed.")
    public static int maxDrops = 2;

    @Override
    public void register() {
        ENDERMITE_POWDER = new EndermitePowderItem(this);

        // setup and register the entity
        ENTITY = RegistryHandler.entity(ID, FabricEntityTypeBuilder
            .<EndermitePowderEntity>create(SpawnGroup.MISC, EndermitePowderEntity::new)
            .trackRangeBlocks(80)
            .trackedUpdateRate(10)
            .dimensions(EntityDimensions.fixed(2.0F, 2.0F)));
    }

    @Override
    public void init() {
        // react to entity drops
        EntityDropsCallback.AFTER.register(this::tryDrop);
    }

    private ActionResult tryDrop(Entity entity, DamageSource source, int lootingLevel) {
        if (!entity.world.isClient && entity instanceof EndermiteEntity) {
            World world = entity.getEntityWorld();
            BlockPos pos = entity.getBlockPos();
            int amount = ItemHelper.getAmountWithLooting(world.random, maxDrops, lootingLevel, (float)lootingBoost);
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ENDERMITE_POWDER, amount)));
        }
        return ActionResult.PASS;
    }

    public static void triggerAdvancement(ServerPlayerEntity playerEntity) {
        CharmAdvancements.ACTION_PERFORMED.trigger(playerEntity, new Identifier(Charm.MOD_ID, "used_endermite_powder"));
    }
}
