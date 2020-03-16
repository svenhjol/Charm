package svenhjol.charm.base.compat;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.charm.smithing.module.TallowIncreasesDurability;
import svenhjol.meson.Meson;
import svenhjol.meson.enums.ColorVariant;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.vanity.item.RuneItem;
import vazkii.quark.vanity.module.ColorRunesModule;

import javax.annotation.Nullable;
import java.util.Random;

public class QuarkCompat implements IQuarkCompat {
    public void onTallowAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;

        if (left.isEmpty() || right.isEmpty()) return;

        Item l = left.getItem();
        Item r = right.getItem();

        Item tallow = ForgeRegistries.ITEMS.getValue(new ResourceLocation("quark:tallow"));
        if (r != tallow) return;

        boolean repairable = l instanceof ToolItem
            || l instanceof ArmorItem
            || TallowIncreasesDurability.repairable.contains(l.getClass());
        if (!repairable) return;

        int currentDamage = l.getDamage(left);
        int maxDamage = left.getMaxDamage();

        if (currentDamage == 0) return;

        out = left.copy();
        out.setRepairCost(left.getRepairCost() + (new Random().nextDouble() < TallowIncreasesDurability.chanceOfCost ? 1 : 0));
        out.setDamage(currentDamage - (int) (maxDamage * TallowIncreasesDurability.amountRepaired));

        event.setOutput(out);
        event.setCost(TallowIncreasesDurability.xpCost);
        event.setMaterialCost(1);
    }

    public boolean hasColorRuneModule() {
        return ModuleLoader.INSTANCE.isModuleEnabled(ColorRunesModule.class);
    }

    public boolean isRune(ItemStack stack) {
        return stack.getItem() instanceof RuneItem;
    }

    @Nullable
    public ColorVariant getRuneColor(ItemStack stack) {
        ColorVariant color = ColorVariant.WHITE;

        if (isRune(stack)) {
            RuneItem item = (RuneItem) stack.getItem();
            ResourceLocation itemRegName = item.getRegistryName();
            if (itemRegName == null)
                return null;

            String colorName = itemRegName.getPath().replace("_rune", "").toUpperCase();
            try {
                color = ColorVariant.valueOf(colorName);
            } catch (Exception e) {
                Meson.LOG.debug("Failed to get color of rune" + stack.getItem());
                return null;
            }
        }

        return color;
    }

    public ItemStack getRune(ColorVariant color) {
        ResourceLocation res = new ResourceLocation(Quark.MOD_ID, color.getName() + "_rune");
        Item runeItem = ForgeRegistries.ITEMS.getValue(res);
        return runeItem == null ? ItemStack.EMPTY : new ItemStack(runeItem);
    }
}
