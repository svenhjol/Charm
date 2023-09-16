package svenhjol.charm.feature.proximity_workstations;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.proximity_workstations.ProximityWorkstationsNetwork.OpenSpecificWorkstation;
import svenhjol.charm.feature.proximity_workstations.ProximityWorkstationsNetwork.OpenWorkstationSelector;
import svenhjol.charm.feature.proximity_workstations.ProximityWorkstationsNetwork.OpenWorkstationSelectorScreen;
import svenhjol.charm.feature.proximity_workstations.menu.*;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.TagHelper;

import java.util.*;
import java.util.function.Function;

@Feature(mod = Charm.MOD_ID, description = "Use workstations such as crafting tables when in range of the block.")
public class ProximityWorkstations extends CharmFeature {
    static final Map<Block, Function<BlockPos, MenuProvider>> MENU_PROVIDERS = new LinkedHashMap<>();
    static final Map<UUID, Map<Block, BlockPos>> WORKSTATIONS_IN_RANGE = new WeakHashMap<>();
    static final Map<UUID, Long> LAST_WORKSTATION_CHECK = new WeakHashMap<>();
    static final Component CRAFTING_MENU_TITLE = Component.translatable("container.crafting");
    static final Component SMITHING_MENU_TITLE = Component.translatable("container.upgrade");
    static final Component ANVIL_MENU_TITLE = Component.translatable("container.repair");
    static final Component STONECUTTER_MENU_TITLE = Component.translatable("container.stonecutter");
    static final Component ENCHANTMENT_MENU_TITLE = Component.translatable("container.enchant");
    static TagKey<Block> validWorkstations;

    @Configurable(name = "Distance", description = "Range from which player can access a workstation.")
    public static int distance = 10;

    @Override
    public void register() {
        ProximityWorkstationsNetwork.register();

        validWorkstations = TagKey.create(BuiltInRegistries.BLOCK.key(),
            Charm.instance().makeId("proximity_workstations"));

        registerBlockMenu(Blocks.CRAFTING_TABLE, pos -> new SimpleMenuProvider(
            (i, inv, p) -> new ProximityCraftingMenu(i, inv,
                ContainerLevelAccess.create(p.level(), p.blockPosition()), pos), CRAFTING_MENU_TITLE));

        registerBlockMenu(Blocks.SMITHING_TABLE, pos -> new SimpleMenuProvider(
            (i, inv, p) -> new ProximitySmithingMenu(i, inv,
                ContainerLevelAccess.create(p.level(), p.blockPosition()), pos), SMITHING_MENU_TITLE));

        Function<BlockPos, MenuProvider> anvilMenuProvider = pos -> new SimpleMenuProvider(
            (i, inv, p) -> new ProximityAnvilMenu(i, inv,
                ContainerLevelAccess.create(p.level(), p.blockPosition()), pos), ANVIL_MENU_TITLE);

        registerBlockMenu(Blocks.ANVIL, anvilMenuProvider);
        registerBlockMenu(Blocks.CHIPPED_ANVIL, anvilMenuProvider);
        registerBlockMenu(Blocks.DAMAGED_ANVIL, anvilMenuProvider);

        registerBlockMenu(Blocks.STONECUTTER, pos -> new SimpleMenuProvider(
            (i, inv, p) -> new ProximityStonecutterMenu(i, inv,
                ContainerLevelAccess.create(p.level(), p.blockPosition()), pos), STONECUTTER_MENU_TITLE));

        registerBlockMenu(Blocks.ENCHANTING_TABLE, pos -> new SimpleMenuProvider(
            (i, inv, p) -> new ProximityEnchantingMenu(i, inv,
                ContainerLevelAccess.create(p.level(), p.blockPosition()), pos), ENCHANTMENT_MENU_TITLE));
    }

    public static void registerBlockMenu(Block block, Function<BlockPos, MenuProvider> menuProvider) {
        MENU_PROVIDERS.put(block, menuProvider);
    }

    static Map<Block, BlockPos> getWorkstationsInRange(Player player) {
        var uuid = player.getUUID();
        var level = player.level();
        var gametime = level.getGameTime();
        var pos = player.blockPosition();

        if (!WORKSTATIONS_IN_RANGE.containsKey(uuid)
            || !LAST_WORKSTATION_CHECK.containsKey(uuid)
            || LAST_WORKSTATION_CHECK.get(uuid) < gametime - 40
        ) {
            Map<Block, BlockPos> workstations = new LinkedHashMap<>();
            TagHelper.getValues(BuiltInRegistries.BLOCK, validWorkstations).forEach(
                workstation -> {
                    var existingBlocks = workstations.keySet();
                    var result = BlockPos.findClosestMatch(pos, distance, distance, p -> level.getBlockState(p).is(workstation));
                    result.ifPresent(blockPos -> {

                        // Anvils are special because we don't want to include chipped and damaged anvils as separate workstations.
                        if (workstation.defaultBlockState().is(BlockTags.ANVIL)) {
                            for (var existing : existingBlocks) {
                                if (existing.defaultBlockState().is(BlockTags.ANVIL)) return;
                            }
                        }

                        workstations.put(workstation, blockPos);
                    });
                }
            );
            WORKSTATIONS_IN_RANGE.remove(uuid);
            WORKSTATIONS_IN_RANGE.put(uuid, workstations);
            LAST_WORKSTATION_CHECK.put(uuid, gametime);
        }

        return WORKSTATIONS_IN_RANGE.getOrDefault(uuid, new HashMap<>());
    }

    static void handleOpenedSelector(OpenWorkstationSelector message, Player player) {
        var workstations = getWorkstationsInRange(player);
        var blocks = new LinkedList<>(workstations.keySet());

        if (blocks.size() == 1) {
            var block = blocks.get(0);
            var position = workstations.get(block);
            openContainer((ServerPlayer) player, block, position);
        } else if (blocks.size() > 1) {
            openSelector((ServerPlayer) player, blocks);
        }
    }

    static void handleOpenedSpecificWorkstation(OpenSpecificWorkstation message, Player player) {
        var workstations = getWorkstationsInRange(player);
        var workstation = message.getWorkstation();
        var position = workstations.get(workstation);
        openContainer((ServerPlayer)player, workstation, position);
    }

    static void openSelector(ServerPlayer player, List<Block> workstations) {
        player.closeContainer();
        OpenWorkstationSelectorScreen.send(player, workstations);
    }

    static void openContainer(ServerPlayer player, Block block, BlockPos pos) {
        if (MENU_PROVIDERS.containsKey(block)) {
            player.closeContainer();
            var provider = MENU_PROVIDERS.get(block);
            player.openMenu(provider.apply(pos));
        }
    }
}
