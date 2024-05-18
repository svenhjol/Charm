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
import svenhjol.charm.feature.core.custom_wood.common.ChestBlockEntity;
import svenhjol.charm.feature.core.custom_wood.common.TrappedChestBlockEntity;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class BlockEntityRenderer<T extends ChestBlockEntity & LidBlockEntity> extends ChestRenderer<T> {
    private static final Map<CustomMaterial, Map<ChestType, Material>> NORMAL_TEXTURES = new HashMap<>();
    private static final Map<CustomMaterial, Map<ChestType, Material>> TRAPPED_TEXTURES = new HashMap<>();

    public BlockEntityRenderer(BlockEntityRendererProvider.Context dispatcher) {
        super(dispatcher);
    }

    public static void addTexture(CustomMaterial material, ChestType chestType, ResourceLocation id, boolean trapped) {
        var textures = trapped
            ? BlockEntityRenderer.TRAPPED_TEXTURES
            : BlockEntityRenderer.NORMAL_TEXTURES;

        if (!textures.containsKey(material)) {
            textures.put(material, new HashMap<>());
        }

        textures.get(material).put(chestType, new Material(Sheets.CHEST_SHEET, id));
    }

    @Nullable
    public static Material getChestMaterial(BlockEntity blockEntity, ChestType chestType) {
        if (!(blockEntity instanceof ChestBlockEntity)) {
            return null;
        }

        var textures = blockEntity instanceof TrappedChestBlockEntity
            ? TRAPPED_TEXTURES
            : NORMAL_TEXTURES;

        var material = ((ChestBlockEntity)blockEntity).getMaterial();

        if (textures.containsKey(material)) {
            return textures.get(material).getOrDefault(chestType, null);
        }

        return null;
    }
}
