package svenhjol.charm.mixin.colored_nether_portals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.module.colored_nether_portals.ColoredNetherPortals;

@Mixin(ItemEntity.class)
public abstract class HandleInsidePortalMixin extends Entity {
    private boolean hasChangedPortalColor = false;

    public HandleInsidePortalMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void handleInsidePortal(BlockPos pos) {
        var itemEntity = (ItemEntity)(Entity)this;
        var level = getLevel();
        var state = level.getBlockState(pos);

        if (state.getBlock() instanceof NetherPortalBlock
            && itemEntity.getItem().getItem() instanceof DyeItem dyeItem
            && level instanceof ServerLevel
            && !hasChangedPortalColor) {
            var dyeColor = dyeItem.getDyeColor();
            ColoredNetherPortals.replacePortal(level, pos, state, dyeColor);
            hasChangedPortalColor = true;
        }

        super.handleInsidePortal(pos);
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerLevel level) {
        hasChangedPortalColor = false;
        return super.changeDimension(level);
    }
}
