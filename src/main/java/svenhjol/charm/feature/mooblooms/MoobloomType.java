package svenhjol.charm.feature.mooblooms;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmTags;

import java.util.List;

public enum MoobloomType {
    ALLIUM("allium", FlowerBlockState.ALLIUM),
    AZURE_BLUET("azure_bluet", FlowerBlockState.AZURE_BLUET),
    BLUE_ORCHID("blue_orchid", FlowerBlockState.BLUE_ORCHID),
    CHERRY_BLOSSOM("cherry_blossom", FlowerBlockState.PINK_PETALS),
    CORNFLOWER("cornflower", FlowerBlockState.CORNFLOWER),
    DANDELION("dandelion", FlowerBlockState.DANDELION),
    LILY_OF_THE_VALLEY("lily_of_the_valley", FlowerBlockState.LILY_OF_THE_VALLEY),
    ORANGE_TULIP("orange_tulip", FlowerBlockState.ORANGE_TULIP),
    OXEYE_DAISY("oxeye_daisy", FlowerBlockState.OXEYE_DAISY),
    PINK_TULIP("pink_tulip", FlowerBlockState.PINK_TULIP),
    POPPY("poppy", FlowerBlockState.POPPY),
    RED_TULIP("red_tulip", FlowerBlockState.RED_TULIP),
    SUNFLOWER("sunflower", FlowerBlockState.SUNFLOWER),
    WHITE_TULIP("white_tulip", FlowerBlockState.WHITE_TULIP);

    private final String name;
    private final FlowerBlockState flower;
    private final ResourceLocation texture;
    public final static List<MoobloomType> COMMON_TYPES = List.of(
        ALLIUM, AZURE_BLUET, BLUE_ORCHID, CORNFLOWER, DANDELION,
        LILY_OF_THE_VALLEY, ORANGE_TULIP, OXEYE_DAISY, PINK_TULIP,
        POPPY, RED_TULIP, WHITE_TULIP
    );

    public final static List<MoobloomType> RARE_TYPES = List.of(
        SUNFLOWER, CHERRY_BLOSSOM
    );

    MoobloomType(String name, FlowerBlockState flower) {
        this.name = name;
        this.flower = flower;
        this.texture = new ResourceLocation(Charm.ID, "textures/entity/moobloom/" + name + ".png");
    }

    public FlowerBlockState getFlower() {
        return flower;
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public static MoobloomType fromName(String name) {
        for (var value : values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }

        return ALLIUM;
    }

    public static List<MoobloomType> getTypesForPos(LevelAccessor level, BlockPos pos) {
        List<MoobloomType> types;
        var biome = level.getBiome(pos);

        if (biome.is(CharmTags.SPAWNS_CHERRY_BLOSSOM_MOOBLOOMS)) {
            types = List.of(MoobloomType.CHERRY_BLOSSOM);
        } else if (biome.is(CharmTags.SPAWNS_SUNFLOWER_MOOBLOOMS)) {
            types = List.of(MoobloomType.SUNFLOWER);
        } else {
            types = MoobloomType.COMMON_TYPES;
        }

        return types;
    }
}