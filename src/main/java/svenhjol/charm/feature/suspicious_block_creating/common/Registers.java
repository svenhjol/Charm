package svenhjol.charm.feature.suspicious_block_creating.common;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.feature.suspicious_block_creating.SuspiciousBlockCreating;
import svenhjol.charm.charmony.feature.RegisterHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<SuspiciousBlockCreating> {
    public final Map<Block, Block> blockConversions = new HashMap<>();
    public final Supplier<SoundEvent> createBlockSound;

    public Registers(SuspiciousBlockCreating feature) {
        super(feature);

        createBlockSound = feature().registry().soundEvent("create_suspicious_block");
        registerBlockConversion(Blocks.SAND, Blocks.SUSPICIOUS_SAND);
        registerBlockConversion(Blocks.GRAVEL, Blocks.SUSPICIOUS_GRAVEL);
    }

    public void registerBlockConversion(Block normal, Block suspicious) {
        blockConversions.put(normal, suspicious);
    }
}
