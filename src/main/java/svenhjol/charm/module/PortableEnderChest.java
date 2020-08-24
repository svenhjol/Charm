package svenhjol.charm.module;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.client.PortableEnderChestClient;
import svenhjol.charm.container.PortableEnderChestScreenHandler;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(description = "Allows access to chest contents if the player has an Ender Chest in their inventory.")
public class PortableEnderChest extends MesonModule {
    private static final Text LABEL = new TranslatableText("container.charm.portable_ender_chest");
    public static final Identifier MSG_SERVER_OPEN_ENDER_CHEST = new Identifier(Charm.MOD_ID, "server_open_ender_chest");
    public static PortableEnderChestClient client;

    @Config(name = "Offhand only", description = "If true, the chest button will only appear when the player holds the ender chest in their offhand.")
    public static boolean offhandOnly = false;

    @Override
    public void init() {
        // listen for network requests to open the portable ender chest
        ServerSidePacketRegistry.INSTANCE.register(MSG_SERVER_OPEN_ENDER_CHEST, (context, data) -> {
            context.getTaskQueue().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
                if (player == null || !player.inventory.contains(new ItemStack(Blocks.ENDER_CHEST)))
                    return;

                PortableEnderChest.openContainer(player);
            });
        });
    }

    @Override
    public void initClient() {
        client = new PortableEnderChestClient(this);
    }

    public static void openContainer(ServerPlayerEntity player) {
        player.world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 0.4F, 1.08F);
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, inv, p) -> new PortableEnderChestScreenHandler(i, inv, p.getEnderChestInventory()), LABEL));
    }
}
