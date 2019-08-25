package svenhjol.charm.world.module;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.world.entity.EndermitePowderEntity;
import svenhjol.charm.world.item.EndermitePowderItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD, hasSubscriptions = true)
public class EndermitePowder extends MesonModule
{
    public static EndermitePowderItem item;
    public static EntityType<?> entity;

    @Override
    public void init()
    {
        item = new EndermitePowderItem(this);
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, "endermite_powder");

        entity = EntityType.Builder.create(EndermitePowderEntity::new, EntityClassification.MISC)
            .setTrackingRange(80)
            .setUpdateInterval(10)
            .setShouldReceiveVelocityUpdates(false)
            .size(2.0F, 2.0F)
            .build(res.getPath())
            .setRegistryName(res);

        RegistryHandler.registerEntity(entity, res);
    }

    @SubscribeEvent
    public void onEndermiteDrops(LivingDropsEvent event)
    {
        if (!event.getEntityLiving().world.isRemote
            && event.getEntityLiving() instanceof EndermiteEntity
            && event.getSource().getTrueSource() instanceof PlayerEntity
            && event.getEntityLiving().world.rand.nextFloat() <= 0.5D + (0.1D * event.getLootingLevel())
        ) {
            EndermiteEntity endermite = (EndermiteEntity)event.getEntityLiving();
            ItemStack stack = new ItemStack(item);
            event.getDrops().add(new ItemEntity(endermite.getEntityWorld(), endermite.posX, endermite.posY, endermite.posZ, stack));
        }
    }
}
