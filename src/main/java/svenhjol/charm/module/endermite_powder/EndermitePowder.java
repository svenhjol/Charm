package svenhjol.charm.module.endermite_powder;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.EntityDropItemsCallback;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Endermites drop endermite powder that can be used to locate an End City.")
public class EndermitePowder extends CharmModule {
    public static ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "endermite_powder");
    public static EntityType<EndermitePowderEntity> ENTITY;
    public static EndermitePowderItem ENDERMITE_POWDER;

    public static double lootingBoost = 0.3D;

    @Config(name = "Maximum drops", description = "Maximum endermite powder dropped when endermite is killed.")
    public static int maxDrops = 2;

    @Override
    public void register() {
        ENDERMITE_POWDER = new EndermitePowderItem(this);

        // setup and register the entity
        ENTITY = RegistryHelper.entity(ID, FabricEntityTypeBuilder
            .<EndermitePowderEntity>create(MobCategory.MISC, EndermitePowderEntity::new)
            .trackRangeBlocks(80)
            .trackedUpdateRate(10)
            .dimensions(EntityDimensions.fixed(2.0F, 2.0F)));
    }

    @Override
    public void runWhenEnabled() {
        // react to entity drops
        EntityDropItemsCallback.AFTER.register(this::tryDrop);
    }

    private InteractionResult tryDrop(Entity entity, DamageSource source, int lootingLevel) {
        if (!entity.level.isClientSide && entity instanceof Endermite) {
            Level world = entity.getCommandSenderWorld();
            BlockPos pos = entity.blockPosition();
            int amount = ItemHelper.getAmountWithLooting(world.random, maxDrops, lootingLevel, (float)lootingBoost);
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ENDERMITE_POWDER, amount)));
        }
        return InteractionResult.PASS;
    }

    public static void triggerAdvancement(ServerPlayer playerEntity) {
        CharmAdvancements.ACTION_PERFORMED.trigger(playerEntity, new ResourceLocation(Charm.MOD_ID, "used_endermite_powder"));
    }
}
