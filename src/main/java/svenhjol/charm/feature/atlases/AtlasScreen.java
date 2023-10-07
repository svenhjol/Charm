package svenhjol.charm.feature.atlases;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix4f;
import svenhjol.charmony.base.CharmonyContainerScreen;
import svenhjol.charmony.helper.KeyboardHelper;

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
@SuppressWarnings("ConstantConditions")
public class AtlasScreen extends CharmonyContainerScreen<AtlasContainer> {

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
    private final WorldMap worldMap = new WorldMap();
    private final SingleMap singleMap = new SingleMap(null);
    private Map<ButtonDirection, AtlasImageButton> buttons;
    private MapGui mapGui;
    private int slot;
    private int lastSize;
    private MapRenderer mapItemRenderer;
    private final Player player;
    private final Inventory inventory;

    public AtlasScreen(AtlasContainer menu, Inventory inv, Component title) {
        super(menu, inv, title, AtlasesClient.CONTAINER_BACKGROUND);
        this.imageWidth = 175;
        this.imageHeight = 168;
        this.inventory = inv;
        this.player = inv.player;
    }

    @Override
    protected void init() {
        super.init();

        var atlasInventory = menu.getInventory();
        var mapInfos = atlasInventory.getCurrentDimensionMapInfos(minecraft.level);

        slot = inventory.findSlotMatchingItem(atlasInventory.getAtlasItem());
        lastSize = mapInfos.size();
        mapGui = lastSize > 1 ? getWorldMap() : getSingleMap(lastSize == 0 ? null : mapInfos.values().iterator().next());
        buttons = new EnumMap<>(ButtonDirection.class);

        for (var direction : ButtonDirection.values()) {
            buttons.put(direction, createButton(direction));
        }

        mapItemRenderer = minecraft.gameRenderer.getMapRenderer();
    }

    private AtlasImageButton createButton(ButtonDirection dir) {
        return new AtlasImageButton(() -> getX() + LEFT + dir.left, () -> getY() + TOP + dir.top, dir.width, dir.height,
            dir.sprite, it -> mapGui.buttonClick(dir));
    }

    private WorldMap getWorldMap() {
        worldMap.fixedMapDistance = false;
        return worldMap;
    }

    private SingleMap getSingleMap(AtlasInventory.MapInfo mapInfo) {
        singleMap.mapInfo = mapInfo;
        return singleMap;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        updateGui();
        updateButtonState();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        var pose = guiGraphics.pose();

        pose.pushPose();
        pose.translate(LEFT, TOP, 0);
        pose.scale(BASE_SCALE, BASE_SCALE, 1);

        var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        var background = bufferSource.getBuffer(AtlasesClient.MAP_BACKGROUND);

        AtlasMapHelper.drawBackgroundVertex(pose, LIGHT, background);

        mapGui.render(guiGraphics, bufferSource, mouseX, mouseY);
        bufferSource.endBatch();
        pose.popPose();
    }

    private void updateGui() {
        var mapInfos = menu.getInventory().getCurrentDimensionMapInfos(Minecraft.getInstance().level);
        var size = mapInfos.size();

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
                if (!(this.narratables.contains(button))) {
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
            AtlasesNetwork.TransferAtlas.send(this.slot, slot.index, -1, MoveMode.FROM_INVENTORY);
        } else {
            super.slotClicked(slot, slotId, mouseButton, type);
        }
    }

    private void changeGui(MapGui gui) {
        mapGui = gui;
    }

