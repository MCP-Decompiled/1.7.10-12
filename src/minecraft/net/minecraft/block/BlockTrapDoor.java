package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor extends Block
{
    private static final String __OBFID = "CL_00000327";

    protected BlockTrapDoor(Material p_i45434_1_)
    {
        super(p_i45434_1_);
        float var2 = 0.5F;
        float var3 = 1.0F;
        this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var3, 0.5F + var2);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, int x, int y, int z)
    {
        return !isTrapdoorOpen(worldIn.getBlockMetadata(x, y, z));
    }

    public int getRenderType()
    {
        return 0;
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.getSelectedBoundingBoxFromPool(worldIn, x, y, z);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.getCollisionBoundingBoxFromPool(worldIn, x, y, z);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        this.func_150117_b(worldIn.getBlockMetadata(x, y, z));
    }

    public void setBlockBoundsForItemRender()
    {
        float var1 = 0.1875F;
        this.setBlockBounds(0.0F, 0.5F - var1 / 2.0F, 0.0F, 1.0F, 0.5F + var1 / 2.0F, 1.0F);
    }

    public void func_150117_b(int p_150117_1_)
    {
        float var2 = 0.1875F;

        if ((p_150117_1_ & 8) != 0)
        {
            this.setBlockBounds(0.0F, 1.0F - var2, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var2, 1.0F);
        }

        if (isTrapdoorOpen(p_150117_1_))
        {
            if ((p_150117_1_ & 3) == 0)
            {
                this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
            }

            if ((p_150117_1_ & 3) == 1)
            {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
            }

            if ((p_150117_1_ & 3) == 2)
            {
                this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }

            if ((p_150117_1_ & 3) == 3)
            {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
            }
        }
    }

    public void onBlockClicked(World worldIn, int x, int y, int z, EntityPlayer player) {}

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (this.blockMaterial == Material.iron)
        {
            return true;
        }
        else
        {
            int var10 = worldIn.getBlockMetadata(x, y, z);
            worldIn.setBlockMetadataWithNotify(x, y, z, var10 ^ 4, 2);
            worldIn.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
            return true;
        }
    }

    public void func_150120_a(World p_150120_1_, int p_150120_2_, int p_150120_3_, int p_150120_4_, boolean p_150120_5_)
    {
        int var6 = p_150120_1_.getBlockMetadata(p_150120_2_, p_150120_3_, p_150120_4_);
        boolean var7 = (var6 & 4) > 0;

        if (var7 != p_150120_5_)
        {
            p_150120_1_.setBlockMetadataWithNotify(p_150120_2_, p_150120_3_, p_150120_4_, var6 ^ 4, 2);
            p_150120_1_.playAuxSFXAtEntity((EntityPlayer)null, 1003, p_150120_2_, p_150120_3_, p_150120_4_, 0);
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!worldIn.isRemote)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);
            int var7 = x;
            int var8 = z;

            if ((var6 & 3) == 0)
            {
                var8 = z + 1;
            }

            if ((var6 & 3) == 1)
            {
                --var8;
            }

            if ((var6 & 3) == 2)
            {
                var7 = x + 1;
            }

            if ((var6 & 3) == 3)
            {
                --var7;
            }

            if (!isValidSupportBlock(worldIn.getBlock(var7, y, var8)))
            {
                worldIn.setBlockToAir(x, y, z);
                this.dropBlockAsItem(worldIn, x, y, z, var6, 0);
            }

            boolean var9 = worldIn.isBlockIndirectlyGettingPowered(x, y, z);

            if (var9 || neighbor.canProvidePower())
            {
                this.func_150120_a(worldIn, x, y, z, var9);
            }
        }
    }

    public MovingObjectPosition collisionRayTrace(World worldIn, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.collisionRayTrace(worldIn, x, y, z, startVec, endVec);
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        int var10 = 0;

        if (side == 2)
        {
            var10 = 0;
        }

        if (side == 3)
        {
            var10 = 1;
        }

        if (side == 4)
        {
            var10 = 2;
        }

        if (side == 5)
        {
            var10 = 3;
        }

        if (side != 1 && side != 0 && subY > 0.5F)
        {
            var10 |= 8;
        }

        return var10;
    }

    public boolean canPlaceBlockOnSide(World worldIn, int x, int y, int z, int side)
    {
        if (side == 0)
        {
            return false;
        }
        else if (side == 1)
        {
            return false;
        }
        else
        {
            if (side == 2)
            {
                ++z;
            }

            if (side == 3)
            {
                --z;
            }

            if (side == 4)
            {
                ++x;
            }

            if (side == 5)
            {
                --x;
            }

            return isValidSupportBlock(worldIn.getBlock(x, y, z));
        }
    }

    public static boolean isTrapdoorOpen(int p_150118_0_)
    {
        return (p_150118_0_ & 4) != 0;
    }

    private static boolean isValidSupportBlock(Block p_150119_0_)
    {
        return p_150119_0_.blockMaterial.isOpaque() && p_150119_0_.renderAsNormalBlock() || p_150119_0_ == Blocks.glowstone || p_150119_0_ instanceof BlockSlab || p_150119_0_ instanceof BlockStairs;
    }
}
