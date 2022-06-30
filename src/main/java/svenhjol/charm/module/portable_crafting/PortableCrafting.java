package svenhjol.charm.module.portable_crafting;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.portable_crafting.network.ServerReceiveOpenCrafting;

@CommonModule(mod = Charm.MOD_ID, description = "Allows crafting from inventory if the player has a crafting table in their inventory.")
public class PortableCrafting extends CharmModule {
    private static final Component LABEL = TextHelper.translatable("container.charm.portable_crafting_table");

    public static ServerReceiveOpenCrafting SERVER_RECEIVE_OPEN_CRAFTING;
    public static final ResourceLocation TRIGGER_USED_CRAFTING_TABLE = new ResourceLocation(Charm.MOD_ID, "used_crafting_table");

    @Config(name = "Enable keybind", description = "If true, sets a keybind for opening the portable crafting table (defaults to 'v').")
    public static boolean enableKeybind = true;

    @Override
    public void runWhenEnabled() {
        SERVER_RECEIVE_OPEN_CRAFTING = new ServerReceiveOpenCrafting();
    }

    public static void openContainer(ServerPlayer player) {
        player.closeContainer();
        player.openMenu(new SimpleMenuProvider((i, inv, p) -> new PortableCraftingMenu(i, inv, ContainerLevelAccess.create(p.level, p.blockPosition())), LABEL));
    }

    public static void triggerUsedCraftingTable(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_USED_CRAFTING_TABLE);
    }
}
