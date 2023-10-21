package svenhjol.charm.feature.lumberjacks;

import net.minecraft.resources.ResourceLocation;
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
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.GenericTradeOffers;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Lumberjacks extends CommonFeature {
    private static final String ID = "lumberjack";
    public static Supplier<VillagerProfession> profession;
    public static Supplier<SoundEvent> workSound;

    @Override
    public String description() {
        return "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.";
    }

    @Override
    public int priority() {
        return -1;
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> mod().loader().isEnabled(Woodcutters.class));
    }

    @Override
    public void register() {
        var registry = mod().registry();

        workSound = registry.soundEvent("lumberjack");
        profession = registry.villagerProfession(ID, Woodcutters.BLOCK_ID, List.of(Woodcutters.block), workSound);
        registry.villagerGift(ID);

        if (isEnabled()) {
            addTrades();
        }
    }

    private void addTrades() {
        var charm = mod();
        var registry = charm.registry();
        var loader = charm.loader();

        var useVariantBarrels = loader.isEnabled(VariantWood.class) && VariantWood.variantBarrels;
        var usevariantBookshelves = loader.isEnabled(VariantWood.class) && VariantWood.variantBookshelves;
        var useVariantLadders = loader.isEnabled(VariantWood.class) && VariantWood.variantLadders;

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
            BlockTags.BEDS, 3, 2, 1, 0, 7, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.WOODEN_FENCES, 2, 1, 1, 0, 6, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.FENCE_GATES, 2, 1, 1, 0, 6, 20));

        // Tier 3

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.WARPED_STEMS, 7, 1, 1, 0, 10, 20));

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.CRIMSON_STEMS, 7, 1, 1, 0, 10, 20));

        registry.villagerTrade(profession, 3, () -> new LumberjackTradeOffers.SaplingsForEmeralds(
            List.of(Items.ACACIA_SAPLING, Items.DARK_OAK_SAPLING), 2, 1, 10,  20));

        registry.villagerTrade(profession, 3, () -> new LumberjackTradeOffers.BarkForLogs(
            10, 12, 10, 10));

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.WOODEN_DOORS, 2, 1, 1, 0, 10, 20));

        // Tier 4

        if (useVariantBarrels) {
            registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.TagForEmeralds<>(
                CharmTags.BARRELS, 4, 1, 1, 0, 15, 20));
        } else {
            registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
                Items.BARREL, 4, 1, 15, 20));
        }

        if (usevariantBookshelves) {
            registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.TagForEmeralds<>(
                CharmTags.CHISELED_BOOKSHELVES, 4, 1, 1, 0, 15, 20));
        } else {
            registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
                Items.BOOKSHELF, 4, 1, 15, 20));
        }

        registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.NOTE_BLOCK, 7, 1, 1, 0, 15, 20));

        // Tier 5

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.JUKEBOX, 11, 3, 1, 0, 15, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.CARTOGRAPHY_TABLE, 5, 1, 1, 0, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.LOOM, 4, 1, 1, 0, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.COMPOSTER, 3, 1, 1, 0, 30, 20));
    }

    public static void triggerTradedWithLumberjack(Player player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "traded_with_lumberjack"), player);
    }
}
