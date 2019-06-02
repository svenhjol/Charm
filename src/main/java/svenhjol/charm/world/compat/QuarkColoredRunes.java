package svenhjol.charm.world.compat;

import net.minecraft.item.ItemStack;
import svenhjol.charm.world.feature.EndPortalRunes;
import svenhjol.meson.MesonBlock;
import vazkii.quark.misc.feature.ColorRunes;
import vazkii.quark.misc.item.ItemRune;

public class QuarkColoredRunes
{
    public boolean isRune(ItemStack stack)
    {
        return stack.getItem() instanceof ItemRune;
    }

    public int getMetaFromRune(ItemStack rune)
    {
        if (!isRune(rune)) return 0;
        return rune.getItemDamage();
    }

    public ItemStack getRuneFromMeta(int meta)
    {
        return new ItemStack(ColorRunes.rune, 1, meta);
    }

    public MesonBlock.ColorVariant getRuneColor(ItemStack rune)
    {
        return MesonBlock.ColorVariant.byMetadata(EndPortalRunes.quarkRunes.getMetaFromRune(rune));
    }
}
