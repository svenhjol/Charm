package svenhjol.charm.module.portable_crafting;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Allows crafting from inventory if the player has a crafting table in their inventory.")
public class PortableCrafting extends CharmModule {
    private static final Component LABEL = new TranslatableComponent("container.charm.portable_crafting_table");
    public static final ResourceLocation MSG_SERVER_OPEN_CRAFTING = new ResourceLocation(Charm.MOD_ID, "server_open_crafting");
    public static final ResourceLocation TRIGGER_USED_CRAFTING_TABLE = new ResourceLocation(Charm.MOD_ID, "used_crafting_table");

    @Config(name = "Enable keybind", description = "If true, sets a keybind for opening the portable crafting table (defaults to 'c').")
    public static boolean enableKeybind = true;

    @Override
    public void runWhenEnabled() {
        // listen for network requests to open the portable ender chest
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_OPEN_CRAFTING, this::handleServerOpenCrafting);
    }

    public static void openContainer(ServerPlayer player) {
        player.closeContainer();
        player.openMenu(new SimpleMenuProvider((i, inv, p) -> new PortableCraftingMenu(i, inv, ContainerLevelAccess.create(p.level, p.blockPosition())), LABEL));
    }

    private void handleServerOpenCrafting(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf data, PacketSender sender) {
        server.execute(() -> {
            if (player == null || !player.inventory.contains(new ItemStack(Blocks.CRAFTING_TABLE))) return;
            triggerUsedCraftingTable(player);
            PortableCrafting.openContainer(player);
        });
    }

    public static void triggerUsedCraftingTable(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_USED_CRAFTING_TABLE);
    }
}
