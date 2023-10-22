package svenhjol.charm.feature.coral_squids;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.CharmTags;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony_api.iface.IWandererTrade;
import svenhjol.charmony_api.iface.IWandererTradeProvider;

import java.util.List;
import java.util.function.Supplier;

public class CoralSquids extends CommonFeature implements IWandererTradeProvider {
    private static final String ID = "coral_squid";
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
    public void register() {
        var registry = mod().registry();

        entity = registry.entity(ID, () -> EntityType.Builder
            .of(CoralSquidEntity::new, MobCategory.WATER_AMBIENT)
            .sized(0.54f, 0.54f));

        spawnEggItem = registry.spawnEggItem("coral_squid_spawn_egg", entity, 0x0000ff, 0xff00ff, new Item.Properties());
        bucketItem = registry.item("coral_squid_bucket", CoralSquidBucketItem::new);

        registry.biomeSpawn(holder -> holder.is(CharmTags.SPAWNS_CORAL_SQUIDS), MobCategory.WATER_AMBIENT, entity, 50, 2, 4);
        registry.entitySpawnPlacement(entity, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CoralSquidEntity::canSpawn);
        registry.entityAttributes(entity, CoralSquidEntity::createSquidAttributes);
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