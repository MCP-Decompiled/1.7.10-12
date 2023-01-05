package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiSnooper extends GuiScreen
{
    private final GuiScreen field_146608_a;
    private final GameSettings game_settings_2;
    private final java.util.List field_146604_g = new ArrayList();
    private final java.util.List field_146609_h = new ArrayList();
    private String field_146610_i;
    private String[] field_146607_r;
    private GuiSnooper.List field_146606_s;
    private GuiButton field_146605_t;
    private static final String __OBFID = "CL_00000714";

    public GuiSnooper(GuiScreen p_i1061_1_, GameSettings p_i1061_2_)
    {
        this.field_146608_a = p_i1061_1_;
        this.game_settings_2 = p_i1061_2_;
    }

    public void initGui()
    {
        this.field_146610_i = I18n.format("options.snooper.title", new Object[0]);
        String var1 = I18n.format("options.snooper.desc", new Object[0]);
        ArrayList var2 = new ArrayList();
        Iterator var3 = this.fontRendererObj.listFormattedStringToWidth(var1, this.width - 30).iterator();

        while (var3.hasNext())
        {
            String var4 = (String)var3.next();
            var2.add(var4);
        }

        this.field_146607_r = (String[])var2.toArray(new String[0]);
        this.field_146604_g.clear();
        this.field_146609_h.clear();
        this.buttonList.add(this.field_146605_t = new GuiButton(1, this.width / 2 - 152, this.height - 30, 150, 20, this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED)));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height - 30, 150, 20, I18n.format("gui.done", new Object[0])));
        boolean var6 = this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().getPlayerUsageSnooper() != null;
        Iterator var7 = (new TreeMap(this.mc.getPlayerUsageSnooper().getCurrentStats())).entrySet().iterator();
        Entry var5;

        while (var7.hasNext())
        {
            var5 = (Entry)var7.next();
            this.field_146604_g.add((var6 ? "C " : "") + (String)var5.getKey());
            this.field_146609_h.add(this.fontRendererObj.trimStringToWidth((String)var5.getValue(), this.width - 220));
        }

        if (var6)
        {
            var7 = (new TreeMap(this.mc.getIntegratedServer().getPlayerUsageSnooper().getCurrentStats())).entrySet().iterator();

            while (var7.hasNext())
            {
                var5 = (Entry)var7.next();
                this.field_146604_g.add("S " + (String)var5.getKey());
                this.field_146609_h.add(this.fontRendererObj.trimStringToWidth((String)var5.getValue(), this.width - 220));
            }
        }

        this.field_146606_s = new GuiSnooper.List();
    }

    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 2)
            {
                this.game_settings_2.saveOptions();
                this.game_settings_2.saveOptions();
                this.mc.displayGuiScreen(this.field_146608_a);
            }

            if (button.id == 1)
            {
                this.game_settings_2.setOptionValue(GameSettings.Options.SNOOPER_ENABLED, 1);
                this.field_146605_t.displayString = this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.field_146606_s.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.field_146610_i, this.width / 2, 8, 16777215);
        int var4 = 22;
        String[] var5 = this.field_146607_r;
        int var6 = var5.length;

        for (int var7 = 0; var7 < var6; ++var7)
        {
            String var8 = var5[var7];
            this.drawCenteredString(this.fontRendererObj, var8, this.width / 2, var4, 8421504);
            var4 += this.fontRendererObj.FONT_HEIGHT;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    class List extends GuiSlot
    {
        private static final String __OBFID = "CL_00000715";

        public List()
        {
            super(GuiSnooper.this.mc, GuiSnooper.this.width, GuiSnooper.this.height, 80, GuiSnooper.this.height - 40, GuiSnooper.this.fontRendererObj.FONT_HEIGHT + 1);
        }

        protected int getSize()
        {
            return GuiSnooper.this.field_146604_g.size();
        }

        protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_) {}

        protected boolean isSelected(int p_148131_1_)
        {
            return false;
        }

        protected void drawBackground() {}

        protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_)
        {
            GuiSnooper.this.fontRendererObj.drawString((String)GuiSnooper.this.field_146604_g.get(p_148126_1_), 10, p_148126_3_, 16777215);
            GuiSnooper.this.fontRendererObj.drawString((String)GuiSnooper.this.field_146609_h.get(p_148126_1_), 230, p_148126_3_, 16777215);
        }

        protected int getScrollBarX()
        {
            return this.width - 10;
        }
    }
}
