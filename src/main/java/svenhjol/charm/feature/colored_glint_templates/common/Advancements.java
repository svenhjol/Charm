package svenhjol.charm.feature.colored_glint_templates.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.colored_glint_templates.ColoredGlintTemplates;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ColoredGlintTemplates> {
    public Advancements(ColoredGlintTemplates feature) {
        super(feature);
    }

    public void appliedColoredGlintTemplate(Player player) {
        trigger("applied_colored_glint_template", player);
    }
}
