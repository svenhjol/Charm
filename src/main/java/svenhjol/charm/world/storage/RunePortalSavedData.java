package svenhjol.charm.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;

public class RunePortalSavedData extends WorldSavedData
{
    private static final String ID = "RunePortalSavedData";
    private static final String PORTALS = "portals";
    private static final String LINKS = "links";

    public Map<BlockPos, String> portals = new HashMap<>();
    public Map<BlockPos, BlockPos> links = new HashMap<>();

    public RunePortalSavedData(String id)
    {
        super(id);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        // get links
        for (String key : getTagLinks(tag).getKeySet()) {
            BlockPos toPos = BlockPos.fromLong(getTagLinks(tag).getLong(key));
            links.put(BlockPos.fromLong(Long.valueOf(key)), toPos);
        }

        // get portals
        for (String key : getTagPortals(tag).getKeySet()) {
            String portalOrder = getTagPortals(tag).getString(key);
            portals.put(BlockPos.fromLong(Long.valueOf(key)), portalOrder);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        // set links
        for (BlockPos fromPos : links.keySet()) {
            BlockPos toPos = links.get(fromPos);
            getTagLinks(tag).setLong(Long.toString(fromPos.toLong()), toPos.toLong());
        }

        // set portals
        for (BlockPos pos : portals.keySet()) {
            String portalOrder = portals.get(pos);
            getTagPortals(tag).setString(Long.toString(pos.toLong()), portalOrder);
        }

        return tag;
    }

    public static RunePortalSavedData get(World world)
    {
        if (world.getMapStorage() == null) return null;

        RunePortalSavedData data = (RunePortalSavedData)world.getMapStorage().getOrLoadData(RunePortalSavedData.class, ID);
        if (data == null) {
            data = new RunePortalSavedData(ID);
            data.markDirty();
            world.getMapStorage().setData(ID, data);
        }
        return data;
    }

    private NBTTagCompound getTagPortals(NBTTagCompound tag)
    {
        if (tag.getCompoundTag(PORTALS).isEmpty()) {
            tag.setTag(PORTALS, new NBTTagCompound());
        }
        return tag.getCompoundTag(PORTALS);
    }

    private NBTTagCompound getTagLinks(NBTTagCompound tag)
    {
        if (tag.getCompoundTag(LINKS).isEmpty()) {
            tag.setTag(LINKS, new NBTTagCompound());
        }
        return tag.getCompoundTag(LINKS);
    }
}
