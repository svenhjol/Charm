package svenhjol.charm.mixin.feature.copper_pistons;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.PistonType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.copper_pistons.CopperPistons;

@SuppressWarnings("UnreachableCode")
@Mixin(PistonHeadBlock.class)
public class PistonHeadBlockMixin {
    @Shadow
    @Final
    public static EnumProperty<PistonType> TYPE;

    @ModifyReturnValue(
            method = "getCloneItemStack",
            at = @At("RETURN")
    )
    private ItemStack hookGetCloneItemStack(ItemStack original, @Local(argsOnly = true) BlockState blockState) {
        if (isCopperPistonBlock()) {
            var copperPistons = Resolve.feature(CopperPistons.class);
            copperPistons.log().debug("changing itemstack to copper");
            return new ItemStack(
                    blockState.getValue(TYPE) == PistonType.STICKY
                            ? copperPistons.registers.stickyCopperPistonBlock.get()
                            : copperPistons.registers.copperPistonBlock.get()
            );
        }
        return original;
    }

    @Unique
    private boolean isCopperPistonBlock() {
        var pistonBlockState = ((PistonHeadBlock)(Object)this).defaultBlockState();
        return pistonBlockState.is(Resolve.feature(CopperPistons.class).registers.copperPistonBlock.get());
    }
}
