package svenhjol.charm.feature.core.custom_wood.client;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charm.api.iface.CustomMaterial;
import svenhjol.charm.feature.core.custom_wood.blocks.entity.CustomChestBlockEntity;
import svenhjol.charm.feature.core.custom_wood.blocks.entity.CustomTrappedChestBlockEntity;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CustomBlockEntityRenderer<T extends CustomChestBlockEntity & LidBlockEntity> extends ChestRenderer<T> {
    private static final Map<CustomMaterial, Map<ChestType, Material>> NORMAL_TEXTURES = new HashMap<>();
    private static final Map<CustomMaterial, Map<ChestType, Material>> TRAPPED_TEXTURES = new HashMap<>();

    public CustomBlockEntityRenderer(BlockEntityRendererProvider.Context dispatcher) {
        super(dispatcher);
    }

    public static void addTexture(CustomMaterial material, ChestType chestType, ResourceLocation id, boolean trapped) {
        var textures = trapped
            ? CustomBlockEntityRenderer.TRAPPED_TEXTURES
            : CustomBlockEntityRenderer.NORMAL_TEXTURES;

        if (!textures.containsKey(material)) {
            textures.put(material, new HashMap<>());
        }

        textures.get(material).put(chestType, new Material(Sheets.CHEST_SHEET, id));
    }

    @Nullable
    public static Material getChestMaterial(BlockEntity blockEntity, ChestType chestType) {
        if (!(blockEntity instanceof CustomChestBlockEntity)) {
            return null;
        }

        var textures = blockEntity instanceof CustomTrappedChestBlockEntity
            ? TRAPPED_TEXTURES
            : NORMAL_TEXTURES;

        var material = ((CustomChestBlockEntity)blockEntity).getMaterial();

        if (textures.containsKey(material)) {
            return textures.get(material).getOrDefault(chestType, null);
        }

        return null;
    }
}
