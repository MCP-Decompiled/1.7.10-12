package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockColored extends Block
{
    private IIcon[] field_150033_a;
    private static final String __OBFID = "CL_00000217";

    public BlockColored(Material p_i45398_1_)
    {
        super(p_i45398_1_);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        return this.field_150033_a[meta % this.field_150033_a.length];
    }

    public int damageDropped(int meta)
    {
        return meta;
    }

    public static int func_150032_b(int p_150032_0_)
    {
        return func_150031_c(p_150032_0_);
    }

    public static int func_150031_c(int p_150031_0_)
    {
        return ~p_150031_0_ & 15;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int var4 = 0; var4 < 16; ++var4)
        {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_150033_a = new IIcon[16];

        for (int var2 = 0; var2 < this.field_150033_a.length; ++var2)
        {
            this.field_150033_a[var2] = reg.registerIcon(this.getTextureName() + "_" + ItemDye.dyeIcons[func_150031_c(var2)]);
        }
    }

    public MapColor getMapColor(int meta)
    {
        return MapColor.getMapColorForBlockColored(meta);
    }
}
