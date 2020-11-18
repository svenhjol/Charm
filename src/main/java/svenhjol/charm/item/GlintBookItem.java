package svenhjol.charm.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.item.CharmItem;
import svenhjol.charm.handler.ColoredGlintHandler;

public class GlintBookItem extends CharmItem {
    private final String color;

    public GlintBookItem(CharmModule module, String color) {
        super(module, color + "_glint_book", new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));
        this.color = color;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> items) {
        if (this.enabled()) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putString(ColoredGlintHandler.GLINT_TAG, color);
            items.add(stack);
        }
    }
}
