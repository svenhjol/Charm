package svenhjol.charm.feature.colored_glint_smithing_templates;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class ColoredGlintTemplateItem extends SmithingTemplateItem {
    static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;

    static final List<ResourceLocation> EMPTY_ITEMS = List.of(
        new ResourceLocation("item/empty_armor_slot_chestplate"),
        new ResourceLocation("item/empty_armor_slot_helmet"),
        new ResourceLocation("item/empty_slot_sword"),
        new ResourceLocation("item/empty_slot_pickaxe")
    );

    public ColoredGlintTemplateItem() {
        super(
            makeText("applies_to").withStyle(DESCRIPTION_FORMAT),
            makeText("ingredients").withStyle(DESCRIPTION_FORMAT),
            makeText("feature").withStyle(TITLE_FORMAT),
            makeText("base_slot_description"),
            makeText("additions_slot_description"),
            EMPTY_ITEMS,
            ColoredGlintSmithingTemplates.emptyDyes
        );
    }

    private static MutableComponent makeText(String id) {
        return Component.translatable("smithing_template.charm.colored_glint_" + id);
    }
}
