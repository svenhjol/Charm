package vazkii.quark.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WireSegal
 * Created at 2:22 PM on 8/17/19.
 */
public interface IRuneColorProvider {

	@OnlyIn(Dist.CLIENT)
    int getRuneColor(ItemStack stack);
}
