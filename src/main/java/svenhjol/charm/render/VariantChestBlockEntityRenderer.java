package svenhjol.charm.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.blockentity.VariantChestBlockEntity;
import svenhjol.charm.blockentity.VariantTrappedChestBlockEntity;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class VariantChestBlockEntityRenderer<T extends VariantChestBlockEntity & ChestAnimationProgress> extends ChestBlockEntityRenderer<T> {
    private static final Map<IVariantMaterial, Map<ChestType, SpriteIdentifier>> normalTextures = new HashMap<>();
    private static final Map<IVariantMaterial, Map<ChestType, SpriteIdentifier>> trappedTextures = new HashMap<>();

    public VariantChestBlockEntityRenderer(BlockEntityRendererFactory.Context dispatcher) {
        super(dispatcher);
    }

    public static void addTexture(IVariantMaterial material, ChestType chestType, Identifier id, boolean trapped) {
        Map<IVariantMaterial, Map<ChestType, SpriteIdentifier>> textures = trapped
            ? VariantChestBlockEntityRenderer.trappedTextures
            : VariantChestBlockEntityRenderer.normalTextures;

        if (!textures.containsKey(material))
            textures.put(material, new HashMap<>());

        textures.get(material).put(chestType, new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, id));
    }

    @Nullable
    public static SpriteIdentifier getMaterial(BlockEntity blockEntity, ChestType chestType) {
        if (!(blockEntity instanceof VariantChestBlockEntity))
            return null;

        Map<IVariantMaterial, Map<ChestType, SpriteIdentifier>> textures = blockEntity instanceof VariantTrappedChestBlockEntity
            ? trappedTextures
            : normalTextures;

        IVariantMaterial material = ((VariantChestBlockEntity)blockEntity).getMaterialType();

        if (textures.containsKey(material))
            return textures.get(material).getOrDefault(chestType, null);

        return null;
    }
}
