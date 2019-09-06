package svenhjol.charm.decoration.module;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ObjectHolder;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.decoration.block.CrateBaseBlock;
import svenhjol.charm.decoration.block.CrateOpenBlock;
import svenhjol.charm.decoration.block.CrateSealedBlock;
import svenhjol.charm.decoration.container.CrateContainer;
import svenhjol.charm.decoration.inventory.CrateScreen;
import svenhjol.charm.decoration.tileentity.CrateTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.WoodType;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.helper.ClientHelper;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION, hasSubscriptions = true,
    description = "A smaller storage solution with the benefit of being transportable." +
        "You can also seal a crate by combining it with an iron ingot on an anvil.  The only way to get things out is to break it.")
public class Crates extends MesonModule
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
    public void setupClient(FMLClientSetupEvent event)
    {
        ScreenManager.registerFactory(container, CrateScreen::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void effectInteract(BlockPos pos, int type)
    {
        ClientWorld world = ClientHelper.getClientWorld();
        PlayerEntity player = ClientHelper.getClientPlayer();
        if (!player.isSpectator()) {
            SoundEvent sound = null;
            switch (type) {
                case 0: sound = SoundEvents.BLOCK_BARREL_OPEN; break;
                case 1: sound = SoundEvents.BLOCK_BARREL_CLOSE; break;
                case 2: sound = CharmSounds.WOOD_SMASH; break;
            }
            if (sound != null) {
                world.playSound(player, pos, sound, SoundCategory.BLOCKS, 0.5f, world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    public static boolean canInsertItem(ItemStack stack)
    {
        Class<? extends Block> blockClass = Block.getBlockFromItem(stack.getItem()).getClass();
        return !invalidItems.contains(stack.getItem().getClass())
            && !invalidBlocks.contains(blockClass);
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
}
