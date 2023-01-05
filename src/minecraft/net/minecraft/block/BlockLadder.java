package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLadder extends Block
{
    private static final String __OBFID = "CL_00000262";

    protected BlockLadder()
    {
        super(Material.circuits);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.getCollisionBoundingBoxFromPool(worldIn, x, y, z);
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.getSelectedBoundingBoxFromPool(worldIn, x, y, z);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        this.func_149797_b(worldIn.getBlockMetadata(x, y, z));
    }

    public void func_149797_b(int p_149797_1_)
    {
        float var3 = 0.125F;

        if (p_149797_1_ == 2)
        {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - var3, 1.0F, 1.0F, 1.0F);
        }

        if (p_149797_1_ == 3)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var3);
        }

        if (p_149797_1_ == 4)
        {
            this.setBlockBounds(1.0F - var3, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (p_149797_1_ == 5)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, var3, 1.0F, 1.0F);
        }
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return 8;
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlock(x - 1, y, z).isNormalCube() ? true : (worldIn.getBlock(x + 1, y, z).isNormalCube() ? true : (worldIn.getBlock(x, y, z - 1).isNormalCube() ? true : worldIn.getBlock(x, y, z + 1).isNormalCube()));
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        int var10 = meta;

        if ((meta == 0 || side == 2) && worldIn.getBlock(x, y, z + 1).isNormalCube())
        {
            var10 = 2;
        }

        if ((var10 == 0 || side == 3) && worldIn.getBlock(x, y, z - 1).isNormalCube())
        {
            var10 = 3;
        }

        if ((var10 == 0 || side == 4) && worldIn.getBlock(x + 1, y, z).isNormalCube())
        {
            var10 = 4;
        }

        if ((var10 == 0 || side == 5) && worldIn.getBlock(x - 1, y, z).isNormalCube())
        {
            var10 = 5;
        }

        return var10;
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);
        boolean var7 = false;

        if (var6 == 2 && worldIn.getBlock(x, y, z + 1).isNormalCube())
        {
            var7 = true;
        }

        if (var6 == 3 && worldIn.getBlock(x, y, z - 1).isNormalCube())
        {
            var7 = true;
        }

        if (var6 == 4 && worldIn.getBlock(x + 1, y, z).isNormalCube())
        {
            var7 = true;
        }

        if (var6 == 5 && worldIn.getBlock(x - 1, y, z).isNormalCube())
        {
            var7 = true;
        }

        if (!var7)
        {
            this.dropBlockAsItem(worldIn, x, y, z, var6, 0);
            worldIn.setBlockToAir(x, y, z);
        }

        super.onNeighborBlockChange(worldIn, x, y, z, neighbor);
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }
}
