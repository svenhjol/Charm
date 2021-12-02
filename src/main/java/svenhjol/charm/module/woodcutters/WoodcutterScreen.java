package svenhjol.charm.module.woodcutters;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

@Environment(EnvType.CLIENT)
public class WoodcutterScreen extends AbstractContainerScreen<WoodcutterMenu> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/stonecutter.png");
   private float scrollAmount;
   private boolean mouseClicked;
   private int scrollOffset;
   private boolean canCraft;

   public WoodcutterScreen(WoodcutterMenu handler, Inventory inventory, Component title) {
      super(handler, inventory, title);
      handler.setContentsChangedListener(this::onInventoryChange);
      --this.titleLabelY;
   }

   public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
      super.render(poseStack, mouseX, mouseY, delta);
      this.renderTooltip(poseStack, mouseX, mouseY);
   }

   protected void renderBg(PoseStack poseStack, float delta, int mouseX, int mouseY) {
      this.renderBackground(poseStack);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, TEXTURE);
      int i = this.getX();
      int j = this.getY();
      this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
      int k = (int)(41.0F * this.scrollAmount);
      this.blit(poseStack, i + 119, j + 15 + k, 176 + (this.shouldScroll() ? 0 : 12), 0, 12, 15);
      int l = this.getX() + 52;
      int m = this.getY() + 14;
      int n = this.scrollOffset + 12;
      this.renderRecipeBackground(poseStack, mouseX, mouseY, l, m, n);
      this.renderRecipeIcons(l, m, n);
   }

   protected void renderTooltip(PoseStack poseStack, int x, int y) {
      super.renderTooltip(poseStack, x, y);
      if (this.canCraft) {
         int i = this.getX() + 52;
         int j = this.getY() + 14;
         int k = this.scrollOffset + 12;
         List<WoodcuttingRecipe> list = (this.menu).getAvailableRecipes();

         for(int l = this.scrollOffset; l < k && l < (this.menu).getAvailableRecipeCount(); ++l) {
            int m = l - this.scrollOffset;
            int n = i + m % 4 * 16;
            int o = j + m / 4 * 18 + 2;
            if (x >= n && x < n + 16 && y >= o && y < o + 18) {
               this.renderTooltip(poseStack, list.get(l).getResultItem(), x, y);
            }
         }
      }
   }

   private void renderRecipeBackground(PoseStack matrixStack, int i, int j, int k, int l, int m) {
      for(int n = this.scrollOffset; n < m && n < (this.menu).getAvailableRecipeCount(); ++n) {
         int o = n - this.scrollOffset;
         int p = k + o % 4 * 16;
         int q = o / 4;
         int r = l + q * 18 + 2;
         int s = this.imageHeight;
         if (n == (this.menu).getSelectedRecipe()) {
            s += 18;
         } else if (i >= p && j >= r && i < p + 16 && j < r + 18) {
            s += 36;
         }

         this.blit(matrixStack, p, r - 1, 0, s, 16, 18);
      }

   }

   private void renderRecipeIcons(int x, int y, int scrollOffset) {
      List<WoodcuttingRecipe> list = (this.menu).getAvailableRecipes();

      for(int i = this.scrollOffset; i < scrollOffset && i < (this.menu).getAvailableRecipeCount(); ++i) {
         int j = i - this.scrollOffset;
         int k = x + j % 4 * 16;
         int l = j / 4;
         int m = y + l * 18 + 2;
         this.minecraft.getItemRenderer().renderAndDecorateItem((list.get(i)).getResultItem(), k, m);
      }

   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.mouseClicked = false;
      if (this.canCraft) {
         int i = this.getX() + 52;
         int j = this.getY() + 14;
         int k = this.scrollOffset + 12;

         for(int l = this.scrollOffset; l < k; ++l) {
            int m = l - this.scrollOffset;
            double d = mouseX - (double)(i + m % 4 * 16);
            double e = mouseY - (double)(j + m / 4 * 18);
            if (d >= 0.0D && e >= 0.0D && d < 16.0D && e < 18.0D && (this.menu).clickMenuButton(this.minecraft.player, l)) {
               Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
               this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
               return true;
            }
         }

         i = this.getX() + 119;
         j = this.getY() + 9;
         if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
            this.mouseClicked = true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.mouseClicked && this.shouldScroll()) {
         int i = this.getY() + 14;
         int j = i + 54;
         this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
         this.scrollAmount = Mth.clamp(this.scrollAmount, 0.0F, 1.0F);
         this.scrollOffset = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5D) * 4;
         return true;
      } else {
         return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
      if (this.shouldScroll()) {
         int i = this.getMaxScroll();
         this.scrollAmount = (float)((double)this.scrollAmount - amount / (double)i);
         this.scrollAmount = Mth.clamp(this.scrollAmount, 0.0F, 1.0F);
         this.scrollOffset = (int)((double)(this.scrollAmount * (float)i) + 0.5D) * 4;
      }

      return true;
   }

   private int getX() {
      return leftPos;
   }

   private int getY() {
      return topPos;
   }

   private boolean shouldScroll() {
      return this.canCraft && (this.menu).getAvailableRecipeCount() > 12;
   }

   protected int getMaxScroll() {
      return ((this.menu).getAvailableRecipeCount() + 4 - 1) / 4 - 3;
   }

   private void onInventoryChange() {
      this.canCraft = (this.menu).canCraft();
      if (!this.canCraft) {
         this.scrollAmount = 0.0F;
         this.scrollOffset = 0;
      }
   }
}
