package svenhjol.charm.module;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.base.enums.VanillaVariantMaterial;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.ItemHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.CrateBlock;
import svenhjol.charm.blockentity.CrateBlockEntity;
import svenhjol.charm.client.CratesClient;
import svenhjol.charm.screenhandler.CrateScreenHandler;

import java.util.*;

@Module(mod = Charm.MOD_ID, client = CratesClient.class, description = "A smaller storage solution with the benefit of being transportable.")
public class Crates extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "crate");
    public static final Map<IVariantMaterial, CrateBlock> CRATE_BLOCKS = new HashMap<>();

    public static ScreenHandlerType<CrateScreenHandler> SCREEN_HANDLER;
    public static BlockEntityType<CrateBlockEntity> BLOCK_ENTITY;

    // add blocks and items to these lists to blacklist them from crates
    public static final List<Class<? extends Block>> INVALID_CRATE_BLOCKS = new ArrayList<>();
    public static final List<Class<? extends Block>> INVALID_SHULKER_BOX_BLOCKS = new ArrayList<>();

    @Config(name = "Show tooltip", description = "If true, hovering over a crate will show its contents in a tooltip.")
    public static boolean showTooltip = true;

    public static boolean isEnabled = false;

    @Override
    public void register() {
        for (VanillaVariantMaterial type : VanillaVariantMaterial.values()) {
            CRATE_BLOCKS.put(type, new CrateBlock(this, type));
        }

        INVALID_CRATE_BLOCKS.add(ShulkerBoxBlock.class);
        INVALID_CRATE_BLOCKS.add(CrateBlock.class);
        INVALID_SHULKER_BOX_BLOCKS.add(CrateBlock.class);

        SCREEN_HANDLER = RegistryHandler.screenHandler(ID, CrateScreenHandler::new);
        BLOCK_ENTITY = RegistryHandler.blockEntity(ID, CrateBlockEntity::new);

        isEnabled = this.enabled;
    }

    public static boolean canCrateInsertItem(ItemStack stack) {
        return !isEnabled || !INVALID_CRATE_BLOCKS.contains(ItemHelper.getBlockClass(stack));
    }

    public static boolean canShulkerBoxInsertItem(ItemStack stack) {
        return !isEnabled || !INVALID_SHULKER_BOX_BLOCKS.contains(ItemHelper.getBlockClass(stack));
    }

    public static CrateBlock getRandomCrateBlock(Random rand) {
        List<CrateBlock> values = new ArrayList<>(Crates.CRATE_BLOCKS.values());
        return values.get(rand.nextInt(values.size()));
    }
}
