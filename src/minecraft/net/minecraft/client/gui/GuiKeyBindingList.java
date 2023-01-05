package net.minecraft.client.gui;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.ArrayUtils;

public class GuiKeyBindingList extends GuiListExtended
{
    private final GuiControls field_148191_k;
    private final Minecraft mc;
    private final GuiListExtended.IGuiListEntry[] listEntries;
    private int maxListLabelWidth = 0;
    private static final String __OBFID = "CL_00000732";

    public GuiKeyBindingList(GuiControls p_i45031_1_, Minecraft p_i45031_2_)
    {
        super(p_i45031_2_, p_i45031_1_.width, p_i45031_1_.height, 63, p_i45031_1_.height - 32, 20);
        this.field_148191_k = p_i45031_1_;
        this.mc = p_i45031_2_;
        KeyBinding[] var3 = (KeyBinding[])ArrayUtils.clone(p_i45031_2_.gameSettings.keyBindings);
        this.listEntries = new GuiListExtended.IGuiListEntry[var3.length + KeyBinding.getKeybinds().size()];
        Arrays.sort(var3);
        int var4 = 0;
        String var5 = null;
        KeyBinding[] var6 = var3;
        int var7 = var3.length;

        for (int var8 = 0; var8 < var7; ++var8)
        {
            KeyBinding var9 = var6[var8];
            String var10 = var9.getKeyCategory();

            if (!var10.equals(var5))
            {
                var5 = var10;
                this.listEntries[var4++] = new GuiKeyBindingList.CategoryEntry(var10);
            }

            int var11 = p_i45031_2_.fontRendererObj.getStringWidth(I18n.format(var9.getKeyDescription(), new Object[0]));

            if (var11 > this.maxListLabelWidth)
            {
                this.maxListLabelWidth = var11;
            }

            this.listEntries[var4++] = new GuiKeyBindingList.KeyEntry(var9, null);
        }
    }

    protected int getSize()
    {
        return this.listEntries.length;
    }

    public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_)
    {
        return this.listEntries[p_148180_1_];
    }

    protected int getScrollBarX()
    {
        return super.getScrollBarX() + 15;
    }

    public int getListWidth()
    {
        return super.getListWidth() + 32;
    }

    public class CategoryEntry implements GuiListExtended.IGuiListEntry
    {
        private final String labelText;
        private final int labelWidth;
        private static final String __OBFID = "CL_00000734";

        public CategoryEntry(String p_i45028_2_)
        {
            this.labelText = I18n.format(p_i45028_2_, new Object[0]);
            this.labelWidth = GuiKeyBindingList.this.mc.fontRendererObj.getStringWidth(this.labelText);
        }

        public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
        {
            GuiKeyBindingList.this.mc.fontRendererObj.drawString(this.labelText, GuiKeyBindingList.this.mc.currentScreen.width / 2 - this.labelWidth / 2, p_148279_3_ + p_148279_5_ - GuiKeyBindingList.this.mc.fontRendererObj.FONT_HEIGHT - 1, 16777215);
        }

        public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
        {
            return false;
        }

        public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {}
    }

    public class KeyEntry implements GuiListExtended.IGuiListEntry
    {
        private final KeyBinding field_148282_b;
        private final String field_148283_c;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnReset;
        private static final String __OBFID = "CL_00000735";

        private KeyEntry(KeyBinding p_i45029_2_)
        {
            this.field_148282_b = p_i45029_2_;
            this.field_148283_c = I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]);
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 18, I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]));
            this.btnReset = new GuiButton(0, 0, 0, 50, 18, I18n.format("controls.reset", new Object[0]));
        }

        public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
        {
            boolean var10 = GuiKeyBindingList.this.field_148191_k.buttonId == this.field_148282_b;
            GuiKeyBindingList.this.mc.fontRendererObj.drawString(this.field_148283_c, p_148279_2_ + 90 - GuiKeyBindingList.this.maxListLabelWidth, p_148279_3_ + p_148279_5_ / 2 - GuiKeyBindingList.this.mc.fontRendererObj.FONT_HEIGHT / 2, 16777215);
            this.btnReset.xPosition = p_148279_2_ + 190;
            this.btnReset.yPosition = p_148279_3_;
            this.btnReset.enabled = this.field_148282_b.getKeyCode() != this.field_148282_b.getKeyCodeDefault();
            this.btnReset.drawButton(GuiKeyBindingList.this.mc, p_148279_7_, p_148279_8_);
            this.btnChangeKeyBinding.xPosition = p_148279_2_ + 105;
            this.btnChangeKeyBinding.yPosition = p_148279_3_;
            this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.field_148282_b.getKeyCode());
            boolean var11 = false;

            if (this.field_148282_b.getKeyCode() != 0)
            {
                KeyBinding[] var12 = GuiKeyBindingList.this.mc.gameSettings.keyBindings;
                int var13 = var12.length;

                for (int var14 = 0; var14 < var13; ++var14)
                {
                    KeyBinding var15 = var12[var14];

                    if (var15 != this.field_148282_b && var15.getKeyCode() == this.field_148282_b.getKeyCode())
                    {
                        var11 = true;
                        break;
                    }
                }
            }

            if (var10)
            {
                this.btnChangeKeyBinding.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + this.btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <";
            }
            else if (var11)
            {
                this.btnChangeKeyBinding.displayString = EnumChatFormatting.RED + this.btnChangeKeyBinding.displayString;
            }

            this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, p_148279_7_, p_148279_8_);
        }

        public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
        {
            if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_))
            {
                GuiKeyBindingList.this.field_148191_k.buttonId = this.field_148282_b;
                return true;
            }
            else if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_))
            {
                GuiKeyBindingList.this.mc.gameSettings.setOptionKeyBinding(this.field_148282_b, this.field_148282_b.getKeyCodeDefault());
                KeyBinding.resetKeyBindingArrayAndHash();
                return true;
            }
            else
            {
                return false;
            }
        }

        public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_)
        {
            this.btnChangeKeyBinding.mouseReleased(p_148277_2_, p_148277_3_);
            this.btnReset.mouseReleased(p_148277_2_, p_148277_3_);
        }

        KeyEntry(KeyBinding p_i45030_2_, Object p_i45030_3_)
        {
            this(p_i45030_2_);
        }
    }
}
