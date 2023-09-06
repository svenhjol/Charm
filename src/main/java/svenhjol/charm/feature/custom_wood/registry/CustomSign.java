package svenhjol.charm.feature.custom_wood.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.block.CharmStandingSignBlock;
import svenhjol.charmony.block.CharmWallSignBlock;
import svenhjol.charmony.item.CharmSignItem;

import java.util.List;
import java.util.function.Supplier;

public class CustomSign {
    public final Supplier<CharmStandingSignBlock> standingBlock;
    public final Supplier<CharmWallSignBlock> wallBlock;
    public final Supplier<CharmSignItem> item;

    public CustomSign(CustomWoodHolder holder) {
        var registry = holder.getRegistry();
        var feature = holder.getFeature();
        var material = holder.getMaterial();
        var woodType = holder.getWoodType();

        var signId = holder.getMaterialName() + "_sign";
        var wallSignId = holder.getMaterialName() + "_wall_sign";

        standingBlock = registry.block(signId, () -> new CharmStandingSignBlock(feature, material, woodType));
        wallBlock = registry.wallSignBlock(wallSignId, feature, material, standingBlock, woodType);
        item = registry.item(signId, () -> new CharmSignItem(feature, material, standingBlock, wallBlock));

        // This is needed so we can set the correct blocks to signs later on in the registration.
        CustomWoodHelper.addSignItem(item);

        // Associate with the sign block entity.
        registry.blockEntityBlocks(() -> BlockEntityType.SIGN, List.of(standingBlock, wallBlock));

        holder.addCreativeTabItem(CustomWoodHelper.SIGNS, item);
    }
}
