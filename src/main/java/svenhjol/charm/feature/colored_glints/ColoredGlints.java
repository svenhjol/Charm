package svenhjol.charm.feature.colored_glints;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.colored_glints.common.Handlers;
import svenhjol.charm.feature.colored_glints.common.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.helper.ConfigHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BooleanSupplier;

@Feature(description = """
    Customizable item enchantment colors. This feature is a helper for other Charm features and mods.
    If disabled then other features and mods that rely on it will not function properly.""")
public class ColoredGlints extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    public ColoredGlints(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> ConfigHelper.isModLoaded("optifabric"));
    }

    public static DyeColor get(@Nullable ItemStack stack) {
        return Resolve.feature(ColoredGlints.class).handlers.get(stack);
    }

    public static boolean has(@Nullable ItemStack stack) {
        return Resolve.feature(ColoredGlints.class).handlers.has(stack);
    }

    public static void apply(ItemStack stack, DyeColor color) {
        Resolve.feature(ColoredGlints.class).handlers.apply(stack, color);
    }

    public static void remove(ItemStack stack) {
        Resolve.feature(ColoredGlints.class).handlers.remove(stack);
    }
}
