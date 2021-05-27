package svenhjol.charm.module.variant_chains;

import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.block.CharmChainBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, client = VariantChainsClient.class, description = "Variant chains crafted from vanilla metal ingots and nuggets.")
public class VariantChains extends CharmModule {
    public static List<CharmChainBlock> CHAINS = new ArrayList<>();

    @Override
    public void register() {
        for (String material : new String[]{"copper", "gold", "netherite"}) {
            CHAINS.add(new CharmChainBlock(this, material + "_chain"));
        }
    }

    @Override
    public List<Identifier> getRecipesToRemove() {
        List<Identifier> remove = new ArrayList<>();

        // remove chain recipes if nuggets module is disabled
        if (!ModuleHandler.enabled("charm:extra_nuggets")) {
            remove.addAll(Arrays.asList(
                new Identifier(Charm.MOD_ID, "variant_chains/copper_chain"),
                new Identifier(Charm.MOD_ID, "variant_chains/netherite_chain")
            ));
        }

        return remove;
    }
}
