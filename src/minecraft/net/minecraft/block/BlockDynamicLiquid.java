package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockDynamicLiquid extends BlockLiquid
{
    int field_149815_a;
    boolean[] field_149814_b = new boolean[4];
    int[] field_149816_M = new int[4];
    private static final String __OBFID = "CL_00000234";

    protected BlockDynamicLiquid(Material p_i45403_1_)
    {
        super(p_i45403_1_);
    }

    private void func_149811_n(World p_149811_1_, int p_149811_2_, int p_149811_3_, int p_149811_4_)
    {
        int var5 = p_149811_1_.getBlockMetadata(p_149811_2_, p_149811_3_, p_149811_4_);
        p_149811_1_.setBlock(p_149811_2_, p_149811_3_, p_149811_4_, Block.getBlockById(Block.getIdFromBlock(this) + 1), var5, 2);
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        int var6 = this.func_149804_e(worldIn, x, y, z);
        byte var7 = 1;

        if (this.blockMaterial == Material.lava && !worldIn.provider.isHellWorld)
        {
            var7 = 2;
        }

        boolean var8 = true;
        int var9 = this.tickRate(worldIn);
        int var11;

        if (var6 > 0)
        {
            byte var10 = -100;
            this.field_149815_a = 0;
            int var13 = this.func_149810_a(worldIn, x - 1, y, z, var10);
            var13 = this.func_149810_a(worldIn, x + 1, y, z, var13);
            var13 = this.func_149810_a(worldIn, x, y, z - 1, var13);
            var13 = this.func_149810_a(worldIn, x, y, z + 1, var13);
            var11 = var13 + var7;

            if (var11 >= 8 || var13 < 0)
            {
                var11 = -1;
            }

            if (this.func_149804_e(worldIn, x, y + 1, z) >= 0)
            {
                int var12 = this.func_149804_e(worldIn, x, y + 1, z);

                if (var12 >= 8)
                {
                    var11 = var12;
                }
                else
                {
                    var11 = var12 + 8;
                }
            }

            if (this.field_149815_a >= 2 && this.blockMaterial == Material.water)
            {
                if (worldIn.getBlock(x, y - 1, z).getMaterial().isSolid())
                {
                    var11 = 0;
                }
                else if (worldIn.getBlock(x, y - 1, z).getMaterial() == this.blockMaterial && worldIn.getBlockMetadata(x, y - 1, z) == 0)
                {
                    var11 = 0;
                }
            }

            if (this.blockMaterial == Material.lava && var6 < 8 && var11 < 8 && var11 > var6 && random.nextInt(4) != 0)
            {
                var9 *= 4;
            }

            if (var11 == var6)
            {
                if (var8)
                {
                    this.func_149811_n(worldIn, x, y, z);
                }
            }
            else
            {
                var6 = var11;

                if (var11 < 0)
                {
                    worldIn.setBlockToAir(x, y, z);
                }
                else
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z, var11, 2);
                    worldIn.scheduleBlockUpdate(x, y, z, this, var9);
                    worldIn.notifyBlocksOfNeighborChange(x, y, z, this);
                }
            }
        }
        else
        {
            this.func_149811_n(worldIn, x, y, z);
        }

        if (this.func_149809_q(worldIn, x, y - 1, z))
        {
            if (this.blockMaterial == Material.lava && worldIn.getBlock(x, y - 1, z).getMaterial() == Material.water)
            {
                worldIn.setBlock(x, y - 1, z, Blocks.stone);
                this.func_149799_m(worldIn, x, y - 1, z);
                return;
            }

            if (var6 >= 8)
            {
                this.func_149813_h(worldIn, x, y - 1, z, var6);
            }
            else
            {
                this.func_149813_h(worldIn, x, y - 1, z, var6 + 8);
            }
        }
        else if (var6 >= 0 && (var6 == 0 || this.func_149807_p(worldIn, x, y - 1, z)))
        {
            boolean[] var14 = this.func_149808_o(worldIn, x, y, z);
            var11 = var6 + var7;

            if (var6 >= 8)
            {
                var11 = 1;
            }

            if (var11 >= 8)
            {
                return;
            }

            if (var14[0])
            {
                this.func_149813_h(worldIn, x - 1, y, z, var11);
            }

            if (var14[1])
            {
                this.func_149813_h(worldIn, x + 1, y, z, var11);
            }

            if (var14[2])
            {
                this.func_149813_h(worldIn, x, y, z - 1, var11);
            }

            if (var14[3])
            {
                this.func_149813_h(worldIn, x, y, z + 1, var11);
            }
        }
    }

    private void func_149813_h(World p_149813_1_, int p_149813_2_, int p_149813_3_, int p_149813_4_, int p_149813_5_)
    {
        if (this.func_149809_q(p_149813_1_, p_149813_2_, p_149813_3_, p_149813_4_))
        {
            Block var6 = p_149813_1_.getBlock(p_149813_2_, p_149813_3_, p_149813_4_);

            if (this.blockMaterial == Material.lava)
            {
                this.func_149799_m(p_149813_1_, p_149813_2_, p_149813_3_, p_149813_4_);
            }
            else
            {
                var6.dropBlockAsItem(p_149813_1_, p_149813_2_, p_149813_3_, p_149813_4_, p_149813_1_.getBlockMetadata(p_149813_2_, p_149813_3_, p_149813_4_), 0);
            }

            p_149813_1_.setBlock(p_149813_2_, p_149813_3_, p_149813_4_, this, p_149813_5_, 3);
        }
    }

    private int func_149812_c(World p_149812_1_, int p_149812_2_, int p_149812_3_, int p_149812_4_, int p_149812_5_, int p_149812_6_)
    {
        int var7 = 1000;

        for (int var8 = 0; var8 < 4; ++var8)
        {
            if ((var8 != 0 || p_149812_6_ != 1) && (var8 != 1 || p_149812_6_ != 0) && (var8 != 2 || p_149812_6_ != 3) && (var8 != 3 || p_149812_6_ != 2))
            {
                int var9 = p_149812_2_;
                int var11 = p_149812_4_;

                if (var8 == 0)
                {
                    var9 = p_149812_2_ - 1;
                }

                if (var8 == 1)
                {
                    ++var9;
                }

                if (var8 == 2)
                {
                    var11 = p_149812_4_ - 1;
                }

                if (var8 == 3)
                {
                    ++var11;
                }

                if (!this.func_149807_p(p_149812_1_, var9, p_149812_3_, var11) && (p_149812_1_.getBlock(var9, p_149812_3_, var11).getMaterial() != this.blockMaterial || p_149812_1_.getBlockMetadata(var9, p_149812_3_, var11) != 0))
                {
                    if (!this.func_149807_p(p_149812_1_, var9, p_149812_3_ - 1, var11))
                    {
                        return p_149812_5_;
                    }

                    if (p_149812_5_ < 4)
                    {
                        int var12 = this.func_149812_c(p_149812_1_, var9, p_149812_3_, var11, p_149812_5_ + 1, var8);

                        if (var12 < var7)
                        {
                            var7 = var12;
                        }
                    }
                }
            }
        }

        return var7;
    }

    private boolean[] func_149808_o(World p_149808_1_, int p_149808_2_, int p_149808_3_, int p_149808_4_)
    {
        int var5;
        int var6;

        for (var5 = 0; var5 < 4; ++var5)
        {
            this.field_149816_M[var5] = 1000;
            var6 = p_149808_2_;
            int var8 = p_149808_4_;

            if (var5 == 0)
            {
                var6 = p_149808_2_ - 1;
            }

            if (var5 == 1)
            {
                ++var6;
            }

            if (var5 == 2)
            {
                var8 = p_149808_4_ - 1;
            }

            if (var5 == 3)
            {
                ++var8;
            }

            if (!this.func_149807_p(p_149808_1_, var6, p_149808_3_, var8) && (p_149808_1_.getBlock(var6, p_149808_3_, var8).getMaterial() != this.blockMaterial || p_149808_1_.getBlockMetadata(var6, p_149808_3_, var8) != 0))
            {
                if (this.func_149807_p(p_149808_1_, var6, p_149808_3_ - 1, var8))
                {
                    this.field_149816_M[var5] = this.func_149812_c(p_149808_1_, var6, p_149808_3_, var8, 1, var5);
                }
                else
                {
                    this.field_149816_M[var5] = 0;
                }
            }
        }

        var5 = this.field_149816_M[0];

        for (var6 = 1; var6 < 4; ++var6)
        {
            if (this.field_149816_M[var6] < var5)
            {
                var5 = this.field_149816_M[var6];
            }
        }

        for (var6 = 0; var6 < 4; ++var6)
        {
            this.field_149814_b[var6] = this.field_149816_M[var6] == var5;
        }

        return this.field_149814_b;
    }

    private boolean func_149807_p(World p_149807_1_, int p_149807_2_, int p_149807_3_, int p_149807_4_)
    {
        Block var5 = p_149807_1_.getBlock(p_149807_2_, p_149807_3_, p_149807_4_);
        return var5 != Blocks.wooden_door && var5 != Blocks.iron_door && var5 != Blocks.standing_sign && var5 != Blocks.ladder && var5 != Blocks.reeds ? (var5.blockMaterial == Material.portal ? true : var5.blockMaterial.blocksMovement()) : true;
    }

    protected int func_149810_a(World p_149810_1_, int p_149810_2_, int p_149810_3_, int p_149810_4_, int p_149810_5_)
    {
        int var6 = this.func_149804_e(p_149810_1_, p_149810_2_, p_149810_3_, p_149810_4_);

        if (var6 < 0)
        {
            return p_149810_5_;
        }
        else
        {
            if (var6 == 0)
            {
                ++this.field_149815_a;
            }

            if (var6 >= 8)
            {
                var6 = 0;
            }

            return p_149810_5_ >= 0 && var6 >= p_149810_5_ ? p_149810_5_ : var6;
        }
    }

    private boolean func_149809_q(World p_149809_1_, int p_149809_2_, int p_149809_3_, int p_149809_4_)
    {
        Material var5 = p_149809_1_.getBlock(p_149809_2_, p_149809_3_, p_149809_4_).getMaterial();
        return var5 == this.blockMaterial ? false : (var5 == Material.lava ? false : !this.func_149807_p(p_149809_1_, p_149809_2_, p_149809_3_, p_149809_4_));
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);

        if (worldIn.getBlock(x, y, z) == this)
        {
            worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
        }
    }

    public boolean requiresUpdates()
    {
        return true;
    }
}
