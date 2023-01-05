package net.minecraft.client.gui;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public class GuiLabel extends Gui
{
    protected int field_146167_a;
    protected int field_146161_f;
    public int field_146162_g;
    public int field_146174_h;
    private ArrayList field_146173_k;
    private boolean centered;
    public boolean visible;
    private boolean labelBgEnabled;
    private int field_146168_n;
    private int field_146169_o;
    private int field_146166_p;
    private int field_146165_q;
    private FontRenderer fontRenderer;
    private int field_146163_s;
    private static final String __OBFID = "CL_00000671";

    public void drawLabel(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawLabelBackground(mc, mouseX, mouseY);
            int var4 = this.field_146174_h + this.field_146161_f / 2 + this.field_146163_s / 2;
            int var5 = var4 - this.field_146173_k.size() * 10 / 2;

            for (int var6 = 0; var6 < this.field_146173_k.size(); ++var6)
            {
                if (this.centered)
                {
                    this.drawCenteredString(this.fontRenderer, (String)this.field_146173_k.get(var6), this.field_146162_g + this.field_146167_a / 2, var5 + var6 * 10, this.field_146168_n);
                }
                else
                {
                    this.drawString(this.fontRenderer, (String)this.field_146173_k.get(var6), this.field_146162_g, var5 + var6 * 10, this.field_146168_n);
                }
            }
        }
    }

    protected void drawLabelBackground(Minecraft p_146160_1_, int p_146160_2_, int p_146160_3_)
    {
        if (this.labelBgEnabled)
        {
            int var4 = this.field_146167_a + this.field_146163_s * 2;
            int var5 = this.field_146161_f + this.field_146163_s * 2;
            int var6 = this.field_146162_g - this.field_146163_s;
            int var7 = this.field_146174_h - this.field_146163_s;
            drawRect(var6, var7, var6 + var4, var7 + var5, this.field_146169_o);
            this.drawHorizontalLine(var6, var6 + var4, var7, this.field_146166_p);
            this.drawHorizontalLine(var6, var6 + var4, var7 + var5, this.field_146165_q);
            this.drawVerticalLine(var6, var7, var7 + var5, this.field_146166_p);
            this.drawVerticalLine(var6 + var4, var7, var7 + var5, this.field_146165_q);
        }
    }
}
