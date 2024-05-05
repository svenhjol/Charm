package svenhjol.charm.feature.colored_glint_smithing_templates;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ColoredGlintSmithingTemplates extends CommonFeature {
    static List<ResourceLocation> emptyDyes = new ArrayList<>();
    static ResourceKey<LootTable> lootTable;
    static Supplier<Item> item;

    @Configurable(
        name = "Loot table",
        description = "Loot table in which a colored glint smithing template will be added."
    )
    public static String configurableLootTable = "minecraft:chests/stronghold_library";

    @Configurable(
        name = "Loot chance",
        description = "Chance (out of 1.0) of a colored glint smithing template appearing in loot."
    )
    public static double lootChance = 1.0d;

    @Override
    public String description() {
        return "Smithing template that changes the glint color of any enchanted item.";
    }

    @Override
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    public static void triggerAppliedColoredGlintTemplate(Player player) {
        Advancements.trigger(Charm.id("applied_colored_glint_template"), player);
    }
}
