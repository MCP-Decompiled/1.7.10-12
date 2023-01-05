package net.minecraft.block;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneRepeater extends BlockRedstoneDiode
{
    public static final double[] repeaterTorchOffset = new double[] { -0.0625D, 0.0625D, 0.1875D, 0.3125D};
    private static final int[] repeaterState = new int[] {1, 2, 3, 4};
    private static final String __OBFID = "CL_00000301";

    protected BlockRedstoneRepeater(boolean p_i45424_1_)
    {
        super(p_i45424_1_);
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        int var10 = worldIn.getBlockMetadata(x, y, z);
        int var11 = (var10 & 12) >> 2;
        var11 = var11 + 1 << 2 & 12;
        worldIn.setBlockMetadataWithNotify(x, y, z, var11 | var10 & 3, 3);
        return true;
    }

    protected int func_149901_b(int p_149901_1_)
    {
        return repeaterState[(p_149901_1_ & 12) >> 2] * 2;
    }

    protected BlockRedstoneDiode getBlockPowered()
    {
        return Blocks.powered_repeater;
    }

    protected BlockRedstoneDiode getBlockUnpowered()
    {
        return Blocks.unpowered_repeater;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.repeater;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.repeater;
    }

    public int getRenderType()
    {
        return 15;
    }

    public boolean func_149910_g(IBlockAccess p_149910_1_, int p_149910_2_, int p_149910_3_, int p_149910_4_, int p_149910_5_)
    {
        return this.func_149902_h(p_149910_1_, p_149910_2_, p_149910_3_, p_149910_4_, p_149910_5_) > 0;
    }

    protected boolean func_149908_a(Block p_149908_1_)
    {
        return isRedstoneRepeaterBlockID(p_149908_1_);
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        if (this.isRepeaterPowered)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);
            int var7 = getDirection(var6);
            double var8 = (double)((float)x + 0.5F) + (double)(random.nextFloat() - 0.5F) * 0.2D;
            double var10 = (double)((float)y + 0.4F) + (double)(random.nextFloat() - 0.5F) * 0.2D;
            double var12 = (double)((float)z + 0.5F) + (double)(random.nextFloat() - 0.5F) * 0.2D;
            double var14 = 0.0D;
            double var16 = 0.0D;

            if (random.nextInt(2) == 0)
            {
                switch (var7)
                {
                    case 0:
                        var16 = -0.3125D;
                        break;

                    case 1:
                        var14 = 0.3125D;
                        break;

                    case 2:
                        var16 = 0.3125D;
                        break;

                    case 3:
                        var14 = -0.3125D;
                }
            }
            else
            {
                int var18 = (var6 & 12) >> 2;

                switch (var7)
                {
                    case 0:
                        var16 = repeaterTorchOffset[var18];
                        break;

                    case 1:
                        var14 = -repeaterTorchOffset[var18];
                        break;

                    case 2:
                        var16 = -repeaterTorchOffset[var18];
                        break;

                    case 3:
                        var14 = repeaterTorchOffset[var18];
                }
            }

            worldIn.spawnParticle("reddust", var8 + var14, var10, var12 + var16, 0.0D, 0.0D, 0.0D);
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
        this.func_149911_e(worldIn, x, y, z);
    }
}
