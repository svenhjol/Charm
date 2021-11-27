package svenhjol.charm.module.core;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, priority = 100, alwaysEnabled = true, description = "Core configuration values.")
public class Core extends CharmModule {
    public static final ResourceLocation ADVANCEMENT_PLAYER_JOINED = new ResourceLocation(Charm.MOD_ID, "player_joined");
    public static final ResourceLocation MSG_SERVER_OPEN_INVENTORY = new ResourceLocation(Charm.MOD_ID, "server_open_inventory");

    @Config(name = "Debug mode", description = "If true, routes additional debug messages into the standard game log.")
    public static boolean debug = false;

    @Config(name = "Advancements", description = "If true, Charm will add its own advancement tree.")
    public static boolean doAdvancements = true;

    @Config(name = "Allow zero cost anvil recipes", description = "If true, allows anvil recipes that do not require any XP cost.")
    public static boolean allowZeroCostAnvilRecipes = true;

    @Override
    public void register() {
        ServerEntityEvents.ENTITY_LOAD.register(this::handleServerJoin);
    }

    private void handleServerJoin(Entity entity, ServerLevel level) {
        if (entity instanceof ServerPlayer player) {
            CharmAdvancements.ACTION_PERFORMED.trigger(player, ADVANCEMENT_PLAYER_JOINED);
        }
    }

    public static boolean allowZeroCostAnvilRecipes(Player player, DataSlot levelCost) {
        return allowZeroCostAnvilRecipes && (player.getAbilities().instabuild || ((player.experienceLevel >= levelCost.get()) && levelCost.get() > -1));
    }
}
