package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockStoneBrick extends Block
{
    public static final String[] field_150142_a = new String[] {"default", "mossy", "cracked", "chiseled"};
    public static final String[] field_150141_b = new String[] {null, "mossy", "cracked", "carved"};
    private IIcon[] field_150143_M;
    private static final String __OBFID = "CL_00000318";

    public BlockStoneBrick()
    {
        super(Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        if (meta < 0 || meta >= field_150141_b.length)
        {
            meta = 0;
        }

        return this.field_150143_M[meta];
    }

    public int damageDropped(int meta)
    {
        return meta;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int var4 = 0; var4 < 4; ++var4)
        {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_150143_M = new IIcon[field_150141_b.length];

        for (int var2 = 0; var2 < this.field_150143_M.length; ++var2)
        {
            String var3 = this.getTextureName();

            if (field_150141_b[var2] != null)
            {
                var3 = var3 + "_" + field_150141_b[var2];
            }

            this.field_150143_M[var2] = reg.registerIcon(var3);
        }
    }
}
