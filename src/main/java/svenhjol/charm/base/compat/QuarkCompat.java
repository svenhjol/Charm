package svenhjol.charm.base.compat;

import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.meson.Meson;
import svenhjol.meson.enums.ColorVariant;

import javax.annotation.Nullable;

public class QuarkCompat implements IQuarkCompat {
    public boolean hasColorRuneModule() {
        return false;
    }

    public boolean isRune(ItemStack stack) {
        return false;
    }

    @Nullable
    public ColorVariant getRuneColor(ItemStack stack) {
        return ColorVariant.WHITE;
    }

    public ItemStack getRune(ColorVariant color) {
        return ItemStack.EMPTY;
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
//        Item runeItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Quark.MOD_ID, color.getName() + "_rune"));
//        if (runeItem instanceof RuneItem) {
//            ItemStack rune = new ItemStack(runeItem);
//            ItemNBTHelper.setBoolean(stack, ColorRunesModule.TAG_RUNE_ATTACHED, true);
//            ItemNBTHelper.setCompound(stack, ColorRunesModule.TAG_RUNE_COLOR, rune.serializeNBT());
//        }
    }
}
