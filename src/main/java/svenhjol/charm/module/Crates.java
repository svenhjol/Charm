package svenhjol.charm.module;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.block.CrateBlock;
import svenhjol.charm.blockentity.CrateBlockEntity;
import svenhjol.charm.gui.CrateScreen;
import svenhjol.charm.screenhandler.CrateScreenHandler;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IStorageMaterial;
import svenhjol.meson.enums.VanillaStorageMaterial;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.iface.Module;

import java.util.*;

@Module(description = "A smaller storage solution with the benefit of being transportable.")
public class Crates extends MesonModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "crate");
    public static final Map<IStorageMaterial, CrateBlock> CRATE_BLOCKS = new HashMap<>();

    public static ScreenHandlerType<CrateScreenHandler> SCREEN_HANDLER;
    public static BlockEntityType<CrateBlockEntity> BLOCK_ENTITY;

    // add blocks and items to these lists to blacklist them from crates
    public static final List<Class<? extends Block>> invalidBlocks = new ArrayList<>();
    public static final List<Class<? extends Item>> invalidItems = new ArrayList<>();

    @Override
    public void init() {
        for (VanillaStorageMaterial type : VanillaStorageMaterial.values()) {
            CRATE_BLOCKS.put(type, new CrateBlock(this, type));
        }

        invalidBlocks.add(ShulkerBoxBlock.class);
        invalidBlocks.add(CrateBlock.class);

        SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(ID, CrateScreenHandler::new); // registers via fabric magic
        BLOCK_ENTITY = BlockEntityType.Builder.create(CrateBlockEntity::new).build(null);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, ID, BLOCK_ENTITY);
    }

    @Override
    public void initClient() {
        ScreenRegistry.register(SCREEN_HANDLER, CrateScreen::new);
    }

    public static boolean canContainItem(ItemStack stack) {
        return !invalidItems.contains(stack.getItem().getClass()) && !invalidBlocks.contains(ItemHelper.getBlockClass(stack));
    }

    public static CrateBlock getRandomCrateBlock(Random rand) {
        List<CrateBlock> values = new ArrayList<>(Crates.CRATE_BLOCKS.values());
        return values.get(rand.nextInt(values.size()));
    }
}
