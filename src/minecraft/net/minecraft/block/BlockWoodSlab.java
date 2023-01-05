package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockWoodSlab extends BlockSlab
{
    public static final String[] field_150005_b = new String[] {"oak", "spruce", "birch", "jungle", "acacia", "big_oak"};
    private static final String __OBFID = "CL_00000337";

    public BlockWoodSlab(boolean p_i45437_1_)
    {
        super(p_i45437_1_, Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        return Blocks.planks.getIcon(side, meta & 7);
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Item.getItemFromBlock(Blocks.wooden_slab);
    }

    protected ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(Item.getItemFromBlock(Blocks.wooden_slab), 2, meta & 7);
    }

    public String getFullSlabName(int p_150002_1_)
    {
        if (p_150002_1_ < 0 || p_150002_1_ >= field_150005_b.length)
        {
            p_150002_1_ = 0;
        }

        return super.getUnlocalizedName() + "." + field_150005_b[p_150002_1_];
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        if (itemIn != Item.getItemFromBlock(Blocks.double_wooden_slab))
        {
            for (int var4 = 0; var4 < field_150005_b.length; ++var4)
            {
                list.add(new ItemStack(itemIn, 1, var4));
            }
        }
    }

    public void registerIcons(IIconRegister reg) {}
}
