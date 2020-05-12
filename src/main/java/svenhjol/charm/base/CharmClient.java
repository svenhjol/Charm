package svenhjol.charm.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class CharmClient {
    public boolean isInMineshaft = false;
    public boolean isInStronghold = false;
    public boolean isInFortress = false;
    public boolean isInShipwreck = false;
    public boolean isInVillage = false;
    public boolean isInBigDungeon = false;
    public boolean isDaytime = true;

    public static int clientTicks = 0;

    /**
     * This method uses code from AutoRegLib by Vazkii.
     * @param event
     */
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientTick(ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Screen currentScreen = Minecraft.getInstance().currentScreen;
            if (currentScreen == null || !currentScreen.isPauseScreen()) {
                ++clientTicks;
            }
        }
    }

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