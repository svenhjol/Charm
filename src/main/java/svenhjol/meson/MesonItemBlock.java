package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IMesonItemBlock;

import java.util.ArrayList;
import java.util.List;

public class MesonItemBlock extends ItemBlock implements IMesonItemBlock
{
    public IMesonBlock block;
    public List<ResourceLocation> variants = new ArrayList<>();

    public MesonItemBlock(Block block, ResourceLocation name)
    {
        super(block);

        this.block = (IMesonBlock)block;

        if (this.block.getVariants().length > 0) {
            setHasSubtypes(true);
        }
        setRegistryName(name);
    }

    @Override
    public String getModId()
    {
        return block.getModId();
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack stack)
    {
        int meta = stack.getItemDamage();
        String[] variants = block.getVariants();
        String name;

        if (meta >= variants.length) {
            name = block.getName();
        } else {
            name = block.getName() + "_" + variants[meta];
        }

        return "tile." + block.getModId() + ":" + name;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        String[] variants = block.getVariants();
        if (isInCreativeTab(tab)) {
            for (int i = 0; i < variants.length; i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @SuppressWarnings("unused")
    public ItemMeshDefinition getCustomMeshDefinition()
    {
        return null;
    }
}
