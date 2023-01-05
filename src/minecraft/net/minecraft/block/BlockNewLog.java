package net.minecraft.block;

import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockNewLog extends BlockLog
{
    public static final String[] field_150169_M = new String[] {"acacia", "big_oak"};
    private static final String __OBFID = "CL_00000277";

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_150167_a = new IIcon[field_150169_M.length];
        this.field_150166_b = new IIcon[field_150169_M.length];

        for (int var2 = 0; var2 < this.field_150167_a.length; ++var2)
        {
            this.field_150167_a[var2] = reg.registerIcon(this.getTextureName() + "_" + field_150169_M[var2]);
            this.field_150166_b[var2] = reg.registerIcon(this.getTextureName() + "_" + field_150169_M[var2] + "_top");
        }
    }
}
