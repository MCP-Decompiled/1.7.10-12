package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStairs extends Block
{
    private static final int[][] field_150150_a = new int[][] {{2, 6}, {3, 7}, {2, 3}, {6, 7}, {0, 4}, {1, 5}, {0, 1}, {4, 5}};
    private final Block field_150149_b;
    private final int field_150151_M;
    private boolean field_150152_N;
    private int field_150153_O;
    private static final String __OBFID = "CL_00000314";

    protected BlockStairs(Block p_i45428_1_, int p_i45428_2_)
    {
        super(p_i45428_1_.blockMaterial);
        this.field_150149_b = p_i45428_1_;
        this.field_150151_M = p_i45428_2_;
        this.setHardness(p_i45428_1_.blockHardness);
        this.setResistance(p_i45428_1_.blockResistance / 3.0F);
        this.setStepSound(p_i45428_1_.stepSound);
        this.setLightOpacity(255);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        if (this.field_150152_N)
        {
            this.setBlockBounds(0.5F * (float)(this.field_150153_O % 2), 0.5F * (float)(this.field_150153_O / 2 % 2), 0.5F * (float)(this.field_150153_O / 4 % 2), 0.5F + 0.5F * (float)(this.field_150153_O % 2), 0.5F + 0.5F * (float)(this.field_150153_O / 2 % 2), 0.5F + 0.5F * (float)(this.field_150153_O / 4 % 2));
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
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
        return 10;
    }

    public void setBaseBounds(IBlockAccess p_150147_1_, int p_150147_2_, int p_150147_3_, int p_150147_4_)
    {
        int var5 = p_150147_1_.getBlockMetadata(p_150147_2_, p_150147_3_, p_150147_4_);

        if ((var5 & 4) != 0)
        {
            this.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
    }

    public static boolean isBlockStairs(Block p_150148_0_)
    {
        return p_150148_0_ instanceof BlockStairs;
    }

    private boolean func_150146_f(IBlockAccess p_150146_1_, int p_150146_2_, int p_150146_3_, int p_150146_4_, int p_150146_5_)
    {
        Block var6 = p_150146_1_.getBlock(p_150146_2_, p_150146_3_, p_150146_4_);
        return isBlockStairs(var6) && p_150146_1_.getBlockMetadata(p_150146_2_, p_150146_3_, p_150146_4_) == p_150146_5_;
    }

    public boolean func_150145_f(IBlockAccess p_150145_1_, int p_150145_2_, int p_150145_3_, int p_150145_4_)
    {
        int var5 = p_150145_1_.getBlockMetadata(p_150145_2_, p_150145_3_, p_150145_4_);
        int var6 = var5 & 3;
        float var7 = 0.5F;
        float var8 = 1.0F;

        if ((var5 & 4) != 0)
        {
            var7 = 0.0F;
            var8 = 0.5F;
        }

        float var9 = 0.0F;
        float var10 = 1.0F;
        float var11 = 0.0F;
        float var12 = 0.5F;
        boolean var13 = true;
        Block var14;
        int var15;
        int var16;

        if (var6 == 0)
        {
            var9 = 0.5F;
            var12 = 1.0F;
            var14 = p_150145_1_.getBlock(p_150145_2_ + 1, p_150145_3_, p_150145_4_);
            var15 = p_150145_1_.getBlockMetadata(p_150145_2_ + 1, p_150145_3_, p_150145_4_);

            if (isBlockStairs(var14) && (var5 & 4) == (var15 & 4))
            {
                var16 = var15 & 3;

                if (var16 == 3 && !this.func_150146_f(p_150145_1_, p_150145_2_, p_150145_3_, p_150145_4_ + 1, var5))
                {
                    var12 = 0.5F;
                    var13 = false;
                }
                else if (var16 == 2 && !this.func_150146_f(p_150145_1_, p_150145_2_, p_150145_3_, p_150145_4_ - 1, var5))
                {
                    var11 = 0.5F;
                    var13 = false;
                }
            }
        }
        else if (var6 == 1)
        {
            var10 = 0.5F;
            var12 = 1.0F;
            var14 = p_150145_1_.getBlock(p_150145_2_ - 1, p_150145_3_, p_150145_4_);
            var15 = p_150145_1_.getBlockMetadata(p_150145_2_ - 1, p_150145_3_, p_150145_4_);

            if (isBlockStairs(var14) && (var5 & 4) == (var15 & 4))
            {
                var16 = var15 & 3;

                if (var16 == 3 && !this.func_150146_f(p_150145_1_, p_150145_2_, p_150145_3_, p_150145_4_ + 1, var5))
                {
                    var12 = 0.5F;
                    var13 = false;
                }
                else if (var16 == 2 && !this.func_150146_f(p_150145_1_, p_150145_2_, p_150145_3_, p_150145_4_ - 1, var5))
                {
                    var11 = 0.5F;
                    var13 = false;
                }
            }
        }
        else if (var6 == 2)
        {
            var11 = 0.5F;
            var12 = 1.0F;
            var14 = p_150145_1_.getBlock(p_150145_2_, p_150145_3_, p_150145_4_ + 1);
            var15 = p_150145_1_.getBlockMetadata(p_150145_2_, p_150145_3_, p_150145_4_ + 1);

            if (isBlockStairs(var14) && (var5 & 4) == (var15 & 4))
            {
                var16 = var15 & 3;

                if (var16 == 1 && !this.func_150146_f(p_150145_1_, p_150145_2_ + 1, p_150145_3_, p_150145_4_, var5))
                {
                    var10 = 0.5F;
                    var13 = false;
                }
                else if (var16 == 0 && !this.func_150146_f(p_150145_1_, p_150145_2_ - 1, p_150145_3_, p_150145_4_, var5))
                {
                    var9 = 0.5F;
                    var13 = false;
                }
            }
        }
        else if (var6 == 3)
        {
            var14 = p_150145_1_.getBlock(p_150145_2_, p_150145_3_, p_150145_4_ - 1);
            var15 = p_150145_1_.getBlockMetadata(p_150145_2_, p_150145_3_, p_150145_4_ - 1);

            if (isBlockStairs(var14) && (var5 & 4) == (var15 & 4))
            {
                var16 = var15 & 3;

                if (var16 == 1 && !this.func_150146_f(p_150145_1_, p_150145_2_ + 1, p_150145_3_, p_150145_4_, var5))
                {
                    var10 = 0.5F;
                    var13 = false;
                }
                else if (var16 == 0 && !this.func_150146_f(p_150145_1_, p_150145_2_ - 1, p_150145_3_, p_150145_4_, var5))
                {
                    var9 = 0.5F;
                    var13 = false;
                }
            }
        }

        this.setBlockBounds(var9, var7, var11, var10, var8, var12);
        return var13;
    }

    public boolean func_150144_g(IBlockAccess p_150144_1_, int p_150144_2_, int p_150144_3_, int p_150144_4_)
    {
        int var5 = p_150144_1_.getBlockMetadata(p_150144_2_, p_150144_3_, p_150144_4_);
        int var6 = var5 & 3;
        float var7 = 0.5F;
        float var8 = 1.0F;

        if ((var5 & 4) != 0)
        {
            var7 = 0.0F;
            var8 = 0.5F;
        }

        float var9 = 0.0F;
        float var10 = 0.5F;
        float var11 = 0.5F;
        float var12 = 1.0F;
        boolean var13 = false;
        Block var14;
        int var15;
        int var16;

        if (var6 == 0)
        {
            var14 = p_150144_1_.getBlock(p_150144_2_ - 1, p_150144_3_, p_150144_4_);
            var15 = p_150144_1_.getBlockMetadata(p_150144_2_ - 1, p_150144_3_, p_150144_4_);

            if (isBlockStairs(var14) && (var5 & 4) == (var15 & 4))
            {
                var16 = var15 & 3;

                if (var16 == 3 && !this.func_150146_f(p_150144_1_, p_150144_2_, p_150144_3_, p_150144_4_ - 1, var5))
                {
                    var11 = 0.0F;
                    var12 = 0.5F;
                    var13 = true;
                }
                else if (var16 == 2 && !this.func_150146_f(p_150144_1_, p_150144_2_, p_150144_3_, p_150144_4_ + 1, var5))
                {
                    var11 = 0.5F;
                    var12 = 1.0F;
                    var13 = true;
                }
            }
        }
        else if (var6 == 1)
        {
            var14 = p_150144_1_.getBlock(p_150144_2_ + 1, p_150144_3_, p_150144_4_);
            var15 = p_150144_1_.getBlockMetadata(p_150144_2_ + 1, p_150144_3_, p_150144_4_);

            if (isBlockStairs(var14) && (var5 & 4) == (var15 & 4))
            {
                var9 = 0.5F;
                var10 = 1.0F;
                var16 = var15 & 3;

                if (var16 == 3 && !this.func_150146_f(p_150144_1_, p_150144_2_, p_150144_3_, p_150144_4_ - 1, var5))
                {
                    var11 = 0.0F;
                    var12 = 0.5F;
                    var13 = true;
                }
                else if (var16 == 2 && !this.func_150146_f(p_150144_1_, p_150144_2_, p_150144_3_, p_150144_4_ + 1, var5))
                {
                    var11 = 0.5F;
                    var12 = 1.0F;
                    var13 = true;
                }
            }
        }
        else if (var6 == 2)
        {
            var14 = p_150144_1_.getBlock(p_150144_2_, p_150144_3_, p_150144_4_ - 1);
            var15 = p_150144_1_.getBlockMetadata(p_150144_2_, p_150144_3_, p_150144_4_ - 1);

            if (isBlockStairs(var14) && (var5 & 4) == (var15 & 4))
            {
                var11 = 0.0F;
                var12 = 0.5F;
                var16 = var15 & 3;

                if (var16 == 1 && !this.func_150146_f(p_150144_1_, p_150144_2_ - 1, p_150144_3_, p_150144_4_, var5))
                {
                    var13 = true;
                }
                else if (var16 == 0 && !this.func_150146_f(p_150144_1_, p_150144_2_ + 1, p_150144_3_, p_150144_4_, var5))
                {
                    var9 = 0.5F;
                    var10 = 1.0F;
                    var13 = true;
                }
            }
        }
        else if (var6 == 3)
        {
            var14 = p_150144_1_.getBlock(p_150144_2_, p_150144_3_, p_150144_4_ + 1);
            var15 = p_150144_1_.getBlockMetadata(p_150144_2_, p_150144_3_, p_150144_4_ + 1);

            if (isBlockStairs(var14) && (var5 & 4) == (var15 & 4))
            {
                var16 = var15 & 3;

                if (var16 == 1 && !this.func_150146_f(p_150144_1_, p_150144_2_ - 1, p_150144_3_, p_150144_4_, var5))
                {
                    var13 = true;
                }
                else if (var16 == 0 && !this.func_150146_f(p_150144_1_, p_150144_2_ + 1, p_150144_3_, p_150144_4_, var5))
                {
                    var9 = 0.5F;
                    var10 = 1.0F;
                    var13 = true;
                }
            }
        }

        if (var13)
        {
            this.setBlockBounds(var9, var7, var11, var10, var8, var12);
        }

        return var13;
    }

    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider)
    {
        this.setBaseBounds(worldIn, x, y, z);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        boolean var8 = this.func_150145_f(worldIn, x, y, z);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);

        if (var8 && this.func_150144_g(worldIn, x, y, z))
        {
            super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        this.field_150149_b.randomDisplayTick(worldIn, x, y, z, random);
    }

    public void onBlockClicked(World worldIn, int x, int y, int z, EntityPlayer player)
    {
        this.field_150149_b.onBlockClicked(worldIn, x, y, z, player);
    }

    public void onBlockDestroyedByPlayer(World worldIn, int x, int y, int z, int meta)
    {
        this.field_150149_b.onBlockDestroyedByPlayer(worldIn, x, y, z, meta);
    }

    public int getMixedBrightnessForBlock(IBlockAccess worldIn, int x, int y, int z)
    {
        return this.field_150149_b.getMixedBrightnessForBlock(worldIn, x, y, z);
    }

    public float getExplosionResistance(Entity exploder)
    {
        return this.field_150149_b.getExplosionResistance(exploder);
    }

    public int getRenderBlockPass()
    {
        return this.field_150149_b.getRenderBlockPass();
    }

    public IIcon getIcon(int side, int meta)
    {
        return this.field_150149_b.getIcon(side, this.field_150151_M);
    }

    public int tickRate(World worldIn)
    {
        return this.field_150149_b.tickRate(worldIn);
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return this.field_150149_b.getSelectedBoundingBoxFromPool(worldIn, x, y, z);
    }

    public void modifyEntityVelocity(World worldIn, int x, int y, int z, Entity entityIn, Vec3 velocity)
    {
        this.field_150149_b.modifyEntityVelocity(worldIn, x, y, z, entityIn, velocity);
    }

    public boolean isCollidable()
    {
        return this.field_150149_b.isCollidable();
    }

    public boolean canStopRayTrace(int meta, boolean includeLiquid)
    {
        return this.field_150149_b.canStopRayTrace(meta, includeLiquid);
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return this.field_150149_b.canPlaceBlockAt(worldIn, x, y, z);
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        this.onNeighborBlockChange(worldIn, x, y, z, Blocks.air);
        this.field_150149_b.onBlockAdded(worldIn, x, y, z);
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        this.field_150149_b.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public void onEntityWalking(World worldIn, int x, int y, int z, Entity entityIn)
    {
        this.field_150149_b.onEntityWalking(worldIn, x, y, z, entityIn);
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        this.field_150149_b.updateTick(worldIn, x, y, z, random);
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        return this.field_150149_b.onBlockActivated(worldIn, x, y, z, player, 0, 0.0F, 0.0F, 0.0F);
    }

    public void onBlockDestroyedByExplosion(World worldIn, int x, int y, int z, Explosion explosionIn)
    {
        this.field_150149_b.onBlockDestroyedByExplosion(worldIn, x, y, z, explosionIn);
    }

    public MapColor getMapColor(int meta)
    {
        return this.field_150149_b.getMapColor(this.field_150151_M);
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int var7 = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int var8 = worldIn.getBlockMetadata(x, y, z) & 4;

        if (var7 == 0)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 2 | var8, 2);
        }

        if (var7 == 1)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 1 | var8, 2);
        }

        if (var7 == 2)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 3 | var8, 2);
        }

        if (var7 == 3)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 0 | var8, 2);
        }
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        return side != 0 && (side == 1 || (double)subY <= 0.5D) ? meta : meta | 4;
    }

    public MovingObjectPosition collisionRayTrace(World worldIn, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
        MovingObjectPosition[] var7 = new MovingObjectPosition[8];
        int var8 = worldIn.getBlockMetadata(x, y, z);
        int var9 = var8 & 3;
        boolean var10 = (var8 & 4) == 4;
        int[] var11 = field_150150_a[var9 + (var10 ? 4 : 0)];
        this.field_150152_N = true;
        int var14;
        int var15;
        int var16;

        for (int var12 = 0; var12 < 8; ++var12)
        {
            this.field_150153_O = var12;
            int[] var13 = var11;
            var14 = var11.length;

            for (var15 = 0; var15 < var14; ++var15)
            {
                var16 = var13[var15];

                if (var16 == var12)
                {
                    ;
                }
            }

            var7[var12] = super.collisionRayTrace(worldIn, x, y, z, startVec, endVec);
        }

        int[] var21 = var11;
        int var23 = var11.length;

        for (var14 = 0; var14 < var23; ++var14)
        {
            var15 = var21[var14];
            var7[var15] = null;
        }

        MovingObjectPosition var22 = null;
        double var24 = 0.0D;
        MovingObjectPosition[] var25 = var7;
        var16 = var7.length;

        for (int var17 = 0; var17 < var16; ++var17)
        {
            MovingObjectPosition var18 = var25[var17];

            if (var18 != null)
            {
                double var19 = var18.hitVec.squareDistanceTo(endVec);

                if (var19 > var24)
                {
                    var22 = var18;
                    var24 = var19;
                }
            }
        }

        return var22;
    }

    public void registerIcons(IIconRegister reg) {}
}
