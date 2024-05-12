package svenhjol.charm.feature.colored_glint_smithing_templates.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.colored_glint_smithing_templates.ColoredGlintSmithingTemplates;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ColoredGlintSmithingTemplates> {
    public Advancements(ColoredGlintSmithingTemplates feature) {
        super(feature);
    }

    public void appliedColoredGlintTemplate(Player player) {
        trigger("applied_colored_glint_template", player);
    }
}
