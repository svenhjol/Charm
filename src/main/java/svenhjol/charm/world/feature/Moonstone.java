package svenhjol.charm.world.feature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.world.item.ItemMoonstone;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.RecipeHandler;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.registry.ProxyRegistry;

public class Moonstone extends Feature
{
    public static ItemMoonstone moonstone;
    public static boolean showInformation;
    public static boolean glowOnOrigin;

    @Override
    public String getDescription()
    {
        return "The moonstone helps you locate a specific block co-ordinate or aid axis alignment across dimensions.";
    }

    @Override
    public void setupConfig()
    {
        showInformation = propBoolean(
            "Add coordinates to tooltip",
            "If true, adds co-ordinates and dimension to the moonstone tooltip.",
            true
        );
        glowOnOrigin = propBoolean(
            "Add glow effect on origin",
            "If true, the moonstone will have the glow effect when player is standing on the origin X and Z co-ordinate.",
            true
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        moonstone = new ItemMoonstone();
        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(moonstone, 1),
            " G ", "GQG", " G ",
            'Q', Items.QUARTZ,
            'G', Items.GLOWSTONE_DUST
        );

        for (EnumDyeColor value : EnumDyeColor.values()) {
            int meta = value.getDyeDamage();
            if (meta == 0) continue;
            RecipeHandler.addShapelessRecipe(ProxyRegistry.newStack(moonstone, 1, meta),
                ProxyRegistry.newStack(moonstone),
                ProxyRegistry.newStack(Items.DYE, 1, meta)
            );
            RecipeHandler.addShapelessRecipe(ProxyRegistry.newStack(moonstone, 1, 0),
                ProxyRegistry.newStack(moonstone, 1, meta));
        }

    }

    @SubscribeEvent
    public void onMoonstoneUse(RightClickBlock event)
    {
        if (!event.getWorld().isRemote
            && event.getEntityPlayer() != null
            && event.getEntityPlayer().getHeldItem(event.getHand()).getItem() == moonstone
        ) {
            World world = event.getWorld();
            EntityPlayer player = event.getEntityPlayer();
            EnumHand hand = event.getHand();
            ItemStack held = player.getHeldItem(hand);
            BlockPos pos = event.getPos();
            ItemStack item = new ItemStack(moonstone, 1, held.getMetadata());

            if (player.isSneaking()) {
                // when sneaking, bind to coordinates
                ItemMoonstone.setStonePos(item, pos);
                ItemMoonstone.setStoneDim(item, world.provider.getDimension());

                player.swingArm(hand);
                PlayerHelper.setHeldItem(player, hand, item);
            } else {
                // when the block has dye color then try and set the stone to the same
                world.getBlockState(pos).getProperties().forEach((p1, p2) -> {
                    if (p1.getValueClass() != EnumDyeColor.class) return;
                    int meta = EnumDyeColor.valueOf(p2.toString().toUpperCase()).getDyeDamage();
                    held.setItemDamage(meta);
                });
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
