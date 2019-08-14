package svenhjol.charm.crafting.feature;

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
import net.minecraftforge.registries.ObjectHolder;
import svenhjol.charm.Charm;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.crafting.block.CrateBaseBlock;
import svenhjol.charm.crafting.block.CrateOpenBlock;
import svenhjol.charm.crafting.block.CrateSealedBlock;
import svenhjol.charm.crafting.container.CrateContainer;
import svenhjol.charm.crafting.inventory.CrateScreen;
import svenhjol.charm.crafting.tileentity.CrateTileEntity;
import svenhjol.meson.Feature;
import svenhjol.meson.enums.WoodType;
import svenhjol.meson.handler.RegistryHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Crate extends Feature
{
    public static Map<WoodType, CrateOpenBlock> openTypes = new HashMap<>();
    public static Map<WoodType, CrateSealedBlock> sealedTypes = new HashMap<>();
    public static List<Class<? extends Block>> invalidBlocks = new ArrayList<>();
    public static List<Class<? extends Item>> invalidItems = new ArrayList<>();

    @ObjectHolder("charm:crate")
    public static ContainerType<CrateContainer> crate;

    @ObjectHolder("charm:crate")
    public static TileEntityType<CrateTileEntity> tile;

    @Override
    public void init()
    {
        super.init();

        // create all wood types for open and sealed crates
        for (WoodType wood : WoodType.values()) {
            openTypes.put(wood, new CrateOpenBlock(wood));
            sealedTypes.put(wood, new CrateSealedBlock(wood));
        }

        // add invalid blocks
        CharmApi.addInvalidCrateBlock(ShulkerBoxBlock.class);
        CharmApi.addInvalidCrateBlock(CrateOpenBlock.class);
        CharmApi.addInvalidCrateBlock(CrateSealedBlock.class);

        crate = new ContainerType<>(CrateContainer::instance);
        tile = TileEntityType.Builder.create(CrateTileEntity::new).build(null);

        RegistryHandler.registerContainer(crate.setRegistryName(new ResourceLocation(Charm.MOD_ID, "crate")));
        RegistryHandler.registerTile(tile.setRegistryName(new ResourceLocation(Charm.MOD_ID, "crate")));
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
            WoodType wood = ((CrateOpenBlock) block).wood;

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
        return !Crate.invalidItems.contains(stack.getItem().getClass())
            && !Crate.invalidBlocks.contains(blockClass);
    }

    @Override
    public void registerScreens()
    {
        ScreenManager.registerFactory(crate, CrateScreen::new);
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
