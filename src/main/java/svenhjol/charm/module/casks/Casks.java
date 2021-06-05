package svenhjol.charm.module.casks;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;

@Module(mod = Charm.MOD_ID, client = CasksClient.class, description = "Casks let you combine up to 64 potions, keeping an average of duration. Use glass bottles to extract home brew from the cask.")
public class Casks extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "cask");
    public static final Identifier TRIGGER_FILLED_WITH_POTION = new Identifier(Charm.MOD_ID, "filled_with_potion");
    public static final Identifier TRIGGER_TAKEN_BREW = new Identifier(Charm.MOD_ID, "taken_brew");

    @Config(name = "Show label", description = "If true, casks show their custom name and capacity as a hovering label. Requires the 'Storage Labels' feature to be enabled.")
    public static boolean showLabel = true;

    public static CaskBlock CASK;
    public static BlockEntityType<CaskBlockEntity> BLOCK_ENTITY;

    public static final Identifier MSG_CLIENT_ADDED_TO_CASK = new Identifier(Charm.MOD_ID, "client_added_to_cask");

    @Override
    public void register() {
        CASK = new CaskBlock(this);
        BLOCK_ENTITY = RegistryHelper.blockEntity(ID, CaskBlockEntity::new, CASK);
    }

    public static void triggerFilledWithPotion(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_FILLED_WITH_POTION);
    }

    public static void triggerTakenBrew(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_TAKEN_BREW);
    }
}
