package cc.xypp.battery_shield.helper;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class AssetsManager {
    public static HashMap<ResourceLocation, ImageAssets> IMAGES = new HashMap<>();
    public static class ImageAssets {
        public final ResourceLocation resourceLocation;
        public final int width;
        public final int height;

        public ImageAssets(String namespace, String path, int width, int height) {
            this.resourceLocation = new ResourceLocation(namespace, path);
            this.width = width;
            this.height = height;
        }

        public void blit(GuiGraphics pGuiGraphics, int x, int y) {
            this.blit(pGuiGraphics, x, y, 0, 0, width, height);
        }

        public void blit(GuiGraphics pGuiGraphics, int x, int y, int drawWidth, int drawHeight) {
            this.blit(pGuiGraphics, x, y, 0, 0, drawWidth, drawHeight);
        }

        public void blit(GuiGraphics pGuiGraphics, int x, int y, int drawX, int drawY, int drawWidth, int drawHeight) {
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.disableDepthTest();
            pGuiGraphics.blit(resourceLocation, x, y, drawX, drawY, drawWidth, drawHeight, width, height);
            pGuiGraphics.flush();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }

        public void blit(GuiGraphics pGuiGraphics, int x, int y, int order, int drawX, int drawY, int drawWidth, int drawHeight) {
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.disableDepthTest();
            pGuiGraphics.blit(resourceLocation, x, y, order, drawX, drawY, drawWidth, drawHeight, width, height);
            pGuiGraphics.flush();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }
    }

    public static ImageAssets register(ResourceLocation location, ResourceLocation assetsPath, int width, int height) {
        ImageAssets assets = new ImageAssets(assetsPath.getNamespace(), assetsPath.getPath(), width, height);
        IMAGES.put(location, assets);
        return assets;
    }

    private static ImageAssets register(ResourceLocation location, int width, int height) {
        ImageAssets assets = new ImageAssets(location.getNamespace(),"textures/gui/" + location.getPath() + ".png", width, height);
        IMAGES.put(location, assets);
        return assets;
    }

    private static ResourceLocation byPath(String path) {
        return new ResourceLocation("battery_shield", path);
    }

    private static ResourceLocation byPngPath(String path) {
        return byPath("textures/" + path + ".png");
    }

    public static ImageAssets SHIELD_BORDER = register(byPath("battery_empty"), 21, 6);
    public static ImageAssets SHIELD_WHITE = register(byPath("battery_fill_w"), 21, 6);
    public static ImageAssets SHIELD_BLUE = register(byPath("battery_fill_b"), 21, 6);
    public static ImageAssets SHIELD_PERPLE = register(byPath("battery_fill_p"), 21, 6);
    public static ImageAssets SHIELD_RED = register(byPath("battery_fill_r"), 21, 6);

    //----------------------------------------------------------------------------------------------
    public static ImageAssets HEALTH_FILL = register(byPath("blood_fill"), 96, 6);
    public static ImageAssets HEALTH_EMPTY = register(byPath("blood_empty"), 96, 6);

    //----------------------------------------------------------------------------------------------
    public static ImageAssets SHIELD_ICON_RED = register(byPath("shield_r"), 16, 16);
    public static ImageAssets SHIELD_ICON_PERPLE = register(byPath("shield_p"), 16, 16);
    public static ImageAssets SHIELD_ICON_BLUE = register(byPath("shield_b"), 16, 16);
    public static ImageAssets SHIELD_ICON_WHITE = register(byPath("shield_w"), 16, 16);

}
