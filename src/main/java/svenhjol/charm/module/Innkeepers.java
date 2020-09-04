package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;
import svenhjol.charm.block.BrewBottleBlock;
import svenhjol.charm.event.StructureSetupCallback;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(description = "")
public class Innkeepers extends MesonModule {
    public static BrewBottleBlock BREW_BOTTLE;

    @Override
    public void init() {
        BREW_BOTTLE = new BrewBottleBlock(this);

        // register innkeeper structures
        StructureSetupCallback.EVENT.register(() -> {
            StructureProcessorList processor = StructureProcessorLists.MOSSIFY_10_PERCENT;
            StructurePool.Projection projection = StructurePool.Projection.RIGID;

            Map<Identifier, Integer> plainsBuildings = new HashMap<>();
            Map<Identifier, Integer> savannaBuildings = new HashMap<>();
            Map<Identifier, Integer> taigaBuildings = new HashMap<>();

            Identifier plainsHouses = new Identifier("village/plains/houses");
            Identifier savannaHouses = new Identifier("village/savanna/houses");
            Identifier taigaHouses = new Identifier("village/taiga/houses");

            // @Coranthes: register your buildings here, 1 per line.
            // plainsBuildings.put(new Identifier("charm:village/plains/houses/plains_beekeeper_1"), 10);
            // savannaBuildings.put(new Identifier("charm:village/savanna/houses/savanna_beekeeper_1"), 10);
            // taigaBuildings.put(new Identifier("charm:village/plains/houses/plains_beekeeper_1"), 10);

            plainsBuildings.forEach((building, count) -> StructureSetupCallback.addStructurePoolElement(plainsHouses, building, processor, projection, count));
            savannaBuildings.forEach((building, count) -> StructureSetupCallback.addStructurePoolElement(savannaHouses, building, processor, projection, count));
            taigaBuildings.forEach((building, count) -> StructureSetupCallback.addStructurePoolElement(taigaHouses, building, processor, projection, count));
        });
    }

    @Override
    public void initClient() {
        RenderLayersAccessor.getBlocks().put(BREW_BOTTLE, RenderLayer.getCutout());
    }
}
