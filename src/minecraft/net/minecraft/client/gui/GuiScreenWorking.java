package net.minecraft.client.gui;

import net.minecraft.util.IProgressUpdate;

public class GuiScreenWorking extends GuiScreen implements IProgressUpdate
{
    private String field_146591_a = "";
    private String field_146589_f = "";
    private int field_146590_g;
    private boolean field_146592_h;
    private static final String __OBFID = "CL_00000707";

    public void displaySavingString(String p_73720_1_)
    {
        this.resetProgressAndMessage(p_73720_1_);
    }

    public void resetProgressAndMessage(String p_73721_1_)
    {
        this.field_146591_a = p_73721_1_;
        this.displayLoadingString("Working...");
    }

    public void displayLoadingString(String p_73719_1_)
    {
        this.field_146589_f = p_73719_1_;
        this.setLoadingProgress(0);
    }

    public void setLoadingProgress(int p_73718_1_)
    {
        this.field_146590_g = p_73718_1_;
    }

    public void setDoneWorking()
    {
        this.field_146592_h = true;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (this.field_146592_h)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else
        {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, this.field_146591_a, this.width / 2, 70, 16777215);
            this.drawCenteredString(this.fontRendererObj, this.field_146589_f + " " + this.field_146590_g + "%", this.width / 2, 90, 16777215);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
