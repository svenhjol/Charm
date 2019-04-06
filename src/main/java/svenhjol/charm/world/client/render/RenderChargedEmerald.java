package svenhjol.charm.world.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import svenhjol.charm.world.entity.EntityChargedEmerald;
import svenhjol.charm.world.feature.ChargedEmeralds;

public class RenderChargedEmerald extends RenderSnowball<EntityChargedEmerald>
{
    public static final IRenderFactory FACTORY = RenderChargedEmerald::new;

    public RenderChargedEmerald(RenderManager manager)
    {
        super(manager, ChargedEmeralds.emerald, Minecraft.getMinecraft().getRenderItem());
    }

    @Override
    public ItemStack getStackToRender(EntityChargedEmerald entityIn)
    {
        return new ItemStack(ChargedEmeralds.emerald, 1);
    }
}
