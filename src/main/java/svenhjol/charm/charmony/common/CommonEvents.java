package svenhjol.charm.charmony.common;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charm.charmony.Charmony;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.callback.BlockBreakSpeedCallback;
import svenhjol.charm.charmony.callback.EntityTickCallback;
import svenhjol.charm.charmony.callback.PlayerLoginCallback;
import svenhjol.charm.charmony.callback.PlayerTickCallback;
import svenhjol.charm.charmony.event.*;

import javax.annotation.Nullable;

public final class CommonEvents {
    private static final Log LOGGER = new Log(Charmony.ID, "CommonEvents");
    private static boolean initialized = false;

    private final CommonLoader loader;
    private final CommonRegistry registry;

    public CommonEvents(CommonLoader loader) {
        this.loader = loader;
        this.registry = loader.registry();

        // Ensures global events are set up.
        runOnce();
    }

    public static void runOnce() {
        if (initialized) return;

        // These are global Fabric events that any mod/feature can observe.
        AttackEntityCallback.EVENT.register(CommonEvents::handleAttackEntity);
        PlayerBlockBreakEvents.BEFORE.register(CommonEvents::handleBlockBreak);
        BlockBreakSpeedCallback.EVENT.register(CommonEvents::handleBlockBreakSpeed);
        EntityTickCallback.EVENT.register(CommonEvents::handleEntityTick);
        ServerLivingEntityEvents.AFTER_DEATH.register(CommonEvents::handleDeathEvent);
        LootTableEvents.MODIFY.register(CommonEvents::handleLootTableModify);
        PlayerLoginCallback.EVENT.register(CommonEvents::handlePlayerLogin);
        PlayerTickCallback.EVENT.register(CommonEvents::handlePlayerTick);
        ServerEntityEvents.ENTITY_LOAD.register(CommonEvents::handleServerEntityLoad);
        ServerEntityEvents.ENTITY_UNLOAD.register(CommonEvents::handleServerEntityUnload);
        ServerWorldEvents.LOAD.register(CommonEvents::handleServerWorldLoad);
        UseBlockCallback.EVENT.register(CommonEvents::handleUseBlock);
        UseItemCallback.EVENT.register(CommonEvents::handleUseItem);
        UseEntityCallback.EVENT.register(CommonEvents::handleUseEntity);

        LOGGER.debug("Called runOnce");
        initialized = true;
    }

    public String id() {
        return registry.id();
    }

    private static InteractionResult handleAttackEntity(Player player, Level level, InteractionHand handle,
                                                        Entity entity, @Nullable EntityHitResult hitResult) {
        return EntityAttackEvent.INSTANCE.invoke(player, level, handle, entity, hitResult);
    }

    private static boolean handleBlockBreak(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        return BlockBreakEvent.INSTANCE.invoke(level, pos, state, player);
    }

    private static float handleBlockBreakSpeed(Player player, BlockState state, float originalSpeed) {
        return BlockBreakSpeedEvent.INSTANCE.invoke(player, state, originalSpeed);
    }

    private static void handleDeathEvent(LivingEntity entity, DamageSource damageSource) {
        EntityKilledEvent.INSTANCE.invoke(entity, damageSource);
    }

    private static void handleEntityTick(LivingEntity entity) {
        EntityTickEvent.INSTANCE.invoke(entity);
    }

    private static void handleLootTableModify(ResourceManager resourceManager, LootDataManager dataManager, ResourceLocation id, LootTable.Builder supplier, LootTableSource source) {
        LootTableModifyEvent.INSTANCE.invoke(id, source, dataManager, supplier);
    }

    private static void handlePlayerLogin(Player player) {
        PlayerLoginEvent.INSTANCE.invoke(player);
    }

    private static void handlePlayerTick(Player player) {
        PlayerTickEvent.INSTANCE.invoke(player);
    }

    private static void handleServerEntityLoad(Entity entity, ServerLevel serverLevel) {
        EntityJoinEvent.INSTANCE.invoke(entity, serverLevel);
    }

    private static void handleServerEntityUnload(Entity entity, Level level) {
        EntityLeaveEvent.INSTANCE.invoke(entity, level);
    }

    private static void handleServerWorldLoad(MinecraftServer server, ServerLevel level) {
        LevelLoadEvent.INSTANCE.invoke(server, level);
    }

    private static InteractionResult handleUseBlock(Player player, Level level, InteractionHand hand,
                                                    BlockHitResult hitResult) {
        return BlockUseEvent.INSTANCE.invoke(player, level, hand, hitResult);
    }

    private static InteractionResultHolder<ItemStack> handleUseItem(Player player, Level level, InteractionHand hand) {
        return ItemUseEvent.INSTANCE.invoke(player, level, hand);
    }

    private static InteractionResult handleUseEntity(Player player, Level level, InteractionHand hand, Entity entity,
                                                     @Nullable EntityHitResult hitResult) {
        return EntityUseEvent.INSTANCE.invoke(player, level, hand, entity, hitResult);
    }
}
