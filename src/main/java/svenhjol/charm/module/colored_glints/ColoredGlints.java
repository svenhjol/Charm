package svenhjol.charm.module.colored_glints;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.logging.log4j.util.TriConsumer;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.TakeAnvilOutputCallback;
import svenhjol.charm.event.UpdateAnvilCallback;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Use dye on an anvil to change an item's enchantment color.")
public class ColoredGlints extends CharmModule {
    public static final ResourceLocation TRIGGER_CHANGED_GLINT_COLOR = new ResourceLocation(Charm.MOD_ID, "changed_glint_color");
    public static boolean enabled;

    @Config(name = "Default glint color", description = "Set the default glint color for all enchanted items.")
    public static String glintColor = DyeColor.PURPLE.getName();

    @Config(name = "XP cost", description = "Number of levels of XP required to change an item's enchantment color using dye on an anvil.")
    public static int xpCost = 0;

    @Override
    public void runWhenEnabled() {
        if (!Charm.LOADER.isEnabled("anvil_improvements") && xpCost < 1)
            xpCost = 1;

        enabled = Charm.LOADER.isEnabled(ColoredGlints.class);

        // listen for anvil behavior
        UpdateAnvilCallback.EVENT.register(this::handleAnvilBehavior);

        // listen for when player takes item from anvil
        TakeAnvilOutputCallback.EVENT.register(this::handleTakeOutput);
    }

    /**
     * Adds the enchantment and color directly to the input stack with no sanity checking.
     */
    public static void applyTint(ItemStack stack, String color) {
        stack.getOrCreateTag().putString(ColoredGlintsClient.GLINT_NBT, color);
    }

    public static boolean hasTint(ItemStack stack) {
        return stack.getOrCreateTag().contains(ColoredGlintsClient.GLINT_NBT);
    }

    private InteractionResult handleAnvilBehavior(AnvilMenu handler, Player player, ItemStack left, ItemStack right, Container output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply) {
        ItemStack out;

        if (left.isEmpty() || right.isEmpty())
            return InteractionResult.PASS;

        if (EnchantmentHelper.getEnchantments(left).size() == 0 || !(right.getItem() instanceof DyeItem))
            return InteractionResult.PASS;

        int cost = Math.max(0, xpCost);
        out = left.copy();
        DyeItem dye = (DyeItem)right.getItem();
        String color = dye.getDyeColor().getSerializedName();

        applyTint(out, color);
        apply.accept(out, cost, 1);

        return InteractionResult.SUCCESS;
    }

    private void handleTakeOutput(AnvilMenu handler, Player player, ItemStack stack) {
        if (!player.level.isClientSide && hasTint(stack))
            triggerChangedGlintColor((ServerPlayer) player);
    }

    public static void triggerChangedGlintColor(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_CHANGED_GLINT_COLOR);
    }
}
