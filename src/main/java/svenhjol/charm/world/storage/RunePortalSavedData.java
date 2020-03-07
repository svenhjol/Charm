package svenhjol.charm.world.storage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;

public class RunePortalSavedData extends WorldSavedData {
    private static final String ID = "RunePortalSavedData";
    private static final String PORTALS = "portals";
    private static final String LINKS = "links";
    public Map<BlockPos, int[]> portals = new HashMap<>();
    public Map<BlockPos, BlockPos> links = new HashMap<>();


    public RunePortalSavedData() {
        super(ID);
    }

    @Override
    public void read(CompoundNBT tag) {
        // get links
        for (String key : getTagLinks(tag).keySet()) {
            BlockPos toPos = BlockPos.fromLong(getTagLinks(tag).getLong(key));
            links.put(BlockPos.fromLong(Long.parseLong(key)), toPos);
        }

        // get portals
        for (String key : getTagPortals(tag).keySet()) {
            int[] portalOrder = getTagPortals(tag).getIntArray(key);
            portals.put(BlockPos.fromLong(Long.parseLong(key)), portalOrder);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        // set links
        for (BlockPos fromPos : links.keySet()) {
            BlockPos toPos = links.get(fromPos);
            getTagLinks(tag).putLong(Long.toString(fromPos.toLong()), toPos.toLong());
        }

        // set portals
        for (BlockPos pos : portals.keySet()) {
            getTagPortals(tag).putIntArray(Long.toString(pos.toLong()), portals.get(pos));
        }

        return tag;
    }

    public static RunePortalSavedData get(ServerWorld world) {
        return world.getSavedData().getOrCreate(RunePortalSavedData::new, ID);
    }

    private CompoundNBT getTagPortals(CompoundNBT tag) {
        CompoundNBT p = (CompoundNBT) tag.get(PORTALS);
        if (p == null || p.isEmpty()) {
            tag.put(PORTALS, new CompoundNBT());
        }
        return (CompoundNBT) tag.get(PORTALS);
    }

    private CompoundNBT getTagLinks(CompoundNBT tag) {
        CompoundNBT l = (CompoundNBT) tag.get(LINKS);
        if (l == null || l.isEmpty()) {
            tag.put(LINKS, new CompoundNBT());
        }
        return (CompoundNBT) tag.get(LINKS);
    }
}
