package svenhjol.charm.module.totem_of_preserving;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.block.CharmSyncedBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TotemBlockEntity extends CharmSyncedBlockEntity {
    public static final String OWNER_TAG = "owner";

    public List<ItemStack> items = new ArrayList<>();
    private int xp;
    private String message;
    private UUID owner;
    public float rotateTicks = 0F;

    public TotemBlockEntity(BlockPos pos, BlockState state) {
        super(TotemOfPreserving.BLOCK_ENTITY, pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items.clear();

        var itemsList = tag.getList(TotemOfPreservingItem.ITEMS_TAG, 10);
        for (Tag t : itemsList) {
            var stack = ItemStack.of((CompoundTag) t);
            items.add(stack);
        }

        message = tag.getString(TotemOfPreservingItem.MESSAGE_TAG);
        xp = tag.getInt(TotemOfPreservingItem.XP_TAG);
        owner = tag.getUUID(OWNER_TAG);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        var itemsList = new ListTag();
        for (ItemStack stack : items) {
            var saved = new CompoundTag();
            itemsList.add(stack.save(saved));
        }

        tag.put(TotemOfPreservingItem.ITEMS_TAG, itemsList);
        tag.putInt(TotemOfPreservingItem.XP_TAG, xp);
        tag.putString(TotemOfPreservingItem.MESSAGE_TAG, message);
        tag.putUUID(OWNER_TAG, owner);
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public int getXp() {
        return xp;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public String getMessage() {
        return message;
    }

    public UUID getOwner() {
        return owner;
    }
}
