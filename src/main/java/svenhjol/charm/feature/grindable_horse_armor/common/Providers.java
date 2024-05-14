package svenhjol.charm.feature.grindable_horse_armor.common;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.IGrindableItemProvider;
import svenhjol.charm.feature.grindable_horse_armor.GrindableHorseArmor;
import svenhjol.charm.foundation.feature.ProviderHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.List;

public final class Providers extends ProviderHolder<GrindableHorseArmor> implements IGrindableItemProvider {
    public Providers(GrindableHorseArmor feature) {
        super(feature);
    }

    @Override
    public List<Pair<ItemLike, ItemLike>> getItemGrindResults() {
        return List.of(
            Pair.of(Items.SADDLE, Items.LEATHER),
            Pair.of(Items.LEATHER_HORSE_ARMOR, Items.LEATHER),
            Pair.of(Items.IRON_HORSE_ARMOR, Items.IRON_INGOT),
            Pair.of(Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT),
            Pair.of(Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND)
        );
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(IGrindableItemProvider.class,
            provider -> provider.getItemGrindResults().forEach(
                result -> feature().registers.recipes.put(result.getFirst(), result.getSecond())));
    }
}
