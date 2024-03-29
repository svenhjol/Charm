package svenhjol.charm.feature.nearby_workstations.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class NearbyAnvilMenu extends AnvilMenu {
    private final BlockPos pos;

    public NearbyAnvilMenu(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, BlockPos pos) {
        super(i, inventory, containerLevelAccess);
        this.pos = pos;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.level().getBlockState(pos).is(BlockTags.ANVIL);
    }
}
