package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWire extends Block
{
    private static final String __OBFID = "CL_00000328";

    public BlockTripWire()
    {
        super(Material.circuits);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
        this.setTickRandomly(true);
    }

    public int tickRate(World worldIn)
    {
        return 10;
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

    public int getRenderBlockPass()
    {
        return 1;
    }

    public int getRenderType()
    {
        return 30;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.string;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.string;
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);
        boolean var7 = (var6 & 2) == 2;
        boolean var8 = !World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z);

        if (var7 != var8)
        {
            this.dropBlockAsItem(worldIn, x, y, z, var6, 0);
            worldIn.setBlockToAir(x, y, z);
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        boolean var6 = (var5 & 4) == 4;
        boolean var7 = (var5 & 2) == 2;

        if (!var7)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
        }
        else if (!var6)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
        }
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        int var5 = World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) ? 0 : 2;
        worldIn.setBlockMetadataWithNotify(x, y, z, var5, 3);
        this.func_150138_a(worldIn, x, y, z, var5);
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        this.func_150138_a(worldIn, x, y, z, meta | 1);
    }

    public void onBlockHarvested(World worldIn, int x, int y, int z, int meta, EntityPlayer player)
    {
        if (!worldIn.isRemote)
        {
            if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, meta | 8, 4);
            }
        }
    }

    private void func_150138_a(World p_150138_1_, int p_150138_2_, int p_150138_3_, int p_150138_4_, int p_150138_5_)
    {
        int var6 = 0;

        while (var6 < 2)
        {
            int var7 = 1;

            while (true)
            {
                if (var7 < 42)
                {
                    int var8 = p_150138_2_ + Direction.offsetX[var6] * var7;
                    int var9 = p_150138_4_ + Direction.offsetZ[var6] * var7;
                    Block var10 = p_150138_1_.getBlock(var8, p_150138_3_, var9);

                    if (var10 == Blocks.tripwire_hook)
                    {
                        int var11 = p_150138_1_.getBlockMetadata(var8, p_150138_3_, var9) & 3;

                        if (var11 == Direction.rotateOpposite[var6])
                        {
                            Blocks.tripwire_hook.func_150136_a(p_150138_1_, var8, p_150138_3_, var9, false, p_150138_1_.getBlockMetadata(var8, p_150138_3_, var9), true, var7, p_150138_5_);
                        }
                    }
                    else if (var10 == Blocks.tripwire)
                    {
                        ++var7;
                        continue;
                    }
                }

                ++var6;
                break;
            }
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if ((worldIn.getBlockMetadata(x, y, z) & 1) != 1)
            {
                this.func_150140_e(worldIn, x, y, z);
            }
        }
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isRemote)
        {
            if ((worldIn.getBlockMetadata(x, y, z) & 1) == 1)
            {
                this.func_150140_e(worldIn, x, y, z);
            }
        }
    }

    private void func_150140_e(World p_150140_1_, int p_150140_2_, int p_150140_3_, int p_150140_4_)
    {
        int var5 = p_150140_1_.getBlockMetadata(p_150140_2_, p_150140_3_, p_150140_4_);
        boolean var6 = (var5 & 1) == 1;
        boolean var7 = false;
        List var8 = p_150140_1_.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBox((double)p_150140_2_ + this.minX, (double)p_150140_3_ + this.minY, (double)p_150140_4_ + this.minZ, (double)p_150140_2_ + this.maxX, (double)p_150140_3_ + this.maxY, (double)p_150140_4_ + this.maxZ));

        if (!var8.isEmpty())
        {
            Iterator var9 = var8.iterator();

            while (var9.hasNext())
            {
                Entity var10 = (Entity)var9.next();

                if (!var10.doesEntityNotTriggerPressurePlate())
                {
                    var7 = true;
                    break;
                }
            }
        }

        if (var7 && !var6)
        {
            var5 |= 1;
        }

        if (!var7 && var6)
        {
            var5 &= -2;
        }

        if (var7 != var6)
        {
            p_150140_1_.setBlockMetadataWithNotify(p_150140_2_, p_150140_3_, p_150140_4_, var5, 3);
            this.func_150138_a(p_150140_1_, p_150140_2_, p_150140_3_, p_150140_4_, var5);
        }

        if (var7)
        {
            p_150140_1_.scheduleBlockUpdate(p_150140_2_, p_150140_3_, p_150140_4_, this, this.tickRate(p_150140_1_));
        }
    }

    public static boolean func_150139_a(IBlockAccess p_150139_0_, int p_150139_1_, int p_150139_2_, int p_150139_3_, int p_150139_4_, int p_150139_5_)
    {
        int var6 = p_150139_1_ + Direction.offsetX[p_150139_5_];
        int var8 = p_150139_3_ + Direction.offsetZ[p_150139_5_];
        Block var9 = p_150139_0_.getBlock(var6, p_150139_2_, var8);
        boolean var10 = (p_150139_4_ & 2) == 2;
        int var11;

        if (var9 == Blocks.tripwire_hook)
        {
            var11 = p_150139_0_.getBlockMetadata(var6, p_150139_2_, var8);
            int var13 = var11 & 3;
            return var13 == Direction.rotateOpposite[p_150139_5_];
        }
        else if (var9 == Blocks.tripwire)
        {
            var11 = p_150139_0_.getBlockMetadata(var6, p_150139_2_, var8);
            boolean var12 = (var11 & 2) == 2;
            return var10 == var12;
        }
        else
        {
            return false;
        }
    }
}
