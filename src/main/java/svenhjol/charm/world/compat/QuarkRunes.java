package svenhjol.charm.world.compat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.meson.Meson;
import svenhjol.meson.enums.ColorVariant;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.vanity.item.RuneItem;
import vazkii.quark.vanity.module.ColorRunesModule;

import javax.annotation.Nullable;

public class QuarkRunes
{
    public boolean hasColorRuneModule()
    {
        return ModuleLoader.INSTANCE.isModuleEnabled(ColorRunesModule.class);
    }

    public boolean isRune(ItemStack stack)
    {
        return stack.getItem() instanceof RuneItem;
    }

    @Nullable
    public ColorVariant getColor(ItemStack stack)
    {
        ColorVariant color = ColorVariant.WHITE;

        if (isRune(stack)) {
            RuneItem item = (RuneItem)stack.getItem();
            String colorName = item.getRegistryName().getPath().replace("_rune", "").toUpperCase();
            try {
                color = ColorVariant.valueOf(colorName);
            } catch (Exception e) {
                Meson.debug("Failed to get color of rune", stack.getItem());
                return null;
            }
        }

        return color;
    }

    public ItemStack getRune(ColorVariant color)
    {
        ResourceLocation res = new ResourceLocation(Quark.MOD_ID, color.getName() + "_rune");
        Item runeItem = ForgeRegistries.ITEMS.getValue(res);
        return runeItem == null ? ItemStack.EMPTY : new ItemStack(runeItem);
    }
}
