package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.BlockItem;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.iface.IConditionalRecipe;
import svenhjol.charmony_api.iface.IConditionalRecipeProvider;

import java.util.List;
import java.util.function.Supplier;

public class SmoothGlowstone extends CommonFeature implements IConditionalRecipeProvider {
    public static final String ID = "smooth_glowstone";
    public static Supplier<SmoothGlowstoneBlock> block;
    public static Supplier<BlockItem> blockItem;

    @Override
    public String description() {
        return "Smooth glowstone.";
    }

    @Override
    public void register() {
        var registry = mod().registry();
        block = registry.block(ID, SmoothGlowstoneBlock::new);
        blockItem = registry.item(ID, () -> new SmoothGlowstoneBlock.BlockItem(block));

        CharmonyApi.registerProvider(this);
    }

    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        return List.of(
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return isFiringEnabled();
                }

                @Override
                public List<String> recipes() {
                    return List.of(
                        "smooth_glowstone/firing/*"
                    );
                }
            }
        );
    }

    public static boolean isFiringEnabled() {
        return Mods.common(Charm.ID).loader().isEnabled(Firing.class);
    }
}
