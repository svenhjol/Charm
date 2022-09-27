package svenhjol.charm.module.totem_of_preserving;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.EntityDropXpCallback;
import svenhjol.charm.api.event.PlayerDropInventoryCallback;
import svenhjol.charm.api.event.TotemOfPreservingEvents;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@CommonModule(mod = Charm.MOD_ID, description = """
    The player's inventory items will be preserved in the Totem of Preserving upon death.
    A totem will always be spawned to preserve items upon dying.""")
public class TotemOfPreserving extends CharmModule {
    public static TotemOfPreservingItem ITEM;
    public static final ResourceLocation TRIGGER_USED_TOTEM_OF_PRESERVING = new ResourceLocation(Charm.MOD_ID, "used_totem_of_preserving");
    public static final ResourceLocation BLOCK_ID = new ResourceLocation(Charm.MOD_ID, "totem_block");
    public static TotemBlock BLOCK;
    public static BlockEntityType<TotemBlockEntity> BLOCK_ENTITY;
    public static Map<ResourceLocation, List<BlockPos>> PROTECT_POSITIONS = new HashMap<>();
    private static final List<Function<Player, List<ItemStack>>> INVENTORY_FETCH = new ArrayList<>();
    private static final List<Consumer<Player>> INVENTORY_DELETE = new ArrayList<>();

    @Config(name = "Owner only", description = "If true, only the owner of the totem may pick it up.")
    public static boolean ownerOnly = false;

    @Config(name = "Preserve XP", description = "If true, the totem will preserve the player's experience and restore when broken.")
    public static boolean preserveXp = false;

    @Config(name = "Show death position", description = "If true, the coordinates where you died will be added to the player's chat screen.")
    public static boolean showDeathPosition = false;

    @Override
    public void register() {
        ITEM = new TotemOfPreservingItem(this);
        BLOCK = new TotemBlock(this);
        BLOCK_ENTITY = CommonRegistry.blockEntity(BLOCK_ID, TotemBlockEntity::new, BLOCK);
    }

    @Override
    public void runWhenEnabled() {
        ItemHelper.ITEM_LIFETIME.put(ITEM, Integer.MAX_VALUE); // probably stupid
        PlayerDropInventoryCallback.EVENT.register(this::handleDropInventory);
        EntityDropXpCallback.BEFORE.register(this::handleDropXp);

        registerInventoryFetch(player -> player.inventory.items);
        registerInventoryFetch(player -> player.inventory.armor);
        registerInventoryFetch(player -> player.inventory.offhand);

        registerInventoryDelete(player -> {
            var inventories = List.of(player.inventory.items, player.inventory.armor, player.inventory.offhand);
            for (var inv : inventories) {
                Collections.fill(inv, ItemStack.EMPTY);
            }
        });
    }

    /**
     * Use this method to register a callback to fetch all items from an inventory.
     */
    public static void registerInventoryFetch(Function<Player, List<ItemStack>> func) {
        INVENTORY_FETCH.add(func);
    }

    /**
     * Use this method to register a callback to delete all items from an inventory.
     */
    public static void registerInventoryDelete(Consumer<Player> supplier) {
        INVENTORY_DELETE.add(supplier);
    }

    public InteractionResult handleDropXp(LivingEntity entity) {
        if (!preserveXp || !(entity instanceof Player) || entity.level.isClientSide) {
            return InteractionResult.PASS;
        }

        return InteractionResult.SUCCESS;
    }

    public InteractionResult handleDropInventory(Player player, Inventory inventory) {
        if (player.level.isClientSide()) {
            return InteractionResult.PASS;
        }

        var serverPlayer = (ServerPlayer) player;

        // Add all inventories.
        List<ItemStack> items = new ArrayList<>();
        INVENTORY_FETCH.forEach(i -> items.addAll(i.apply(serverPlayer)));

        var preserve = items.stream()
            .filter(stack -> !stack.isEmpty())
            .filter(stack -> TotemOfPreservingEvents.BEFORE_ADD_STACK.invoker().invoke(serverPlayer, stack) != InteractionResult.FAIL)
            .collect(Collectors.toList());

        // Don't spawn if there are no items to add.
        if (items.isEmpty()) {
            LogHelper.debug(getClass(), "No items found to store in totem, giving up.");
            return InteractionResult.PASS;
        }

        // Try spawn a totem.
        var result = spawnTotem(preserve, serverPlayer);
        if (!result) {
            return InteractionResult.PASS;
        }

        // Clear all inventories.
        INVENTORY_DELETE.forEach(i -> i.accept(serverPlayer));

        return InteractionResult.SUCCESS;
    }

    private boolean spawnTotem(List<ItemStack> items, ServerPlayer player) {
        var level = player.getLevel();
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
            LogHelper.debug(getClass(), "(Void check) Adjusting, new pos: " + pos);
            pos = new BlockPos(pos.getX(), level.getSeaLevel(), pos.getZ());
        }

        if (state.isAir() || fluid.is(FluidTags.WATER)) {

            // Air and water are valid spawn positions.
            LogHelper.debug(getClass(), "(Standard check) Found an air/water block to spawn in: " + pos);
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
                    LogHelper.debug(getClass(), "(Lava check) Found an air/water block to spawn in after checking " + tries + " times: " + pos);
                    spawnPos = tryPos;
                }

                break;
            }

            // If that failed, replace the lava with the totem.
            if (spawnPos == null) {
                LogHelper.debug(getClass(), "(Lava check) Going to replace lava with totem at: " + pos);
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
                    LogHelper.debug(getClass(), "(Solid check) Found an air/water block to spawn in, direction: " + direction + ", pos: " + pos);
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
                    LogHelper.debug(getClass(), "(Distance check) Found an air/water block to spawn in after checking " + tries + " times: " + pos);
                    spawnPos = tryPos;
                    break;
                }
            }

        }

        if (spawnPos == null) {
            LogHelper.debug(getClass(), "Could not find a block to spawn totem, giving up.");
            return false;
        }

        level.setBlockAndUpdate(spawnPos, BLOCK.defaultBlockState());
        if (!(level.getBlockEntity(spawnPos) instanceof TotemBlockEntity totem)) {
            LogHelper.debug(getClass(), "Not a valid block entity at pos, giving up. Pos: " + pos);
            return false;
        }

        if (preserveXp) {
            int xp = player.totalExperience;
            LogHelper.debug(getClass(), "Preserving player XP in totem: " + xp);
            totem.setXp(xp);
        }

        totem.setItems(items);
        totem.setMessage(message);
        totem.setOwner(uuid);
        totem.setChanged();

        PROTECT_POSITIONS
            .computeIfAbsent(level.dimension().location(), a -> new ArrayList<>())
            .add(spawnPos);

        triggerUsedTotemOfPreserving(player);
        LogHelper.info(getClass(), "Spawned a totem at: " + spawnPos);

        // Show the death position as chat message.
        if (showDeathPosition) {
            var x = spawnPos.getX();
            var y = spawnPos.getY();
            var z = spawnPos.getZ();

            player.displayClientMessage(TextHelper.translatable("gui.charm.totem_of_preserving.deathpos", x, y, z), false);
        }

        return true;
    }

    public static void triggerUsedTotemOfPreserving(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_USED_TOTEM_OF_PRESERVING);
    }
}