    private void renderDecorations(PoseStack pose, MultiBufferSource buffer, MapItemSavedData mapData, float relativeScale, Predicate<MapDecoration> filter) {
        int k = 0;

        for (var decoration : mapData.getDecorations()) {
            if (!filter.test(decoration)) continue;

            pose.pushPose();
            pose.translate(decoration.x() / 2f + 64, decoration.y() / 2f + 64, 0.02);
            pose.mulPose(Axis.ZP.rotationDegrees(decoration.rot() * 22.5f));
            pose.scale(relativeScale * 4, relativeScale * 4, 3);
            pose.translate(-0.125, 0.125, 0);
            byte b0 = decoration.getImage();
            float f1 = (float) (b0 % 16) / 16f;
            float f2 = (float) (b0 / 16) / 16f;
            float f3 = (float) (b0 % 16 + 1) / 16f;
            float f4 = (float) (b0 / 16 + 1) / 16f;
            Matrix4f matrix4f = pose.last().pose();
            VertexConsumer builder = buffer.getBuffer(AtlasesClient.MAP_DECORATIONS);
            float z = k * 0.001f;
            builder.vertex(matrix4f, -1, 1, z).color(255, 255, 255, 255).uv(f1, f2).uv2(LIGHT).endVertex();
            builder.vertex(matrix4f, 1, 1, z).color(255, 255, 255, 255).uv(f3, f2).uv2(LIGHT).endVertex();
            builder.vertex(matrix4f, 1, -1, z).color(255, 255, 255, 255).uv(f3, f4).uv2(LIGHT).endVertex();
            builder.vertex(matrix4f, -1, -1, z).color(255, 255, 255, 255).uv(f1, f4).uv2(LIGHT).endVertex();
            pose.popPose();

            ++k;
        }
    }

    private int getX() {
        return leftPos;
    }

    private int getY() {
        return topPos;
    }

    private enum ButtonDirection {
        LEFT(-BUTTON_SIZE - BUTTON_DISTANCE, CENTER, BUTTON_SIZE, BUTTON_SIZE, AtlasesClient.LEFT_BUTTON, -1, 0),
        UP(CENTER, -BUTTON_SIZE - BUTTON_DISTANCE, BUTTON_SIZE, BUTTON_SIZE, AtlasesClient.UP_BUTTON, 0, -1),
        RIGHT(SIZE + BUTTON_DISTANCE, CENTER, BUTTON_SIZE, BUTTON_SIZE, AtlasesClient.RIGHT_BUTTON, 1, 0),
        DOWN(CENTER, SIZE + BUTTON_DISTANCE, BUTTON_SIZE, BUTTON_SIZE, AtlasesClient.DOWN_BUTTON, 0, 1),
        BACK(82, -12, 16, 16, AtlasesClient.BACK_BUTTON, 0, 0),
        ZOOM_OUT(79, SIZE + BUTTON_DISTANCE - 5, 8, 9, AtlasesClient.ZOOM_OUT_BUTTON, 0, 0),
        ZOOM_IN(87, SIZE + BUTTON_DISTANCE - 5, 8, 9, AtlasesClient.ZOOM_IN_BUTTON, 0, 0);
        final int left;
        final int top;
        final int width;
        final int height;
        final WidgetSprites sprite;
        final AtlasInventory.Index vector;

        ButtonDirection(int left, int top, int width, int height, WidgetSprites sprite, int x, int y) {
            this.left = left;
            this.top = top;
            this.width = width;
            this.height = height;
            this.sprite = sprite;
            this.vector = AtlasInventory.Index.of(x, y);
        }
    }

    private interface MapGui {
        void render(GuiGraphics guiGraphics, MultiBufferSource.BufferSource bufferSource, int mouseX, int mouseY);

        default boolean mouseClicked(double mouseX, double mouseY, int button) {
            return false;
        }

        void buttonClick(ButtonDirection direction);

        boolean buttonVisible(ButtonDirection direction);

        boolean buttonEnabled(ButtonDirection direction);
    }

    private static class Extremes {
        public final AtlasInventory.Index min;
        public final AtlasInventory.Index max;

        private Extremes(int minX, int maxX, int minY, int maxY) {
            min = AtlasInventory.Index.of(minX, minY);
            max = AtlasInventory.Index.of(maxX, maxY);
        }

        public int getMaxDistance() {
            return Math.max(max.x + 1 - min.x, max.y + 1 - min.y);
        }
    }

    private class WorldMap implements MapGui {
        private final Collector<AtlasInventory.Index, ?, Extremes> extremesCollector = Collector.of(() -> new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE},
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
        private AtlasInventory.Index corner = null;
        private Extremes extremes = new Extremes(0, 0, 0, 0);
        private int maxMapDistance = 1;
        private int mapDistance = 1;
        private boolean fixedMapDistance = false;

