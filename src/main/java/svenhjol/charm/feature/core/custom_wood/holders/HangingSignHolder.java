package svenhjol.charm.feature.core.custom_wood.holders;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.core.custom_wood.blocks.CustomCeilingHangingSignBlock;
import svenhjol.charm.charmony.common.block.CharmWallHangingSignBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.charmony.common.item.CharmHangingSignItem;

import java.util.List;
import java.util.function.Supplier;

public class HangingSignHolder {
    public final Supplier<CustomCeilingHangingSignBlock> hangingBlock;
    public final Supplier<CharmWallHangingSignBlock> wallBlock;
    public final Supplier<CharmHangingSignItem> item;

    public HangingSignHolder(CustomWoodHolder holder) {
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var woodType = holder.woodType();

        var hangingId = holder.getMaterialName() + "_hanging_sign";
        var wallId = holder.getMaterialName() + "_wall_hanging_sign";

        hangingBlock = registry.block(hangingId, () -> new CustomCeilingHangingSignBlock(material, woodType));
        wallBlock = registry.wallHangingSignBlock(wallId, material, hangingBlock, woodType);
        item = registry.item(hangingId, () -> new CharmHangingSignItem(material, hangingBlock, wallBlock));

        // This is needed so we can set the correct blocks to hanging signs later on in the registration.
        holder.feature().registers.addHangingSignItem(item);

        // Associate with the hanging sign block entity.
        registry.blockEntityBlocks(() -> BlockEntityType.HANGING_SIGN, List.of(hangingBlock, wallBlock));

        holder.addCreativeTabItem(CustomType.HANGING_SIGN, item);
    }
}
