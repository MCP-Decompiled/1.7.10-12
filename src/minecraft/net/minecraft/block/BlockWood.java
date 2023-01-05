package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockWood extends Block
{
    public static final String[] field_150096_a = new String[] {"oak", "spruce", "birch", "jungle", "acacia", "big_oak"};
    private IIcon[] field_150095_b;
    private static final String __OBFID = "CL_00000335";

    public BlockWood()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        if (meta < 0 || meta >= this.field_150095_b.length)
        {
            meta = 0;
        }

        return this.field_150095_b[meta];
    }

    public int damageDropped(int meta)
    {
        return meta;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
        list.add(new ItemStack(itemIn, 1, 2));
        list.add(new ItemStack(itemIn, 1, 3));
        list.add(new ItemStack(itemIn, 1, 4));
        list.add(new ItemStack(itemIn, 1, 5));
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_150095_b = new IIcon[field_150096_a.length];

        for (int var2 = 0; var2 < this.field_150095_b.length; ++var2)
        {
            this.field_150095_b[var2] = reg.registerIcon(this.getTextureName() + "_" + field_150096_a[var2]);
        }
    }
}
