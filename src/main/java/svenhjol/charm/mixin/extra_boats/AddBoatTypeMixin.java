package svenhjol.charm.mixin.extra_boats;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.module.azalea_wood.AzaleaWood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"SameParameterValue", "unused", "target"})
@Mixin(Boat.Type.class)
public class AddBoatTypeMixin {
    @Shadow
    @Final
    @Mutable
    private static Boat.Type[] $VALUES;

    static {
        addVariant("AZALEA", AzaleaWood.PLANKS, "charm_azalea");
    }

    @Invoker("<init>")
    public static Boat.Type invokeInit(String internalName, int internalId, Block block, String type) {
        throw new AssertionError();
    }

    private static void addVariant(String newName, Block block, String type) {
        List<Boat.Type> variants = new ArrayList<>(Arrays.asList(AddBoatTypeMixin.$VALUES));
        variants.add(invokeInit(newName, variants.get(variants.size() - 1).ordinal() + 1, block, type));
        AddBoatTypeMixin.$VALUES = variants.toArray(new Boat.Type[0]);
    }
}
