package svenhjol.charm.crafting.module;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ObjectHolder;
import svenhjol.charm.Charm;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.crafting.block.CrateBaseBlock;
import svenhjol.charm.crafting.block.CrateOpenBlock;
import svenhjol.charm.crafting.block.CrateSealedBlock;
import svenhjol.charm.crafting.container.CrateContainer;
import svenhjol.charm.crafting.inventory.CrateScreen;
import svenhjol.charm.crafting.tileentity.CrateTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.WoodType;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Module(mod = Charm.MOD_ID, category = CharmCategories.CRAFTING, hasSubscriptions = true)
public class Crate extends MesonModule
{
    public static Map<WoodType, CrateOpenBlock> openTypes = new HashMap<>();
    public static Map<WoodType, CrateSealedBlock> sealedTypes = new HashMap<>();
    public static List<Class<? extends Block>> invalidBlocks = new ArrayList<>();
    public static List<Class<? extends Item>> invalidItems = new ArrayList<>();

    @ObjectHolder("charm:crate")
    public static ContainerType<CrateContainer> container;

    @ObjectHolder("charm:crate")
    public static TileEntityType<CrateTileEntity> tile;

    @Override
    public void init()
    {
        // create all wood types for open and sealed crates
        for (WoodType wood : WoodType.values()) {
            openTypes.put(wood, new CrateOpenBlock(this, wood));
            sealedTypes.put(wood, new CrateSealedBlock(this, wood));
        }

        // add invalid blocks
        CharmApi.addInvalidCrateBlock(ShulkerBoxBlock.class);
        CharmApi.addInvalidCrateBlock(CrateOpenBlock.class);
        CharmApi.addInvalidCrateBlock(CrateSealedBlock.class);

        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, "crate");

        container = new ContainerType<>(CrateContainer::instance);
        tile = TileEntityType.Builder.create(CrateTileEntity::new).build(null);

        RegistryHandler.registerContainer(container, res);
        RegistryHandler.registerTile(tile, res);
        RegistryHandler.registerSound(CharmSounds.WOOD_SMASH);
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out = event.getOutput();
        if (left.isEmpty() || right.isEmpty()) return;

        Block block = Block.getBlockFromItem(left.getItem());
        if (!(block instanceof CrateBaseBlock)) return;

        if (right.getItem() == Items.IRON_INGOT && block instanceof CrateOpenBlock) {
            WoodType wood = ((CrateOpenBlock) block).getWood();

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

    public static boolean canInsertItem(ItemStack stack)
    {
        Class<? extends Block> blockClass = Block.getBlockFromItem(stack.getItem()).getClass();
        return !invalidItems.contains(stack.getItem().getClass())
            && !invalidBlocks.contains(blockClass);
    }

    @Override
    public void setupClient(FMLClientSetupEvent event)
    {
        ScreenManager.registerFactory(container, CrateScreen::new);
    }
}
