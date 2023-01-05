package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockRedstoneOre extends Block
{
    private boolean field_150187_a;
    private static final String __OBFID = "CL_00000294";

    public BlockRedstoneOre(boolean p_i45420_1_)
    {
        super(Material.rock);

        if (p_i45420_1_)
        {
            this.setTickRandomly(true);
        }

        this.field_150187_a = p_i45420_1_;
    }

    public int tickRate(World worldIn)
    {
        return 30;
    }

    public void onBlockClicked(World worldIn, int x, int y, int z, EntityPlayer player)
    {
        this.func_150185_e(worldIn, x, y, z);
        super.onBlockClicked(worldIn, x, y, z, player);
    }

    public void onEntityWalking(World worldIn, int x, int y, int z, Entity entityIn)
    {
        this.func_150185_e(worldIn, x, y, z);
        super.onEntityWalking(worldIn, x, y, z, entityIn);
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        this.func_150185_e(worldIn, x, y, z);
        return super.onBlockActivated(worldIn, x, y, z, player, side, subX, subY, subZ);
    }

    private void func_150185_e(World p_150185_1_, int p_150185_2_, int p_150185_3_, int p_150185_4_)
    {
        this.func_150186_m(p_150185_1_, p_150185_2_, p_150185_3_, p_150185_4_);

        if (this == Blocks.redstone_ore)
        {
            p_150185_1_.setBlock(p_150185_2_, p_150185_3_, p_150185_4_, Blocks.lit_redstone_ore);
        }
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (this == Blocks.lit_redstone_ore)
        {
            worldIn.setBlock(x, y, z, Blocks.redstone_ore);
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.redstone;
    }

    public int quantityDroppedWithBonus(int maxBonus, Random random)
    {
        return this.quantityDropped(random) + random.nextInt(maxBonus + 1);
    }

    public int quantityDropped(Random random)
    {
        return 4 + random.nextInt(2);
    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, x, y, z, meta, chance, fortune);

        if (this.getItemDropped(meta, worldIn.rand, fortune) != Item.getItemFromBlock(this))
        {
            int var8 = 1 + worldIn.rand.nextInt(5);
            this.dropXpOnBlockBreak(worldIn, x, y, z, var8);
        }
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        if (this.field_150187_a)
        {
            this.func_150186_m(worldIn, x, y, z);
        }
    }

    private void func_150186_m(World p_150186_1_, int p_150186_2_, int p_150186_3_, int p_150186_4_)
    {
        Random var5 = p_150186_1_.rand;
        double var6 = 0.0625D;

        for (int var8 = 0; var8 < 6; ++var8)
        {
            double var9 = (double)((float)p_150186_2_ + var5.nextFloat());
            double var11 = (double)((float)p_150186_3_ + var5.nextFloat());
            double var13 = (double)((float)p_150186_4_ + var5.nextFloat());

            if (var8 == 0 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_ + 1, p_150186_4_).isOpaqueCube())
            {
                var11 = (double)(p_150186_3_ + 1) + var6;
            }

            if (var8 == 1 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_ - 1, p_150186_4_).isOpaqueCube())
            {
                var11 = (double)(p_150186_3_ + 0) - var6;
            }

            if (var8 == 2 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_, p_150186_4_ + 1).isOpaqueCube())
            {
                var13 = (double)(p_150186_4_ + 1) + var6;
            }

            if (var8 == 3 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_, p_150186_4_ - 1).isOpaqueCube())
            {
                var13 = (double)(p_150186_4_ + 0) - var6;
            }

            if (var8 == 4 && !p_150186_1_.getBlock(p_150186_2_ + 1, p_150186_3_, p_150186_4_).isOpaqueCube())
            {
                var9 = (double)(p_150186_2_ + 1) + var6;
            }

            if (var8 == 5 && !p_150186_1_.getBlock(p_150186_2_ - 1, p_150186_3_, p_150186_4_).isOpaqueCube())
            {
                var9 = (double)(p_150186_2_ + 0) - var6;
            }

            if (var9 < (double)p_150186_2_ || var9 > (double)(p_150186_2_ + 1) || var11 < 0.0D || var11 > (double)(p_150186_3_ + 1) || var13 < (double)p_150186_4_ || var13 > (double)(p_150186_4_ + 1))
            {
                p_150186_1_.spawnParticle("reddust", var9, var11, var13, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(Blocks.redstone_ore);
    }
}
