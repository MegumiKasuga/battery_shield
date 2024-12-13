package cc.xypp.battery_shield.hud;

import cc.xypp.battery_shield.Config;
import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.data.ShieldType;
import cc.xypp.battery_shield.helper.AssetsManager;
import cc.xypp.battery_shield.items.Register;
import cc.xypp.battery_shield.utils.RenderUtils;
import cc.xypp.battery_shield.utils.ShieldUtil;
import cc.xypp.battery_shield.utils.TypeBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import static cc.xypp.battery_shield.utils.RenderUtils.blendedHeadTextures;
import static cc.xypp.battery_shield.utils.RenderUtils.renderHead;

public class PlayerShieldOverlay implements IGuiOverlay {
    PlayerShieldOverlay() {

    }

    protected void renderItem(GuiGraphics guiGraphics, float scale, int x, int y) {
        ItemStack item = new ItemStack(Blocks.PLAYER_HEAD);
        if (Minecraft.getInstance().player != null) {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putUUID("SkullOwner", Minecraft.getInstance().player.getUUID());
            item.setTag(compoundTag);
        }
        guiGraphics.renderItem(item, x, y);
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
        if (!Config.display_hud) return;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        ILivingEntityA iliving = ((ILivingEntityA) (player));
        float max = iliving.battery_shield$getMaxShield();
        float shield = iliving.battery_shield$getShield();

        ShieldType type = iliving.battery_shield$getShieldType();
        if (type == ShieldType.RAW) return;
        TypeBinding binding = type.getBinding();
        if (binding == null) return;
        RenderUtils.renderBar(guiGraphics, 37, height - 25, 96, 6, type, AssetsManager.SHIELD_BORDER, binding.getImage(), shield, true);
        RenderUtils.renderHealth(guiGraphics, 40, height - 20, 96, 6, player.getHealth(), player.getMaxHealth(), true);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.8f, 0.8f, 0.8f);
        guiGraphics.drawString(Minecraft.getInstance().font, player.getName(), (int) (45 / 0.8), (int) ((height - 32) / 0.8), 0xffffffff);
        guiGraphics.pose().popPose();
        guiGraphics.pose().pushPose();
        if (Config.use_2d_head) {
            guiGraphics.pose().scale(3.0f, 3.0f, 3.0f);
            renderHead(guiGraphics,
                    player.getSkinTextureLocation(),
                    (int) (10 / 3.0),
                    (int) ((height - 33) / 3.0));
        } else {
            guiGraphics.pose().scale(1.7f, 1.7f, 1.7f);
            this.renderItem(guiGraphics, 5f, (int) (15 / 1.7), (int) ((height - 33) / 1.7));
        }
        guiGraphics.pose().popPose();
    }


}
