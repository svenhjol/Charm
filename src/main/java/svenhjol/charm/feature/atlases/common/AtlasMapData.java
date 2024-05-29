package svenhjol.charm.feature.atlases.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.atlases.Atlases;

@SuppressWarnings("unused")
public record AtlasMapData(ItemStack map, MapId mapId, int x, int z, ResourceKey<Level> dimension) {
    private static final Atlases ATLASES = Resolve.feature(Atlases.class);

    public static final Codec<AtlasMapData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.OPTIONAL_CODEC.fieldOf("map")
            .forGetter(AtlasMapData::map),
        MapId.CODEC.fieldOf("map_id")
            .forGetter(AtlasMapData::mapId),
        Codec.INT.fieldOf("x")
            .forGetter(AtlasMapData::x),
        Codec.INT.fieldOf("z")
            .forGetter(AtlasMapData::z),
        ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension")
            .forGetter(a -> a.dimension)
    ).apply(instance, AtlasMapData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AtlasMapData> STREAM_CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC,
            AtlasMapData::map,
        MapId.STREAM_CODEC,
            AtlasMapData::mapId,
        ByteBufCodecs.INT,
            AtlasMapData::x,
        ByteBufCodecs.INT,
            AtlasMapData::z,
        ResourceKey.streamCodec(Registries.DIMENSION),
            AtlasMapData::dimension,
        AtlasMapData::new
    );

    public static AtlasMapData fromMapInfo(AtlasInventory.MapInfo mapInfo) {
        return new AtlasMapData(
            mapInfo.map,
            mapInfo.mapId,
            mapInfo.x,
            mapInfo.z,
            mapInfo.dimension
        );
    }

    public static boolean has(ItemStack stack) {
        return stack.has(ATLASES.registers.mapData.get());
    }

    public static AtlasMapData get(ItemStack stack) {
        return stack.get(ATLASES.registers.mapData.get());
    }

    public static Mutable getMutable(ItemStack stack) {
        return new Mutable(get(stack));
    }

    public static ItemStack set(ItemStack stack, Mutable data) {
        var immutable = data.toImmutable();
        stack.set(ATLASES.registers.mapData.get(), immutable);
        return stack;
    }

    public static class Mutable {
        private ItemStack map;
        private MapId mapId;
        private int x;
        private int z;
        private ResourceKey<Level> dimension;

        public Mutable(AtlasMapData data) {
            this.map = data.map;
            this.mapId = data.mapId;
            this.x = data.x;
            this.z = data.z;
            this.dimension = data.dimension;
        }

        public AtlasMapData toImmutable() {
            return new AtlasMapData(map, mapId, x, z, dimension);
        }

        public void save(ItemStack stack) {
            AtlasMapData.set(stack, this);
        }

        public Mutable setDimension(ResourceKey<Level> dimension) {
            this.dimension = dimension;
            return this;
        }

        public Mutable setMap(ItemStack map) {
            this.map = map;
            return this;
        }

        public Mutable setMapId(MapId mapId) {
            this.mapId = mapId;
            return this;
        }

        public Mutable setX(int x) {
            this.x = x;
            return this;
        }

        public Mutable setZ(int z) {
            this.z = z;
            return this;
        }
    }
}
