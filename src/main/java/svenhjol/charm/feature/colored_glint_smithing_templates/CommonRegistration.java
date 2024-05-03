package svenhjol.charm.feature.colored_glint_smithing_templates;

import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import svenhjol.charm.api.event.LootTableModifyEvent;
import svenhjol.charm.api.event.SmithingTableEvents;
import svenhjol.charm.api.event.SmithingTableEvents.SmithingTableInstance;
import svenhjol.charm.feature.colored_glints.ColoredGlints;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.Tags;

import java.util.List;
import java.util.Optional;

public final class CommonRegistration extends Registration<ColoredGlintSmithingTemplates> {
    static final String ITEM_ID = "colored_glint_smithing_template";

    public CommonRegistration(ColoredGlintSmithingTemplates feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        ColoredGlintSmithingTemplates.emptyDyes.addAll(List.of(
            registry.id("item/empty_dye_01"),
            registry.id("item/empty_dye_02"),
            registry.id("item/empty_dye_03"),
            registry.id("item/empty_dye_04")
        ));

        ColoredGlintSmithingTemplates.item = registry.item(ITEM_ID,
            ColoredGlintTemplateItem::new);

        ColoredGlintSmithingTemplates.lootTable = registry.lootTable(ColoredGlintSmithingTemplates.configurableLootTable);
    }

    @Override
    public void onEnabled() {
        SmithingTableEvents.CAN_PLACE.handle(this::handleCanPlace);
        SmithingTableEvents.CALCULATE_OUTPUT.handle(this::handleCalculateOutput);
        SmithingTableEvents.CAN_TAKE.handle(this::handleCanTake);
        SmithingTableEvents.ON_TAKE.handle(this::handleOnTake);
        LootTableModifyEvent.INSTANCE.handle(this::handleLootTableModify);
    }

    private Optional<LootPool.Builder> handleLootTableModify(ResourceKey<LootTable> key, LootTableSource source) {
        if (key == ColoredGlintSmithingTemplates.lootTable) {
            var builder = LootPool.lootPool();

            if (RandomSource.create().nextDouble() < ColoredGlintSmithingTemplates.lootChance) {
                builder.setRolls(ConstantValue.exactly(1));
            }

            builder.add(LootItem.lootTableItem(ColoredGlintSmithingTemplates.item.get()).setWeight(1));
            return Optional.of(builder);
        }

        return Optional.empty();
    }

    private InteractionResult handleCanTake(SmithingTableInstance instance, Player player) {
        if (!instance.output.isEmpty()) {
            var out = instance.output.getItem(0);
            if (ColoredGlints.stackHasFoilAndColor(out)) {
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
            if (!slot1.isEnchanted() || !slot2.is(Tags.COLORED_DYES)) {
                instance.output.setItem(0, ItemStack.EMPTY);
                return true;
            }

            var dyeColor = ((DyeItem)slot2.getItem()).getDyeColor();
            var itemToChange = slot1.copy();

            ColoredGlints.apply(itemToChange, dyeColor);
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
        } else if (slotNumber == 2 && stack.is(Tags.COLORED_DYES)) {
            return true;
        }

        return false;
    }

    /**
     * We just hook in here to trigger the advancement.
     */
    private boolean handleOnTake(SmithingTableInstance instance, Player player, ItemStack stack) {
        if (ColoredGlints.stackHasFoilAndColor(stack)) {
            ColoredGlintSmithingTemplates.triggerAppliedColoredGlintTemplate(player);
        }
        return false;
    }
}
