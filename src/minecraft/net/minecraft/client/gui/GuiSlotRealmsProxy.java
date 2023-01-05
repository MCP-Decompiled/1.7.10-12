package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.realms.RealmsScrolledSelectionList;

public class GuiSlotRealmsProxy extends GuiSlot
{
    private final RealmsScrolledSelectionList selectionList;
    private static final String __OBFID = "CL_00001846";

    public GuiSlotRealmsProxy(RealmsScrolledSelectionList selectionListIn, int p_i1085_2_, int p_i1085_3_, int p_i1085_4_, int p_i1085_5_, int p_i1085_6_)
    {
        super(Minecraft.getMinecraft(), p_i1085_2_, p_i1085_3_, p_i1085_4_, p_i1085_5_, p_i1085_6_);
        this.selectionList = selectionListIn;
    }

    protected int getSize()
    {
        return this.selectionList.getItemCount();
    }

    protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_)
    {
        this.selectionList.selectItem(p_148144_1_, p_148144_2_, p_148144_3_, p_148144_4_);
    }

    protected boolean isSelected(int p_148131_1_)
    {
        return this.selectionList.isSelectedItem(p_148131_1_);
    }

    protected void drawBackground()
    {
        this.selectionList.renderBackground();
    }

    protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_)
    {
        this.selectionList.renderItem(p_148126_1_, p_148126_2_, p_148126_3_, p_148126_4_, p_148126_6_, p_148126_7_);
    }

    public int func_154338_k()
    {
        return super.width;
    }

    public int func_154339_l()
    {
        return super.mouseY;
    }

    public int func_154337_m()
    {
        return super.mouseX;
    }

    protected int getContentHeight()
    {
        return this.selectionList.getMaxPosition();
    }

    protected int getScrollBarX()
    {
        return this.selectionList.getScrollbarPosition();
    }
}
