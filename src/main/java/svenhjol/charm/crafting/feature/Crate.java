package svenhjol.charm.crafting.feature;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.block.CrateBaseBlock;
import svenhjol.charm.crafting.block.CrateOpenBlock;
import svenhjol.charm.crafting.block.CrateSealedBlock;
import svenhjol.charm.crafting.tileentity.CrateTileEntity;
import svenhjol.meson.Feature;
import svenhjol.meson.enums.WoodType;
import svenhjol.meson.handler.RegistrationHandler;

import java.util.HashMap;
import java.util.Map;


public class Crate extends Feature
{
    public static Map<WoodType, CrateOpenBlock> openTypes = new HashMap<>();
    public static Map<WoodType, CrateSealedBlock> sealedTypes = new HashMap<>();

    @Override
    public void configure()
    {
        super.configure();
    }

    @Override
    public void init()
    {
        super.init();

        // register all the crate types and woods
        for (WoodType wood : WoodType.values()) {
            openTypes.put(wood, new CrateOpenBlock(wood));
            sealedTypes.put(wood, new CrateSealedBlock(wood));
        }

        // register tile entity
        RegistrationHandler.addTileEntity(new ResourceLocation(Charm.MOD_ID, "crate"), CrateTileEntity.factory());
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

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
