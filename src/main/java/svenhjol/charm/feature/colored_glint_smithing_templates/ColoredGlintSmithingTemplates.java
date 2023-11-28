package svenhjol.charm.feature.colored_glint_smithing_templates;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmTags;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.feature.colored_glints.ColoredGlints;
import svenhjol.charmony_api.event.LootTableModifyEvent;
import svenhjol.charmony_api.event.SmithingTableEvents;
import svenhjol.charmony_api.event.SmithingTableEvents.SmithingTableInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ColoredGlintSmithingTemplates extends CommonFeature {
    public static final String ITEM_ID = "colored_glint_smithing_template";
    static List<ResourceLocation> emptyDyes = new ArrayList<>();
    public static Supplier<Item> item;

    @Configurable(
        name = "Loot table",
        description = "Loot table in which a colored glint smithing template will be added."
    )
    public static String lootTable = "minecraft:chests/stronghold_library";

    @Configurable(
        name = "Loot chance",
        description = "Chance (out of 1.0) of a colored glint smithing template appearing in loot."
    )
    public static double lootChance = 1.0D;

    @Override
    public String description() {
        return "Smithing template that changes the glint color of any enchanted item.";
    }

    @Override
    public void register() {
        var registry = mod().registry();

        emptyDyes.addAll(List.of(
            mod().id("item/empty_dye_01"),
            mod().id("item/empty_dye_02"),
            mod().id("item/empty_dye_03"),
            mod().id("item/empty_dye_04")
        ));

        item = registry.item(ITEM_ID,
            ColoredGlintTemplateItem::new);
    }

    @Override
    public void runWhenEnabled() {
        SmithingTableEvents.CAN_PLACE.handle(this::handleCanPlace);
        SmithingTableEvents.CALCULATE_OUTPUT.handle(this::handleCalculateOutput);
        SmithingTableEvents.CAN_TAKE.handle(this::handleCanTake);
        SmithingTableEvents.ON_TAKE.handle(this::handleOnTake);
        LootTableModifyEvent.INSTANCE.handle(this::handleLootTableModify);
    }

    private Optional<LootPool.Builder> handleLootTableModify(LootDataManager manager, ResourceLocation id) {
        if (id.toString().equals(lootTable)) {
            var builder = LootPool.lootPool();

            if (RandomSource.create().nextDouble() < lootChance) {
                builder.setRolls(ConstantValue.exactly(1));
            }

            builder.add(LootItem.lootTableItem(item.get()).setWeight(1));
            return Optional.of(builder);
        }
        return Optional.empty();
    }

    private InteractionResult handleCanTake(SmithingTableInstance instance, Player player) {
        if (!instance.output.isEmpty()) {
            var out = instance.output.getItem(0);
            if (ColoredGlints.hasColoredGlint(out)) {
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private boolean handleCalculateOutput(SmithingTableInstance instance) {
        var input = instance.input;
        var slot0 = input.getItem(0);
        var slot1 = input.getItem(1);
        var slot2 = input.getItem(2);

        if (slot0.is(ColoredGlintSmithingTemplates.item.get())) {
            if (!slot1.isEnchanted() || !slot2.is(CharmTags.COLORED_DYES)) {
                instance.output.setItem(0, ItemStack.EMPTY);
                return true;
            }

            var dyeColor = ((DyeItem)slot2.getItem()).getDyeColor();
            var itemToChange = slot1.copy();

            ColoredGlints.applyColoredGlint(itemToChange, dyeColor);
            instance.output.setItem(0, itemToChange);
            return true;
        }

        return false;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean handleCanPlace(ItemStack template, int slotNumber, ItemStack stack) {
        if (slotNumber == 0 && stack.is(template.getItem())) {
            return true;
        } else if (slotNumber == 1 && stack.isEnchanted()) {
            return true;
        } else if (slotNumber == 2 && stack.is(CharmTags.COLORED_DYES)) {
            return true;
        }

        return false;
    }

    /**
     * We just hook in here to trigger the advancement.
     */
    private boolean handleOnTake(SmithingTableInstance instance, Player player, ItemStack stack) {
        if (ColoredGlints.hasColoredGlint(stack)) {
            triggerAppliedColoredGlintTemplate(player);
        }
        return false;
    }

    public static void triggerAppliedColoredGlintTemplate(Player player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "applied_colored_glint_template"), player);
    }
}