        private boolean updateExtremes() {
            AtlasInventory inventory = menu.getInventory();
            Map<AtlasInventory.Index, AtlasInventory.MapInfo> mapInfos = inventory.getCurrentDimensionMapInfos(Minecraft.getInstance().level);
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
        public void render(GuiGraphics guiGraphics, MultiBufferSource.BufferSource bufferSource, int mouseX, int mouseY) {
            final Minecraft mc = Minecraft.getInstance();
            final Level level = mc.level;
            if (level == null || !updateExtremes()) {
                return;
            }
            float mapSize = (float) NORMAL_SIZE / mapDistance;
            float mapScale = 1f / mapDistance;
            AtlasInventory.Index currentMin = corner != null ? corner : extremes.min;
            AtlasInventory inventory = menu.getInventory();
            Map<AtlasInventory.Index, AtlasInventory.MapInfo> mapInfos = inventory.getCurrentDimensionMapInfos(Minecraft.getInstance().level);
            AtlasInventory.Index playerIndex = inventory.getIndexOf(player);
            for (Map.Entry<AtlasInventory.Index, AtlasInventory.MapInfo> mapInfo : mapInfos.entrySet()) {
                AtlasInventory.Index key = mapInfo.getKey();
                if (corner != null && (corner.x > key.x || key.x >= corner.x + mapDistance || corner.y > key.y || key.y >= corner.y + mapDistance)) {
                    continue;
                }
                int mapId = mapInfo.getValue().id;
                MapItemSavedData mapData = level.getMapData(MapItem.makeKey(mapId));
                if (mapData != null) {
                    var pose = guiGraphics.pose();
                    pose.pushPose();
                    pose.translate(mapSize * (key.x - currentMin.x), mapSize * (key.y - currentMin.y), 0.1);
                    pose.scale(mapScale, mapScale, 1);
                    mapItemRenderer.render(pose, bufferSource, mapId, mapData, false, LIGHT);
                    pose.translate(0, 0, 0.2);
                    renderDecorations(pose, bufferSource, mapData, 1.5f * mapDistance,
                        it -> it.type() != MapDecoration.Type.PLAYER_OFF_MAP && it.type() != MapDecoration.Type.PLAYER_OFF_LIMITS &&
                            (it.type() != MapDecoration.Type.PLAYER || key.equals(playerIndex)));
                    pose.popPose();
                }
            }
            drawLines(guiGraphics);
        }

        private void drawLines(GuiGraphics guiGraphics) {
            var pose = guiGraphics.pose();
            pose.pushPose();
            pose.translate(0, 0, 0.2);
            // need to revert the base scale to avoid some lines being to thin to be drawn
            pose.scale(0.5f / BASE_SCALE, 0.5f / BASE_SCALE, 1);

            for (int xLine = 1; xLine < mapDistance; ++xLine) {
                guiGraphics.vLine(xLine * 2 * SIZE / mapDistance, 0, 2 * SIZE, -1);
            }
            for (int yLine = 1; yLine < mapDistance; ++yLine) {
                guiGraphics.hLine(0, 2 * SIZE, yLine * 2 * SIZE / mapDistance, -1);
            }
            pose.popPose();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            double normX = normalizeForMapArea(LEFT + getX(), mouseX);
            double normY = normalizeForMapArea(TOP + getY(), mouseY);
            if (button == 0 && 0 <= normX && normX < 1 && 0 <= normY && normY < 1) {
                ItemStack heldItem = menu.getCarried();
                if (!heldItem.isEmpty()) {
                    AtlasesNetwork.TransferAtlas.send(slot, -1, -1, MoveMode.FROM_HAND);
                } else {
                    if (updateExtremes()) {
                        AtlasInventory.Index currentMin = corner != null ? corner : extremes.min;
                        AtlasInventory.Index index = AtlasInventory.Index.of((int) (normX * mapDistance), (int) (normY * mapDistance)).plus(currentMin);
                        Map<AtlasInventory.Index, AtlasInventory.MapInfo> mapInfos = menu.getInventory().getCurrentDimensionMapInfos(Minecraft.getInstance().level);
                        AtlasInventory.MapInfo mapInfo = mapInfos.get(index);
                        if (mapInfo != null) {
                            if (KeyboardHelper.isHoldingShift()) {
                                AtlasesNetwork.TransferAtlas.send(slot, mapInfo.x, mapInfo.z, MoveMode.TO_INVENTORY);
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
                case LEFT, UP, RIGHT, DOWN -> {
                    if (corner != null) {
                        corner = corner.plus(direction.vector.multiply(mapDistance)).clamp(extremes.min, extremes.max.plus(1 - mapDistance));
                    }
                }
                case ZOOM_IN -> {
                    fixedMapDistance = true;
                    --mapDistance;
                    if (mapDistance == 1) {
                        Map<AtlasInventory.Index, AtlasInventory.MapInfo> mapInfos = menu.getInventory().getCurrentDimensionMapInfos(Minecraft.getInstance().level);
                        changeGui(getSingleMap(mapInfos.get(corner != null ? corner : extremes.min)));
                    }
                }
                case ZOOM_OUT -> {
                    fixedMapDistance = true;
                    ++mapDistance;
                }
            }
        }

        @Override
        public boolean buttonVisible(ButtonDirection direction) {
            return switch (direction) {
                case LEFT, UP, RIGHT, DOWN -> corner != null;
                case ZOOM_IN, ZOOM_OUT -> true;
                default -> false;
            };
        }

        @Override
        public boolean buttonEnabled(ButtonDirection direction) {
            return switch (direction) {
                case LEFT -> corner != null && corner.x > extremes.min.x;
                case UP -> corner != null && corner.y > extremes.min.y;
                case RIGHT -> corner != null && corner.x + mapDistance <= extremes.max.x;
                case DOWN -> corner != null && corner.y + mapDistance <= extremes.max.y;
                case ZOOM_IN -> mapDistance > 1;
                case ZOOM_OUT -> mapDistance < maxMapDistance;
                default -> false;
            };
        }

        private double normalizeForMapArea(double base, double val) {
            return (val - base) / SIZE;
        }
    }

    private class SingleMap implements MapGui {
        private final Set<ButtonDirection> supportedDirections = EnumSet.of(ButtonDirection.LEFT, ButtonDirection.UP, ButtonDirection.RIGHT,
            ButtonDirection.DOWN, ButtonDirection.BACK);
        private AtlasInventory.MapInfo mapInfo;

        public SingleMap(AtlasInventory.MapInfo mapInfo) {
            this.mapInfo = mapInfo;
        }

        @Override
        public void render(GuiGraphics guiGraphics, MultiBufferSource.BufferSource bufferSource, int mouseX, int mouseY) {
            Minecraft mc = Minecraft.getInstance();
            var level = mc.level;

            if (level == null) {
                return;
            }

            if (mapInfo != null) {
                var mapId = mapInfo.id;
                var mapData = level.getMapData(MapItem.makeKey(mapId));

                if (mapData != null) {
                    var pose = guiGraphics.pose();
                    pose.pushPose();
                    pose.translate(0, 0, 1);
                    mapItemRenderer.render(pose, bufferSource, mapId, mapData, true, LIGHT);
                    renderDecorations(pose, bufferSource, mapData, 2f, it -> true);
                    pose.popPose();
                }
            }
        }

        @Override
        public void buttonClick(ButtonDirection direction) {
            if (direction == ButtonDirection.BACK) {
                changeGui(getWorldMap());
            } else {
                AtlasInventory inventory = menu.getInventory();
                Map<AtlasInventory.Index, AtlasInventory.MapInfo> mapInfos = inventory.getCurrentDimensionMapInfos(Minecraft.getInstance().level);
                AtlasInventory.MapInfo mapInfo1 = mapInfos.get(inventory.convertCoordsToIndex(mapInfo.x, mapInfo.z).plus(direction.vector));
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
            AtlasInventory inventory = menu.getInventory();
            Map<AtlasInventory.Index, AtlasInventory.MapInfo> mapInfos = inventory.getCurrentDimensionMapInfos(Minecraft.getInstance().level);
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
                    AtlasesNetwork.TransferAtlas.send(slot, -1, -1, MoveMode.FROM_HAND);
                } else if (mapInfo != null) {
                    if (KeyboardHelper.isHoldingShift()) {
                        AtlasesNetwork.TransferAtlas.send(slot, mapInfo.x, mapInfo.z, MoveMode.TO_INVENTORY);
                    } else {
                        AtlasesNetwork.TransferAtlas.send(slot, mapInfo.x, mapInfo.z, MoveMode.TO_HAND);
                    }
                    changeGui(getSingleMap(null));
                }
                return true;
            }
            return false;
        }
    }
}
