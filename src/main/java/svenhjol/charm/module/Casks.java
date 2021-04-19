package svenhjol.charm.module;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.CaskBlock;
import svenhjol.charm.blockentity.CaskBlockEntity;
import svenhjol.charm.client.CasksClient;

@Module(mod = Charm.MOD_ID, client = CasksClient.class)
public class Casks extends CharmModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "cask");
    public static CaskBlock CASK;
    public static BlockEntityType<CaskBlockEntity> BLOCK_ENTITY;

    public static final Identifier MSG_CLIENT_ADDED_TO_CASK = new Identifier(Charm.MOD_ID, "client_added_to_cask");

    @Override
    public void register() {
        CASK = new CaskBlock(this);
        BLOCK_ENTITY = RegistryHandler.blockEntity(ID, CaskBlockEntity::new, CASK);
    }
}
