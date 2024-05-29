package svenhjol.charm.feature.core.custom_wood.holders;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.core.custom_wood.blocks.CustomStandingSignBlock;
import svenhjol.charm.charmony.common.block.CharmWallSignBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.charmony.common.item.CharmSignItem;

import java.util.List;
import java.util.function.Supplier;

public class SignHolder {
    public final Supplier<CustomStandingSignBlock> standingBlock;
    public final Supplier<CharmWallSignBlock> wallBlock;
    public final Supplier<CharmSignItem> item;

    public SignHolder(CustomWoodHolder holder) {
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var woodType = holder.woodType();

        var signId = holder.getMaterialName() + "_sign";
        var wallSignId = holder.getMaterialName() + "_wall_sign";

        standingBlock = registry.block(signId, () -> new CustomStandingSignBlock(material, woodType));
        wallBlock = registry.wallSignBlock(wallSignId, material, standingBlock, woodType);
        item = registry.item(signId, () -> new CharmSignItem(material, standingBlock, wallBlock));

        // This is needed so we can set the correct blocks to signs later on in the registration.
        holder.feature().registers.addSignItem(item);

        // Associate with the sign block entity.
        registry.blockEntityBlocks(() -> BlockEntityType.SIGN, List.of(standingBlock, wallBlock));

        holder.addCreativeTabItem(CustomType.SIGN, item);
    }
}
