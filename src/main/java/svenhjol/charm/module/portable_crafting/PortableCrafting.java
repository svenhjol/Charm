package svenhjol.charm.module.portable_crafting;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.init.CharmTags;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.portable_crafting.network.ServerReceiveOpenCrafting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@CommonModule(mod = Charm.MOD_ID, description = "Allows crafting from inventory if the player has a crafting table in their inventory.")
public class PortableCrafting extends CharmModule {
    private static final Component LABEL = TextHelper.translatable("container.charm.portable_crafting_table");

    public static ServerReceiveOpenCrafting RECEIVE_OPEN_CRAFTING;
    public static final ResourceLocation TRIGGER_USED_CRAFTING_TABLE = new ResourceLocation(Charm.MOD_ID, "used_crafting_table");

    public static final List<Predicate<Player>> INVENTORY_CHECKS = new ArrayList<>();

    @Config(name = "Enable keybind", description = "If true, sets a keybind for opening the portable crafting table (defaults to 'v').")
    public static boolean enableKeybind = true;

    @Override
    public void runWhenEnabled() {
        RECEIVE_OPEN_CRAFTING = new ServerReceiveOpenCrafting();

        registerInventoryCheck(player -> {
            List<ItemStack> items = new ArrayList<>();
            items.addAll(player.inventory.items);
            items.addAll(player.inventory.offhand);
            return items.stream().anyMatch(stack -> stack.is(CharmTags.CRAFTING_TABLES));
        });
    }

    public static void registerInventoryCheck(Predicate<Player> predicate) {
        INVENTORY_CHECKS.add(predicate);
    }

    public static boolean hasCraftingTable(Player player) {
        return PortableCrafting.INVENTORY_CHECKS.stream().anyMatch(check -> check.test(player));
    }

    public static void handleReceiveOpenCrafting(ServerPlayer player) {
        if (!hasCraftingTable(player)) return;;
        PortableCrafting.triggerUsedCraftingTable(player);
        PortableCrafting.openContainer(player);
    }

    public static void openContainer(ServerPlayer player) {
        player.closeContainer();
        player.openMenu(new SimpleMenuProvider((i, inv, p) -> new PortableCraftingMenu(i, inv, ContainerLevelAccess.create(p.level, p.blockPosition())), LABEL));
    }

    public static void triggerUsedCraftingTable(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_USED_CRAFTING_TABLE);
    }
}
