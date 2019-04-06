package svenhjol.meson;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MesonTileInventory extends MesonTile
{
    protected String lootTable;
    protected String name;

    public abstract ItemStackHandler getInventory();

    public abstract int getInventorySize();

    public void generateLoot(@Nullable EntityPlayer player)
    {
        if (!this.world.isRemote && hasLootTable()) {
            ResourceLocation res = new ResourceLocation(lootTable);
            LootTable table = world.getLootTableManager().getLootTableFromLocation(res);
            LootContext.Builder builder = new LootContext.Builder((WorldServer) world);
            if (player != null) builder.withLuck(player.getLuck());

            LootContext context = builder.build();
            List<ItemStack> list = table.generateLootForPools(world.rand, context);

            List<Integer> slots = new ArrayList<>();
            for (int i = 0; i < getInventorySize(); i++) {
                slots.add(i);
            }
            Collections.shuffle(slots);

            int i = 0;
            for (ItemStack item : list) {
                if (i >= getInventorySize()) continue;
                getInventory().setStackInSlot(slots.get(i++), item);
            }

            this.lootTable = null;
        }
    }

    @Override
    public void readTag(NBTTagCompound tag)
    {
        super.readTag(tag);
        getInventory().deserializeNBT(tag.getCompoundTag("inventory"));
        setLootTable(tag.getString("lootTable"));
    }

    @Override
    public void writeTag(NBTTagCompound tag)
    {
        super.writeTag(tag);
        tag.setTag("inventory", getInventory().serializeNBT());
        tag.setString("lootTable", getLootTable());
    }

    public String getName()
    {
        return hasCustomName() ? name : "";
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean hasCustomName()
    {
        return name != null && !name.isEmpty();
    }

    public boolean hasLootTable()
    {
        return lootTable != null && !lootTable.isEmpty();
    }

    public String getLootTable()
    {
        return hasLootTable() ? lootTable : "";
    }

    public void setLootTable(ResourceLocation lootTable)
    {
        this.lootTable = lootTable.toString();
    }

    public void setLootTable(ResourceLocation lootTable, long seed)
    {
        setLootTable(lootTable.toString());
    }

    public void setLootTable(String lootTable)
    {
        this.lootTable = lootTable;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
//        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getInventory());
//        }
//        return null;
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)getInventory() : super.getCapability(capability, facing);
    }
}
