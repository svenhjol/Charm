package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.api.enums.TotemType;
import svenhjol.charm.api.event.AnvilUpdateEvent;
import svenhjol.charm.foundation.Globals;
import svenhjol.charm.foundation.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class CommonHandlers {
    static final Log LOGGER = new Log(Charm.ID, "TotemOfPreservingHandler");

    static InteractionResult handlePlayerInventoryDrop(Player player, Inventory inventory) {
        if (player.level().isClientSide) {
            return InteractionResult.PASS;
        }

        ItemStack found = ItemStack.EMPTY;
        var damage = 0; // Track how much damage the totem has taken
        var loader = Globals.common(Charm.ID);
        var serverPlayer = (ServerPlayer)player;

        // Get items to preserve.
        List<ItemStack> preserveItems = new ArrayList<>();
        for (var provider : TotemOfPreserving.preservingProviders) {
            preserveItems.addAll(provider
                .getInventoryItemsForTotem(player)
                .stream().filter(i -> !i.isEmpty())
                .toList());
        }

        // When not in grave mode, look through inventory items for the first empty totem of preserving.
        if (!TotemOfPreserving.graveMode) {
            var totemWorksFromInventory = loader.isEnabled("totems_work_from_inventory");

            if (!totemWorksFromInventory) {
                for (var held : player.getHandSlots()) {
                    if (!held.is(TotemOfPreserving.item.get()) || TotemItem.hasItems(held)) {
                        continue;
                    }

                    // Found totem in hand.
                    LOGGER.debug("Found totem in hand");
                    damage = held.getDamageValue();
                    found = held;
                    break;
                }
            } else {
                for (var provider : TotemOfPreserving.inventoryCheckProviders) {
                    var item = provider.findTotemFromInventory(player, TotemType.PRESERVING);

                    if (item.isPresent()) {
                        // Found totem in inventory.
                        LOGGER.debug("Found totem in inventory");
                        found = item.get();
                        damage = found.getDamageValue();
                        break;
                    }
                }
            }

            if (found.isEmpty()) {
                LOGGER.debug("Could not find an empty totem, giving up");
                return InteractionResult.PASS;
            }

            // Give up if the player doesn't have anything to preserve
            if (preserveItems.isEmpty() || preserveItems.size() == 1) {
                LOGGER.debug("No items to store in totem (graveMode = false), giving up");
                return InteractionResult.PASS;
            }
        }

        // Give up if the player doesn't have anything to preserve
        if (preserveItems.isEmpty()) {
            LOGGER.debug("No items to store in totem (graveMode = true), giving up");
            return InteractionResult.PASS;
        }

        // Remove the totem from the inventory so it doesn't get included in the preserved items.
        // This doesn't do anything in graveMode because found is always empty.
        if (!found.isEmpty()) {
            found.shrink(found.getCount());
        }

        // Place a totem block in the world.
        var result = tryCreateTotemBlock(serverPlayer, preserveItems, damage);
        if (!result) {
            return InteractionResult.PASS;
        }

        // Delete player inventory items.
        for (var provider : TotemOfPreserving.preservingProviders) {
            provider.deleteInventoryItems(player);
        }

        return InteractionResult.SUCCESS;
    }

    static boolean tryCreateTotemBlock(ServerPlayer player, List<ItemStack> preserve, int damage) {
        var level = player.level();
        var random = player.getRandom();
        var uuid = player.getUUID();
        var message = player.getScoreboardName();

        // Get the death position.
        var pos = player.blockPosition();
        var vehicle = player.getVehicle();
        if (vehicle != null) {
            pos = vehicle.blockPosition();
        }

        // Do spawn checks.
        BlockPos spawnPos = null;
        var maxHeight = level.getMaxBuildHeight() - 1;
        var minHeight = level.getMinBuildHeight() + 1;
        var state = level.getBlockState(pos);
        var fluid = level.getFluidState(pos);

        // Adjust for void.
        if (pos.getY() < minHeight) {
            pos = new BlockPos(pos.getX(), level.getSeaLevel(), pos.getZ());
            LOGGER.debug("(Void check) Adjusting, new pos: " + pos);
        }

        if (state.isAir() || fluid.is(FluidTags.WATER)) {

            // Air and water are valid spawn positions.
            LOGGER.debug("(Standard check) Found an air/water block to spawn in: " + pos);
            spawnPos = pos;

        } else if (fluid.is(FluidTags.LAVA)) {

            // Lava: Keep moving upward to find air pocket, give up if solid (or void) reached.
            for (int tries = 0; tries < 20; tries++) {
                var y = pos.getY() + tries;
                if (y >= maxHeight) break;

                var tryPos = new BlockPos(pos.getX(), y, pos.getZ());
                var tryState = level.getBlockState(tryPos);
                var tryFluid = level.getFluidState(tryPos);
                if (tryFluid.is(FluidTags.LAVA)) continue;

                if (tryState.isAir() || tryFluid.is(FluidTags.WATER)) {
                    LOGGER.debug("(Lava check) Found an air/water block to spawn in after checking " + tries + " times: " + pos);
                    spawnPos = tryPos;
                }

                break;
            }

            // If that failed, replace the lava with the totem.
            if (spawnPos == null) {
                LOGGER.debug("(Lava check) Going to replace lava with totem at: " + pos);
                spawnPos = pos;
            }

        } else {

            // Solid block: Check above and nesw for an air or water pocket.
            List<Direction> directions = Arrays.asList(
                Direction.UP, Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH
            );
            for (var direction : directions) {
                var tryPos = pos.relative(direction);
                var tryState = level.getBlockState(tryPos);
                var tryFluid = level.getFluidState(tryPos);

                if (tryPos.getY() >= maxHeight) continue;
                if (tryState.isAir() || tryFluid.is(FluidTags.WATER)) {
                    LOGGER.debug("(Solid check) Found an air/water block to spawn in, direction: " + direction + ", pos: " + pos);
                    spawnPos = tryPos;
                    break;
                }
            }
        }

        if (spawnPos == null) {

            // Try and find a valid pos within 8 blocks of the death position.
            for (var tries = 0; tries < 8; tries++) {
                var x = pos.getX() + random.nextInt(tries + 1) - tries;
                var z = pos.getZ() + random.nextInt(tries + 1) - tries;

                // If upper void reached, count downward.
                var y = pos.getY() + tries;
                if (y > maxHeight) {
                    y = pos.getY() - tries;
                    if (y < minHeight) continue;
                }

                // Look for fluid and replacable solid blocks at increasing distance from the death point.
                var tryPos = new BlockPos(x, y, z);
                var tryState = level.getBlockState(tryPos);
                var tryFluid = level.getFluidState(tryPos);
                if (tryState.isAir() || tryFluid.is(FluidTags.WATER)) {
                    LOGGER.debug("(Distance check) Found an air/water block to spawn in after checking " + tries + " times: " + pos);
                    spawnPos = tryPos;
                    break;
                }
            }
        }

        if (spawnPos == null) {
            LOGGER.debug("Could not find a block to spawn totem, giving up.");
            return false;
        }

        level.setBlockAndUpdate(spawnPos, TotemOfPreserving.block.get().defaultBlockState());
        if (!(level.getBlockEntity(spawnPos) instanceof TotemBlockEntity totemBlockEntity)) {
            LOGGER.debug("Not a valid block entity at pos, giving up. Pos: " + pos);
            return false;
        }

        totemBlockEntity.setItems(preserve);
        totemBlockEntity.setMessage(message);
        totemBlockEntity.setOwner(uuid);
        totemBlockEntity.setDamage(damage);
        totemBlockEntity.setChanged();
        totemBlockEntity.setDirty();

        TotemOfPreserving.PROTECT_POSITIONS
            .computeIfAbsent(level.dimension().location(), a -> new ArrayList<>())
            .add(spawnPos);

        LOGGER.info("Spawned a totem at: " + spawnPos);
        TotemOfPreserving.triggerUsedTotemOfPreserving(player);
        level.playSound(null, spawnPos, TotemOfPreserving.storeSound.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        // Show the death position as chat message.
        if (TotemOfPreserving.showDeathPositionInChat) {
            var x = spawnPos.getX();
            var y = spawnPos.getY();
            var z = spawnPos.getZ();

            player.displayClientMessage(Component.translatable("gui.charm.totem_of_preserving.death_position", x, y, z), false);
        }

        return true;
    }

    static Optional<AnvilUpdateEvent.AnvilRecipe> handleAnvilUpdate(Player player, ItemStack input, ItemStack material, long cost) {
        if (input.is(TotemOfPreserving.item.get()) && input.isDamaged() && material.is(Items.ECHO_SHARD)) {
            var recipe = new AnvilUpdateEvent.AnvilRecipe();
            recipe.output = input.copy();
            recipe.output.setDamageValue(input.getDamageValue() - 1);
            recipe.experienceCost = 1;
            recipe.materialCost = 1;
            return Optional.of(recipe);
        }
        return Optional.empty();
    }
}
