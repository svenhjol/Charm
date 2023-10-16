package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.ApiHelper;
import svenhjol.charmony.helper.TextHelper;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.event.PlayerInventoryDropEvent;
import svenhjol.charmony_api.iface.ITotemPreservingProvider;

import java.util.*;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Preserves your items on death.")
public class TotemOfPreserving extends CharmonyFeature {
    static Supplier<Item> item;
    static Supplier<Block> block;
    static Supplier<BlockEntityType<TotemBlockEntity>> blockEntity;
    public static Map<ResourceLocation, List<BlockPos>> PROTECT_POSITIONS = new HashMap<>();

    @Configurable(
        name = "Owner only",
        description = "If true, only the owner of the totem may pick it up.",
        requireRestart = false
    )
    public static boolean ownerOnly = false;

    @Configurable(
        name = "Show death position",
        description = "If true, the coordinates where you died will be added to the player's chat screen.",
        requireRestart = false
    )
    public static boolean showDeathPositionInChat = false;

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        block = registry.block("totem_of_preserving_holder",
            () -> new TotemBlock(this));
        blockEntity = registry.blockEntity("totem_block",
            () -> TotemBlockEntity::new, List.of(block));
        item = registry.item("totem_of_preserving",
            () -> new TotemItem(this));

        CharmonyApi.registerProvider(new TotemInventoryProvider());
    }

    @Override
    public void runWhenEnabled() {
        PlayerInventoryDropEvent.INSTANCE.handle(this::handlePlayerInventoryDrop);
    }

    private InteractionResult handlePlayerInventoryDrop(Player player, Inventory inventory) {
        if (player.level().isClientSide) {
            return InteractionResult.PASS;
        }

        var log = Charm.instance().log();
        var serverPlayer = (ServerPlayer)player;
        List<ItemStack> preserve = new ArrayList<>();

        // Fetch items from all inventories on demand.
        ApiHelper.consume(ITotemPreservingProvider.class,
            provider -> preserve.addAll(provider.getInventoryItemsForTotem(player)));

        if (preserve.isEmpty()) {
            log.debug(getClass(), "No items to store in totem, giving up");
            return InteractionResult.PASS;
        }

        // Place a totem in the world.
        var result = tryCreateTotemBlock(serverPlayer, preserve);
        if (!result) {
            return InteractionResult.PASS;
        }

        // Delete inventory items on demand.
        ApiHelper.consume(ITotemPreservingProvider.class,
            provider -> provider.deleteInventoryItems(player));

        return InteractionResult.SUCCESS;
    }

    private boolean tryCreateTotemBlock(ServerPlayer player, List<ItemStack> preserve) {
        var log = Charm.instance().log();

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
            log.debug(getClass(), "(Void check) Adjusting, new pos: " + pos);
            pos = new BlockPos(pos.getX(), level.getSeaLevel(), pos.getZ());
        }


        if (state.isAir() || fluid.is(FluidTags.WATER)) {

            // Air and water are valid spawn positions.
            log.debug(getClass(), "(Standard check) Found an air/water block to spawn in: " + pos);
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
                    log.debug(getClass(), "(Lava check) Found an air/water block to spawn in after checking " + tries + " times: " + pos);
                    spawnPos = tryPos;
                }

                break;
            }

            // If that failed, replace the lava with the totem.
            if (spawnPos == null) {
                log.debug(getClass(), "(Lava check) Going to replace lava with totem at: " + pos);
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
                    log.debug(getClass(), "(Solid check) Found an air/water block to spawn in, direction: " + direction + ", pos: " + pos);
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
                    log.debug(getClass(), "(Distance check) Found an air/water block to spawn in after checking " + tries + " times: " + pos);
                    spawnPos = tryPos;
                    break;
                }
            }
        }

        if (spawnPos == null) {
            log.debug(getClass(), "Could not find a block to spawn totem, giving up.");
            return false;
        }

        level.setBlockAndUpdate(spawnPos, block.get().defaultBlockState());
        if (!(level.getBlockEntity(spawnPos) instanceof TotemBlockEntity totem)) {
            log.debug(getClass(), "Not a valid block entity at pos, giving up. Pos: " + pos);
            return false;
        }

        totem.setItems(preserve);
        totem.setMessage(message);
        totem.setOwner(uuid);
        totem.setChanged();

        PROTECT_POSITIONS
            .computeIfAbsent(level.dimension().location(), a -> new ArrayList<>())
            .add(spawnPos);

        log.info(getClass(), "Spawned a totem at: " + spawnPos);
        triggerUsedTotemOfPreserving(player);

        // Show the death position as chat message.
        if (showDeathPositionInChat) {
            var x = spawnPos.getX();
            var y = spawnPos.getY();
            var z = spawnPos.getZ();

            player.displayClientMessage(TextHelper.translatable("gui.charm.totem_of_preserving.deathpos", x, y, z), false);
        }

        return true;
    }

    public static void triggerUsedTotemOfPreserving(Player player) {
        Advancements.trigger(Charm.instance().makeId("used_totem_of_preserving"), player);
    }
}
