package svenhjol.charm.mixin.deepslate_dungeons;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.feature.deepslate_dungeons.DeepslateDungeons;

import java.util.function.Predicate;

@Mixin(MonsterRoomFeature.class)
public abstract class MonsterRoomFeatureMixin extends Feature<NoneFeatureConfiguration> {
    public MonsterRoomFeatureMixin(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected void safeSetBlock(WorldGenLevel level, BlockPos pos, BlockState state, Predicate<BlockState> predicate) {
        state = DeepslateDungeons.changeBlockState(level, pos, state);
        super.safeSetBlock(level, pos, state, predicate);
    }
}