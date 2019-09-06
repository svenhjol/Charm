package svenhjol.charm.decoration.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import svenhjol.charm.decoration.container.CrateContainer;
import svenhjol.charm.base.message.MessageCrateInteract;
import svenhjol.charm.decoration.module.Crates;
import svenhjol.meson.handler.PacketHandler;
import svenhjol.meson.iface.IMesonTileEntity;
import vazkii.quark.api.ITransferManager;
import vazkii.quark.api.QuarkCapabilities;

import javax.annotation.Nullable;

public class CrateTileEntity extends LockableLootTileEntity implements IMesonTileEntity, ITransferManager
{
    public static int SIZE = 9;
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public CrateTileEntity()
    {
        // TODO set wood type so we can display name
        super(Crates.tile);
    }

    @Override
    public void read(CompoundNBT tag)
    {
        super.read(tag);
        loadFromNBT(tag);
    }

    public void loadFromNBT(CompoundNBT tag)
    {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(tag) && tag.contains("Items")) {
            ItemStackHelper.loadAllItems(tag, this.items);
        }
    }

    @Override
    public boolean acceptsTransfer(PlayerEntity playerEntity)
    {
        return true;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);
        return writeToNBT(tag);
    }

    public CompoundNBT writeToNBT(CompoundNBT tag)
    {
        if (!this.checkLootAndRead(tag)) {
            ItemStackHelper.saveAllItems(tag, this.items, false);
        }
        return tag;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        return QuarkCapabilities.TRANSFER.orEmpty(cap, LazyOptional.of(() -> this));
    }

    //    @Nullable
//    @Override
//    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
//    {
//        LazyOptional<ITransferManager> opt = LazyOptional.of(() -> this);
//        return QuarkCapabilities.TRANSFER.orEmpty(cap, opt);
//
////        if (opt.isPresent()) {
////            return opt;
////        }
////
////        return super.getCapability(cap, side);
//
//
////       if (cap == QuarkCapabilities.TRANSFER) {
////           return (LazyOptional<T>)LazyOptional.of(() -> this);
////       }
////
////       return super.getCapability(cap, side);
//
////        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)getInventory() : super.getCapability(capability, facing);
//    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return CrateContainer.instance(id, player, this);
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("tile.charm.crate");
    }

    @Override
    public NonNullList<ItemStack> getItems()
    {
        return this.items;
    }

    @Override
    public int getSizeInventory()
    {
        return this.items.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void setItems(NonNullList<ItemStack> items)
    {
        this.items = items;
    }

    @Override
    public void openInventory(PlayerEntity player)
    {
        PacketHandler.sendTo(new MessageCrateInteract(this.pos, 0), (ServerPlayerEntity)player);
    }

    @Override
    public void closeInventory(PlayerEntity player)
    {
        PacketHandler.sendTo(new MessageCrateInteract(this.pos, 1), (ServerPlayerEntity)player);
    }
}
