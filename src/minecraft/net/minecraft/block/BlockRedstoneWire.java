package net.minecraft.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneWire extends Block
{
    private boolean canProvidePower = true;
    private Set field_150179_b = new HashSet();
    private IIcon redstoneCrossIcon;
    private IIcon redstoneLineIcon;
    private IIcon redstoneCrossOverlayIcon;
    private IIcon redstoneLineOverlayIcon;
    private static final String __OBFID = "CL_00000295";

    public BlockRedstoneWire()
    {
        super(Material.circuits);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
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

    public int getRenderType()
    {
        return 5;
    }

    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        return 8388608;
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) || worldIn.getBlock(x, y - 1, z) == Blocks.glowstone;
    }

    private void func_150177_e(World p_150177_1_, int p_150177_2_, int p_150177_3_, int p_150177_4_)
    {
        this.func_150175_a(p_150177_1_, p_150177_2_, p_150177_3_, p_150177_4_, p_150177_2_, p_150177_3_, p_150177_4_);
        ArrayList var5 = new ArrayList(this.field_150179_b);
        this.field_150179_b.clear();

        for (int var6 = 0; var6 < var5.size(); ++var6)
        {
            ChunkPosition var7 = (ChunkPosition)var5.get(var6);
            p_150177_1_.notifyBlocksOfNeighborChange(var7.chunkPosX, var7.chunkPosY, var7.chunkPosZ, this);
        }
    }

    private void func_150175_a(World p_150175_1_, int p_150175_2_, int p_150175_3_, int p_150175_4_, int p_150175_5_, int p_150175_6_, int p_150175_7_)
    {
        int var8 = p_150175_1_.getBlockMetadata(p_150175_2_, p_150175_3_, p_150175_4_);
        byte var9 = 0;
        int var15 = this.func_150178_a(p_150175_1_, p_150175_5_, p_150175_6_, p_150175_7_, var9);
        this.canProvidePower = false;
        int var10 = p_150175_1_.getStrongestIndirectPower(p_150175_2_, p_150175_3_, p_150175_4_);
        this.canProvidePower = true;

        if (var10 > 0 && var10 > var15 - 1)
        {
            var15 = var10;
        }

        int var11 = 0;

        for (int var12 = 0; var12 < 4; ++var12)
        {
            int var13 = p_150175_2_;
            int var14 = p_150175_4_;

            if (var12 == 0)
            {
                var13 = p_150175_2_ - 1;
            }

            if (var12 == 1)
            {
                ++var13;
            }

            if (var12 == 2)
            {
                var14 = p_150175_4_ - 1;
            }

            if (var12 == 3)
            {
                ++var14;
            }

            if (var13 != p_150175_5_ || var14 != p_150175_7_)
            {
                var11 = this.func_150178_a(p_150175_1_, var13, p_150175_3_, var14, var11);
            }

            if (p_150175_1_.getBlock(var13, p_150175_3_, var14).isNormalCube() && !p_150175_1_.getBlock(p_150175_2_, p_150175_3_ + 1, p_150175_4_).isNormalCube())
            {
                if ((var13 != p_150175_5_ || var14 != p_150175_7_) && p_150175_3_ >= p_150175_6_)
                {
                    var11 = this.func_150178_a(p_150175_1_, var13, p_150175_3_ + 1, var14, var11);
                }
            }
            else if (!p_150175_1_.getBlock(var13, p_150175_3_, var14).isNormalCube() && (var13 != p_150175_5_ || var14 != p_150175_7_) && p_150175_3_ <= p_150175_6_)
            {
                var11 = this.func_150178_a(p_150175_1_, var13, p_150175_3_ - 1, var14, var11);
            }
        }

        if (var11 > var15)
        {
            var15 = var11 - 1;
        }
        else if (var15 > 0)
        {
            --var15;
        }
        else
        {
            var15 = 0;
        }

        if (var10 > var15 - 1)
        {
            var15 = var10;
        }

        if (var8 != var15)
        {
            p_150175_1_.setBlockMetadataWithNotify(p_150175_2_, p_150175_3_, p_150175_4_, var15, 2);
            this.field_150179_b.add(new ChunkPosition(p_150175_2_, p_150175_3_, p_150175_4_));
            this.field_150179_b.add(new ChunkPosition(p_150175_2_ - 1, p_150175_3_, p_150175_4_));
            this.field_150179_b.add(new ChunkPosition(p_150175_2_ + 1, p_150175_3_, p_150175_4_));
            this.field_150179_b.add(new ChunkPosition(p_150175_2_, p_150175_3_ - 1, p_150175_4_));
            this.field_150179_b.add(new ChunkPosition(p_150175_2_, p_150175_3_ + 1, p_150175_4_));
            this.field_150179_b.add(new ChunkPosition(p_150175_2_, p_150175_3_, p_150175_4_ - 1));
            this.field_150179_b.add(new ChunkPosition(p_150175_2_, p_150175_3_, p_150175_4_ + 1));
        }
    }

    private void func_150172_m(World p_150172_1_, int p_150172_2_, int p_150172_3_, int p_150172_4_)
    {
        if (p_150172_1_.getBlock(p_150172_2_, p_150172_3_, p_150172_4_) == this)
        {
            p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_, p_150172_3_, p_150172_4_, this);
            p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_ - 1, p_150172_3_, p_150172_4_, this);
            p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_ + 1, p_150172_3_, p_150172_4_, this);
            p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_, p_150172_3_, p_150172_4_ - 1, this);
            p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_, p_150172_3_, p_150172_4_ + 1, this);
            p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_, p_150172_3_ - 1, p_150172_4_, this);
            p_150172_1_.notifyBlocksOfNeighborChange(p_150172_2_, p_150172_3_ + 1, p_150172_4_, this);
        }
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);

        if (!worldIn.isRemote)
        {
            this.func_150177_e(worldIn, x, y, z);
            worldIn.notifyBlocksOfNeighborChange(x, y + 1, z, this);
            worldIn.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            this.func_150172_m(worldIn, x - 1, y, z);
            this.func_150172_m(worldIn, x + 1, y, z);
            this.func_150172_m(worldIn, x, y, z - 1);
            this.func_150172_m(worldIn, x, y, z + 1);

            if (worldIn.getBlock(x - 1, y, z).isNormalCube())
            {
                this.func_150172_m(worldIn, x - 1, y + 1, z);
            }
            else
            {
                this.func_150172_m(worldIn, x - 1, y - 1, z);
            }

            if (worldIn.getBlock(x + 1, y, z).isNormalCube())
            {
                this.func_150172_m(worldIn, x + 1, y + 1, z);
            }
            else
            {
                this.func_150172_m(worldIn, x + 1, y - 1, z);
            }

            if (worldIn.getBlock(x, y, z - 1).isNormalCube())
            {
                this.func_150172_m(worldIn, x, y + 1, z - 1);
            }
            else
            {
                this.func_150172_m(worldIn, x, y - 1, z - 1);
            }

            if (worldIn.getBlock(x, y, z + 1).isNormalCube())
            {
                this.func_150172_m(worldIn, x, y + 1, z + 1);
            }
            else
            {
                this.func_150172_m(worldIn, x, y - 1, z + 1);
            }
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);

        if (!worldIn.isRemote)
        {
            worldIn.notifyBlocksOfNeighborChange(x, y + 1, z, this);
            worldIn.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            worldIn.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            worldIn.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            worldIn.notifyBlocksOfNeighborChange(x, y, z + 1, this);
            worldIn.notifyBlocksOfNeighborChange(x, y, z - 1, this);
            this.func_150177_e(worldIn, x, y, z);
            this.func_150172_m(worldIn, x - 1, y, z);
            this.func_150172_m(worldIn, x + 1, y, z);
            this.func_150172_m(worldIn, x, y, z - 1);
            this.func_150172_m(worldIn, x, y, z + 1);

            if (worldIn.getBlock(x - 1, y, z).isNormalCube())
            {
                this.func_150172_m(worldIn, x - 1, y + 1, z);
            }
            else
            {
                this.func_150172_m(worldIn, x - 1, y - 1, z);
            }

            if (worldIn.getBlock(x + 1, y, z).isNormalCube())
            {
                this.func_150172_m(worldIn, x + 1, y + 1, z);
            }
            else
            {
                this.func_150172_m(worldIn, x + 1, y - 1, z);
            }

            if (worldIn.getBlock(x, y, z - 1).isNormalCube())
            {
                this.func_150172_m(worldIn, x, y + 1, z - 1);
            }
            else
            {
                this.func_150172_m(worldIn, x, y - 1, z - 1);
            }

            if (worldIn.getBlock(x, y, z + 1).isNormalCube())
            {
                this.func_150172_m(worldIn, x, y + 1, z + 1);
            }
            else
            {
                this.func_150172_m(worldIn, x, y - 1, z + 1);
            }
        }
    }

    private int func_150178_a(World p_150178_1_, int p_150178_2_, int p_150178_3_, int p_150178_4_, int p_150178_5_)
    {
        if (p_150178_1_.getBlock(p_150178_2_, p_150178_3_, p_150178_4_) != this)
        {
            return p_150178_5_;
        }
        else
        {
            int var6 = p_150178_1_.getBlockMetadata(p_150178_2_, p_150178_3_, p_150178_4_);
            return var6 > p_150178_5_ ? var6 : p_150178_5_;
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!worldIn.isRemote)
        {
            boolean var6 = this.canPlaceBlockAt(worldIn, x, y, z);

            if (var6)
            {
                this.func_150177_e(worldIn, x, y, z);
            }
            else
            {
                this.dropBlockAsItem(worldIn, x, y, z, 0, 0);
                worldIn.setBlockToAir(x, y, z);
            }

            super.onNeighborBlockChange(worldIn, x, y, z, neighbor);
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.redstone;
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return !this.canProvidePower ? 0 : this.isProvidingWeakPower(worldIn, x, y, z, side);
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        if (!this.canProvidePower)
        {
            return 0;
        }
        else
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);

            if (var6 == 0)
            {
                return 0;
            }
            else if (side == 1)
            {
                return var6;
            }
            else
            {
                boolean var7 = func_150176_g(worldIn, x - 1, y, z, 1) || !worldIn.getBlock(x - 1, y, z).isNormalCube() && func_150176_g(worldIn, x - 1, y - 1, z, -1);
                boolean var8 = func_150176_g(worldIn, x + 1, y, z, 3) || !worldIn.getBlock(x + 1, y, z).isNormalCube() && func_150176_g(worldIn, x + 1, y - 1, z, -1);
                boolean var9 = func_150176_g(worldIn, x, y, z - 1, 2) || !worldIn.getBlock(x, y, z - 1).isNormalCube() && func_150176_g(worldIn, x, y - 1, z - 1, -1);
                boolean var10 = func_150176_g(worldIn, x, y, z + 1, 0) || !worldIn.getBlock(x, y, z + 1).isNormalCube() && func_150176_g(worldIn, x, y - 1, z + 1, -1);

                if (!worldIn.getBlock(x, y + 1, z).isNormalCube())
                {
                    if (worldIn.getBlock(x - 1, y, z).isNormalCube() && func_150176_g(worldIn, x - 1, y + 1, z, -1))
                    {
                        var7 = true;
                    }

                    if (worldIn.getBlock(x + 1, y, z).isNormalCube() && func_150176_g(worldIn, x + 1, y + 1, z, -1))
                    {
                        var8 = true;
                    }

                    if (worldIn.getBlock(x, y, z - 1).isNormalCube() && func_150176_g(worldIn, x, y + 1, z - 1, -1))
                    {
                        var9 = true;
                    }

                    if (worldIn.getBlock(x, y, z + 1).isNormalCube() && func_150176_g(worldIn, x, y + 1, z + 1, -1))
                    {
                        var10 = true;
                    }
                }

                return !var9 && !var8 && !var7 && !var10 && side >= 2 && side <= 5 ? var6 : (side == 2 && var9 && !var7 && !var8 ? var6 : (side == 3 && var10 && !var7 && !var8 ? var6 : (side == 4 && var7 && !var9 && !var10 ? var6 : (side == 5 && var8 && !var9 && !var10 ? var6 : 0))));
            }
        }
    }

    public boolean canProvidePower()
    {
        return this.canProvidePower;
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);

        if (var6 > 0)
        {
            double var7 = (double)x + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
            double var9 = (double)((float)y + 0.0625F);
            double var11 = (double)z + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
            float var13 = (float)var6 / 15.0F;
            float var14 = var13 * 0.6F + 0.4F;

            if (var6 == 0)
            {
                var14 = 0.0F;
            }

            float var15 = var13 * var13 * 0.7F - 0.5F;
            float var16 = var13 * var13 * 0.6F - 0.7F;

            if (var15 < 0.0F)
            {
                var15 = 0.0F;
            }

            if (var16 < 0.0F)
            {
                var16 = 0.0F;
            }

            worldIn.spawnParticle("reddust", var7, var9, var11, (double)var14, (double)var15, (double)var16);
        }
    }

    public static boolean isPowerProviderOrWire(IBlockAccess p_150174_0_, int p_150174_1_, int p_150174_2_, int p_150174_3_, int p_150174_4_)
    {
        Block var5 = p_150174_0_.getBlock(p_150174_1_, p_150174_2_, p_150174_3_);

        if (var5 == Blocks.redstone_wire)
        {
            return true;
        }
        else if (!Blocks.unpowered_repeater.func_149907_e(var5))
        {
            return var5.canProvidePower() && p_150174_4_ != -1;
        }
        else
        {
            int var6 = p_150174_0_.getBlockMetadata(p_150174_1_, p_150174_2_, p_150174_3_);
            return p_150174_4_ == (var6 & 3) || p_150174_4_ == Direction.rotateOpposite[var6 & 3];
        }
    }

    public static boolean func_150176_g(IBlockAccess p_150176_0_, int p_150176_1_, int p_150176_2_, int p_150176_3_, int p_150176_4_)
    {
        if (isPowerProviderOrWire(p_150176_0_, p_150176_1_, p_150176_2_, p_150176_3_, p_150176_4_))
        {
            return true;
        }
        else if (p_150176_0_.getBlock(p_150176_1_, p_150176_2_, p_150176_3_) == Blocks.powered_repeater)
        {
            int var5 = p_150176_0_.getBlockMetadata(p_150176_1_, p_150176_2_, p_150176_3_);
            return p_150176_4_ == (var5 & 3);
        }
        else
        {
            return false;
        }
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.redstone;
    }

    public void registerIcons(IIconRegister reg)
    {
        this.redstoneCrossIcon = reg.registerIcon(this.getTextureName() + "_" + "cross");
        this.redstoneLineIcon = reg.registerIcon(this.getTextureName() + "_" + "line");
        this.redstoneCrossOverlayIcon = reg.registerIcon(this.getTextureName() + "_" + "cross_overlay");
        this.redstoneLineOverlayIcon = reg.registerIcon(this.getTextureName() + "_" + "line_overlay");
        this.blockIcon = this.redstoneCrossIcon;
    }

    public static IIcon getRedstoneWireIcon(String p_150173_0_)
    {
        return p_150173_0_.equals("cross") ? Blocks.redstone_wire.redstoneCrossIcon : (p_150173_0_.equals("line") ? Blocks.redstone_wire.redstoneLineIcon : (p_150173_0_.equals("cross_overlay") ? Blocks.redstone_wire.redstoneCrossOverlayIcon : (p_150173_0_.equals("line_overlay") ? Blocks.redstone_wire.redstoneLineOverlayIcon : null)));
    }
}
