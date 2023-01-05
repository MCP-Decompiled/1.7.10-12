package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockQuartz extends Block
{
    public static final String[] field_150191_a = new String[] {"default", "chiseled", "lines"};
    private static final String[] field_150189_b = new String[] {"side", "chiseled", "lines", null, null};
    private IIcon[] field_150192_M;
    private IIcon field_150193_N;
    private IIcon field_150194_O;
    private IIcon field_150190_P;
    private IIcon field_150188_Q;
    private static final String __OBFID = "CL_00000292";

    public BlockQuartz()
    {
        super(Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        if (meta != 2 && meta != 3 && meta != 4)
        {
            if (side != 1 && (side != 0 || meta != 1))
            {
                if (side == 0)
                {
                    return this.field_150188_Q;
                }
                else
                {
                    if (meta < 0 || meta >= this.field_150192_M.length)
                    {
                        meta = 0;
                    }

                    return this.field_150192_M[meta];
                }
            }
            else
            {
                return meta == 1 ? this.field_150193_N : this.field_150190_P;
            }
        }
        else
        {
            return meta == 2 && (side == 1 || side == 0) ? this.field_150194_O : (meta == 3 && (side == 5 || side == 4) ? this.field_150194_O : (meta == 4 && (side == 2 || side == 3) ? this.field_150194_O : this.field_150192_M[meta]));
        }
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        if (meta == 2)
        {
            switch (side)
            {
                case 0:
                case 1:
                    meta = 2;
                    break;

                case 2:
                case 3:
                    meta = 4;
                    break;

                case 4:
                case 5:
                    meta = 3;
            }
        }

        return meta;
    }

    public int damageDropped(int meta)
    {
        return meta != 3 && meta != 4 ? meta : 2;
    }

    protected ItemStack createStackedBlock(int meta)
    {
        return meta != 3 && meta != 4 ? super.createStackedBlock(meta) : new ItemStack(Item.getItemFromBlock(this), 1, 2);
    }

    public int getRenderType()
    {
        return 39;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
        list.add(new ItemStack(itemIn, 1, 2));
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_150192_M = new IIcon[field_150189_b.length];

        for (int var2 = 0; var2 < this.field_150192_M.length; ++var2)
        {
            if (field_150189_b[var2] == null)
            {
                this.field_150192_M[var2] = this.field_150192_M[var2 - 1];
            }
            else
            {
                this.field_150192_M[var2] = reg.registerIcon(this.getTextureName() + "_" + field_150189_b[var2]);
            }
        }

        this.field_150190_P = reg.registerIcon(this.getTextureName() + "_" + "top");
        this.field_150193_N = reg.registerIcon(this.getTextureName() + "_" + "chiseled_top");
        this.field_150194_O = reg.registerIcon(this.getTextureName() + "_" + "lines_top");
        this.field_150188_Q = reg.registerIcon(this.getTextureName() + "_" + "bottom");
    }

    public MapColor getMapColor(int meta)
    {
        return MapColor.quartzColor;
    }
}
