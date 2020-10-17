package svenhjol.charm.module.core;

import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlock;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, description = "Adds storage blocks.")
public class StorageBlocks extends CharmModule {
    public static WheatSeedBlock WHEAT_SEED_BLOCK;
    public static RottenFleshBlock ROTTEN_FLESH_BLOCK;
    public static BambooBlock BAMBOO_BLOCK;
    public static EggBlock EGG_BLOCK;
    public static BeetrootBlock BEETROOT_BLOCK;
    public static PotatoBlock POTATO_BLOCK;
    public static CarrotBlock CARROT_BLOCK;

    public static List<CharmBlock> STORAGE_BLOCKS = new ArrayList<>();

    @Config(name = "Wheat seed sack")
    public static boolean wheatSeeds = true;

    @Config(name = "Rotten Flesh bundle")
    public static boolean rottenflesh = true;

    @Config(name = "Bamboo bundle")
    public static boolean bamboo = true;

    @Config(name = "Carrot crate")
    public static boolean carrot = true;

    @Config(name = "Potato crate")
    public static boolean potato = true;

    @Config(name = "Beetroot crate")
    public static boolean beetroot = true;

    @Config(name = "Egg crate")
    public static boolean egg = true;

    @Override
    public void register() {
        WHEAT_SEED_BLOCK = new WheatSeedBlock(this);
        ROTTEN_FLESH_BLOCK = new RottenFleshBlock(this);
        BAMBOO_BLOCK = new BambooBlock(this);
        EGG_BLOCK = new EggBlock(this);
        POTATO_BLOCK = new PotatoBlock(this);
        BEETROOT_BLOCK = new BeetrootBlock(this);
        CARROT_BLOCK = new CarrotBlock(this);

        STORAGE_BLOCKS.addAll(Arrays.asList(
            WHEAT_SEED_BLOCK,
            ROTTEN_FLESH_BLOCK,
            BAMBOO_BLOCK,
            EGG_BLOCK,
            BEETROOT_BLOCK,
            POTATO_BLOCK,
            CARROT_BLOCK

        ));
    }

    @Override
    public List<Identifier> getRecipesToRemove() {
        List<Identifier> recipes = new ArrayList<>();
        if (!wheatSeeds) {
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/wheat_seed_block_from_wheat_seeds"));
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/wheat_seeds_from_wheat_seed_block"));
        }

        if (!rottenflesh) {
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/rotten_flesh_block_from_rotten_flesh"));
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/rotten_flesh_from_rotten_flesh_block"));
        }

        if (!bamboo) {
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/bamboo_block_from_bamboo"));
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/bamboo_from_bamboo_block"));
        }

        if (!egg) {
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/egg_block_from_egg"));
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/eggs_from_egg_block"));
        }

        if (!potato) {
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/potato_block_from_potato"));
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/potato_from_potato_block"));
        }

        if (!beetroot) {
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/beetroot_block_from_beetroot"));
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/beetroot_from_beetroot_block"));
        }

        if (!carrot) {
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/carrot_block_from_carrot"));
            recipes.add(new Identifier(Charm.MOD_ID, "storage_blocks/carrot_from_carrot_block"));
        }

        return recipes;
    }
}
