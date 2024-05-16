package svenhjol.charm.feature.core.custom_wood.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.foundation.block.CharmChestBlock;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.item.CharmHangingSignItem;
import svenhjol.charm.foundation.item.CharmSignItem;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public final class Handlers extends FeatureHolder<CustomWood> {
    public final Map<Boat.Type, Supplier<BoatItem>> BOAT_ITEM = new HashMap<>();
    public final Map<Boat.Type, Supplier<BoatItem>> CHEST_BOAT_ITEM = new HashMap<>();
    public final Map<Boat.Type, ResourceLocation> BOAT_PLANKS = new HashMap<>();
    public final Map<String, Map<CustomType, List<Supplier<? extends Item>>>> CREATIVE_TAB_ITEMS = new HashMap<>();
    public final List<Supplier<CharmSignItem>> SIGN_ITEMS = new ArrayList<>();
    public final List<Supplier<CharmHangingSignItem>> HANGING_SIGN_ITEMS = new ArrayList<>();

    public Handlers(CustomWood feature) {
        super(feature);
    }

    @SuppressWarnings("deprecation")
    public void levelLoad(MinecraftServer server, ServerLevel level) {
        if (level.dimension() == Level.OVERWORLD) {
            // Set each boat type's planks.
            getBoatPlanks().forEach(
                (type, id) -> BuiltInRegistries.BLOCK.getOptional(id).ifPresent(
                    block -> type.planks = block));

            // Set each sign's block and item.
            getSignItems().forEach(supplier -> {
                var sign = supplier.get();
                sign.wallBlock = sign.getWallSignBlock().get();
                sign.wallBlock.item = sign;
                sign.block = sign.getSignBlock().get();
                sign.block.item = sign;
            });

            // Set each hanging sign's block and item.
            getHangingSignItems().forEach(supplier -> {
                var sign = supplier.get();
                sign.wallBlock = sign.getWallSignBlock().get();
                sign.wallBlock.item = sign;
                sign.block = sign.getHangingBlock().get();
                sign.block.item = sign;
            });
        }
    }

    public InteractionResult animalInteraction(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity instanceof AbstractChestedHorse horse) {
            var held = player.getItemInHand(hand);
            var item = held.getItem();
            var block = Block.byItem(item);

            if (block instanceof CharmChestBlock
                && horse.isTamed()
                && !horse.hasChest()
                && !horse.isBaby()
            ) {
                var random = horse.getRandom();
                horse.setChest(true);
                horse.playSound(SoundEvents.DONKEY_CHEST, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);

                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }

                horse.createInventory();
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    public void addCreativeTabItem(String modId, CustomType customType, Supplier<? extends Item> item) {
        CREATIVE_TAB_ITEMS.computeIfAbsent(modId, m -> new LinkedHashMap<>())
            .computeIfAbsent(customType, a -> new LinkedList<>()).add(item);
    }

    public Map<String, Map<CustomType, List<Supplier<? extends Item>>>> getCreativeTabItems() {
        return CREATIVE_TAB_ITEMS;
    }

    public void setItemForBoat(Boat.Type type, Supplier<BoatItem> item) {
        BOAT_ITEM.put(type, item);
    }

    public void setItemForChestBoat(Boat.Type type, Supplier<BoatItem> item) {
        CHEST_BOAT_ITEM.put(type, item);
    }

    public void setPlanksForBoat(Boat.Type type, ResourceLocation id) {
        BOAT_PLANKS.put(type, id);
    }

    public void addSignItem(Supplier<CharmSignItem> item) {
        if (!SIGN_ITEMS.contains(item)) {
            SIGN_ITEMS.add(item);
        }
    }

    public void addHangingSignItem(Supplier<CharmHangingSignItem> item) {
        if (!HANGING_SIGN_ITEMS.contains(item)) {
            HANGING_SIGN_ITEMS.add(item);
        }
    }

    @Nullable
    public BoatItem getBoatByType(Boat.Type boatType) {
        return BOAT_ITEM.getOrDefault(boatType, () -> null).get();
    }

    @Nullable
    public BoatItem getChestBoatByType(Boat.Type boatType) {
        return CHEST_BOAT_ITEM.getOrDefault(boatType, () -> null).get();
    }

    public List<Supplier<CharmSignItem>> getSignItems() {
        return SIGN_ITEMS;
    }

    public List<Supplier<CharmHangingSignItem>> getHangingSignItems() {
        return HANGING_SIGN_ITEMS;
    }

    public Map<Boat.Type, ResourceLocation> getBoatPlanks() {
        return BOAT_PLANKS;
    }
}
