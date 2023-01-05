package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public abstract class BlockRotatedPillar extends Block
{
    protected IIcon field_150164_N;
    private static final String __OBFID = "CL_00000302";

    protected BlockRotatedPillar(Material p_i45425_1_)
    {
        super(p_i45425_1_);
    }

    public int getRenderType()
    {
        return 31;
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        int var10 = meta & 3;
        byte var11 = 0;

        switch (side)
        {
            case 0:
            case 1:
                var11 = 0;
                break;

            case 2:
            case 3:
                var11 = 8;
                break;

            case 4:
            case 5:
                var11 = 4;
        }

        return var10 | var11;
    }

    public IIcon getIcon(int side, int meta)
    {
        int var3 = meta & 12;
        int var4 = meta & 3;
        return var3 == 0 && (side == 1 || side == 0) ? this.getTopIcon(var4) : (var3 == 4 && (side == 5 || side == 4) ? this.getTopIcon(var4) : (var3 == 8 && (side == 2 || side == 3) ? this.getTopIcon(var4) : this.getSideIcon(var4)));
    }

    protected abstract IIcon getSideIcon(int p_150163_1_);

    protected IIcon getTopIcon(int p_150161_1_)
    {
        return this.field_150164_N;
    }

    public int damageDropped(int meta)
    {
        return meta & 3;
    }

    public int func_150162_k(int p_150162_1_)
    {
        return p_150162_1_ & 3;
    }

    protected ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.func_150162_k(meta));
    }
}
