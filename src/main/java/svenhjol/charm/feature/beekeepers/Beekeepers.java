package svenhjol.charm.feature.beekeepers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.GenericTradeOffers;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Beekeepers are villagers that trade beekeeping items. Their job site is the beehive.")
public class Beekeepers extends CharmonyFeature {
    private static final String VILLAGER_ID = "beekeeper";
    private static final String BLOCK_ID = "minecraft:beehive";
    static final TagKey<Item> BEEKEEPER_SELLS_FLOWERS = TagKey.create(BuiltInRegistries.ITEM.key(),
        Charm.instance().makeId("beekeeper_sells_flowers"));
    public static Supplier<VillagerProfession> profession;

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        profession = registry.villagerProfession(
            VILLAGER_ID, BLOCK_ID, List.of(), () -> SoundEvents.BEEHIVE_WORK);

        registry.villagerGift(VILLAGER_ID);

        if (isEnabled()) {
            addTrades();
        }
    }

    private void addTrades() {
        var registry = Charm.instance().registry();

        // Tier 1

        registry.villagerTrade(profession, 1, () -> new BeekeeperTradeOffers.EmeraldsForFlowers(
            14, 2, 1, 0, 2, 20));

        registry.villagerTrade(profession, 1, () -> new GenericTradeOffers.ItemsForEmeralds(
            Items.GLASS_BOTTLE, 1, 4, 2, 20));

        // Tier 2

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.EmeraldsForItems(
            Items.CHARCOAL, 15, 2, 1, 0, 5, 20));

        registry.villagerTrade(profession, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            ItemTags.CANDLES, 3, 1, 5, 20));

        // Tier 3

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.EmeraldsForItems(
            Items.HONEYCOMB, 9, 2, 1, 0, 10, 20));

        registry.villagerTrade(profession, 3, () -> new GenericTradeOffers.ItemsForEmeralds(
            Items.CAMPFIRE, 2, 1, 10, 20));

        // Tier 4

        registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.EmeraldsForItems(
            Items.FLOWERING_AZALEA_LEAVES, 7, 1, 1, 0, 15, 20));

        registry.villagerTrade(profession, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Items.LEAD, 6, 1, 15, 20));

        registry.villagerTrade(profession, 4, () -> new BeekeeperTradeOffers.TallFlowerForEmeralds(
            3, 1, 15, 20));

        // Tier 5

        registry.villagerTrade(profession, 5, () -> new BeekeeperTradeOffers.PopulatedBeehiveForEmeralds(
            47, 0, 30, 10));

        registry.villagerTrade(profession, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Items.SOUL_CAMPFIRE, 5, 1, 30, 20));

        registry.villagerTrade(profession, 5, () -> new BeekeeperTradeOffers.EnchantedShearsForEmeralds(
            6, 0, 15, 20));
    }

    public static void triggerTradedWithBeekeeper(Player player) {
        Advancements.trigger(Charm.instance().makeId("traded_with_beekeeper"), player);
    }
}
