package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLilyPad extends BlockBush
{
    private static final String __OBFID = "CL_00000332";

    protected BlockLilyPad()
    {
        float var1 = 0.5F;
        float var2 = 0.015625F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var2, 0.5F + var1);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public int getRenderType()
    {
        return 23;
    }

    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider)
    {
        if (collider == null || !(collider instanceof EntityBoat))
        {
            super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
    }

    public int getBlockColor()
    {
        return 2129968;
    }

    public int getRenderColor(int meta)
    {
        return 2129968;
    }

    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        return 2129968;
    }

    protected boolean canPlaceBlockOn(Block ground)
    {
        return ground == Blocks.water;
    }

    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        return y >= 0 && y < 256 ? worldIn.getBlock(x, y - 1, z).getMaterial() == Material.water && worldIn.getBlockMetadata(x, y - 1, z) == 0 : false;
    }
}
