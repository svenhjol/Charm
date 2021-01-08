package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.gui.AtlasScreen;
import svenhjol.charm.module.Atlas;
import svenhjol.charm.render.AtlasRenderer;

public class AtlasClient extends CharmClientModule {
    private AtlasRenderer renderer;

    public AtlasClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ScreenRegistry.register(Atlas.CONTAINER, AtlasScreen::new);
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() == Atlas.ATLAS_ITEM) {
            if (renderer == null) {
                renderer = new AtlasRenderer();
            }
            renderer.renderAtlas(event.getMatrixStack(), event.getBuffers(), event.getLight(), event.getHand(), event.getEquipProgress(),
                event.getSwingProgress(), itemStack);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty() || stack.getItem() != Atlas.ATLAS_ITEM) return;

        PlayerEntity player = event.getPlayer();
        if (player == null) return;

        AtlasInventory inventory = Atlas.getInventory(player.world, stack);
        event.getToolTip().add(new StringTextComponent("Scale " + inventory.getScale()).mergeStyle(TextFormatting.GRAY));

        ItemStack map = inventory.getLastActiveMapItem();
        if (map == null) return;

        IFormattableTextComponent name = map.hasDisplayName() ? map.getDisplayName().deepCopy()
            : map.getDisplayName().deepCopy().append(new StringTextComponent(" #" + FilledMapItem.getMapId(map)));
        event.getToolTip().add(name.mergeStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
    }

    public static void updateInventory(int atlasSlot) {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null) return;
        ItemStack atlas = player.inventory.getStackInSlot(atlasSlot);
        Atlas.getInventory(mc.world, atlas).reload(atlas);
    }

    public static boolean shouldDrawAtlasCopy(CartographyTableScreen screen) {
        return ModuleHandler.enabled(Atlas.class) && screen.getContainer().getSlot(0).getStack().getItem() == Atlas.ATLAS_ITEM
            && screen.getContainer().getSlot(1).getStack().getItem() == Items.MAP;
    }
}

