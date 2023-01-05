package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRedstoneDiode extends BlockDirectional
{
    protected final boolean isRepeaterPowered;
    private static final String __OBFID = "CL_00000226";

    protected BlockRedstoneDiode(boolean p_i45400_1_)
    {
        super(Material.circuits);
        this.isRepeaterPowered = p_i45400_1_;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return !World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) ? false : super.canPlaceBlockAt(worldIn, x, y, z);
    }

    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        return !World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) ? false : super.canBlockStay(worldIn, x, y, z);
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);

        if (!this.func_149910_g(worldIn, x, y, z, var6))
        {
            boolean var7 = this.isGettingInput(worldIn, x, y, z, var6);

            if (this.isRepeaterPowered && !var7)
            {
                worldIn.setBlock(x, y, z, this.getBlockUnpowered(), var6, 2);
            }
            else if (!this.isRepeaterPowered)
            {
                worldIn.setBlock(x, y, z, this.getBlockPowered(), var6, 2);

                if (!var7)
                {
                    worldIn.scheduleBlockUpdateWithPriority(x, y, z, this.getBlockPowered(), this.func_149899_k(var6), -1);
                }
            }
        }
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 0 ? (this.isRepeaterPowered ? Blocks.redstone_torch.getBlockTextureFromSide(side) : Blocks.unlit_redstone_torch.getBlockTextureFromSide(side)) : (side == 1 ? this.blockIcon : Blocks.double_stone_slab.getBlockTextureFromSide(1));
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return side != 0 && side != 1;
    }

    public int getRenderType()
    {
        return 36;
    }

    protected boolean func_149905_c(int p_149905_1_)
    {
        return this.isRepeaterPowered;
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return this.isProvidingWeakPower(worldIn, x, y, z, side);
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);

        if (!this.func_149905_c(var6))
        {
            return 0;
        }
        else
        {
            int var7 = getDirection(var6);
            return var7 == 0 && side == 3 ? this.func_149904_f(worldIn, x, y, z, var6) : (var7 == 1 && side == 4 ? this.func_149904_f(worldIn, x, y, z, var6) : (var7 == 2 && side == 2 ? this.func_149904_f(worldIn, x, y, z, var6) : (var7 == 3 && side == 5 ? this.func_149904_f(worldIn, x, y, z, var6) : 0)));
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!this.canBlockStay(worldIn, x, y, z))
        {
            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlockToAir(x, y, z);
            worldIn.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            worldIn.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            worldIn.notifyBlocksOfNeighborChange(x, y, z + 1, this);
            worldIn.notifyBlocksOfNeighborChange(x, y, z - 1, this);
            worldIn.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            worldIn.notifyBlocksOfNeighborChange(x, y + 1, z, this);
        }
        else
        {
            this.func_149897_b(worldIn, x, y, z, neighbor);
        }
    }

    protected void func_149897_b(World p_149897_1_, int p_149897_2_, int p_149897_3_, int p_149897_4_, Block p_149897_5_)
    {
        int var6 = p_149897_1_.getBlockMetadata(p_149897_2_, p_149897_3_, p_149897_4_);

        if (!this.func_149910_g(p_149897_1_, p_149897_2_, p_149897_3_, p_149897_4_, var6))
        {
            boolean var7 = this.isGettingInput(p_149897_1_, p_149897_2_, p_149897_3_, p_149897_4_, var6);

            if ((this.isRepeaterPowered && !var7 || !this.isRepeaterPowered && var7) && !p_149897_1_.isBlockTickScheduledThisTick(p_149897_2_, p_149897_3_, p_149897_4_, this))
            {
                byte var8 = -1;

                if (this.func_149912_i(p_149897_1_, p_149897_2_, p_149897_3_, p_149897_4_, var6))
                {
                    var8 = -3;
                }
                else if (this.isRepeaterPowered)
                {
                    var8 = -2;
                }

                p_149897_1_.scheduleBlockUpdateWithPriority(p_149897_2_, p_149897_3_, p_149897_4_, this, this.func_149901_b(var6), var8);
            }
        }
    }

    public boolean func_149910_g(IBlockAccess p_149910_1_, int p_149910_2_, int p_149910_3_, int p_149910_4_, int p_149910_5_)
    {
        return false;
    }

    protected boolean isGettingInput(World p_149900_1_, int p_149900_2_, int p_149900_3_, int p_149900_4_, int p_149900_5_)
    {
        return this.getInputStrength(p_149900_1_, p_149900_2_, p_149900_3_, p_149900_4_, p_149900_5_) > 0;
    }

    protected int getInputStrength(World p_149903_1_, int p_149903_2_, int p_149903_3_, int p_149903_4_, int p_149903_5_)
    {
        int var6 = getDirection(p_149903_5_);
        int var7 = p_149903_2_ + Direction.offsetX[var6];
        int var8 = p_149903_4_ + Direction.offsetZ[var6];
        int var9 = p_149903_1_.getIndirectPowerLevelTo(var7, p_149903_3_, var8, Direction.directionToFacing[var6]);
        return var9 >= 15 ? var9 : Math.max(var9, p_149903_1_.getBlock(var7, p_149903_3_, var8) == Blocks.redstone_wire ? p_149903_1_.getBlockMetadata(var7, p_149903_3_, var8) : 0);
    }

    protected int func_149902_h(IBlockAccess p_149902_1_, int p_149902_2_, int p_149902_3_, int p_149902_4_, int p_149902_5_)
    {
        int var6 = getDirection(p_149902_5_);

        switch (var6)
        {
            case 0:
            case 2:
                return Math.max(this.func_149913_i(p_149902_1_, p_149902_2_ - 1, p_149902_3_, p_149902_4_, 4), this.func_149913_i(p_149902_1_, p_149902_2_ + 1, p_149902_3_, p_149902_4_, 5));

            case 1:
            case 3:
                return Math.max(this.func_149913_i(p_149902_1_, p_149902_2_, p_149902_3_, p_149902_4_ + 1, 3), this.func_149913_i(p_149902_1_, p_149902_2_, p_149902_3_, p_149902_4_ - 1, 2));

            default:
                return 0;
        }
    }

    protected int func_149913_i(IBlockAccess p_149913_1_, int p_149913_2_, int p_149913_3_, int p_149913_4_, int p_149913_5_)
    {
        Block var6 = p_149913_1_.getBlock(p_149913_2_, p_149913_3_, p_149913_4_);
        return this.func_149908_a(var6) ? (var6 == Blocks.redstone_wire ? p_149913_1_.getBlockMetadata(p_149913_2_, p_149913_3_, p_149913_4_) : p_149913_1_.isBlockProvidingPowerTo(p_149913_2_, p_149913_3_, p_149913_4_, p_149913_5_)) : 0;
    }

    public boolean canProvidePower()
    {
        return true;
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int var7 = ((MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;
        worldIn.setBlockMetadataWithNotify(x, y, z, var7, 3);
        boolean var8 = this.isGettingInput(worldIn, x, y, z, var7);

        if (var8)
        {
            worldIn.scheduleBlockUpdate(x, y, z, this, 1);
        }
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        this.func_149911_e(worldIn, x, y, z);
    }

    protected void func_149911_e(World p_149911_1_, int p_149911_2_, int p_149911_3_, int p_149911_4_)
    {
        int var5 = getDirection(p_149911_1_.getBlockMetadata(p_149911_2_, p_149911_3_, p_149911_4_));

        if (var5 == 1)
        {
            p_149911_1_.notifyBlockOfNeighborChange(p_149911_2_ + 1, p_149911_3_, p_149911_4_, this);
            p_149911_1_.notifyBlocksOfNeighborChange(p_149911_2_ + 1, p_149911_3_, p_149911_4_, this, 4);
        }

        if (var5 == 3)
        {
            p_149911_1_.notifyBlockOfNeighborChange(p_149911_2_ - 1, p_149911_3_, p_149911_4_, this);
            p_149911_1_.notifyBlocksOfNeighborChange(p_149911_2_ - 1, p_149911_3_, p_149911_4_, this, 5);
        }

        if (var5 == 2)
        {
            p_149911_1_.notifyBlockOfNeighborChange(p_149911_2_, p_149911_3_, p_149911_4_ + 1, this);
            p_149911_1_.notifyBlocksOfNeighborChange(p_149911_2_, p_149911_3_, p_149911_4_ + 1, this, 2);
        }

        if (var5 == 0)
        {
            p_149911_1_.notifyBlockOfNeighborChange(p_149911_2_, p_149911_3_, p_149911_4_ - 1, this);
            p_149911_1_.notifyBlocksOfNeighborChange(p_149911_2_, p_149911_3_, p_149911_4_ - 1, this, 3);
        }
    }

    public void onBlockDestroyedByPlayer(World worldIn, int x, int y, int z, int meta)
    {
        if (this.isRepeaterPowered)
        {
            worldIn.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            worldIn.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            worldIn.notifyBlocksOfNeighborChange(x, y, z + 1, this);
            worldIn.notifyBlocksOfNeighborChange(x, y, z - 1, this);
            worldIn.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            worldIn.notifyBlocksOfNeighborChange(x, y + 1, z, this);
        }

        super.onBlockDestroyedByPlayer(worldIn, x, y, z, meta);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    protected boolean func_149908_a(Block p_149908_1_)
    {
        return p_149908_1_.canProvidePower();
    }

    protected int func_149904_f(IBlockAccess p_149904_1_, int p_149904_2_, int p_149904_3_, int p_149904_4_, int p_149904_5_)
    {
        return 15;
    }

    public static boolean isRedstoneRepeaterBlockID(Block p_149909_0_)
    {
        return Blocks.unpowered_repeater.func_149907_e(p_149909_0_) || Blocks.unpowered_comparator.func_149907_e(p_149909_0_);
    }

    public boolean func_149907_e(Block p_149907_1_)
    {
        return p_149907_1_ == this.getBlockPowered() || p_149907_1_ == this.getBlockUnpowered();
    }

    public boolean func_149912_i(World p_149912_1_, int p_149912_2_, int p_149912_3_, int p_149912_4_, int p_149912_5_)
    {
        int var6 = getDirection(p_149912_5_);

        if (isRedstoneRepeaterBlockID(p_149912_1_.getBlock(p_149912_2_ - Direction.offsetX[var6], p_149912_3_, p_149912_4_ - Direction.offsetZ[var6])))
        {
            int var7 = p_149912_1_.getBlockMetadata(p_149912_2_ - Direction.offsetX[var6], p_149912_3_, p_149912_4_ - Direction.offsetZ[var6]);
            int var8 = getDirection(var7);
            return var8 != var6;
        }
        else
        {
            return false;
        }
    }

    protected int func_149899_k(int p_149899_1_)
    {
        return this.func_149901_b(p_149899_1_);
    }

    protected abstract int func_149901_b(int p_149901_1_);

    protected abstract BlockRedstoneDiode getBlockPowered();

    protected abstract BlockRedstoneDiode getBlockUnpowered();

    public boolean isAssociatedBlock(Block other)
    {
        return this.func_149907_e(other);
    }
}
