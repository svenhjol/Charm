package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import svenhjol.charm.Charm;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.foundation.Globals;
import svenhjol.charm.foundation.Register;

import java.util.List;

public class RegisterCommon extends Register<SmoothGlowstone> implements IConditionalRecipeProvider {
    public static final String BLOCK_ID = "smooth_glowstone";

    public RegisterCommon(SmoothGlowstone feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var block = Registry.register(BuiltInRegistries.BLOCK,
            Charm.id(BLOCK_ID),
            new SmoothGlowstoneBlock());

        var blockItem = Registry.register(BuiltInRegistries.ITEM,
            Charm.id(BLOCK_ID),
            new SmoothGlowstoneBlock.BlockItem(block));

        SmoothGlowstone.block = () -> block;
        SmoothGlowstone.blockItem = () -> blockItem;

        CharmApi.registerProvider(this);
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
        return Globals.COMMON_LOADERS.get(Charm.ID).isEnabled(Charm.id("firing"));
    }
}
