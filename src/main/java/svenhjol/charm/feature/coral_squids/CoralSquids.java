package svenhjol.charm.feature.coral_squids;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.IWandererTrade;
import svenhjol.charm.api.iface.IWandererTradeProvider;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class CoralSquids extends CommonFeature implements IWandererTradeProvider {
    public static Supplier<Item> spawnEggItem;
    public static Supplier<Item> bucketItem;
    public static Supplier<EntityType<CoralSquidEntity>> entity;

    @Configurable(name = "Drop chance", description = "Chance (out of 1.0) of a coral squid dropping coral when killed.")
    public static double dropChance = 0.2D;

    @Override
    public String description() {
        return "Coral Squids spawn near coral in warm oceans.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    @Override
    public List<IWandererTrade> getRareWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return bucketItem.get();
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public int getCost() {
                return 12;
            }
        });
    }
}