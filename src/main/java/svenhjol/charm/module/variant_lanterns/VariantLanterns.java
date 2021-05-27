package svenhjol.charm.module.variant_lanterns;

import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.block.CharmLanternBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Module(mod = Charm.MOD_ID, client = VariantLanternsClient.class, description = "Variants lanterns crafted from vanilla metal nuggets and torches.")
public class VariantLanterns extends CharmModule {
    public static List<CharmLanternBlock> LANTERNS = new ArrayList<>();

    @Override
    public void register() {
        for (String material : new String[]{"copper", "gold", "netherite"}) {
            LANTERNS.add(new CharmLanternBlock(this, material + "_lantern"));
            LANTERNS.add(new CharmLanternBlock(this, material + "_soul_lantern"));
        }
    }

    @Override
    public List<Identifier> getRecipesToRemove() {
        List<Identifier> remove = new ArrayList<>();

        // remove lantern recipes if nuggets module is disabled
        if (!ModuleHandler.enabled("charm:extra_nuggets")) {
            remove.addAll(Arrays.asList(
                new Identifier(Charm.MOD_ID, "variant_lanterns/copper_lantern"),
                new Identifier(Charm.MOD_ID, "variant_lanterns/copper_soul_lantern"),
                new Identifier(Charm.MOD_ID, "variant_lanterns/netherite_lantern"),
                new Identifier(Charm.MOD_ID, "variant_lanterns/netherite_soul_lantern")
            ));
        }

        return remove;
    }

    public static CharmLanternBlock getRandomLantern(Random random) {
        return LANTERNS.get(random.nextInt(LANTERNS.size()));
    }
}
