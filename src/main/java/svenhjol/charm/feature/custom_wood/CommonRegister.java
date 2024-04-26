package svenhjol.charm.feature.custom_wood;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.foundation.Register;

public class CommonRegister extends Register<CustomWood>  {
    public CommonRegister(CustomWood feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        LevelLoadEvent.INSTANCE.handle(this::handleLevelLoad);
    }

    @SuppressWarnings("deprecation")
    private void handleLevelLoad(MinecraftServer server, ServerLevel level) {
        if (level.dimension() == Level.OVERWORLD) {
            // Set each boat type's planks.
            CustomWoodHelper.getBoatPlanks().forEach(
                (type, id) -> BuiltInRegistries.BLOCK.getOptional(id).ifPresent(
                    block -> type.planks = block));

            // Set each sign's block and item.
            CustomWoodHelper.getSignItems().forEach(supplier -> {
                var sign = supplier.get();
                sign.wallBlock = sign.getWallSignBlock().get();
                sign.wallBlock.item = sign;
                sign.block = sign.getSignBlock().get();
                sign.block.item = sign;
            });

            // Set each hanging sign's block and item.
            CustomWoodHelper.getHangingSignItems().forEach(supplier -> {
                var sign = supplier.get();
                sign.wallBlock = sign.getWallSignBlock().get();
                sign.wallBlock.item = sign;
                sign.block = sign.getHangingBlock().get();
                sign.block.item = sign;
            });
        }
    }
}
