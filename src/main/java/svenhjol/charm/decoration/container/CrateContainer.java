package svenhjol.charm.decoration.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import svenhjol.charm.decoration.module.Crates;
import svenhjol.charm.decoration.tileentity.CrateTileEntity;
import svenhjol.meson.MesonContainer;
import vazkii.quark.api.ITransferManager;
import vazkii.quark.api.QuarkCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrateContainer extends MesonContainer implements ITransferManager, ICapabilityProvider {
    private CrateContainer(ContainerType<?> type, int id, PlayerInventory player, IInventory inventory) {
        super(type, id, player, inventory);

        int index = 0;

        // container's inventory slots
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new CrateSlot(inventory, index++, 8 + (i * 18), 18));
        }

        index = 9;

        // player's main inventory slots
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new Slot(player, index++, 8 + c * 18, 50 + r * 18));
            }
        }

        // player's hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(player, i, 8 + (i * 18), 108));
        }
    }

    public static CrateContainer instance(int id, PlayerInventory playerInventory, IInventory inventory) {
        return new CrateContainer(Crates.container, id, playerInventory, inventory);
    }

    public static CrateContainer instance(int id, PlayerInventory playerInventory) {
        return instance(id, playerInventory, new Inventory(CrateTileEntity.SIZE));
    }

    @Override
    public boolean acceptsTransfer(PlayerEntity playerEntity) {
        return true;
    }

    @SuppressWarnings("ALL") // what
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return QuarkCapabilities.TRANSFER.orEmpty(cap, LazyOptional.of(() -> this));
    }
}
