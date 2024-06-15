package svenhjol.charm.feature.animal_armor_grinding.common;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.GrindableItemProvider;
import svenhjol.charm.charmony.Api;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.feature.animal_armor_grinding.AnimalArmorGrinding;

import java.util.List;

public final class Providers extends ProviderHolder<AnimalArmorGrinding> implements GrindableItemProvider {
    public Providers(AnimalArmorGrinding feature) {
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
        Api.consume(GrindableItemProvider.class,
            provider -> provider.getItemGrindResults().forEach(
                result -> feature().registers.recipes.put(result.getFirst(), result.getSecond())));
    }
}
