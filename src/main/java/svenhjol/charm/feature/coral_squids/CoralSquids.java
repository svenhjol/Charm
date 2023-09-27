package svenhjol.charm.feature.coral_squids;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony_api.iface.IWandererTrade;
import svenhjol.charmony_api.iface.IWandererTradeProvider;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Coral Squids spawn near coral in warm oceans.")
public class CoralSquids extends CharmFeature implements IWandererTradeProvider {
    private static final String ID = "coral_squid";
    public final static TagKey<Biome> SPAWNS_CORAL_SQUIDS =
        TagKey.create(Registries.BIOME, Charm.instance().makeId("spawns_coral_squids"));
    public static Supplier<Item> spawnEggItem;
    public static Supplier<Item> bucketItem;
    public static Supplier<EntityType<CoralSquidEntity>> entity;

    @Configurable(name = "Drop chance", description = "Chance (out of 1.0) of a coral squid dropping coral when killed.")
    public static double dropChance = 0.2D;

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        entity = registry.entity(ID, () -> EntityType.Builder
            .of(CoralSquidEntity::new, MobCategory.WATER_AMBIENT)
            .sized(0.54F, 0.54F));

        spawnEggItem = registry.spawnEggItem("coral_squid_spawn_egg", entity, 0x0000FF, 0xFF00FF, new Item.Properties());
        bucketItem = registry.item("coral_squid_bucket", () -> new CoralSquidBucketItem(this));

        registry.biomeSpawn(holder -> holder.is(SPAWNS_CORAL_SQUIDS), MobCategory.WATER_AMBIENT, entity, 50, 2, 4);
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