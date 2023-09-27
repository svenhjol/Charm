package svenhjol.charm.feature.variant_wood.renderer;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charm.feature.variant_wood.entity.VariantChestBlockEntity;
import svenhjol.charm.feature.variant_wood.entity.VariantTrappedChestBlockEntity;
import svenhjol.charmony_api.iface.IVariantMaterial;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class VariantChestBlockEntityRenderer<T extends VariantChestBlockEntity & LidBlockEntity> extends ChestRenderer<T> {
    private static final Map<IVariantMaterial, Map<ChestType, Material>> NORMAL_TEXTURES = new HashMap<>();
    private static final Map<IVariantMaterial, Map<ChestType, Material>> TRAPPED_TEXTURES = new HashMap<>();

    public VariantChestBlockEntityRenderer(BlockEntityRendererProvider.Context dispatcher) {
        super(dispatcher);
    }

    public static void addTexture(IVariantMaterial material, ChestType chestType, ResourceLocation id, boolean trapped) {
        var textures = trapped
            ? VariantChestBlockEntityRenderer.TRAPPED_TEXTURES
            : VariantChestBlockEntityRenderer.NORMAL_TEXTURES;

        if (!textures.containsKey(material)) {
            textures.put(material, new HashMap<>());
        }

        textures.get(material).put(chestType, new Material(Sheets.CHEST_SHEET, id));
    }

    @Nullable
    public static Material getChestMaterial(BlockEntity blockEntity, ChestType chestType) {
        if (!(blockEntity instanceof VariantChestBlockEntity)) {
            return null;
        }

        var textures = blockEntity instanceof VariantTrappedChestBlockEntity
            ? TRAPPED_TEXTURES
            : NORMAL_TEXTURES;

        var material = ((VariantChestBlockEntity)blockEntity).getMaterial();

        if (textures.containsKey(material)) {
            return textures.get(material).getOrDefault(chestType, null);
        }

        return null;
    }
}
