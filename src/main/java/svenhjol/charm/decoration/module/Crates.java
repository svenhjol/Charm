package svenhjol.charm.decoration.module;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ObjectHolder;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.block.CrateBaseBlock;
import svenhjol.charm.decoration.block.CrateOpenBlock;
import svenhjol.charm.decoration.block.CrateSealedBlock;
import svenhjol.charm.decoration.client.CratesClient;
import svenhjol.charm.decoration.container.CrateContainer;
import svenhjol.charm.decoration.inventory.CrateScreen;
import svenhjol.charm.decoration.tileentity.CrateTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IWoodType;
import svenhjol.meson.enums.VanillaWoodType;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION, hasSubscriptions = true,
    description = "A smaller storage solution with the benefit of being transportable.\n" +
        "You can also seal a crate by combining it with an iron ingot on an anvil.  The only way to get things out is to break it.")
public class Crates extends MesonModule {
    public static Map<IWoodType, CrateOpenBlock> openTypes = new HashMap<>();
    public static Map<IWoodType, CrateSealedBlock> sealedTypes = new HashMap<>();
    public static List<Class<? extends Block>> invalidBlocks = new ArrayList<>();
    public static List<Class<? extends Item>> invalidItems = new ArrayList<>();

    @ObjectHolder("charm:crate")
    public static ContainerType<CrateContainer> container;

    @ObjectHolder("charm:crate")
    public static TileEntityType<CrateTileEntity> tile;

    @Config(name = "Show tooltips", description = "If true, hovering over a crate will show its contents in a tooltip.")
    public static boolean showTooltips = true;

    public static CratesClient client;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void init() {
        // create all wood types for open and sealed crates
        for (VanillaWoodType wood : VanillaWoodType.values()) {
            openTypes.put(wood, new CrateOpenBlock(this, wood));
            sealedTypes.put(wood, new CrateSealedBlock(this, wood));
        }

        // add invalid blocks
        invalidBlocks.add(ShulkerBoxBlock.class);
        invalidBlocks.add(CrateOpenBlock.class);
        invalidBlocks.add(CrateSealedBlock.class);

        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, "crate");

        container = new ContainerType<>(CrateContainer::instance);
        tile = TileEntityType.Builder.create(CrateTileEntity::new).build(null);

        RegistryHandler.registerContainer(container, res);
        RegistryHandler.registerTile(tile, res);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(container, CrateScreen::new);
        client = new CratesClient();
        MinecraftForge.EVENT_BUS.register(client);
    }

    public static boolean canInsertItem(ItemStack stack) {
        Class<? extends Block> blockClass = Block.getBlockFromItem(stack.getItem()).getClass();
        return !invalidItems.contains(stack.getItem().getClass())
            && !invalidBlocks.contains(blockClass);
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out = event.getOutput();
        if (left.isEmpty() || right.isEmpty()) return;

        Block block = Block.getBlockFromItem(left.getItem());
        if (!(block instanceof CrateBaseBlock)) return;

        if (right.getItem() == Items.IRON_INGOT && block instanceof CrateOpenBlock) {
            IWoodType wood = ((CrateOpenBlock) block).getWood();

            out = new ItemStack(sealedTypes.get(wood));
            out.setTag(left.getTag());
            out.setDisplayName(left.getDisplayName());
        }

        if (!out.isEmpty()) {
            event.setOutput(out);
            event.setCost(1);
            event.setMaterialCost(1);
        }
    }
}
