package svenhjol.charm.module;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.client.EndermitePowderClient;
import svenhjol.charm.entity.EndermitePowderEntity;
import svenhjol.charm.item.EndermitePowderItem;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.event.EntityDropsCallback;
import svenhjol.charm.base.helper.ItemHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Endermites drop endermite powder that can be used to locate an End City.")
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
        // TODO "No data fixer registered for endermite_powder"
        ENTITY = EntityType.Builder.<EndermitePowderEntity>create(EndermitePowderEntity::new, SpawnGroup.MISC)
            .maxTrackingRange(80)
            .trackingTickInterval(10)
            .setDimensions(2.0F, 2.0F)
            .build(ID.getPath());

        Registry.register(Registry.ENTITY_TYPE, ID, ENTITY);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void clientRegister() {
        new EndermitePowderClient(this);
    }

    @Override
    public void init() {
        // react to entity drops
        EntityDropsCallback.EVENT.register(this::tryDrop);
    }

    private ActionResult tryDrop(Entity entity, DamageSource source, int lootingLevel) {
        if (!entity.world.isClient
            && entity instanceof EndermiteEntity
        ) {
            World world = entity.getEntityWorld();
            BlockPos pos = entity.getBlockPos();
            int amount = ItemHelper.getAmountWithLooting(world.random, maxDrops, lootingLevel, (float)lootingBoost);
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ENDERMITE_POWDER, amount)));
        }
        return ActionResult.PASS;
    }
}
