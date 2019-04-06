package svenhjol.charm.brewing.block;

import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.feature.FlavoredCake;
import svenhjol.meson.IMesonBlock;
import svenhjol.meson.IMesonBlock.*;

/**
 * Block-flavored Cake.  Mmmmm
 */
@SuppressWarnings("NullableProblems")
public class BlockFlavoredCake extends BlockCake implements IMesonBlock, IHasCustomInventoryItemModel, IHasCustomStateMapper
{
    public Potion flavor;
    private static ModelResourceLocation MODEL_INVENTORY = new ModelResourceLocation(new ResourceLocation("minecraft", "cake"), "inventory");

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    public BlockFlavoredCake(String name)
    {
        super();
        this.register("cake_" + name);
        this.flavor = Potion.getPotionFromResourceLocation(name);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        boolean eaten = super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        if (eaten) {
            PotionEffect effect = new PotionEffect(flavor, FlavoredCake.duration * 20, FlavoredCake.amplifier);
            playerIn.addPotionEffect(effect);
        }

        return eaten;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setStateMapper()
    {
        ModelLoader.setCustomStateMapper(this, FlavoredCakeStateMapper.INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setInventoryItemModel()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, MODEL_INVENTORY);
    }
}
