package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockStainedGlass extends BlockBreakable
{
    private static final IIcon[] field_149998_a = new IIcon[16];
    private static final String __OBFID = "CL_00000312";

    public BlockStainedGlass(Material p_i45427_1_)
    {
        super("glass", p_i45427_1_, false);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        return field_149998_a[meta % field_149998_a.length];
    }

    public int damageDropped(int meta)
    {
        return meta;
    }

    public static int func_149997_b(int p_149997_0_)
    {
        return ~p_149997_0_ & 15;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int var4 = 0; var4 < field_149998_a.length; ++var4)
        {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    public int getRenderBlockPass()
    {
        return 1;
    }

    public void registerIcons(IIconRegister reg)
    {
        for (int var2 = 0; var2 < field_149998_a.length; ++var2)
        {
            field_149998_a[var2] = reg.registerIcon(this.getTextureName() + "_" + ItemDye.dyeIcons[func_149997_b(var2)]);
        }
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    protected boolean canSilkHarvest()
    {
        return true;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }
}
