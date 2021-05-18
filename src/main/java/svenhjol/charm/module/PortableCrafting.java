package svenhjol.charm.module;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.PortableCraftingClient;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.screenhandler.PortableCraftingScreenHandler;

@Module(mod = Charm.MOD_ID, client = PortableCraftingClient.class, description = "Allows crafting from inventory if the player has a crafting table in their inventory.",
    requiresMixins = {"SetupGuiCallback", "RenderGuiCallback"})
public class PortableCrafting extends CharmModule {
    private static final Text LABEL = new TranslatableText("container.charm.portable_crafting_table");
    public static final Identifier MSG_SERVER_OPEN_CRAFTING = new Identifier(Charm.MOD_ID, "server_open_crafting");
    public static final Identifier TRIGGER_USED_CRAFTING_TABLE = new Identifier(Charm.MOD_ID, "used_crafting_table");

    @Config(name = "Enable keybind", description = "If true, sets a keybind for opening the portable crafting table (defaults to 'c').")
    public static boolean enableKeybind = true;

    @Override
    public void init() {
        // listen for network requests to open the portable ender chest
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_OPEN_CRAFTING, this::handleServerOpenCrafting);
    }

    public static void openContainer(ServerPlayerEntity player) {
        player.closeHandledScreen();
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, inv, p) -> new PortableCraftingScreenHandler(i, inv, ScreenHandlerContext.create(p.world, p.getBlockPos())), LABEL));
    }

    private void handleServerOpenCrafting(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        server.execute(() -> {
            if (player == null || !PlayerHelper.getInventory(player).contains(new ItemStack(Blocks.CRAFTING_TABLE)))
                return;

            triggerUsedCraftingTable(player);
            PortableCrafting.openContainer(player);
        });
    }

    public static void triggerUsedCraftingTable(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_USED_CRAFTING_TABLE);
    }
}
