package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockSandStone extends Block
{
    public static final String[] field_150157_a = new String[] {"default", "chiseled", "smooth"};
    private static final String[] field_150156_b = new String[] {"normal", "carved", "smooth"};
    private IIcon[] field_150158_M;
    private IIcon field_150159_N;
    private IIcon field_150160_O;
    private static final String __OBFID = "CL_00000304";

    public BlockSandStone()
    {
        super(Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        if (side != 1 && (side != 0 || meta != 1 && meta != 2))
        {
            if (side == 0)
            {
                return this.field_150160_O;
            }
            else
            {
                if (meta < 0 || meta >= this.field_150158_M.length)
                {
                    meta = 0;
                }

                return this.field_150158_M[meta];
            }
        }
        else
        {
            return this.field_150159_N;
        }
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
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_150158_M = new IIcon[field_150156_b.length];

        for (int var2 = 0; var2 < this.field_150158_M.length; ++var2)
        {
            this.field_150158_M[var2] = reg.registerIcon(this.getTextureName() + "_" + field_150156_b[var2]);
        }

        this.field_150159_N = reg.registerIcon(this.getTextureName() + "_top");
        this.field_150160_O = reg.registerIcon(this.getTextureName() + "_bottom");
    }
}
