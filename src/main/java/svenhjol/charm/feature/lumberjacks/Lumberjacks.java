package svenhjol.charm.feature.lumberjacks;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmTags;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.GenericTradeOffers;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.", priority = -1)
public class Lumberjacks extends CharmonyFeature {
    private static final String VILLAGER_ID = "lumberjack";
    public static Supplier<VillagerProfession> profession;
    public static Supplier<SoundEvent> workSound;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(Woodcutters.class));
    }

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        workSound = registry.soundEvent("lumberjack");
        profession = registry.villagerProfession(VILLAGER_ID, Woodcutters.BLOCK_ID, List.of(Woodcutters.block), workSound);
        registry.villagerGift(VILLAGER_ID);

        if (isEnabled()) {
            addTrades();
        }
    }

    private void addTrades() {
        var charm = Charm.instance();
        var registry = charm.registry();
        var loader = charm.loader();

        var useVariantBarrels = loader.isEnabled(VariantWood.class) && VariantWood.variantBarrels;
        var usevariantBookshelves = loader.isEnabled(VariantWood.class) && VariantWood.variantBookshelves;
        var useVariantLadders = loader.isEnabled(VariantWood.class) && VariantWood.variantLadders;
        var useWoodcutters = loader.isEnabled(Woodcutters.class); // I don't know how you'd get a lumberjack without a woodcutter but just in case

        // Tier 1

        registry.villagerTrade(profession, 1, () -> new GenericTradeOffers.EmeraldsForTag<>(
            CharmTags.OVERWORLD_STRIPPED_LOGS, 8, 1, 1, 0, 2, 20));

        registry.villagerTrade(profession, 1, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.OVERWORLD_NATURAL_LOGS, 8, 1, 1, 0, 2, 20));

        registry.villagerTrade(profession, 3, () -> new LumberjackTradeOffers.SaplingsForEmeralds(
            List.of(Items.OAK_SAPLING, Items.BIRCH_SAPLING, Items.SPRUCE_SAPLING), 1, 0, 2,  20));

        if (useVariantLadders) {
            registry.villagerTrade(profession, 1, () -> new GenericTradeOffers.TagForEmeralds<>(
                CharmTags.LADDERS, 1, 1, 2, 20));
        } else {
            registry.villagerTrade(profession, 1, () -> new GenericTradeOffers.ItemsForEmeralds(
                Items.LADDER, 1, 1, 2, 20));
        }

        // Tier 2

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.EmeraldsForItems(
            Items.BONE, 23, 2, 1, 0, 5, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.BEDS, 4, 1, 7, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.WOODEN_FENCES, 4, 1, 6, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.FENCE_GATES, 2, 1, 6, 20));

        // Tier 3

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.WARPED_STEMS, 7, 1, 1, 0, 10, 20));

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.CRIMSON_STEMS, 7, 1, 1, 0, 10, 20));

        registry.villagerTrade(profession, 3, () -> new LumberjackTradeOffers.SaplingsForEmeralds(
            List.of(Items.ACACIA_SAPLING, Items.DARK_OAK_SAPLING), 3, 0, 10,  20));

        registry.villagerTrade(profession, 3, () -> new LumberjackTradeOffers.BarkForLogs(
            10, 12, 10, 10));

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.WOODEN_DOORS, 2, 1, 10, 20));

        // Tier 4

        if (useVariantBarrels) {
            registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.TagForEmeralds<>(
                CharmTags.BARRELS, 4, 1, 15, 20));
        } else {
            registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
                Items.BARREL, 4, 1, 15, 20));
        }

        if (usevariantBookshelves) {
            registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.TagForEmeralds<>(
                CharmTags.CHISELED_BOOKSHELVES, 4, 1, 15, 20));
        } else {
            registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
                Items.BOOKSHELF, 4, 1, 15, 20));
        }

        registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.NOTE_BLOCK, 7, 1, 15, 20));

        // Tier 5

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.JUKEBOX, 9, 1, 15, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.FLETCHING_TABLE, 5, 1, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.CARTOGRAPHY_TABLE, 5, 1, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.LOOM, 4, 1, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.COMPOSTER, 4, 1, 30, 20));

        if (useWoodcutters) {
            registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
                Woodcutters.block.get(), 5, 1, 30, 20));
        }
    }

    public static void triggerTradedWithLumberjack(Player player) {
        Advancements.trigger(Charm.instance().makeId("traded_with_lumberjack"), player);
    }
}
