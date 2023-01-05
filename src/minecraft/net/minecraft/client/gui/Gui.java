package net.minecraft.client.gui;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Gui
{
    public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
    public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    protected float zLevel;
    private static final String __OBFID = "CL_00000662";

    protected void drawHorizontalLine(int startX, int endX, int y, int color)
    {
        if (endX < startX)
        {
            int var5 = startX;
            startX = endX;
            endX = var5;
        }

        drawRect(startX, y, endX + 1, y + 1, color);
    }

    protected void drawVerticalLine(int x, int startY, int endY, int color)
    {
        if (endY < startY)
        {
            int var5 = startY;
            startY = endY;
            endY = var5;
        }

        drawRect(x, startY + 1, x + 1, endY, color);
    }

    public static void drawRect(int left, int top, int right, int bottom, int color)
    {
        int var5;

        if (left < right)
        {
            var5 = left;
            left = right;
            right = var5;
        }

        if (top < bottom)
        {
            var5 = top;
            top = bottom;
            bottom = var5;
        }

        float var10 = (float)(color >> 24 & 255) / 255.0F;
        float var6 = (float)(color >> 16 & 255) / 255.0F;
        float var7 = (float)(color >> 8 & 255) / 255.0F;
        float var8 = (float)(color & 255) / 255.0F;
        Tessellator var9 = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(var6, var7, var8, var10);
        var9.startDrawingQuads();
        var9.addVertex((double)left, (double)bottom, 0.0D);
        var9.addVertex((double)right, (double)bottom, 0.0D);
        var9.addVertex((double)right, (double)top, 0.0D);
        var9.addVertex((double)left, (double)top, 0.0D);
        var9.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float var7 = (float)(startColor >> 24 & 255) / 255.0F;
        float var8 = (float)(startColor >> 16 & 255) / 255.0F;
        float var9 = (float)(startColor >> 8 & 255) / 255.0F;
        float var10 = (float)(startColor & 255) / 255.0F;
        float var11 = (float)(endColor >> 24 & 255) / 255.0F;
        float var12 = (float)(endColor >> 16 & 255) / 255.0F;
        float var13 = (float)(endColor >> 8 & 255) / 255.0F;
        float var14 = (float)(endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex((double)right, (double)top, (double)this.zLevel);
        var15.addVertex((double)left, (double)top, (double)this.zLevel);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex((double)left, (double)bottom, (double)this.zLevel);
        var15.addVertex((double)right, (double)bottom, (double)this.zLevel);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, x - fontRendererIn.getStringWidth(text) / 2, y, color);
    }

    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, x, y, color);
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)((float)(textureX + 0) * var7), (double)((float)(textureY + height) * var8));
        var9.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)((float)(textureX + width) * var7), (double)((float)(textureY + height) * var8));
        var9.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)((float)(textureX + width) * var7), (double)((float)(textureY + 0) * var8));
        var9.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(textureX + 0) * var7), (double)((float)(textureY + 0) * var8));
        var9.draw();
    }

    public void drawTexturedModelRectFromIcon(int x, int y, IIcon icon, int width, int height)
    {
        Tessellator var6 = Tessellator.instance;
        var6.startDrawingQuads();
        var6.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMaxV());
        var6.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMaxV());
        var6.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMinV());
        var6.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMinV());
        var6.draw();
    }

    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight)
    {
        float var8 = 1.0F / textureWidth;
        float var9 = 1.0F / textureHeight;
        Tessellator var10 = Tessellator.instance;
        var10.startDrawingQuads();
        var10.addVertexWithUV((double)x, (double)(y + height), 0.0D, (double)(u * var8), (double)((v + (float)height) * var9));
        var10.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, (double)((u + (float)width) * var8), (double)((v + (float)height) * var9));
        var10.addVertexWithUV((double)(x + width), (double)y, 0.0D, (double)((u + (float)width) * var8), (double)(v * var9));
        var10.addVertexWithUV((double)x, (double)y, 0.0D, (double)(u * var8), (double)(v * var9));
        var10.draw();
    }

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
    {
        float var10 = 1.0F / tileWidth;
        float var11 = 1.0F / tileHeight;
        Tessellator var12 = Tessellator.instance;
        var12.startDrawingQuads();
        var12.addVertexWithUV((double)x, (double)(y + height), 0.0D, (double)(u * var10), (double)((v + (float)vHeight) * var11));
        var12.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, (double)((u + (float)uWidth) * var10), (double)((v + (float)vHeight) * var11));
        var12.addVertexWithUV((double)(x + width), (double)y, 0.0D, (double)((u + (float)uWidth) * var10), (double)(v * var11));
        var12.addVertexWithUV((double)x, (double)y, 0.0D, (double)(u * var10), (double)(v * var11));
        var12.draw();
    }
}
