package svenhjol.charm.module;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.GlowballBlobBlock;
import svenhjol.charm.client.GlowballsClient;
import svenhjol.charm.entity.GlowballEntity;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.item.GlowballItem;

@Module(mod = Charm.MOD_ID, client = GlowballsClient.class, description = "Glowballs can be thrown to produce a light source where they impact.")
public class Glowballs extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "glowball");
    public static final Identifier TRIGGER_THROWN_GLOWBALL = new Identifier(Charm.MOD_ID, "thrown_glowball");

    public static GlowballItem GLOWBALL_ITEM;
    public static GlowballBlobBlock GLOWBALL_BLOCK;
    public static EntityType<GlowballEntity> GLOWBALL;

    @Override
    public void register() {
        GLOWBALL_BLOCK = new GlowballBlobBlock(this);
        GLOWBALL_ITEM = new GlowballItem(this);

        GLOWBALL = RegistryHandler.entity(ID, FabricEntityTypeBuilder
            .<GlowballEntity>create(SpawnGroup.MISC, GlowballEntity::new)
            .trackRangeBlocks(4)
            .trackedUpdateRate(10)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)));

        DispenserBlock.registerBehavior(GLOWBALL_ITEM, new ProjectileDispenserBehavior() {
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
            return Util.make(new GlowballEntity(world, position.getX(), position.getY(), position.getZ()), (entity) -> entity.setItem(stack));
            }
        });
    }

    public static void triggerThrownGlowball(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_THROWN_GLOWBALL);
    }
}
