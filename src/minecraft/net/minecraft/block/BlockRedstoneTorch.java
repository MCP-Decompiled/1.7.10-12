package net.minecraft.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneTorch extends BlockTorch
{
    private boolean field_150113_a;
    private static Map field_150112_b = new HashMap();
    private static final String __OBFID = "CL_00000298";

    private boolean func_150111_a(World p_150111_1_, int p_150111_2_, int p_150111_3_, int p_150111_4_, boolean p_150111_5_)
    {
        if (!field_150112_b.containsKey(p_150111_1_))
        {
            field_150112_b.put(p_150111_1_, new ArrayList());
        }

        List var6 = (List)field_150112_b.get(p_150111_1_);

        if (p_150111_5_)
        {
            var6.add(new BlockRedstoneTorch.Toggle(p_150111_2_, p_150111_3_, p_150111_4_, p_150111_1_.getTotalWorldTime()));
        }

        int var7 = 0;

        for (int var8 = 0; var8 < var6.size(); ++var8)
        {
            BlockRedstoneTorch.Toggle var9 = (BlockRedstoneTorch.Toggle)var6.get(var8);

            if (var9.field_150847_a == p_150111_2_ && var9.field_150845_b == p_150111_3_ && var9.field_150846_c == p_150111_4_)
            {
                ++var7;

                if (var7 >= 8)
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected BlockRedstoneTorch(boolean p_i45423_1_)
    {
        this.field_150113_a = p_i45423_1_;
        this.setTickRandomly(true);
        this.setCreativeTab((CreativeTabs)null);
    }

    public int tickRate(World worldIn)
    {
        return 2;
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        if (worldIn.getBlockMetadata(x, y, z) == 0)
        {
            super.onBlockAdded(worldIn, x, y, z);
        }

        if (this.field_150113_a)
        {
            worldIn.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            worldIn.notifyBlocksOfNeighborChange(x, y + 1, z, this);
            worldIn.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            worldIn.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            worldIn.notifyBlocksOfNeighborChange(x, y, z - 1, this);
            worldIn.notifyBlocksOfNeighborChange(x, y, z + 1, this);
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        if (this.field_150113_a)
        {
            worldIn.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            worldIn.notifyBlocksOfNeighborChange(x, y + 1, z, this);
            worldIn.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            worldIn.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            worldIn.notifyBlocksOfNeighborChange(x, y, z - 1, this);
            worldIn.notifyBlocksOfNeighborChange(x, y, z + 1, this);
        }
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        if (!this.field_150113_a)
        {
            return 0;
        }
        else
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);
            return var6 == 5 && side == 1 ? 0 : (var6 == 3 && side == 3 ? 0 : (var6 == 4 && side == 2 ? 0 : (var6 == 1 && side == 5 ? 0 : (var6 == 2 && side == 4 ? 0 : 15))));
        }
    }

    private boolean func_150110_m(World p_150110_1_, int p_150110_2_, int p_150110_3_, int p_150110_4_)
    {
        int var5 = p_150110_1_.getBlockMetadata(p_150110_2_, p_150110_3_, p_150110_4_);
        return var5 == 5 && p_150110_1_.getIndirectPowerOutput(p_150110_2_, p_150110_3_ - 1, p_150110_4_, 0) ? true : (var5 == 3 && p_150110_1_.getIndirectPowerOutput(p_150110_2_, p_150110_3_, p_150110_4_ - 1, 2) ? true : (var5 == 4 && p_150110_1_.getIndirectPowerOutput(p_150110_2_, p_150110_3_, p_150110_4_ + 1, 3) ? true : (var5 == 1 && p_150110_1_.getIndirectPowerOutput(p_150110_2_ - 1, p_150110_3_, p_150110_4_, 4) ? true : var5 == 2 && p_150110_1_.getIndirectPowerOutput(p_150110_2_ + 1, p_150110_3_, p_150110_4_, 5))));
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        boolean var6 = this.func_150110_m(worldIn, x, y, z);
        List var7 = (List)field_150112_b.get(worldIn);

        while (var7 != null && !var7.isEmpty() && worldIn.getTotalWorldTime() - ((BlockRedstoneTorch.Toggle)var7.get(0)).field_150844_d > 60L)
        {
            var7.remove(0);
        }

        if (this.field_150113_a)
        {
            if (var6)
            {
                worldIn.setBlock(x, y, z, Blocks.unlit_redstone_torch, worldIn.getBlockMetadata(x, y, z), 3);

                if (this.func_150111_a(worldIn, x, y, z, true))
                {
                    worldIn.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                    for (int var8 = 0; var8 < 5; ++var8)
                    {
                        double var9 = (double)x + random.nextDouble() * 0.6D + 0.2D;
                        double var11 = (double)y + random.nextDouble() * 0.6D + 0.2D;
                        double var13 = (double)z + random.nextDouble() * 0.6D + 0.2D;
                        worldIn.spawnParticle("smoke", var9, var11, var13, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }
        else if (!var6 && !this.func_150111_a(worldIn, x, y, z, false))
        {
            worldIn.setBlock(x, y, z, Blocks.redstone_torch, worldIn.getBlockMetadata(x, y, z), 3);
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!this.func_150108_b(worldIn, x, y, z, neighbor))
        {
            boolean var6 = this.func_150110_m(worldIn, x, y, z);

            if (this.field_150113_a && var6 || !this.field_150113_a && !var6)
            {
                worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
            }
        }
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return side == 0 ? this.isProvidingWeakPower(worldIn, x, y, z, side) : 0;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }

    public boolean canProvidePower()
    {
        return true;
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        if (this.field_150113_a)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);
            double var7 = (double)((float)x + 0.5F) + (double)(random.nextFloat() - 0.5F) * 0.2D;
            double var9 = (double)((float)y + 0.7F) + (double)(random.nextFloat() - 0.5F) * 0.2D;
            double var11 = (double)((float)z + 0.5F) + (double)(random.nextFloat() - 0.5F) * 0.2D;
            double var13 = 0.2199999988079071D;
            double var15 = 0.27000001072883606D;

            if (var6 == 1)
            {
                worldIn.spawnParticle("reddust", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 2)
            {
                worldIn.spawnParticle("reddust", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 3)
            {
                worldIn.spawnParticle("reddust", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 4)
            {
                worldIn.spawnParticle("reddust", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
            }
            else
            {
                worldIn.spawnParticle("reddust", var7, var9, var11, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Item.getItemFromBlock(Blocks.redstone_torch);
    }

    public boolean isAssociatedBlock(Block other)
    {
        return other == Blocks.unlit_redstone_torch || other == Blocks.redstone_torch;
    }

    static class Toggle
    {
        int field_150847_a;
        int field_150845_b;
        int field_150846_c;
        long field_150844_d;
        private static final String __OBFID = "CL_00000299";

        public Toggle(int p_i45422_1_, int p_i45422_2_, int p_i45422_3_, long p_i45422_4_)
        {
            this.field_150847_a = p_i45422_1_;
            this.field_150845_b = p_i45422_2_;
            this.field_150846_c = p_i45422_3_;
            this.field_150844_d = p_i45422_4_;
        }
    }
}
