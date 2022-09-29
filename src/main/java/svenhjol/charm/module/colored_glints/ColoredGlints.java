package svenhjol.charm.module.colored_glints;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@CommonModule(mod = Charm.MOD_ID, description = "Customizable item enchantment colors.")
public class ColoredGlints extends CharmModule {
    private static final String DEFAULT_COLOR = "purple";
    public static final String TAG_GLINT = "charm_glint";

    private static String glintColor;
    private static boolean enabled = false;

    @Config(name = "Default glint color", description = "Set the default glint color for all enchanted items.")
    public static String configGlintColor = DyeColor.PURPLE.getName();

    @Override
    public void register() {
        if (Arrays.stream(DyeColor.values()).noneMatch(d -> d.getSerializedName().toLowerCase(Locale.ROOT).equals(configGlintColor))) {
            glintColor = DEFAULT_COLOR;
        } else {
            glintColor = configGlintColor;
        }
    }

    @Override
    public void runWhenEnabled() {
        enabled = true;
    }

    /**
     * Add enchantment glint color directly to the input stack using the dyecolor enum's name.
     */
    public static void applyGlint(ItemStack stack, DyeColor color) {
        stack.getOrCreateTag().putString(TAG_GLINT, color.getSerializedName().toLowerCase(Locale.ROOT));
    }

    public static void applyRandomGlint(ItemStack stack, RandomSource random) {
        List<DyeColor> colors = new ArrayList<>(Arrays.asList(DyeColor.values()));
        colors.remove(DyeColor.PURPLE);

        var color = colors.get(random.nextInt(colors.size()));
        applyGlint(stack, color);
    }

    public static boolean hasColoredGlint(ItemStack stack) {
        return stack.getOrCreateTag().contains(TAG_GLINT);
    }

    public static String getColoredGlint(ItemStack stack) {
        if (stack != null && stack.hasTag()) {
            var tag = stack.getTag();
            if (tag != null && tag.contains(TAG_GLINT)) {
                return tag.getString(TAG_GLINT);
            }
        }
        return glintColor;
    }

    public static DyeColor getDefaultGlintColor() {
        return DyeColor.valueOf(glintColor.toUpperCase(Locale.ROOT));
    }

    public static boolean enabled() {
        return enabled;
    }

}
