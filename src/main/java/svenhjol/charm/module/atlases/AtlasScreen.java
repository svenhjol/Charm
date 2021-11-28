package svenhjol.charm.module.atlases;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import svenhjol.charm.Charm;
import svenhjol.charm.init.CharmResources;
import svenhjol.charm.module.atlases.AtlasInventory.Index;
import svenhjol.charm.module.atlases.AtlasInventory.MapInfo;
import svenhjol.charm.module.atlases.helper.ClientMapHelper;
import svenhjol.charm.screen.CharmContainerScreen;
import svenhjol.charm.screen.CharmImageButton;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;

/**
 * @author Lukas
 * @since 28.12.2020
 */
public class AtlasScreen extends CharmContainerScreen<AtlasContainer> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Charm.MOD_ID, "textures/gui/atlas_container.png");
    private static final RenderType MAP_DECORATIONS = RenderType.text(new ResourceLocation("textures/map/map_icons.png"));
    private static final int SIZE = 48;
    private static final int LEFT = 74;
    private static final int TOP = 16;
    private static final int BUTTON_SIZE = 9;
    private static final int BUTTON_DISTANCE = 3;
    private static final int CENTER = (SIZE - BUTTON_SIZE) / 2;
    private static final int MAX_MAPS = 8;
    private static final int NORMAL_SIZE = 128;
    private static final float BASE_SCALE = (float) SIZE / NORMAL_SIZE;
    private static final int LIGHT = 240;
    private final int slot;
    private final Map<ButtonDirection, CharmImageButton> buttons;
    private final WorldMap worldMap = new WorldMap();
    private final SingleMap singleMap = new SingleMap(null);
    private MapGui mapGui;
    private int lastSize;
    private final MapRenderer mapItemRenderer;
    private final Player player;

    public AtlasScreen(AtlasContainer screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title, CONTAINER_BACKGROUND);
        this.imageWidth = 175;
        this.imageHeight = 168;
        this.player = inv.player;
        AtlasInventory atlasInventory = screenContainer.getAtlasInventory();
        this.slot = inv.findSlotMatchingItem(atlasInventory.getAtlasItem());
        Map<Index, MapInfo> mapInfos = atlasInventory.getCurrentDimensionMapInfos(Minecraft.getInstance().level);
        lastSize = mapInfos.size();
        mapGui = lastSize > 1 ? getWorldMap() : getSingleMap(lastSize == 0 ? null : mapInfos.values().iterator().next());
        buttons = new EnumMap<>(ButtonDirection.class);
        for (ButtonDirection direction : ButtonDirection.values()) {
            buttons.put(direction, createButton(direction));
        }
        mapItemRenderer = Minecraft.getInstance().gameRenderer.getMapRenderer();
    }

    private CharmImageButton createButton(ButtonDirection dir) {
        return new CharmImageButton(() -> getX() + LEFT + dir.left, () -> getY() + TOP + dir.top, dir.width, dir.height,
            dir.texStart, 0, dir.height, 2 * dir.height, CharmResources.INVENTORY_BUTTONS, it -> mapGui.buttonClick(dir));
    }

    private static boolean isShiftClick() {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(handle, 340) || InputConstants.isKeyDown(handle, 344);
    }

    private WorldMap getWorldMap() {
        worldMap.fixedMapDistance = false;
        return worldMap;
    }

    private SingleMap getSingleMap(MapInfo mapInfo) {
        singleMap.mapInfo = mapInfo;
        return singleMap;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        updateGui();
        updateButtonState();
    }

    @Override
    protected void renderLabels(PoseStack matrices, int mouseX, int mouseY) {
        super.renderLabels(matrices, mouseX, mouseY);
        ClientMapHelper.renderMapWithBackground(matrices, LEFT, TOP, 0, BASE_SCALE, LIGHT, bufferSource -> mapGui.render(matrices, bufferSource, mouseX, mouseY));
    }

    private void updateGui() {
        Map<Index, MapInfo> mapInfos = menu.getAtlasInventory().getCurrentDimensionMapInfos(Minecraft.getInstance().level);
        int size = mapInfos.size();
        if (mapGui instanceof WorldMap) {
            if (mapInfos.size() <= 1) {
                changeGui(getSingleMap(mapInfos.isEmpty() ? null : mapInfos.values().iterator().next()));
            }
        } else if (mapGui instanceof SingleMap && size > lastSize) {
            mapInfos.values().stream().skip(size - 1).findAny().ifPresent(it -> changeGui(getSingleMap(it)));
        }
        lastSize = size;
    }

    private void updateButtonState() {
        buttons.forEach((direction, button) -> {
            button.visible = mapGui.buttonVisible(direction);
            if (button.visible) {
                button.active = mapGui.buttonEnabled(direction);
                if (!this.narratables.contains(button)) {
                    addRenderableWidget(button);
                }
            } else {
                removeButton(button);
            }
        });
    }

    private void removeButton(AbstractWidget button) {
        this.narratables.remove(button);
        this.children.remove(button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mapGui.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        if (type == ClickType.QUICK_MOVE) {
            sendAtlasTransfer(this.slot, slot.index, -1, Atlases.MoveMode.FROM_INVENTORY);
        } else {
            super.slotClicked(slot, slotId, mouseButton, type);
        }
    }

    private void changeGui(MapGui gui) {
        mapGui = gui;
    }

    private void renderDecorations(PoseStack matrices, MultiBufferSource buffer, MapItemSavedData mapData, float relativeScale, Predicate<MapDecoration> filter) {
        int k = 0;

        for (MapDecoration mapdecoration : mapData.getDecorations()) {
            if (!filter.test(mapdecoration)) continue;

            matrices.pushPose();
            matrices.translate(mapdecoration.getX() / 2f + 64, mapdecoration.getY() / 2f + 64, 0.02);
            matrices.mulPose(Vector3f.ZP.rotationDegrees(mapdecoration.getRot() * 22.5f));
            matrices.scale(relativeScale * 4, relativeScale * 4, 3);
            matrices.translate(-0.125, 0.125, 0);
            byte b0 = mapdecoration.getImage();
            float f1 = (float) (b0 % 16) / 16f;
            float f2 = (float) (b0 / 16) / 16f;
            float f3 = (float) (b0 % 16 + 1) / 16f;
            float f4 = (float) (b0 / 16 + 1) / 16f;
            Matrix4f matrix4f = matrices.last().pose();
            VertexConsumer builder = buffer.getBuffer(MAP_DECORATIONS);
            float z = k * 0.001f;
            builder.vertex(matrix4f, -1, 1, z).color(255, 255, 255, 255).uv(f1, f2).uv2(LIGHT).endVertex();
            builder.vertex(matrix4f, 1, 1, z).color(255, 255, 255, 255).uv(f3, f2).uv2(LIGHT).endVertex();
            builder.vertex(matrix4f, 1, -1, z).color(255, 255, 255, 255).uv(f3, f4).uv2(LIGHT).endVertex();
            builder.vertex(matrix4f, -1, -1, z).color(255, 255, 255, 255).uv(f1, f4).uv2(LIGHT).endVertex();
            matrices.popPose();

            ++k;
        }
    }

    private void sendAtlasTransfer(int atlasSlot, int mapX, int mapZ, Atlases.MoveMode mode) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        data.writeInt(atlasSlot);
        data.writeInt(mapX);
        data.writeInt(mapZ);
        data.writeEnum(mode);
        ClientPlayNetworking.send(Atlases.MSG_SERVER_ATLAS_TRANSFER, data);
    }

    private int getX() {
        return leftPos;
    }

    private int getY() {
        return topPos;
    }

    private enum ButtonDirection {
        LEFT(-BUTTON_SIZE - BUTTON_DISTANCE, CENTER, BUTTON_SIZE, BUTTON_SIZE, 77, -1, 0),
        TOP(CENTER, -BUTTON_SIZE - BUTTON_DISTANCE, BUTTON_SIZE, BUTTON_SIZE, 50, 0, -1),
        RIGHT(SIZE + BUTTON_DISTANCE, CENTER, BUTTON_SIZE, BUTTON_SIZE, 68, 1, 0),
        BOTTOM(CENTER, SIZE + BUTTON_DISTANCE, BUTTON_SIZE, BUTTON_SIZE, 59, 0, 1),
        BACK(82, -12, 16, 16, 86, 0, 0),
        OUT(79, SIZE + BUTTON_DISTANCE - 5, 8, 9, 102, 0, 0),
        IN(87, SIZE + BUTTON_DISTANCE - 5, 8, 9, 110, 0, 0);
        final int left;
        final int top;
        final int width;
        final int height;
        final int texStart;
        final Index vector;

        ButtonDirection(int left, int top, int width, int height, int texStart, int x, int y) {
            this.left = left;
            this.top = top;
            this.width = width;
            this.height = height;
            this.texStart = texStart;
            this.vector = Index.of(x, y);
        }
    }

    private interface MapGui {
        void render(PoseStack matrices, MultiBufferSource.BufferSource bufferSource, int mouseX, int mouseY);

        default boolean mouseClicked(double mouseX, double mouseY, int button) {
            return false;
        }

        void buttonClick(ButtonDirection direction);

        boolean buttonVisible(ButtonDirection direction);

        boolean buttonEnabled(ButtonDirection direction);
    }

    private static class Extremes {
        public final Index min;
        public final Index max;

        private Extremes(int minX, int maxX, int minY, int maxY) {
            min = Index.of(minX, minY);
            max = Index.of(maxX, maxY);
        }

        public int getMaxDistance() {
            return Math.max(max.x + 1 - min.x, max.y + 1 - min.y);
        }
    }

    private class WorldMap implements MapGui {
        private final Collector<Index, ?, Extremes> extremesCollector = Collector.of(() -> new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE},
                (acc, index) -> {
                    if (acc[0] > index.x) {
                        acc[0] = index.x;
                    }
                    if (acc[1] < index.x) {
                        acc[1] = index.x;
                    }
                    if (acc[2] > index.y) {
                        acc[2] = index.y;
                    }
                    if (acc[3] < index.y) {
                        acc[3] = index.y;
                    }
                }, (acc1, acc2) -> {
                    for (int i = 0; i < 4; ++i) {
                        acc1[i] += acc2[i];
                    }
                    return acc1;
                }, acc -> new Extremes(acc[0], acc[1], acc[2], acc[3]));
        private Index corner = null;
        private Extremes extremes = new Extremes(0, 0, 0, 0);
        private int maxMapDistance = 1;
        private int mapDistance = 1;
        private boolean fixedMapDistance = false;

        private boolean updateExtremes() {
            AtlasInventory inventory = menu.getAtlasInventory();
            Map<Index, MapInfo> mapInfos = inventory.getCurrentDimensionMapInfos(Minecraft.getInstance().level);
            if (mapInfos.isEmpty()) {
                return false;
            }
            extremes = mapInfos.keySet().stream().collect(extremesCollector);
            maxMapDistance = extremes.getMaxDistance();
            if (maxMapDistance > MAX_MAPS) {
                maxMapDistance = MAX_MAPS;
            }
            mapDistance = fixedMapDistance ? Math.min(mapDistance, maxMapDistance) : maxMapDistance;
            if (mapDistance < maxMapDistance || mapDistance == MAX_MAPS) {
                if (corner == null) {
                    corner = inventory.getIndexOf(player).minus(mapDistance / 2).clamp(extremes.min, extremes.max.plus(1 - mapDistance));
                }
            } else {
                corner = null;
            }
            return true;
        }

        @Override
        public void render(PoseStack matrices, MultiBufferSource.BufferSource bufferSource, int mouseX, int mouseY) {
            final Minecraft mc = Minecraft.getInstance();
            final Level level = mc.level;
            if (level == null || !updateExtremes()) {
                return;
            }
            float mapSize = (float) NORMAL_SIZE / mapDistance;
            float mapScale = 1f / mapDistance;
            Index currentMin = corner != null ? corner : extremes.min;
            AtlasInventory inventory = menu.getAtlasInventory();
            Map<Index, MapInfo> mapInfos = inventory.getCurrentDimensionMapInfos(Minecraft.getInstance().level);
            Index playerIndex = inventory.getIndexOf(player);
            for (Map.Entry<Index, MapInfo> mapInfo : mapInfos.entrySet()) {
                Index key = mapInfo.getKey();
                if (corner != null && (corner.x > key.x || key.x >= corner.x + mapDistance || corner.y > key.y || key.y >= corner.y + mapDistance)) {
                    continue;
                }
                int mapId = mapInfo.getValue().id;
                MapItemSavedData mapData = level.getMapData(MapItem.makeKey(mapId));
                if (mapData != null) {
                    matrices.pushPose();
                    matrices.translate(mapSize * (key.x - currentMin.x), mapSize * (key.y - currentMin.y), 0.1);
                    matrices.scale(mapScale, mapScale, 1);
                    mapItemRenderer.render(matrices, bufferSource, mapId, mapData, false, LIGHT);
                    matrices.translate(0, 0, 0.2);
                    renderDecorations(matrices, bufferSource, mapData, 1.5f * mapDistance,
                            it -> it.getType() != MapDecoration.Type.PLAYER_OFF_MAP && it.getType() != MapDecoration.Type.PLAYER_OFF_LIMITS &&
                                    (it.getType() != MapDecoration.Type.PLAYER || key.equals(playerIndex)));
                    matrices.popPose();
                }
            }
            drawLines(matrices);
        }

        private void drawLines(PoseStack matrices) {
            matrices.pushPose();
            matrices.translate(0, 0, 0.2);
            // need to revert the base scale to avoid some lines being to thin to be drawn
            matrices.scale(0.5f / BASE_SCALE, 0.5f / BASE_SCALE, 1);


            for (int xLine = 1; xLine < mapDistance; ++xLine) {
                vLine(matrices, xLine * 2 * SIZE / mapDistance, 0, 2 * SIZE, -1);
            }
            for (int yLine = 1; yLine < mapDistance; ++yLine) {
                hLine(matrices, 0, 2 * SIZE, yLine * 2 * SIZE / mapDistance, -1);
            }
            matrices.popPose();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            double normX = normalizeForMapArea(LEFT + getX(), mouseX);
            double normY = normalizeForMapArea(TOP + getY(), mouseY);
            if (button == 0 && 0 <= normX && normX < 1 && 0 <= normY && normY < 1) {
                ItemStack heldItem = menu.getCarried();
                if (!heldItem.isEmpty()) {
                    sendAtlasTransfer(slot, -1, -1, Atlases.MoveMode.FROM_HAND);
                } else {
                    if (updateExtremes()) {
                        Index currentMin = corner != null ? corner : extremes.min;
                        Index index = Index.of((int) (normX * mapDistance), (int) (normY * mapDistance)).plus(currentMin);
                        Map<Index, MapInfo> mapInfos = menu.getAtlasInventory().getCurrentDimensionMapInfos(Minecraft.getInstance().level);
                        MapInfo mapInfo = mapInfos.get(index);
                        if (mapInfo != null) {
                            if (isShiftClick()) {
                                sendAtlasTransfer(slot, mapInfo.x, mapInfo.z, Atlases.MoveMode.TO_INVENTORY);
                            } else {
                                changeGui(getSingleMap(mapInfo));
                            }
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public void buttonClick(ButtonDirection direction) {
            switch (direction) {
                case LEFT:
                case TOP:
                case RIGHT:
                case BOTTOM:
                    if (corner != null) {
                        corner = corner.plus(direction.vector.multiply(mapDistance)).clamp(extremes.min, extremes.max.plus(1 - mapDistance));
                    }
                    break;
                case IN:
                    fixedMapDistance = true;
                    --mapDistance;
                    if (mapDistance == 1) {
                        Map<Index, MapInfo> mapInfos = menu.getAtlasInventory().getCurrentDimensionMapInfos(Minecraft.getInstance().level);
                        changeGui(getSingleMap(mapInfos.get(corner != null ? corner : extremes.min)));
                    }
                    break;
                case OUT:
                    fixedMapDistance = true;
                    ++mapDistance;
                    break;
            }
        }

        @Override
        public boolean buttonVisible(ButtonDirection direction) {
            switch (direction) {
                case LEFT:
                case TOP:
                case RIGHT:
                case BOTTOM:
                    return corner != null;
                case IN:
                case OUT:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public boolean buttonEnabled(ButtonDirection direction) {
            switch (direction) {
                case LEFT:
                    return corner != null && corner.x > extremes.min.x;
                case TOP:
                    return corner != null && corner.y > extremes.min.y;
                case RIGHT:
                    return corner != null && corner.x + mapDistance <= extremes.max.x;
                case BOTTOM:
                    return corner != null && corner.y + mapDistance <= extremes.max.y;
                case IN:
                    return mapDistance > 1;
                case OUT:
                    return mapDistance < maxMapDistance;
                default:
                    return false;
            }
        }

        private double normalizeForMapArea(double base, double val) {
            return (val - base) / SIZE;
        }
    }

    private class SingleMap implements MapGui {
        private final Set<ButtonDirection> supportedDirections = EnumSet.of(ButtonDirection.LEFT, ButtonDirection.TOP, ButtonDirection.RIGHT,
                ButtonDirection.BOTTOM, ButtonDirection.BACK);
        private MapInfo mapInfo;

        public SingleMap(MapInfo mapInfo) {
            this.mapInfo = mapInfo;
        }

        @Override
        public void render(PoseStack matrices, MultiBufferSource.BufferSource bufferSource, int mouseX, int mouseY) {
            final Minecraft mc = Minecraft.getInstance();
            final Level level = mc.level;
            if (level == null) {
                return;
            }
            if (mapInfo != null) {
                int mapId = mapInfo.id;
                MapItemSavedData mapData = level.getMapData(MapItem.makeKey(mapId));
                if (mapData != null) {
                    matrices.pushPose();
                    matrices.translate(0, 0, 1);
                    mapItemRenderer.render(matrices, bufferSource, mapId, mapData, true, LIGHT);
                    renderDecorations(matrices, bufferSource, mapData, 2f, it -> true);
                    matrices.popPose();
                }
            }
        }

        @Override
        public void buttonClick(ButtonDirection direction) {
            if (direction == ButtonDirection.BACK) {
                changeGui(getWorldMap());
            } else {
                AtlasInventory inventory = menu.getAtlasInventory();
                Map<Index, MapInfo> mapInfos = inventory.getCurrentDimensionMapInfos(Minecraft.getInstance().level);
                MapInfo mapInfo1 = mapInfos.get(inventory.convertCoordsToIndex(mapInfo.x, mapInfo.z).plus(direction.vector));
                if (mapInfo1 != null) {
                    changeGui(getSingleMap(mapInfo1));
                }
            }
        }

        @Override
        public boolean buttonVisible(ButtonDirection direction) {
            return supportedDirections.contains(direction);
        }

        @Override
        public boolean buttonEnabled(ButtonDirection direction) {
            AtlasInventory inventory = menu.getAtlasInventory();
            Map<Index, MapInfo> mapInfos = inventory.getCurrentDimensionMapInfos(Minecraft.getInstance().level);
            if (direction == ButtonDirection.BACK) {
                return mapInfo == null && !mapInfos.isEmpty() || mapInfos.size() > 1;
            }
            if (mapInfo != null) {
                return mapInfos.containsKey(inventory.convertCoordsToIndex(mapInfo.x, mapInfo.z).plus(direction.vector));
            }
            return false;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0 && getX() + LEFT <= mouseX && mouseX < getX() + LEFT + SIZE && getY() + TOP <= mouseY && mouseY < getY() + TOP + SIZE) {
                ItemStack heldItem = menu.getCarried();
                if (!heldItem.isEmpty()) {
                    sendAtlasTransfer(slot, -1, -1, Atlases.MoveMode.FROM_HAND);
                } else if (mapInfo != null) {
                    if (isShiftClick()) {
                        sendAtlasTransfer(slot, mapInfo.x, mapInfo.z, Atlases.MoveMode.TO_INVENTORY);
                    } else {
                        sendAtlasTransfer(slot, mapInfo.x, mapInfo.z, Atlases.MoveMode.TO_HAND);
                    }
                    changeGui(getSingleMap(null));
                }
                return true;
            }
            return false;
        }
    }
}
