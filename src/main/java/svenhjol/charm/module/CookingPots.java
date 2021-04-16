package svenhjol.charm.module;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.CookingPotBlock;
import svenhjol.charm.blockentity.CookingPotBlockEntity;
import svenhjol.charm.client.CookingPotsClient;
import svenhjol.charm.item.MixedStewItem;

@Module(mod = Charm.MOD_ID, client = CookingPotsClient.class, description = "Cooking pots let you combine up to 64 food items, keeping an average of all hunger and saturation. Use wooden bowls to extract stew from the pot.")
public class CookingPots extends CharmModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "cooking_pot");
    public static CookingPotBlock COOKING_POT;
    public static BlockEntityType<CookingPotBlockEntity> BLOCK_ENTITY;
    public static MixedStewItem MIXED_STEW;

    public static final Identifier MSG_CLIENT_ADDED_TO_POT = new Identifier(Charm.MOD_ID, "client_added_to_pot");

    @Override
    public void register() {
        COOKING_POT = new CookingPotBlock(this);
        BLOCK_ENTITY = RegistryHandler.blockEntity(ID, CookingPotBlockEntity::new, COOKING_POT);
        MIXED_STEW = new MixedStewItem(this);
    }
}
