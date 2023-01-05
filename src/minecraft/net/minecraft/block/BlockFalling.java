package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockFalling extends Block
{
    public static boolean fallInstantly;
    private static final String __OBFID = "CL_00000240";

    public BlockFalling()
    {
        super(Material.sand);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public BlockFalling(Material p_i45405_1_)
    {
        super(p_i45405_1_);
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
        if (!worldIn.isRemote)
        {
            this.func_149830_m(worldIn, x, y, z);
        }
    }

    private void func_149830_m(World p_149830_1_, int p_149830_2_, int p_149830_3_, int p_149830_4_)
    {
        if (canFallBelow(p_149830_1_, p_149830_2_, p_149830_3_ - 1, p_149830_4_) && p_149830_3_ >= 0)
        {
            byte var8 = 32;

            if (!fallInstantly && p_149830_1_.checkChunksExist(p_149830_2_ - var8, p_149830_3_ - var8, p_149830_4_ - var8, p_149830_2_ + var8, p_149830_3_ + var8, p_149830_4_ + var8))
            {
                if (!p_149830_1_.isRemote)
                {
                    EntityFallingBlock var9 = new EntityFallingBlock(p_149830_1_, (double)((float)p_149830_2_ + 0.5F), (double)((float)p_149830_3_ + 0.5F), (double)((float)p_149830_4_ + 0.5F), this, p_149830_1_.getBlockMetadata(p_149830_2_, p_149830_3_, p_149830_4_));
                    this.onStartFalling(var9);
                    p_149830_1_.spawnEntityInWorld(var9);
                }
            }
            else
            {
                p_149830_1_.setBlockToAir(p_149830_2_, p_149830_3_, p_149830_4_);

                while (canFallBelow(p_149830_1_, p_149830_2_, p_149830_3_ - 1, p_149830_4_) && p_149830_3_ > 0)
                {
                    --p_149830_3_;
                }

                if (p_149830_3_ > 0)
                {
                    p_149830_1_.setBlock(p_149830_2_, p_149830_3_, p_149830_4_, this);
                }
            }
        }
    }

    protected void onStartFalling(EntityFallingBlock p_149829_1_) {}

    public int tickRate(World worldIn)
    {
        return 2;
    }

    public static boolean canFallBelow(World p_149831_0_, int p_149831_1_, int p_149831_2_, int p_149831_3_)
    {
        Block var4 = p_149831_0_.getBlock(p_149831_1_, p_149831_2_, p_149831_3_);

        if (var4.blockMaterial == Material.air)
        {
            return true;
        }
        else if (var4 == Blocks.fire)
        {
            return true;
        }
        else
        {
            Material var5 = var4.blockMaterial;
            return var5 == Material.water ? true : var5 == Material.lava;
        }
    }

    public void playSoundWhenFallen(World p_149828_1_, int p_149828_2_, int p_149828_3_, int p_149828_4_, int p_149828_5_) {}
}
