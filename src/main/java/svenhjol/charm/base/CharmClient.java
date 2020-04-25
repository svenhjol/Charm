package svenhjol.charm.base;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CharmClient {

    public boolean isInMineshaft = false;
    public boolean isInStronghold = false;
    public boolean isInFortress = false;
    public boolean isInShipwreck = false;
    public boolean isInVillage = false;
    public boolean isInBigDungeon = false;
    public boolean isDaytime = true;

    public void updateFromServer(CompoundNBT input) {
        isInMineshaft = input.getBoolean("mineshaft");
        isInStronghold = input.getBoolean("stronghold");
        isInFortress = input.getBoolean("fortress");
        isInShipwreck = input.getBoolean("shipwreck");
        isInVillage = input.getBoolean("village");
        isInBigDungeon = input.getBoolean("big_dungeon");
        isDaytime = input.getBoolean("day");
    }
}