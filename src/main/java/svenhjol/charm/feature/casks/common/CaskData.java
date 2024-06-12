package svenhjol.charm.feature.casks.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CaskData(
    int bottles,
    double fermentation,
    List<ResourceLocation> effects,
    Map<ResourceLocation, Integer> durations,
    Map<ResourceLocation, Integer> amplifiers,
    Map<ResourceLocation, Integer> dilutions
) {
    public static final Codec<CaskData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("bottles")
            .forGetter(CaskData::bottles),
        Codec.DOUBLE.fieldOf("fermentation")
            .forGetter(CaskData::fermentation),
        ResourceLocation.CODEC.listOf().fieldOf("effects")
            .forGetter(CaskData::effects),
        Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("durations")
            .forGetter(CaskData::durations),
        Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("amplifiers")
            .forGetter(CaskData::amplifiers),
        Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("dilutions")
            .forGetter(CaskData::dilutions)
    ).apply(instance, CaskData::new));

    public static final StreamCodec<FriendlyByteBuf, CaskData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, CaskData::bottles,
        ByteBufCodecs.DOUBLE, CaskData::fermentation,
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), CaskData::effects,
        ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT), CaskData::durations,
        ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT), CaskData::amplifiers,
        ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT), CaskData::dilutions,
        CaskData::new
    );
    
    public static final double DEFAULT_FERMENTATION = 1.0d;

    public static final CaskData EMPTY = new CaskData(
        0,
        DEFAULT_FERMENTATION,
        new ArrayList<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>());
}
