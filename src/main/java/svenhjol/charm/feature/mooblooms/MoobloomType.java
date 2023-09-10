package svenhjol.charm.feature.mooblooms;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import svenhjol.charm.Charm;

import java.util.List;
import java.util.Optional;

public enum MoobloomType {
    ALLIUM("allium", Blocks.ALLIUM.defaultBlockState()),
    AZURE_BLUET("azure_bluet", Blocks.AZURE_BLUET.defaultBlockState()),
    BLUE_ORCHID("blue_orchid", Blocks.BLUE_ORCHID.defaultBlockState()),
    CHERRY_BLOSSOM("cherry_blossom", Blocks.PINK_PETALS.defaultBlockState()
        .setValue(PinkPetalsBlock.AMOUNT, 1)),
    CORNFLOWER("cornflower", Blocks.CORNFLOWER.defaultBlockState()),
    DANDELION("dandelion", Blocks.DANDELION.defaultBlockState()),
    LILY_OF_THE_VALLEY("lily_of_the_valley", Blocks.LILY_OF_THE_VALLEY.defaultBlockState()),
    ORANGE_TULIP("orange_tulip", Blocks.ORANGE_TULIP.defaultBlockState()),
    OXEYE_DAISY("oxeye_daisy", Blocks.OXEYE_DAISY.defaultBlockState()),
    PINK_TULIP("pink_tulip", Blocks.PINK_TULIP.defaultBlockState()),
    POPPY("poppy", Blocks.POPPY.defaultBlockState()),
    RED_TULIP("red_tulip", Blocks.RED_TULIP.defaultBlockState()),
    SUNFLOWER("sunflower", Blocks.SUNFLOWER.defaultBlockState()),
    WHITE_TULIP("white_tulip", Blocks.WHITE_TULIP.defaultBlockState());

    private final String name;
    private final BlockState flower;
    private final ResourceLocation texture;

    private static final int CHERRY_BLOSSOM_HEALING_DURATION = 4;
    private static final int SUNFLOWER_HEALTH_DURATION = 12;

    public final static List<MoobloomType> COMMON_TYPES = List.of(
        ALLIUM, AZURE_BLUET, BLUE_ORCHID, CORNFLOWER, DANDELION,
        LILY_OF_THE_VALLEY, ORANGE_TULIP, OXEYE_DAISY, PINK_TULIP,
        POPPY, RED_TULIP, WHITE_TULIP
    );

    MoobloomType(String name, BlockState flower) {
        this.name = name;
        this.flower = flower;
        this.texture = Charm.instance().makeId("textures/entity/moobloom/" + name + ".png");
    }

    public BlockState getFlower() {
        return this.flower;
    }

    public Optional<Pair<MobEffect, Integer>> getEffect() {
        var block = flower.getBlock();
        if (block instanceof FlowerBlock flowerBlock) {
            return Optional.of(Pair.of(flowerBlock.getSuspiciousEffect(), flowerBlock.getEffectDuration()));
        } else if (this.equals(SUNFLOWER)) {
            return Optional.of(Pair.of(MobEffects.HEALTH_BOOST, SUNFLOWER_HEALTH_DURATION * 20));
        } else if (this.equals(CHERRY_BLOSSOM)) {
            return Optional.of(Pair.of(MobEffects.HEAL, CHERRY_BLOSSOM_HEALING_DURATION * 20));
        }

        return Optional.empty();
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
}