package svenhjol.charm.module.endermite_powder;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
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
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.EntityDropItemsCallback;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.lib.Advancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.extra_wandering_trades.ExtraWanderingTrades;
import svenhjol.charm.registry.CommonRegistry;

@CommonModule(mod = Charm.MOD_ID, description = "Endermites drop endermite powder that can be used to locate an End City.")
public class EndermitePowder extends CharmModule {
    public static ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "endermite_powder");
    public static EntityType<EndermitePowderEntity> ENTITY;
    public static EndermitePowderItem ENDERMITE_POWDER;

    public static SoundEvent LAUNCH_SOUND;

    public static double lootingBoost = 0.3D;

    @Config(name = "Maximum drops", description = "Maximum endermite powder dropped when endermite is killed.")
    public static int maxDrops = 2;

    @Override
    public void register() {
        ENDERMITE_POWDER = new EndermitePowderItem(this);
        LAUNCH_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "endermite_powder_launch"));

        // setup and register the entity
        ENTITY = CommonRegistry.entity(ID, FabricEntityTypeBuilder
            .<EndermitePowderEntity>create(MobCategory.MISC, EndermitePowderEntity::new)
            .trackRangeBlocks(80)
            .trackedUpdateRate(10)
            .dimensions(EntityDimensions.fixed(2.0F, 2.0F)));
    }

    @Override
    public void runWhenEnabled() {
        EntityDropItemsCallback.AFTER.register(this::tryDrop);
        ExtraWanderingTrades.registerRareItem(ENDERMITE_POWDER, 3, 12);
    }

    private InteractionResult tryDrop(Entity entity, DamageSource source, int lootingLevel) {
        if (!entity.level.isClientSide && entity instanceof Endermite) {
            Level level = entity.getCommandSenderWorld();
            BlockPos pos = entity.blockPosition();
            int amount = ItemHelper.getAmountWithLooting(level.random, maxDrops, lootingLevel, (float)lootingBoost);
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ENDERMITE_POWDER, amount)));
        }
        return InteractionResult.PASS;
    }

    public static void triggerAdvancement(ServerPlayer playerEntity) {
        Advancements.triggerActionPerformed(playerEntity, new ResourceLocation(Charm.MOD_ID, "used_endermite_powder"));
    }
}
