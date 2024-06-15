package svenhjol.charm.feature.glint_color_templates.common;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import svenhjol.charm.feature.glint_color_templates.GlintColorTemplates;
import svenhjol.charm.charmony.Resolve;

import java.util.List;

public class Item extends SmithingTemplateItem {
    private static final GlintColorTemplates COLORED_GLINT_SMITHING_TEMPLATES
        = Resolve.feature(GlintColorTemplates.class);
    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;

    static final List<ResourceLocation> EMPTY_ITEMS = List.of(
            new ResourceLocation("item/empty_armor_slot_chestplate"),
            new ResourceLocation("item/empty_armor_slot_helmet"),
            new ResourceLocation("item/empty_slot_sword"),
            new ResourceLocation("item/empty_slot_pickaxe")
    );

    public Item() {
        super(
            makeText("applies_to").withStyle(DESCRIPTION_FORMAT),
            makeText("ingredients").withStyle(DESCRIPTION_FORMAT),
            makeText("feature").withStyle(TITLE_FORMAT),
            makeText("base_slot_description"),
            makeText("additions_slot_description"),
            EMPTY_ITEMS,
            COLORED_GLINT_SMITHING_TEMPLATES.registers.emptyDyes
        );
    }

    private static MutableComponent makeText(String id) {
        return Component.translatable("smithing_template.charm.glint_color_" + id);
    }
}
