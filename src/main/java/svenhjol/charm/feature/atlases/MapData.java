package svenhjol.charm.feature.atlases;

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

public record MapData(ItemStack map, MapId mapId, int x, int z, ResourceKey<Level> dimension) {
    public static final Codec<MapData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.OPTIONAL_CODEC.fieldOf("map")
            .forGetter(MapData::map),
        MapId.CODEC.fieldOf("map_id")
            .forGetter(MapData::mapId),
        Codec.INT.fieldOf("x")
            .forGetter(MapData::x),
        Codec.INT.fieldOf("z")
            .forGetter(MapData::z),
        ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension")
            .forGetter(a -> a.dimension)
    ).apply(instance, MapData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MapData> STREAM_CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC,
            MapData::map,
        MapId.STREAM_CODEC,
            MapData::mapId,
        ByteBufCodecs.INT,
            MapData::x,
        ByteBufCodecs.INT,
            MapData::z,
        ResourceKey.streamCodec(Registries.DIMENSION),
            MapData::dimension,
        MapData::new
    );

    public static MapData fromMapInfo(AtlasInventory.MapInfo mapInfo) {
        return new MapData(
            mapInfo.map,
            mapInfo.mapId,
            mapInfo.x,
            mapInfo.z,
            mapInfo.dimension
        );
    }

    public static boolean has(ItemStack stack) {
        return stack.has(Atlases.mapData.get());
    }

    public static MapData get(ItemStack stack) {
        return stack.get(Atlases.mapData.get());
    }

    public static MapData.Mutable getMutable(ItemStack stack) {
        return new MapData.Mutable(get(stack));
    }

    public static ItemStack set(ItemStack stack, MapData.Mutable data) {
        var immutable = data.toImmutable();
        stack.set(Atlases.mapData.get(), immutable);
        return stack;
    }

    public static class Mutable {
        private ItemStack map;
        private MapId mapId;
        private int x;
        private int z;
        private ResourceKey<Level> dimension;

        public Mutable(MapData data) {
            this.map = data.map;
            this.mapId = data.mapId;
            this.x = data.x;
            this.z = data.z;
            this.dimension = data.dimension;
        }

        public MapData toImmutable() {
            return new MapData(map, mapId, x, z, dimension);
        }

        public void setDimension(ResourceKey<Level> dimension) {
            this.dimension = dimension;
        }

        public void setMap(ItemStack map) {
            this.map = map;
        }

        public void setMapId(MapId mapId) {
            this.mapId = mapId;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setZ(int z) {
            this.z = z;
        }
    }
}
