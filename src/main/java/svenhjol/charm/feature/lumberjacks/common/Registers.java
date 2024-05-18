package svenhjol.charm.feature.lumberjacks.common;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.feature.core.custom_wood.common.Tags;
import svenhjol.charm.feature.lumberjacks.Lumberjacks;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.feature.RegisterHolder;
import svenhjol.charm.foundation.villages.GenericTrades;

import java.util.List;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Lumberjacks> {
    private static final Woodcutters WOODCUTTERS = Resolve.feature(Woodcutters.class);

    public final Supplier<VillagerProfession> profession;
    public final Supplier<SoundEvent> workSound;

    public Registers(Lumberjacks feature) {
        super(feature);
        var registry = feature().registry();

        workSound = registry.soundEvent(Lumberjacks.PROFESSION_ID);
        profession = registry.villagerProfession(Lumberjacks.PROFESSION_ID,
            Woodcutters.BLOCK_ID,
            List.of(WOODCUTTERS.registers.block),
            workSound);

        registry.villagerGift(Lumberjacks.PROFESSION_ID); // TODO: seems to be absent in Charm 1.20.4?
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();

        // Tier 1
        registry.villagerTrade(profession, 1, () -> new GenericTrades.EmeraldsForTag<>(
            Tags.OVERWORLD_STRIPPED_LOGS, 8, 1, 1, 0, 2, 20));

        registry.villagerTrade(profession, 1, () -> new GenericTrades.EmeraldsForTag<>(
            BlockTags.OVERWORLD_NATURAL_LOGS, 8, 1, 1, 0, 2, 20));

        registry.villagerTrade(profession, 3, () -> new Trades.SaplingsForEmeralds(
            List.of(Items.OAK_SAPLING, Items.BIRCH_SAPLING, Items.SPRUCE_SAPLING), 1, 0, 2,  20));

        if (Lumberjacks.customLadders) {
            registry.villagerTrade(profession, 1, () -> new GenericTrades.TagForEmeralds<>(
                Tags.LADDERS, 1, 1, 2, 20));
        } else {
            registry.villagerTrade(profession, 1, () -> new GenericTrades.ItemsForEmeralds(
                Items.LADDER, 1, 1, 2, 20));
        }

        // Tier 2
        registry.villagerTrade(profession, 2, () -> new GenericTrades.EmeraldsForItems(
            Items.BONE, 23, 2, 1, 0, 5, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTrades.TagForEmeralds<>(
            BlockTags.BEDS, 3, 2, 1, 0, 7, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTrades.TagForEmeralds<>(
            BlockTags.WOODEN_FENCES, 2, 1, 1, 0, 6, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTrades.TagForEmeralds<>(
            BlockTags.FENCE_GATES, 2, 1, 1, 0, 6, 20));

        // Tier 3
        registry.villagerTrade(profession, 3, () -> new GenericTrades.EmeraldsForTag<>(
            BlockTags.WARPED_STEMS, 7, 1, 1, 0, 10, 20));

        registry.villagerTrade(profession, 3, () -> new GenericTrades.EmeraldsForTag<>(
            BlockTags.CRIMSON_STEMS, 7, 1, 1, 0, 10, 20));

        registry.villagerTrade(profession, 3, () -> new Trades.SaplingsForEmeralds(
            List.of(Items.ACACIA_SAPLING, Items.DARK_OAK_SAPLING), 2, 1, 10,  20));

        registry.villagerTrade(profession, 3, () -> new Trades.BarkForLogs(
            10, 12, 10, 10));

        registry.villagerTrade(profession, 3, () -> new GenericTrades.TagForEmeralds<>(
            BlockTags.WOODEN_DOORS, 2, 1, 1, 0, 10, 20));

        // Tier 4
        if (Lumberjacks.customBarrels) {
            registry.villagerTrade(profession, 4, () -> new GenericTrades.TagForEmeralds<>(
                Tags.BARRELS, 4, 1, 1, 0, 15, 20));
        } else {
            registry.villagerTrade(profession, 4, () -> new GenericTrades.ItemsForEmeralds(
                Items.BARREL, 4, 1, 15, 20));
        }

        if (Lumberjacks.customBookshelves) {
            registry.villagerTrade(profession, 4, () -> new GenericTrades.TagForEmeralds<>(
                Tags.CHISELED_BOOKSHELVES, 4, 1, 1, 0, 15, 20));
        } else {
            registry.villagerTrade(profession, 4, () -> new GenericTrades.ItemsForEmeralds(
                Items.BOOKSHELF, 4, 1, 15, 20));
        }

        registry.villagerTrade(profession, 4, () -> new GenericTrades.ItemsForEmeralds(
            Blocks.NOTE_BLOCK, 7, 1, 1, 0, 15, 20));

        // Tier 5
        registry.villagerTrade(profession, 5, () -> new GenericTrades.ItemsForEmeralds(
            Blocks.JUKEBOX, 11, 3, 1, 0, 15, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTrades.ItemsForEmeralds(
            Blocks.CARTOGRAPHY_TABLE, 5, 1, 1, 0, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTrades.ItemsForEmeralds(
            Blocks.LOOM, 4, 1, 1, 0, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTrades.ItemsForEmeralds(
            Blocks.COMPOSTER, 3, 1, 1, 0, 30, 20));
    }
}
