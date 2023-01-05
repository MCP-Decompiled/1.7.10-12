package net.minecraft.block;

import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockNewLeaf extends BlockLeaves
{
    public static final String[][] field_150132_N = new String[][] {{"leaves_acacia", "leaves_big_oak"}, {"leaves_acacia_opaque", "leaves_big_oak_opaque"}};
    public static final String[] field_150133_O = new String[] {"acacia", "big_oak"};
    private static final String __OBFID = "CL_00000276";

    protected void func_150124_c(World p_150124_1_, int p_150124_2_, int p_150124_3_, int p_150124_4_, int p_150124_5_, int p_150124_6_)
    {
        if ((p_150124_5_ & 3) == 1 && p_150124_1_.rand.nextInt(p_150124_6_) == 0)
        {
            this.dropBlockAsItem(p_150124_1_, p_150124_2_, p_150124_3_, p_150124_4_, new ItemStack(Items.apple, 1, 0));
        }
    }

    public int damageDropped(int meta)
    {
        return super.damageDropped(meta) + 4;
    }

    public int getDamageValue(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlockMetadata(x, y, z) & 3;
    }

    public IIcon getIcon(int side, int meta)
    {
        return (meta & 3) == 1 ? this.field_150129_M[this.field_150127_b][1] : this.field_150129_M[this.field_150127_b][0];
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    public void registerIcons(IIconRegister reg)
    {
        for (int var2 = 0; var2 < field_150132_N.length; ++var2)
        {
            this.field_150129_M[var2] = new IIcon[field_150132_N[var2].length];

            for (int var3 = 0; var3 < field_150132_N[var2].length; ++var3)
            {
                this.field_150129_M[var2][var3] = reg.registerIcon(field_150132_N[var2][var3]);
            }
        }
    }

    public String[] func_150125_e()
    {
        return field_150133_O;
    }
}
