package net.minecraft.block;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;

public class BlockMushroom extends BlockBush implements IGrowable
{
    private static final String __OBFID = "CL_00000272";

    protected BlockMushroom()
    {
        float var1 = 0.2F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var1 * 2.0F, 0.5F + var1);
        this.setTickRandomly(true);
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (random.nextInt(25) == 0)
        {
            byte var6 = 4;
            int var7 = 5;
            int var8;
            int var9;
            int var10;

            for (var8 = x - var6; var8 <= x + var6; ++var8)
            {
                for (var9 = z - var6; var9 <= z + var6; ++var9)
                {
                    for (var10 = y - 1; var10 <= y + 1; ++var10)
                    {
                        if (worldIn.getBlock(var8, var10, var9) == this)
                        {
                            --var7;

                            if (var7 <= 0)
                            {
                                return;
                            }
                        }
                    }
                }
            }

            var8 = x + random.nextInt(3) - 1;
            var9 = y + random.nextInt(2) - random.nextInt(2);
            var10 = z + random.nextInt(3) - 1;

            for (int var11 = 0; var11 < 4; ++var11)
            {
                if (worldIn.isAirBlock(var8, var9, var10) && this.canBlockStay(worldIn, var8, var9, var10))
                {
                    x = var8;
                    y = var9;
                    z = var10;
                }

                var8 = x + random.nextInt(3) - 1;
                var9 = y + random.nextInt(2) - random.nextInt(2);
                var10 = z + random.nextInt(3) - 1;
            }

            if (worldIn.isAirBlock(var8, var9, var10) && this.canBlockStay(worldIn, var8, var9, var10))
            {
                worldIn.setBlock(var8, var9, var10, this, 0, 2);
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return super.canPlaceBlockAt(worldIn, x, y, z) && this.canBlockStay(worldIn, x, y, z);
    }

    protected boolean canPlaceBlockOn(Block ground)
    {
        return ground.isFullBlock();
    }

    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        if (y >= 0 && y < 256)
        {
            Block var5 = worldIn.getBlock(x, y - 1, z);
            return var5 == Blocks.mycelium || var5 == Blocks.dirt && worldIn.getBlockMetadata(x, y - 1, z) == 2 || worldIn.getFullBlockLightValue(x, y, z) < 13 && this.canPlaceBlockOn(var5);
        }
        else
        {
            return false;
        }
    }

    public boolean fertilizeMushroom(World p_149884_1_, int p_149884_2_, int p_149884_3_, int p_149884_4_, Random p_149884_5_)
    {
        int var6 = p_149884_1_.getBlockMetadata(p_149884_2_, p_149884_3_, p_149884_4_);
        p_149884_1_.setBlockToAir(p_149884_2_, p_149884_3_, p_149884_4_);
        WorldGenBigMushroom var7 = null;

        if (this == Blocks.brown_mushroom)
        {
            var7 = new WorldGenBigMushroom(0);
        }
        else if (this == Blocks.red_mushroom)
        {
            var7 = new WorldGenBigMushroom(1);
        }

        if (var7 != null && var7.generate(p_149884_1_, p_149884_5_, p_149884_2_, p_149884_3_, p_149884_4_))
        {
            return true;
        }
        else
        {
            p_149884_1_.setBlock(p_149884_2_, p_149884_3_, p_149884_4_, this, var6, 3);
            return false;
        }
    }

    public boolean canFertilize(World worldIn, int x, int y, int z, boolean isClient)
    {
        return true;
    }

    public boolean shouldFertilize(World worldIn, Random random, int x, int y, int z)
    {
        return (double)random.nextFloat() < 0.4D;
    }

    public void fertilize(World worldIn, Random random, int x, int y, int z)
    {
        this.fertilizeMushroom(worldIn, x, y, z, random);
    }
}
