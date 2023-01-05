package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockLeavesBase extends Block
{
    protected boolean field_150121_P;
    private static final String __OBFID = "CL_00000326";

    protected BlockLeavesBase(Material p_i45433_1_, boolean p_i45433_2_)
    {
        super(p_i45433_1_);
        this.field_150121_P = p_i45433_2_;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        Block var6 = worldIn.getBlock(x, y, z);
        return !this.field_150121_P && var6 == this ? false : super.shouldSideBeRendered(worldIn, x, y, z, side);
    }
}
