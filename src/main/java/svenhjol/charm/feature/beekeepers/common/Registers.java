package svenhjol.charm.feature.beekeepers.common;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.common.villages.GenericTrades;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.beekeepers.Beekeepers;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Beekeepers> {
    private final Supplier<Optional<VillagerProfession>> profession;

    public Registers(Beekeepers feature) {
        super(feature);
        var registry = feature().registry();

        profession = registry.villagerProfession(feature, Beekeepers.VILLAGER_ID, Beekeepers.BLOCK_ID, List.of(),
            () -> SoundEvents.BEEHIVE_WORK);

        registry.villagerGift(Beekeepers.VILLAGER_ID);
    }

    public Optional<Supplier<VillagerProfession>> profession() {
        var optional = profession.get();
        return Optional.of(optional::get);
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();
        profession().ifPresent(profession -> {

            // Tier 1
            registry.villagerTrade(profession, 1, () -> new Trades.EmeraldsForFlowers(
                14, 2, 1, 0, 2, 20));

            registry.villagerTrade(profession, 1, () -> new GenericTrades.ItemsForEmeralds(
                Items.GLASS_BOTTLE, 1, 0, 3, 2, 2, 20));

            // Tier 2
            registry.villagerTrade(profession, 2, () -> new GenericTrades.EmeraldsForItems(
                Items.CHARCOAL, 15, 2, 1, 0, 5, 20));

            registry.villagerTrade(profession, 2, () -> new GenericTrades.TagForEmeralds<>(
                ItemTags.CANDLES, 3, 1, 1, 0, 5, 20));

            // Tier 3
            registry.villagerTrade(profession, 3, () -> new GenericTrades.EmeraldsForItems(
                Items.HONEYCOMB, 9, 2, 1, 0, 10, 20));

            registry.villagerTrade(profession, 3, () -> new GenericTrades.ItemsForEmeralds(
                Items.CAMPFIRE, 2, 1, 1, 0, 10, 20));

            // Tier 4
            registry.villagerTrade(profession, 4, () -> new GenericTrades.EmeraldsForItems(
                Items.FLOWERING_AZALEA_LEAVES, 7, 1, 1, 0, 15, 20));

            registry.villagerTrade(profession, 4, () -> new GenericTrades.ItemsForEmeralds(
                Items.LEAD, 6, 1, 1, 0, 15, 20));

            registry.villagerTrade(profession, 4, () -> new Trades.TallFlowerForEmeralds(
                3, 1, 15, 20));

            // Tier 5
            registry.villagerTrade(profession, 5, () -> new Trades.PopulatedBeehiveForEmeralds(
                47, 5, 30, 10));

            registry.villagerTrade(profession, 5, () -> new GenericTrades.ItemsForEmeralds(
                Items.SOUL_CAMPFIRE, 5, 1, 1, 0, 30, 20));

            registry.villagerTrade(profession, 5, () -> new Trades.EnchantedShearsForEmeralds(
                6, 0, 15, 20));
        });
    }
}
