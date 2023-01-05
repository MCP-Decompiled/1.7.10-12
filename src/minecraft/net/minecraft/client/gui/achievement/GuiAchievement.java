package net.minecraft.client.gui.achievement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiAchievement extends Gui
{
    private static final ResourceLocation achievementBg = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    private Minecraft mc;
    private int width;
    private int height;
    private String achievementTitle;
    private String achievementDescription;
    private Achievement theAchievement;
    private long notificationTime;
    private RenderItem renderItem;
    private boolean permanentNotification;
    private static final String __OBFID = "CL_00000721";

    public GuiAchievement(Minecraft mc)
    {
        this.mc = mc;
        this.renderItem = new RenderItem();
    }

    public void displayAchievement(Achievement p_146256_1_)
    {
        this.achievementTitle = I18n.format("achievement.get", new Object[0]);
        this.achievementDescription = p_146256_1_.getStatName().getUnformattedText();
        this.notificationTime = Minecraft.getSystemTime();
        this.theAchievement = p_146256_1_;
        this.permanentNotification = false;
    }

    public void displayUnformattedAchievement(Achievement p_146255_1_)
    {
        this.achievementTitle = p_146255_1_.getStatName().getUnformattedText();
        this.achievementDescription = p_146255_1_.getDescription();
        this.notificationTime = Minecraft.getSystemTime() + 2500L;
        this.theAchievement = p_146255_1_;
        this.permanentNotification = true;
    }

    private void updateAchievementWindowScale()
    {
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.width = this.mc.displayWidth;
        this.height = this.mc.displayHeight;
        ScaledResolution var1 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.width = var1.getScaledWidth();
        this.height = var1.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double)this.width, (double)this.height, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    public void updateAchievementWindow()
    {
        if (this.theAchievement != null && this.notificationTime != 0L && Minecraft.getMinecraft().thePlayer != null)
        {
            double var1 = (double)(Minecraft.getSystemTime() - this.notificationTime) / 3000.0D;

            if (!this.permanentNotification)
            {
                if (var1 < 0.0D || var1 > 1.0D)
                {
                    this.notificationTime = 0L;
                    return;
                }
            }
            else if (var1 > 0.5D)
            {
                var1 = 0.5D;
            }

            this.updateAchievementWindowScale();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            double var3 = var1 * 2.0D;

            if (var3 > 1.0D)
            {
                var3 = 2.0D - var3;
            }

            var3 *= 4.0D;
            var3 = 1.0D - var3;

            if (var3 < 0.0D)
            {
                var3 = 0.0D;
            }

            var3 *= var3;
            var3 *= var3;
            int var5 = this.width - 160;
            int var6 = 0 - (int)(var3 * 36.0D);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            this.mc.getTextureManager().bindTexture(achievementBg);
            GL11.glDisable(GL11.GL_LIGHTING);
            this.drawTexturedModalRect(var5, var6, 96, 202, 160, 32);

            if (this.permanentNotification)
            {
                this.mc.fontRendererObj.drawSplitString(this.achievementDescription, var5 + 30, var6 + 7, 120, -1);
            }
            else
            {
                this.mc.fontRendererObj.drawString(this.achievementTitle, var5 + 30, var6 + 7, -256);
                this.mc.fontRendererObj.drawString(this.achievementDescription, var5 + 30, var6 + 18, -1);
            }

            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            this.renderItem.renderItemAndEffectIntoGUI(this.mc.fontRendererObj, this.mc.getTextureManager(), this.theAchievement.theItemStack, var5 + 8, var6 + 8);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }

    public void clearAchievements()
    {
        this.theAchievement = null;
        this.notificationTime = 0L;
    }
}
