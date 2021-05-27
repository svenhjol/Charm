package svenhjol.charm.mixin.accessor;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(ItemRenderer.class)
@CharmMixin(required = true)
public interface ItemRendererAccessor {
    @Invoker("renderBakedItemModel")
    void invokeRenderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices);
}
