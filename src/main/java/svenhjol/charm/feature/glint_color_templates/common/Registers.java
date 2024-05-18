package svenhjol.charm.feature.glint_color_templates.common;

import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import svenhjol.charm.feature.glint_color_templates.GlintColorTemplates;
import svenhjol.charm.feature.glint_coloring.GlintColoring;
import svenhjol.charm.feature.glint_coloring.common.Tags;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<GlintColorTemplates> {
    public final List<ResourceLocation> emptyDyes = new ArrayList<>();
    public final ResourceKey<LootTable> lootTable;
    public final Supplier<net.minecraft.world.item.Item> item;

    public static final String ITEM_ID = "glint_color_template";

    public Registers(GlintColorTemplates feature) {
        super(feature);
        var registry = feature.registry();

        emptyDyes.addAll(List.of(
            registry.id("item/empty_dye_01"),
            registry.id("item/empty_dye_02"),
            registry.id("item/empty_dye_03"),
            registry.id("item/empty_dye_04")
        ));

        item = registry.item(ITEM_ID,
            Item::new);

        lootTable = registry.lootTable(GlintColorTemplates.configurableLootTable);
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
        if (key == lootTable) {
            var builder = LootPool.lootPool();

            if (RandomSource.create().nextDouble() < GlintColorTemplates.lootChance) {
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
            if (GlintColoring.has(out)) {
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

        if (slot0.is(item.get())) {
            if (!slot1.isEnchanted() || !slot2.is(Tags.COLORED_DYES)) {
                instance.output.setItem(0, ItemStack.EMPTY);
                return true;
            }

            var dyeColor = ((DyeItem)slot2.getItem()).getDyeColor();
            var itemToChange = slot1.copy();

            GlintColoring.apply(itemToChange, dyeColor);
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
        if (GlintColoring.has(stack)) {
            feature().advancements.appliedGlintColorTemplate(player);
        }
        return false;
    }
}
