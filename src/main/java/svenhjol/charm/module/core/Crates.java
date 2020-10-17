package svenhjol.charm.module.core;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.block.CrateBlock;
import svenhjol.charm.blockentity.CrateBlockEntity;
import svenhjol.charm.client.CratesClient;
import svenhjol.charm.gui.CrateScreen;
import svenhjol.charm.screenhandler.CrateScreenHandler;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.base.enums.VanillaVariantMaterial;
import svenhjol.charm.base.helper.ItemHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

import java.util.*;

@Module(mod = Charm.MOD_ID, description = "A smaller storage solution with the benefit of being transportable.")
public class Crates extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "crate");
    public static final Map<IVariantMaterial, CrateBlock> CRATE_BLOCKS = new HashMap<>();

    public static ScreenHandlerType<CrateScreenHandler> SCREEN_HANDLER;
    public static BlockEntityType<CrateBlockEntity> BLOCK_ENTITY;

    // add blocks and items to these lists to blacklist them from crates
    public static final List<Class<? extends Block>> invalidCrateBlocks = new ArrayList<>();
    public static final List<Class<? extends Block>> invalidShulkerBoxBlocks = new ArrayList<>();

    @Config(name = "Show tooltip", description = "If true, hovering over a crate will show its contents in a tooltip.")
    public static boolean showTooltip = true;

    public static boolean isEnabled = false;

    @Override
    public void register() {
        for (VanillaVariantMaterial type : VanillaVariantMaterial.values()) {
            CRATE_BLOCKS.put(type, new CrateBlock(this, type));
        }

        invalidCrateBlocks.add(ShulkerBoxBlock.class);
        invalidCrateBlocks.add(CrateBlock.class);
        invalidShulkerBoxBlocks.add(CrateBlock.class);

        SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(ID, CrateScreenHandler::new); // registers via fabric magic
        BLOCK_ENTITY = BlockEntityType.Builder.create(CrateBlockEntity::new).build(null);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, ID, BLOCK_ENTITY);

        isEnabled = this.enabled;
    }

    @Override
    public void clientInit() {
        new CratesClient(this);
        ScreenRegistry.register(SCREEN_HANDLER, CrateScreen::new);
    }

    public static boolean canCrateInsertItem(ItemStack stack) {
        return !isEnabled || !invalidCrateBlocks.contains(ItemHelper.getBlockClass(stack));
    }

    public static boolean canShulkerBoxInsertItem(ItemStack stack) {
        return !isEnabled || !invalidShulkerBoxBlocks.contains(ItemHelper.getBlockClass(stack));
    }

    public static CrateBlock getRandomCrateBlock(Random rand) {
        List<CrateBlock> values = new ArrayList<>(Crates.CRATE_BLOCKS.values());
        return values.get(rand.nextInt(values.size()));
    }
}
