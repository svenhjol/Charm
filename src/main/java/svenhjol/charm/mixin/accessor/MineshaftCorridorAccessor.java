package svenhjol.charm.mixin.accessor;

import net.minecraft.util.math.BlockBox;
import net.minecraft.world.StructureWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(targets = {"net/minecraft/structure/MineshaftGenerator$MineshaftCorridor"})
@CharmMixin(required = true)
public interface MineshaftCorridorAccessor {
    @Invoker("method_36422")
    boolean invokePositionChecker(StructureWorldAccess world, BlockBox box, int x, int y, int z, int count);
}
