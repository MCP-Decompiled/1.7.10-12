package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLiquid extends Block
{
    private IIcon[] field_149806_a;
    private static final String __OBFID = "CL_00000265";

    protected BlockLiquid(Material p_i45413_1_)
    {
        super(p_i45413_1_);
        float var2 = 0.0F;
        float var3 = 0.0F;
        this.setBlockBounds(0.0F + var3, 0.0F + var2, 0.0F + var3, 1.0F + var3, 1.0F + var2, 1.0F + var3);
        this.setTickRandomly(true);
    }

    public boolean isPassable(IBlockAccess worldIn, int x, int y, int z)
    {
        return this.blockMaterial != Material.lava;
    }

    public int getBlockColor()
    {
        return 16777215;
    }

    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        if (this.blockMaterial != Material.water)
        {
            return 16777215;
        }
        else
        {
            int var5 = 0;
            int var6 = 0;
            int var7 = 0;

            for (int var8 = -1; var8 <= 1; ++var8)
            {
                for (int var9 = -1; var9 <= 1; ++var9)
                {
                    int var10 = worldIn.getBiomeGenForCoords(x + var9, z + var8).waterColorMultiplier;
                    var5 += (var10 & 16711680) >> 16;
                    var6 += (var10 & 65280) >> 8;
                    var7 += var10 & 255;
                }
            }

            return (var5 / 9 & 255) << 16 | (var6 / 9 & 255) << 8 | var7 / 9 & 255;
        }
    }

    public static float getLiquidHeightPercent(int p_149801_0_)
    {
        if (p_149801_0_ >= 8)
        {
            p_149801_0_ = 0;
        }

        return (float)(p_149801_0_ + 1) / 9.0F;
    }

    public IIcon getIcon(int side, int meta)
    {
        return side != 0 && side != 1 ? this.field_149806_a[1] : this.field_149806_a[0];
    }

    protected int func_149804_e(World p_149804_1_, int p_149804_2_, int p_149804_3_, int p_149804_4_)
    {
        return p_149804_1_.getBlock(p_149804_2_, p_149804_3_, p_149804_4_).getMaterial() == this.blockMaterial ? p_149804_1_.getBlockMetadata(p_149804_2_, p_149804_3_, p_149804_4_) : -1;
    }

    protected int getEffectiveFlowDecay(IBlockAccess p_149798_1_, int p_149798_2_, int p_149798_3_, int p_149798_4_)
    {
        if (p_149798_1_.getBlock(p_149798_2_, p_149798_3_, p_149798_4_).getMaterial() != this.blockMaterial)
        {
            return -1;
        }
        else
        {
            int var5 = p_149798_1_.getBlockMetadata(p_149798_2_, p_149798_3_, p_149798_4_);

            if (var5 >= 8)
            {
                var5 = 0;
            }

            return var5;
        }
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean canStopRayTrace(int meta, boolean includeLiquid)
    {
        return includeLiquid && meta == 0;
    }

    public boolean isBlockSolid(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        Material var6 = worldIn.getBlock(x, y, z).getMaterial();
        return var6 == this.blockMaterial ? false : (side == 1 ? true : (var6 == Material.ice ? false : super.isBlockSolid(worldIn, x, y, z, side)));
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        Material var6 = worldIn.getBlock(x, y, z).getMaterial();
        return var6 == this.blockMaterial ? false : (side == 1 ? true : super.shouldSideBeRendered(worldIn, x, y, z, side));
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return null;
    }

    public int getRenderType()
    {
        return 4;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return null;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    private Vec3 getFlowVector(IBlockAccess p_149800_1_, int p_149800_2_, int p_149800_3_, int p_149800_4_)
    {
        Vec3 var5 = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
        int var6 = this.getEffectiveFlowDecay(p_149800_1_, p_149800_2_, p_149800_3_, p_149800_4_);

        for (int var7 = 0; var7 < 4; ++var7)
        {
            int var8 = p_149800_2_;
            int var10 = p_149800_4_;

            if (var7 == 0)
            {
                var8 = p_149800_2_ - 1;
            }

            if (var7 == 1)
            {
                var10 = p_149800_4_ - 1;
            }

            if (var7 == 2)
            {
                ++var8;
            }

            if (var7 == 3)
            {
                ++var10;
            }

            int var11 = this.getEffectiveFlowDecay(p_149800_1_, var8, p_149800_3_, var10);
            int var12;

            if (var11 < 0)
            {
                if (!p_149800_1_.getBlock(var8, p_149800_3_, var10).getMaterial().blocksMovement())
                {
                    var11 = this.getEffectiveFlowDecay(p_149800_1_, var8, p_149800_3_ - 1, var10);

                    if (var11 >= 0)
                    {
                        var12 = var11 - (var6 - 8);
                        var5 = var5.addVector((double)((var8 - p_149800_2_) * var12), (double)((p_149800_3_ - p_149800_3_) * var12), (double)((var10 - p_149800_4_) * var12));
                    }
                }
            }
            else if (var11 >= 0)
            {
                var12 = var11 - var6;
                var5 = var5.addVector((double)((var8 - p_149800_2_) * var12), (double)((p_149800_3_ - p_149800_3_) * var12), (double)((var10 - p_149800_4_) * var12));
            }
        }

        if (p_149800_1_.getBlockMetadata(p_149800_2_, p_149800_3_, p_149800_4_) >= 8)
        {
            boolean var13 = false;

            if (var13 || this.isBlockSolid(p_149800_1_, p_149800_2_, p_149800_3_, p_149800_4_ - 1, 2))
            {
                var13 = true;
            }

            if (var13 || this.isBlockSolid(p_149800_1_, p_149800_2_, p_149800_3_, p_149800_4_ + 1, 3))
            {
                var13 = true;
            }

            if (var13 || this.isBlockSolid(p_149800_1_, p_149800_2_ - 1, p_149800_3_, p_149800_4_, 4))
            {
                var13 = true;
            }

            if (var13 || this.isBlockSolid(p_149800_1_, p_149800_2_ + 1, p_149800_3_, p_149800_4_, 5))
            {
                var13 = true;
            }

            if (var13 || this.isBlockSolid(p_149800_1_, p_149800_2_, p_149800_3_ + 1, p_149800_4_ - 1, 2))
            {
                var13 = true;
            }

            if (var13 || this.isBlockSolid(p_149800_1_, p_149800_2_, p_149800_3_ + 1, p_149800_4_ + 1, 3))
            {
                var13 = true;
            }

            if (var13 || this.isBlockSolid(p_149800_1_, p_149800_2_ - 1, p_149800_3_ + 1, p_149800_4_, 4))
            {
                var13 = true;
            }

            if (var13 || this.isBlockSolid(p_149800_1_, p_149800_2_ + 1, p_149800_3_ + 1, p_149800_4_, 5))
            {
                var13 = true;
            }

            if (var13)
            {
                var5 = var5.normalize().addVector(0.0D, -6.0D, 0.0D);
            }
        }

        var5 = var5.normalize();
        return var5;
    }

    public void modifyEntityVelocity(World worldIn, int x, int y, int z, Entity entityIn, Vec3 velocity)
    {
        Vec3 var7 = this.getFlowVector(worldIn, x, y, z);
        velocity.xCoord += var7.xCoord;
        velocity.yCoord += var7.yCoord;
        velocity.zCoord += var7.zCoord;
    }

    public int tickRate(World worldIn)
    {
        return this.blockMaterial == Material.water ? 5 : (this.blockMaterial == Material.lava ? (worldIn.provider.hasNoSky ? 10 : 30) : 0);
    }

    public int getMixedBrightnessForBlock(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int var6 = worldIn.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);
        int var7 = var5 & 255;
        int var8 = var6 & 255;
        int var9 = var5 >> 16 & 255;
        int var10 = var6 >> 16 & 255;
        return (var7 > var8 ? var7 : var8) | (var9 > var10 ? var9 : var10) << 16;
    }

    public int getRenderBlockPass()
    {
        return this.blockMaterial == Material.water ? 1 : 0;
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        int var6;

        if (this.blockMaterial == Material.water)
        {
            if (random.nextInt(10) == 0)
            {
                var6 = worldIn.getBlockMetadata(x, y, z);

                if (var6 <= 0 || var6 >= 8)
                {
                    worldIn.spawnParticle("suspended", (double)((float)x + random.nextFloat()), (double)((float)y + random.nextFloat()), (double)((float)z + random.nextFloat()), 0.0D, 0.0D, 0.0D);
                }
            }

            for (var6 = 0; var6 < 0; ++var6)
            {
                int var7 = random.nextInt(4);
                int var8 = x;
                int var9 = z;

                if (var7 == 0)
                {
                    var8 = x - 1;
                }

                if (var7 == 1)
                {
                    ++var8;
                }

                if (var7 == 2)
                {
                    var9 = z - 1;
                }

                if (var7 == 3)
                {
                    ++var9;
                }

                if (worldIn.getBlock(var8, y, var9).getMaterial() == Material.air && (worldIn.getBlock(var8, y - 1, var9).getMaterial().blocksMovement() || worldIn.getBlock(var8, y - 1, var9).getMaterial().isLiquid()))
                {
                    float var10 = 0.0625F;
                    double var11 = (double)((float)x + random.nextFloat());
                    double var13 = (double)((float)y + random.nextFloat());
                    double var15 = (double)((float)z + random.nextFloat());

                    if (var7 == 0)
                    {
                        var11 = (double)((float)x - var10);
                    }

                    if (var7 == 1)
                    {
                        var11 = (double)((float)(x + 1) + var10);
                    }

                    if (var7 == 2)
                    {
                        var15 = (double)((float)z - var10);
                    }

                    if (var7 == 3)
                    {
                        var15 = (double)((float)(z + 1) + var10);
                    }

                    double var17 = 0.0D;
                    double var19 = 0.0D;

                    if (var7 == 0)
                    {
                        var17 = (double)(-var10);
                    }

                    if (var7 == 1)
                    {
                        var17 = (double)var10;
                    }

                    if (var7 == 2)
                    {
                        var19 = (double)(-var10);
                    }

                    if (var7 == 3)
                    {
                        var19 = (double)var10;
                    }

                    worldIn.spawnParticle("splash", var11, var13, var15, var17, 0.0D, var19);
                }
            }
        }

        if (this.blockMaterial == Material.water && random.nextInt(64) == 0)
        {
            var6 = worldIn.getBlockMetadata(x, y, z);

            if (var6 > 0 && var6 < 8)
            {
                worldIn.playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "liquid.water", random.nextFloat() * 0.25F + 0.75F, random.nextFloat() * 1.0F + 0.5F, false);
            }
        }

        double var21;
        double var22;
        double var23;

        if (this.blockMaterial == Material.lava && worldIn.getBlock(x, y + 1, z).getMaterial() == Material.air && !worldIn.getBlock(x, y + 1, z).isOpaqueCube())
        {
            if (random.nextInt(100) == 0)
            {
                var21 = (double)((float)x + random.nextFloat());
                var22 = (double)y + this.maxY;
                var23 = (double)((float)z + random.nextFloat());
                worldIn.spawnParticle("lava", var21, var22, var23, 0.0D, 0.0D, 0.0D);
                worldIn.playSound(var21, var22, var23, "liquid.lavapop", 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if (random.nextInt(200) == 0)
            {
                worldIn.playSound((double)x, (double)y, (double)z, "liquid.lava", 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }

        if (random.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) && !worldIn.getBlock(x, y - 2, z).getMaterial().blocksMovement())
        {
            var21 = (double)((float)x + random.nextFloat());
            var22 = (double)y - 1.05D;
            var23 = (double)((float)z + random.nextFloat());

            if (this.blockMaterial == Material.water)
            {
                worldIn.spawnParticle("dripWater", var21, var22, var23, 0.0D, 0.0D, 0.0D);
            }
            else
            {
                worldIn.spawnParticle("dripLava", var21, var22, var23, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public static double getFlowDirection(IBlockAccess p_149802_0_, int p_149802_1_, int p_149802_2_, int p_149802_3_, Material p_149802_4_)
    {
        Vec3 var5 = null;

        if (p_149802_4_ == Material.water)
        {
            var5 = Blocks.flowing_water.getFlowVector(p_149802_0_, p_149802_1_, p_149802_2_, p_149802_3_);
        }

        if (p_149802_4_ == Material.lava)
        {
            var5 = Blocks.flowing_lava.getFlowVector(p_149802_0_, p_149802_1_, p_149802_2_, p_149802_3_);
        }

        return var5.xCoord == 0.0D && var5.zCoord == 0.0D ? -1000.0D : Math.atan2(var5.zCoord, var5.xCoord) - (Math.PI / 2D);
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        this.func_149805_n(worldIn, x, y, z);
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        this.func_149805_n(worldIn, x, y, z);
    }

    private void func_149805_n(World p_149805_1_, int p_149805_2_, int p_149805_3_, int p_149805_4_)
    {
        if (p_149805_1_.getBlock(p_149805_2_, p_149805_3_, p_149805_4_) == this)
        {
            if (this.blockMaterial == Material.lava)
            {
                boolean var5 = false;

                if (var5 || p_149805_1_.getBlock(p_149805_2_, p_149805_3_, p_149805_4_ - 1).getMaterial() == Material.water)
                {
                    var5 = true;
                }

                if (var5 || p_149805_1_.getBlock(p_149805_2_, p_149805_3_, p_149805_4_ + 1).getMaterial() == Material.water)
                {
                    var5 = true;
                }

                if (var5 || p_149805_1_.getBlock(p_149805_2_ - 1, p_149805_3_, p_149805_4_).getMaterial() == Material.water)
                {
                    var5 = true;
                }

                if (var5 || p_149805_1_.getBlock(p_149805_2_ + 1, p_149805_3_, p_149805_4_).getMaterial() == Material.water)
                {
                    var5 = true;
                }

                if (var5 || p_149805_1_.getBlock(p_149805_2_, p_149805_3_ + 1, p_149805_4_).getMaterial() == Material.water)
                {
                    var5 = true;
                }

                if (var5)
                {
                    int var6 = p_149805_1_.getBlockMetadata(p_149805_2_, p_149805_3_, p_149805_4_);

                    if (var6 == 0)
                    {
                        p_149805_1_.setBlock(p_149805_2_, p_149805_3_, p_149805_4_, Blocks.obsidian);
                    }
                    else if (var6 <= 4)
                    {
                        p_149805_1_.setBlock(p_149805_2_, p_149805_3_, p_149805_4_, Blocks.cobblestone);
                    }

                    this.func_149799_m(p_149805_1_, p_149805_2_, p_149805_3_, p_149805_4_);
                }
            }
        }
    }

    protected void func_149799_m(World p_149799_1_, int p_149799_2_, int p_149799_3_, int p_149799_4_)
    {
        p_149799_1_.playSoundEffect((double)((float)p_149799_2_ + 0.5F), (double)((float)p_149799_3_ + 0.5F), (double)((float)p_149799_4_ + 0.5F), "random.fizz", 0.5F, 2.6F + (p_149799_1_.rand.nextFloat() - p_149799_1_.rand.nextFloat()) * 0.8F);

        for (int var5 = 0; var5 < 8; ++var5)
        {
            p_149799_1_.spawnParticle("largesmoke", (double)p_149799_2_ + Math.random(), (double)p_149799_3_ + 1.2D, (double)p_149799_4_ + Math.random(), 0.0D, 0.0D, 0.0D);
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        if (this.blockMaterial == Material.lava)
        {
            this.field_149806_a = new IIcon[] {reg.registerIcon("lava_still"), reg.registerIcon("lava_flow")};
        }
        else
        {
            this.field_149806_a = new IIcon[] {reg.registerIcon("water_still"), reg.registerIcon("water_flow")};
        }
    }

    public static IIcon getLiquidIcon(String p_149803_0_)
    {
        return p_149803_0_ == "water_still" ? Blocks.flowing_water.field_149806_a[0] : (p_149803_0_ == "water_flow" ? Blocks.flowing_water.field_149806_a[1] : (p_149803_0_ == "lava_still" ? Blocks.flowing_lava.field_149806_a[0] : (p_149803_0_ == "lava_flow" ? Blocks.flowing_lava.field_149806_a[1] : null)));
    }
}
