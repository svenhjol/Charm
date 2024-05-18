package svenhjol.charm.feature.glint_color_templates.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.glint_color_templates.GlintColorTemplates;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<GlintColorTemplates> {
    public Advancements(GlintColorTemplates feature) {
        super(feature);
    }

    public void appliedGlintColorTemplate(Player player) {
        trigger("applied_glint_color_template", player);
    }
}
