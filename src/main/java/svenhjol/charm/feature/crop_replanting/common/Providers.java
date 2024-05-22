package svenhjol.charm.feature.crop_replanting.common;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.iface.QuickReplantProvider;
import svenhjol.charm.feature.crop_replanting.CropReplanting;
import svenhjol.charm.foundation.feature.ProviderHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class Providers extends ProviderHolder<CropReplanting> implements QuickReplantProvider {
    public final List<BlockState> replantable = new ArrayList<>();
    public final List<Block> notReplantable = List.of(
        Blocks.TORCHFLOWER,
        Blocks.PITCHER_CROP,
        Blocks.PITCHER_PLANT
    );

    public Providers(CropReplanting feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(QuickReplantProvider.class,
            provider -> provider.getHarvestableBlocks().forEach(
                supplier -> replantable.add(supplier.get())));
    }

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public List<Supplier<BlockState>> getHarvestableBlocks() {
        List<Supplier<BlockState>> harvestables = new ArrayList<>(List.of(
            () -> Blocks.BEETROOTS.defaultBlockState().setValue(BeetrootBlock.AGE, 3),
            () -> Blocks.CARROTS.defaultBlockState().setValue(CarrotBlock.AGE, 7),
            () -> Blocks.NETHER_WART.defaultBlockState().setValue(NetherWartBlock.AGE, 3),
            () -> Blocks.POTATOES.defaultBlockState().setValue(PotatoBlock.AGE, 7),
            () -> Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 7),
            () -> Blocks.PITCHER_CROP.defaultBlockState().setValue(PitcherCropBlock.AGE, 4),
            () -> Blocks.PITCHER_PLANT.defaultBlockState(),
            () -> Blocks.TORCHFLOWER.defaultBlockState()
        ));

        // Cocoa also has FACING property. We need to capture all states with AGE=2.
        Blocks.COCOA.getStateDefinition().getPossibleStates().stream()
            .filter(s -> s.getValue(CocoaBlock.AGE) == 2)
            .forEach(s -> harvestables.add(() -> s));

        return harvestables;
    }
}
