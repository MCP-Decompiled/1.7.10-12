package net.minecraft.client.gui;

import java.util.Iterator;
import net.minecraft.client.resources.I18n;

public class GuiYesNo extends GuiScreen
{
    protected GuiYesNoCallback parentScreen;
    protected String messageLine1;
    private String messageLine2;
    protected String confirmButtonText;
    protected String cancelButtonText;
    protected int parentButtonClickedId;
    private int ticksUntilEnable;
    private static final String __OBFID = "CL_00000684";

    public GuiYesNo(GuiYesNoCallback p_i1082_1_, String p_i1082_2_, String p_i1082_3_, int p_i1082_4_)
    {
        this.parentScreen = p_i1082_1_;
        this.messageLine1 = p_i1082_2_;
        this.messageLine2 = p_i1082_3_;
        this.parentButtonClickedId = p_i1082_4_;
        this.confirmButtonText = I18n.format("gui.yes", new Object[0]);
        this.cancelButtonText = I18n.format("gui.no", new Object[0]);
    }

    public GuiYesNo(GuiYesNoCallback p_i1083_1_, String p_i1083_2_, String p_i1083_3_, String p_i1083_4_, String p_i1083_5_, int p_i1083_6_)
    {
        this.parentScreen = p_i1083_1_;
        this.messageLine1 = p_i1083_2_;
        this.messageLine2 = p_i1083_3_;
        this.confirmButtonText = p_i1083_4_;
        this.cancelButtonText = p_i1083_5_;
        this.parentButtonClickedId = p_i1083_6_;
    }

    public void initGui()
    {
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 6 + 96, this.confirmButtonText));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.cancelButtonText));
    }

    protected void actionPerformed(GuiButton button)
    {
        this.parentScreen.confirmClicked(button.id == 0, this.parentButtonClickedId);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.messageLine1, this.width / 2, 70, 16777215);
        this.drawCenteredString(this.fontRendererObj, this.messageLine2, this.width / 2, 90, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void setButtonDelay(int p_146350_1_)
    {
        this.ticksUntilEnable = p_146350_1_;
        GuiButton var3;

        for (Iterator var2 = this.buttonList.iterator(); var2.hasNext(); var3.enabled = false)
        {
            var3 = (GuiButton)var2.next();
        }
    }

    public void updateScreen()
    {
        super.updateScreen();
        GuiButton var2;

        if (--this.ticksUntilEnable == 0)
        {
            for (Iterator var1 = this.buttonList.iterator(); var1.hasNext(); var2.enabled = true)
            {
                var2 = (GuiButton)var1.next();
            }
        }
    }
}
