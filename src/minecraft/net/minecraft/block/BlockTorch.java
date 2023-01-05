package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockTorch extends Block
{
    private static final String __OBFID = "CL_00000325";

    protected BlockTorch()
    {
        super(Material.circuits);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return null;
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
        return 2;
    }

    private boolean canPlaceTorchOn(World p_150107_1_, int p_150107_2_, int p_150107_3_, int p_150107_4_)
    {
        if (World.doesBlockHaveSolidTopSurface(p_150107_1_, p_150107_2_, p_150107_3_, p_150107_4_))
        {
            return true;
        }
        else
        {
            Block var5 = p_150107_1_.getBlock(p_150107_2_, p_150107_3_, p_150107_4_);
            return var5 == Blocks.fence || var5 == Blocks.nether_brick_fence || var5 == Blocks.glass || var5 == Blocks.cobblestone_wall;
        }
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return worldIn.isBlockNormalCubeDefault(x - 1, y, z, true) ? true : (worldIn.isBlockNormalCubeDefault(x + 1, y, z, true) ? true : (worldIn.isBlockNormalCubeDefault(x, y, z - 1, true) ? true : (worldIn.isBlockNormalCubeDefault(x, y, z + 1, true) ? true : this.canPlaceTorchOn(worldIn, x, y - 1, z))));
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        int var10 = meta;

        if (side == 1 && this.canPlaceTorchOn(worldIn, x, y - 1, z))
        {
            var10 = 5;
        }

        if (side == 2 && worldIn.isBlockNormalCubeDefault(x, y, z + 1, true))
        {
            var10 = 4;
        }

        if (side == 3 && worldIn.isBlockNormalCubeDefault(x, y, z - 1, true))
        {
            var10 = 3;
        }

        if (side == 4 && worldIn.isBlockNormalCubeDefault(x + 1, y, z, true))
        {
            var10 = 2;
        }

        if (side == 5 && worldIn.isBlockNormalCubeDefault(x - 1, y, z, true))
        {
            var10 = 1;
        }

        return var10;
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        super.updateTick(worldIn, x, y, z, random);

        if (worldIn.getBlockMetadata(x, y, z) == 0)
        {
            this.onBlockAdded(worldIn, x, y, z);
        }
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        if (worldIn.getBlockMetadata(x, y, z) == 0)
        {
            if (worldIn.isBlockNormalCubeDefault(x - 1, y, z, true))
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, 1, 2);
            }
            else if (worldIn.isBlockNormalCubeDefault(x + 1, y, z, true))
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, 2, 2);
            }
            else if (worldIn.isBlockNormalCubeDefault(x, y, z - 1, true))
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, 3, 2);
            }
            else if (worldIn.isBlockNormalCubeDefault(x, y, z + 1, true))
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, 4, 2);
            }
            else if (this.canPlaceTorchOn(worldIn, x, y - 1, z))
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, 5, 2);
            }
        }

        this.dropTorchIfCantStay(worldIn, x, y, z);
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        this.func_150108_b(worldIn, x, y, z, neighbor);
    }

    protected boolean func_150108_b(World p_150108_1_, int p_150108_2_, int p_150108_3_, int p_150108_4_, Block p_150108_5_)
    {
        if (this.dropTorchIfCantStay(p_150108_1_, p_150108_2_, p_150108_3_, p_150108_4_))
        {
            int var6 = p_150108_1_.getBlockMetadata(p_150108_2_, p_150108_3_, p_150108_4_);
            boolean var7 = false;

            if (!p_150108_1_.isBlockNormalCubeDefault(p_150108_2_ - 1, p_150108_3_, p_150108_4_, true) && var6 == 1)
            {
                var7 = true;
            }

            if (!p_150108_1_.isBlockNormalCubeDefault(p_150108_2_ + 1, p_150108_3_, p_150108_4_, true) && var6 == 2)
            {
                var7 = true;
            }

            if (!p_150108_1_.isBlockNormalCubeDefault(p_150108_2_, p_150108_3_, p_150108_4_ - 1, true) && var6 == 3)
            {
                var7 = true;
            }

            if (!p_150108_1_.isBlockNormalCubeDefault(p_150108_2_, p_150108_3_, p_150108_4_ + 1, true) && var6 == 4)
            {
                var7 = true;
            }

            if (!this.canPlaceTorchOn(p_150108_1_, p_150108_2_, p_150108_3_ - 1, p_150108_4_) && var6 == 5)
            {
                var7 = true;
            }

            if (var7)
            {
                this.dropBlockAsItem(p_150108_1_, p_150108_2_, p_150108_3_, p_150108_4_, p_150108_1_.getBlockMetadata(p_150108_2_, p_150108_3_, p_150108_4_), 0);
                p_150108_1_.setBlockToAir(p_150108_2_, p_150108_3_, p_150108_4_);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    protected boolean dropTorchIfCantStay(World p_150109_1_, int p_150109_2_, int p_150109_3_, int p_150109_4_)
    {
        if (!this.canPlaceBlockAt(p_150109_1_, p_150109_2_, p_150109_3_, p_150109_4_))
        {
            if (p_150109_1_.getBlock(p_150109_2_, p_150109_3_, p_150109_4_) == this)
            {
                this.dropBlockAsItem(p_150109_1_, p_150109_2_, p_150109_3_, p_150109_4_, p_150109_1_.getBlockMetadata(p_150109_2_, p_150109_3_, p_150109_4_), 0);
                p_150109_1_.setBlockToAir(p_150109_2_, p_150109_3_, p_150109_4_);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    public MovingObjectPosition collisionRayTrace(World worldIn, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
        int var7 = worldIn.getBlockMetadata(x, y, z) & 7;
        float var8 = 0.15F;

        if (var7 == 1)
        {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
        }
        else if (var7 == 2)
        {
            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
        }
        else if (var7 == 3)
        {
            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
        }
        else if (var7 == 4)
        {
            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
        }
        else
        {
            var8 = 0.1F;
            this.setBlockBounds(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
        }

        return super.collisionRayTrace(worldIn, x, y, z, startVec, endVec);
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);
        double var7 = (double)((float)x + 0.5F);
        double var9 = (double)((float)y + 0.7F);
        double var11 = (double)((float)z + 0.5F);
        double var13 = 0.2199999988079071D;
        double var15 = 0.27000001072883606D;

        if (var6 == 1)
        {
            worldIn.spawnParticle("smoke", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle("flame", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
        }
        else if (var6 == 2)
        {
            worldIn.spawnParticle("smoke", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle("flame", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
        }
        else if (var6 == 3)
        {
            worldIn.spawnParticle("smoke", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle("flame", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
        }
        else if (var6 == 4)
        {
            worldIn.spawnParticle("smoke", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle("flame", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
        }
        else
        {
            worldIn.spawnParticle("smoke", var7, var9, var11, 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle("flame", var7, var9, var11, 0.0D, 0.0D, 0.0D);
        }
    }
}
