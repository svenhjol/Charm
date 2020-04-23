package svenhjol.charm.base.compat;

import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.meson.Meson;
import svenhjol.meson.enums.ColorVariant;
import svenhjol.meson.helper.ItemNBTHelper;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.vanity.item.RuneItem;
import vazkii.quark.vanity.module.ColorRunesModule;

import javax.annotation.Nullable;

public class QuarkCompat implements IQuarkCompat {
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

    public ItemStack getQuiltedWool(ColorVariant color) {
        if (Meson.isModuleEnabled(new ResourceLocation("quark:quilted_wool"))) {
            final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("quark:" + color.getName() + "_quilted_wool"));
            if (item != null) {
                return new ItemStack(item);
            }
        }
        return ItemStack.EMPTY;
    }

    public void applyColor(ItemStack stack, DyeColor color) {
        // get the rune
        Item runeItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Quark.MOD_ID, color.getName() + "_rune"));
        if (runeItem instanceof RuneItem) {
            ItemStack rune = new ItemStack(runeItem);
            ItemNBTHelper.setBoolean(stack, ColorRunesModule.TAG_RUNE_ATTACHED, true);
            ItemNBTHelper.setCompound(stack, ColorRunesModule.TAG_RUNE_COLOR, rune.serializeNBT());
        }
    }
}
