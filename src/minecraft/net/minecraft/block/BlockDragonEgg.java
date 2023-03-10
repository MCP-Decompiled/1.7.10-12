package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDragonEgg extends Block
{
    private static final String __OBFID = "CL_00000232";

    public BlockDragonEgg()
    {
        super(Material.dragonEgg);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        this.func_150018_e(worldIn, x, y, z);
    }

    private void func_150018_e(World p_150018_1_, int p_150018_2_, int p_150018_3_, int p_150018_4_)
    {
        if (BlockFalling.canFallBelow(p_150018_1_, p_150018_2_, p_150018_3_ - 1, p_150018_4_) && p_150018_3_ >= 0)
        {
            byte var5 = 32;

            if (!BlockFalling.fallInstantly && p_150018_1_.checkChunksExist(p_150018_2_ - var5, p_150018_3_ - var5, p_150018_4_ - var5, p_150018_2_ + var5, p_150018_3_ + var5, p_150018_4_ + var5))
            {
                EntityFallingBlock var6 = new EntityFallingBlock(p_150018_1_, (double)((float)p_150018_2_ + 0.5F), (double)((float)p_150018_3_ + 0.5F), (double)((float)p_150018_4_ + 0.5F), this);
                p_150018_1_.spawnEntityInWorld(var6);
            }
            else
            {
                p_150018_1_.setBlockToAir(p_150018_2_, p_150018_3_, p_150018_4_);

                while (BlockFalling.canFallBelow(p_150018_1_, p_150018_2_, p_150018_3_ - 1, p_150018_4_) && p_150018_3_ > 0)
                {
                    --p_150018_3_;
                }

                if (p_150018_3_ > 0)
                {
                    p_150018_1_.setBlock(p_150018_2_, p_150018_3_, p_150018_4_, this, 0, 2);
                }
            }
        }
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        this.func_150019_m(worldIn, x, y, z);
        return true;
    }

    public void onBlockClicked(World worldIn, int x, int y, int z, EntityPlayer player)
    {
        this.func_150019_m(worldIn, x, y, z);
    }

    private void func_150019_m(World p_150019_1_, int p_150019_2_, int p_150019_3_, int p_150019_4_)
    {
        if (p_150019_1_.getBlock(p_150019_2_, p_150019_3_, p_150019_4_) == this)
        {
            for (int var5 = 0; var5 < 1000; ++var5)
            {
                int var6 = p_150019_2_ + p_150019_1_.rand.nextInt(16) - p_150019_1_.rand.nextInt(16);
                int var7 = p_150019_3_ + p_150019_1_.rand.nextInt(8) - p_150019_1_.rand.nextInt(8);
                int var8 = p_150019_4_ + p_150019_1_.rand.nextInt(16) - p_150019_1_.rand.nextInt(16);

                if (p_150019_1_.getBlock(var6, var7, var8).blockMaterial == Material.air)
                {
                    if (!p_150019_1_.isRemote)
                    {
                        p_150019_1_.setBlock(var6, var7, var8, this, p_150019_1_.getBlockMetadata(p_150019_2_, p_150019_3_, p_150019_4_), 2);
                        p_150019_1_.setBlockToAir(p_150019_2_, p_150019_3_, p_150019_4_);
                    }
                    else
                    {
                        short var9 = 128;

                        for (int var10 = 0; var10 < var9; ++var10)
                        {
                            double var11 = p_150019_1_.rand.nextDouble();
                            float var13 = (p_150019_1_.rand.nextFloat() - 0.5F) * 0.2F;
                            float var14 = (p_150019_1_.rand.nextFloat() - 0.5F) * 0.2F;
                            float var15 = (p_150019_1_.rand.nextFloat() - 0.5F) * 0.2F;
                            double var16 = (double)var6 + (double)(p_150019_2_ - var6) * var11 + (p_150019_1_.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
                            double var18 = (double)var7 + (double)(p_150019_3_ - var7) * var11 + p_150019_1_.rand.nextDouble() * 1.0D - 0.5D;
                            double var20 = (double)var8 + (double)(p_150019_4_ - var8) * var11 + (p_150019_1_.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
                            p_150019_1_.spawnParticle("portal", var16, var18, var20, (double)var13, (double)var14, (double)var15);
                        }
                    }

                    return;
                }
            }
        }
    }

    public int tickRate(World worldIn)
    {
        return 5;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return true;
    }

    public int getRenderType()
    {
        return 27;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Item.getItemById(0);
    }
}
