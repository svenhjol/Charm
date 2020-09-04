package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import svenhjol.charm.block.BrewBottleBlock;
import svenhjol.charm.event.StructureSetupCallback;
import svenhjol.charm.event.StructureSetupCallback.VillageType;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import static svenhjol.charm.event.StructureSetupCallback.addVillageHouse;

@Module(description = "")
public class Innkeepers extends MesonModule {
    public static BrewBottleBlock BREW_BOTTLE;

    @Override
    public void init() {
        BREW_BOTTLE = new BrewBottleBlock(this);

        // register innkeeper structures
        StructureSetupCallback.EVENT.register(() -> {

            // @Coranthes: register houses here, one per line
            addVillageHouse(VillageType.TAIGA, new Identifier("charm:village/taiga/houses/taiga_innkeeper_1"), 10);
//            addVillageHouse(VillageType.PLAINS, new Identifier("charm:village/plains/houses/plains_innkeeper_1"), 10);
//            addVillageHouse(VillageType.SAVANNA, new Identifier("charm:village/savanna/houses/savanna_innkeeper_1"), 10);

        });
    }

    @Override
    public void initClient() {
        RenderLayersAccessor.getBlocks().put(BREW_BOTTLE, RenderLayer.getCutout());
    }
}
