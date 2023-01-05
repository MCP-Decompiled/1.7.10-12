package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockAir extends Block
{
    private static final String __OBFID = "CL_00000190";

    protected BlockAir()
    {
        super(Material.air);
    }

    public int getRenderType()
    {
        return -1;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean canStopRayTrace(int meta, boolean includeLiquid)
    {
        return false;
    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune) {}
}
