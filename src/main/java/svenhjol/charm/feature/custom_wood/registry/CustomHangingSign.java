package svenhjol.charm.feature.custom_wood.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.block.CharmCeilingHangingSignBlock;
import svenhjol.charmony.block.CharmWallHangingSignBlock;
import svenhjol.charmony.item.CharmHangingSignItem;

import java.util.List;
import java.util.function.Supplier;

public class CustomHangingSign {
    public final Supplier<CharmCeilingHangingSignBlock> hangingBlock;
    public final Supplier<CharmWallHangingSignBlock> wallBlock;
    public final Supplier<CharmHangingSignItem> item;

    public CustomHangingSign(CustomWoodHolder holder) {
        var registry = holder.getRegistry();
        var feature = holder.getFeature();
        var material = holder.getMaterial();
        var woodType = holder.getWoodType();

        var hangingId = holder.getMaterialName() + "_hanging_sign";
        var wallId = holder.getMaterialName() + "_wall_hanging_sign";

        hangingBlock = registry.block(hangingId, () -> new CharmCeilingHangingSignBlock(feature, material, woodType));
        wallBlock = registry.wallHangingSignBlock(wallId, feature, material, hangingBlock, woodType);
        item = registry.item(hangingId, () -> new CharmHangingSignItem(feature, material, hangingBlock, wallBlock));

        // This is needed so we can set the correct blocks to hanging signs later on in the registration.
        CustomWoodHelper.addHangingSignItem(item);

        // Associate with the hanging sign block entity.
        registry.blockEntityBlocks(() -> BlockEntityType.HANGING_SIGN, List.of(hangingBlock, wallBlock));

        holder.addCreativeTabItem(CustomWoodHelper.HANGING_SIGNS, item);
    }
}
