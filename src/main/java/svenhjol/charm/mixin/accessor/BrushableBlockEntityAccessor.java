package svenhjol.charm.mixin.accessor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import javax.annotation.Nullable;

@Mixin(BrushableBlockEntity.class)
public interface BrushableBlockEntityAccessor {
    @Accessor("lootTable")
    void setLootTable(@Nullable ResourceLocation lootTable);

    @Accessor("item")
    void setItem(ItemStack item);
}
