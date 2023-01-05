package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockReed extends Block
{
    private static final String __OBFID = "CL_00000300";

    protected BlockReed()
    {
        super(Material.plants);
        float var1 = 0.375F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 1.0F, 0.5F + var1);
        this.setTickRandomly(true);
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (worldIn.getBlock(x, y - 1, z) == Blocks.reeds || this.func_150170_e(worldIn, x, y, z))
        {
            if (worldIn.isAirBlock(x, y + 1, z))
            {
                int var6;

                for (var6 = 1; worldIn.getBlock(x, y - var6, z) == this; ++var6)
                {
                    ;
                }

                if (var6 < 3)
                {
                    int var7 = worldIn.getBlockMetadata(x, y, z);

                    if (var7 == 15)
                    {
                        worldIn.setBlock(x, y + 1, z, this);
                        worldIn.setBlockMetadataWithNotify(x, y, z, 0, 4);
                    }
                    else
                    {
                        worldIn.setBlockMetadataWithNotify(x, y, z, var7 + 1, 4);
                    }
                }
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        Block var5 = worldIn.getBlock(x, y - 1, z);
        return var5 == this ? true : (var5 != Blocks.grass && var5 != Blocks.dirt && var5 != Blocks.sand ? false : (worldIn.getBlock(x - 1, y - 1, z).getMaterial() == Material.water ? true : (worldIn.getBlock(x + 1, y - 1, z).getMaterial() == Material.water ? true : (worldIn.getBlock(x, y - 1, z - 1).getMaterial() == Material.water ? true : worldIn.getBlock(x, y - 1, z + 1).getMaterial() == Material.water))));
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        this.func_150170_e(worldIn, x, y, z);
    }

    protected final boolean func_150170_e(World p_150170_1_, int p_150170_2_, int p_150170_3_, int p_150170_4_)
    {
        if (!this.canBlockStay(p_150170_1_, p_150170_2_, p_150170_3_, p_150170_4_))
        {
            this.dropBlockAsItem(p_150170_1_, p_150170_2_, p_150170_3_, p_150170_4_, p_150170_1_.getBlockMetadata(p_150170_2_, p_150170_3_, p_150170_4_), 0);
            p_150170_1_.setBlockToAir(p_150170_2_, p_150170_3_, p_150170_4_);
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        return this.canPlaceBlockAt(worldIn, x, y, z);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return null;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.reeds;
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
        return 1;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.reeds;
    }

    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        return worldIn.getBiomeGenForCoords(x, z).getBiomeGrassColor(x, y, z);
    }
}
