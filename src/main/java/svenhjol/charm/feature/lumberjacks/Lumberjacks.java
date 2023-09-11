package svenhjol.charm.feature.lumberjacks;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.GenericTradeOffers;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.")
public class Lumberjacks extends CharmFeature {
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
//        registry.pointOfInterestBlockStates(() -> poitKey, () -> Woodcutters.block.get().getStateDefinition().getPossibleStates());
        registry.villagerGift(VILLAGER_ID);

        if (isEnabled()) {
            addTrades();
        }
    }

    private void addTrades() {
        var registry = Charm.instance().registry();

        // Tier 1

        registry.villagerTrade(profession, 1, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.LOGS_THAT_BURN, 8, 1, 2, 20));

        registry.villagerTrade(profession, 1, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.SAPLINGS, 1, 1, 2, 20));

        registry.villagerTrade(profession, 1, () -> new GenericTradeOffers.EmeraldsForItems(
            Items.LADDER, 1, 1, 2, 20));

        // Tier 2

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.EmeraldsForItems(
            Items.BONE, 10, 2, 1, 0, 5, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.BEDS, 3, 1, 7, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.WOODEN_FENCES, 1, 2, 6, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.FENCE_GATES, 1, 2, 6, 20));

        // Tier 3

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.WARPED_STEMS, 7, 1, 10, 20));

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.CRIMSON_STEMS, 7, 1, 10, 20));

        registry.villagerTrade(profession, 3, () -> new LumberjackTradeOffers.BarkForLogs(
            5, 12, 1, 0, 10, 10));

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.WOODEN_DOORS, 1, 1, 10, 20));

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.WOODEN_TRAPDOORS, 1, 1, 10, 20));

        // Tier 4

        registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.BARREL, 3, 1, 1, 1, 15, 20));

        registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.CHEST, 3, 1, 1, 1, 15, 20));

        registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.NOTE_BLOCK, 8, 1, 15, 20));

        registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.JUKEBOX, 8, 1, 15, 20));

        // Tier 5

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.FLETCHING_TABLE, 5, 1, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.CRAFTING_TABLE, 5, 1, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.CARTOGRAPHY_TABLE, 5, 1, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.LOOM, 5, 1, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.COMPOSTER, 5, 1, 30, 20));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Woodcutters.block.get(), 5, 1, 30, 20));
    }
}
