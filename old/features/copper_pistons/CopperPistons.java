package svenhjol.charm.feature.copper_pistons;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;
import java.util.function.Supplier;

public class CopperPistons extends CommonFeature {
    public static Supplier<Block> copperPistonBlock;
    public static Supplier<Block> copperPistonHeadBlock;
    public static Supplier<Block> movingCopperPistonBlock;
    public static Supplier<Block> stickyCopperPistonBlock;
    public static Supplier<Item> copperPistonBlockItem;
    public static Supplier<Item> stickyCopperPistonBlockItem;

    @Override
    public String description() {
        return "Copper Pistons do not have quasi-connectivity.";
    }

    @Override
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }
}
