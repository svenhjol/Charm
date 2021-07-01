package svenhjol.charm.module.glowballs;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CommonModule;

@Module(mod = Charm.MOD_ID, description = "Glowballs can be thrown to produce a light source where they impact.")
public class Glowballs extends CommonModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "glowball");
    public static final ResourceLocation TRIGGER_THROWN_GLOWBALL = new ResourceLocation(Charm.MOD_ID, "thrown_glowball");

    public static GlowballItem GLOWBALL_ITEM;
    public static GlowballBlobBlock GLOWBALL_BLOCK;
    public static EntityType<GlowballEntity> GLOWBALL;

    @Override
    public void register() {
        GLOWBALL_BLOCK = new GlowballBlobBlock(this);
        GLOWBALL_ITEM = new GlowballItem(this);

        GLOWBALL = RegistryHelper.entity(ID, FabricEntityTypeBuilder
            .<GlowballEntity>create(MobCategory.MISC, GlowballEntity::new)
            .trackRangeBlocks(4)
            .trackedUpdateRate(10)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)));

        DispenserBlock.registerBehavior(GLOWBALL_ITEM, new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level world, Position position, ItemStack stack) {
            return Util.make(new GlowballEntity(world, position.x(), position.y(), position.z()), (entity) -> entity.setItem(stack));
            }
        });
    }

    public static void triggerThrownGlowball(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_THROWN_GLOWBALL);
    }
}
