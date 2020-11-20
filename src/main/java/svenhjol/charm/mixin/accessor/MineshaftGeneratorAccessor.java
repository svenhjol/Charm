package svenhjol.charm.mixin.accessor;

import net.minecraft.world.gen.feature.MineshaftFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = {"net/minecraft/structure/MineshaftGenerator$MineshaftPart"})
public interface MineshaftGeneratorAccessor {
    @Accessor
    MineshaftFeature.Type getMineshaftType();
}
