package svenhjol.charm.module.variant_chests;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charm.enums.IVariantMaterial;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class VariantChestBlockEntityRenderer<T extends VariantChestBlockEntity & LidBlockEntity> extends ChestRenderer<T> {
    private static final Map<IVariantMaterial, Map<ChestType, Material>> normalTextures = new HashMap<>();
    private static final Map<IVariantMaterial, Map<ChestType, Material>> trappedTextures = new HashMap<>();

    public VariantChestBlockEntityRenderer(BlockEntityRendererProvider.Context dispatcher) {
        super(dispatcher);
    }

    public static void addTexture(IVariantMaterial material, ChestType chestType, ResourceLocation id, boolean trapped) {
        Map<IVariantMaterial, Map<ChestType, Material>> textures = trapped
            ? VariantChestBlockEntityRenderer.trappedTextures
            : VariantChestBlockEntityRenderer.normalTextures;

        if (!textures.containsKey(material))
            textures.put(material, new HashMap<>());

        textures.get(material).put(chestType, new Material(Sheets.CHEST_SHEET, id));
    }

    @Nullable
    public static Material getMaterial(BlockEntity blockEntity, ChestType chestType) {
        if (!(blockEntity instanceof VariantChestBlockEntity))
            return null;

        Map<IVariantMaterial, Map<ChestType, Material>> textures = blockEntity instanceof VariantTrappedChestBlockEntity
            ? trappedTextures
            : normalTextures;

        IVariantMaterial material = ((VariantChestBlockEntity)blockEntity).getMaterialType();

        if (textures.containsKey(material))
            return textures.get(material).getOrDefault(chestType, null);

        return null;
    }
}
