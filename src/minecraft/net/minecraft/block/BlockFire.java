package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class BlockFire extends Block
{
    private int[] field_149849_a = new int[256];
    private int[] field_149848_b = new int[256];
    private IIcon[] field_149850_M;
    private static final String __OBFID = "CL_00000245";

    protected BlockFire()
    {
        super(Material.fire);
        this.setTickRandomly(true);
    }

    public static void func_149843_e()
    {
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.planks), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.double_wooden_slab), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.wooden_slab), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.fence), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.oak_stairs), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.birch_stairs), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.spruce_stairs), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.jungle_stairs), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.log), 5, 5);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.log2), 5, 5);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.leaves), 30, 60);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.leaves2), 30, 60);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.bookshelf), 30, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.tnt), 15, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.tallgrass), 60, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.double_plant), 60, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.yellow_flower), 60, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.red_flower), 60, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.wool), 30, 60);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.vine), 15, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.coal_block), 5, 5);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.hay_block), 60, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.carpet), 60, 20);
    }

    public void func_149842_a(int p_149842_1_, int p_149842_2_, int p_149842_3_)
    {
        this.field_149849_a[p_149842_1_] = p_149842_2_;
        this.field_149848_b[p_149842_1_] = p_149842_3_;
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
        return 3;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public int tickRate(World worldIn)
    {
        return 30;
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (worldIn.getGameRules().getGameRuleBooleanValue("doFireTick"))
        {
            boolean var6 = worldIn.getBlock(x, y - 1, z) == Blocks.netherrack;

            if (worldIn.provider instanceof WorldProviderEnd && worldIn.getBlock(x, y - 1, z) == Blocks.bedrock)
            {
                var6 = true;
            }

            if (!this.canPlaceBlockAt(worldIn, x, y, z))
            {
                worldIn.setBlockToAir(x, y, z);
            }

            if (!var6 && worldIn.isRaining() && (worldIn.isRainingAt(x, y, z) || worldIn.isRainingAt(x - 1, y, z) || worldIn.isRainingAt(x + 1, y, z) || worldIn.isRainingAt(x, y, z - 1) || worldIn.isRainingAt(x, y, z + 1)))
            {
                worldIn.setBlockToAir(x, y, z);
            }
            else
            {
                int var7 = worldIn.getBlockMetadata(x, y, z);

                if (var7 < 15)
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z, var7 + random.nextInt(3) / 2, 4);
                }

                worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn) + random.nextInt(10));

                if (!var6 && !this.canNeighborBurn(worldIn, x, y, z))
                {
                    if (!World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) || var7 > 3)
                    {
                        worldIn.setBlockToAir(x, y, z);
                    }
                }
                else if (!var6 && !this.canBlockCatchFire(worldIn, x, y - 1, z) && var7 == 15 && random.nextInt(4) == 0)
                {
                    worldIn.setBlockToAir(x, y, z);
                }
                else
                {
                    boolean var8 = worldIn.isBlockHighHumidity(x, y, z);
                    byte var9 = 0;

                    if (var8)
                    {
                        var9 = -50;
                    }

                    this.tryCatchFire(worldIn, x + 1, y, z, 300 + var9, random, var7);
                    this.tryCatchFire(worldIn, x - 1, y, z, 300 + var9, random, var7);
                    this.tryCatchFire(worldIn, x, y - 1, z, 250 + var9, random, var7);
                    this.tryCatchFire(worldIn, x, y + 1, z, 250 + var9, random, var7);
                    this.tryCatchFire(worldIn, x, y, z - 1, 300 + var9, random, var7);
                    this.tryCatchFire(worldIn, x, y, z + 1, 300 + var9, random, var7);

                    for (int var10 = x - 1; var10 <= x + 1; ++var10)
                    {
                        for (int var11 = z - 1; var11 <= z + 1; ++var11)
                        {
                            for (int var12 = y - 1; var12 <= y + 4; ++var12)
                            {
                                if (var10 != x || var12 != y || var11 != z)
                                {
                                    int var13 = 100;

                                    if (var12 > y + 1)
                                    {
                                        var13 += (var12 - (y + 1)) * 100;
                                    }

                                    int var14 = this.getChanceOfNeighborsEncouragingFire(worldIn, var10, var12, var11);

                                    if (var14 > 0)
                                    {
                                        int var15 = (var14 + 40 + worldIn.difficultySetting.getDifficultyId() * 7) / (var7 + 30);

                                        if (var8)
                                        {
                                            var15 /= 2;
                                        }

                                        if (var15 > 0 && random.nextInt(var13) <= var15 && (!worldIn.isRaining() || !worldIn.isRainingAt(var10, var12, var11)) && !worldIn.isRainingAt(var10 - 1, var12, z) && !worldIn.isRainingAt(var10 + 1, var12, var11) && !worldIn.isRainingAt(var10, var12, var11 - 1) && !worldIn.isRainingAt(var10, var12, var11 + 1))
                                        {
                                            int var16 = var7 + random.nextInt(5) / 4;

                                            if (var16 > 15)
                                            {
                                                var16 = 15;
                                            }

                                            worldIn.setBlock(var10, var12, var11, this, var16, 3);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean requiresUpdates()
    {
        return false;
    }

    private void tryCatchFire(World p_149841_1_, int p_149841_2_, int p_149841_3_, int p_149841_4_, int p_149841_5_, Random p_149841_6_, int p_149841_7_)
    {
        int var8 = this.field_149848_b[Block.getIdFromBlock(p_149841_1_.getBlock(p_149841_2_, p_149841_3_, p_149841_4_))];

        if (p_149841_6_.nextInt(p_149841_5_) < var8)
        {
            boolean var9 = p_149841_1_.getBlock(p_149841_2_, p_149841_3_, p_149841_4_) == Blocks.tnt;

            if (p_149841_6_.nextInt(p_149841_7_ + 10) < 5 && !p_149841_1_.isRainingAt(p_149841_2_, p_149841_3_, p_149841_4_))
            {
                int var10 = p_149841_7_ + p_149841_6_.nextInt(5) / 4;

                if (var10 > 15)
                {
                    var10 = 15;
                }

                p_149841_1_.setBlock(p_149841_2_, p_149841_3_, p_149841_4_, this, var10, 3);
            }
            else
            {
                p_149841_1_.setBlockToAir(p_149841_2_, p_149841_3_, p_149841_4_);
            }

            if (var9)
            {
                Blocks.tnt.onBlockDestroyedByPlayer(p_149841_1_, p_149841_2_, p_149841_3_, p_149841_4_, 1);
            }
        }
    }

    private boolean canNeighborBurn(World p_149847_1_, int p_149847_2_, int p_149847_3_, int p_149847_4_)
    {
        return this.canBlockCatchFire(p_149847_1_, p_149847_2_ + 1, p_149847_3_, p_149847_4_) ? true : (this.canBlockCatchFire(p_149847_1_, p_149847_2_ - 1, p_149847_3_, p_149847_4_) ? true : (this.canBlockCatchFire(p_149847_1_, p_149847_2_, p_149847_3_ - 1, p_149847_4_) ? true : (this.canBlockCatchFire(p_149847_1_, p_149847_2_, p_149847_3_ + 1, p_149847_4_) ? true : (this.canBlockCatchFire(p_149847_1_, p_149847_2_, p_149847_3_, p_149847_4_ - 1) ? true : this.canBlockCatchFire(p_149847_1_, p_149847_2_, p_149847_3_, p_149847_4_ + 1)))));
    }

    private int getChanceOfNeighborsEncouragingFire(World p_149845_1_, int p_149845_2_, int p_149845_3_, int p_149845_4_)
    {
        byte var5 = 0;

        if (!p_149845_1_.isAirBlock(p_149845_2_, p_149845_3_, p_149845_4_))
        {
            return 0;
        }
        else
        {
            int var6 = this.func_149846_a(p_149845_1_, p_149845_2_ + 1, p_149845_3_, p_149845_4_, var5);
            var6 = this.func_149846_a(p_149845_1_, p_149845_2_ - 1, p_149845_3_, p_149845_4_, var6);
            var6 = this.func_149846_a(p_149845_1_, p_149845_2_, p_149845_3_ - 1, p_149845_4_, var6);
            var6 = this.func_149846_a(p_149845_1_, p_149845_2_, p_149845_3_ + 1, p_149845_4_, var6);
            var6 = this.func_149846_a(p_149845_1_, p_149845_2_, p_149845_3_, p_149845_4_ - 1, var6);
            var6 = this.func_149846_a(p_149845_1_, p_149845_2_, p_149845_3_, p_149845_4_ + 1, var6);
            return var6;
        }
    }

    public boolean isCollidable()
    {
        return false;
    }

    public boolean canBlockCatchFire(IBlockAccess p_149844_1_, int p_149844_2_, int p_149844_3_, int p_149844_4_)
    {
        return this.field_149849_a[Block.getIdFromBlock(p_149844_1_.getBlock(p_149844_2_, p_149844_3_, p_149844_4_))] > 0;
    }

    public int func_149846_a(World p_149846_1_, int p_149846_2_, int p_149846_3_, int p_149846_4_, int p_149846_5_)
    {
        int var6 = this.field_149849_a[Block.getIdFromBlock(p_149846_1_.getBlock(p_149846_2_, p_149846_3_, p_149846_4_))];
        return var6 > p_149846_5_ ? var6 : p_149846_5_;
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) || this.canNeighborBurn(worldIn, x, y, z);
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) && !this.canNeighborBurn(worldIn, x, y, z))
        {
            worldIn.setBlockToAir(x, y, z);
        }
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        if (worldIn.provider.dimensionId > 0 || !Blocks.portal.tryToCreatePortal(worldIn, x, y, z))
        {
            if (!World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) && !this.canNeighborBurn(worldIn, x, y, z))
            {
                worldIn.setBlockToAir(x, y, z);
            }
            else
            {
                worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn) + worldIn.rand.nextInt(10));
            }
        }
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        if (random.nextInt(24) == 0)
        {
            worldIn.playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "fire.fire", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
        }

        int var6;
        float var7;
        float var8;
        float var9;

        if (!World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) && !Blocks.fire.canBlockCatchFire(worldIn, x, y - 1, z))
        {
            if (Blocks.fire.canBlockCatchFire(worldIn, x - 1, y, z))
            {
                for (var6 = 0; var6 < 2; ++var6)
                {
                    var7 = (float)x + random.nextFloat() * 0.1F;
                    var8 = (float)y + random.nextFloat();
                    var9 = (float)z + random.nextFloat();
                    worldIn.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canBlockCatchFire(worldIn, x + 1, y, z))
            {
                for (var6 = 0; var6 < 2; ++var6)
                {
                    var7 = (float)(x + 1) - random.nextFloat() * 0.1F;
                    var8 = (float)y + random.nextFloat();
                    var9 = (float)z + random.nextFloat();
                    worldIn.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canBlockCatchFire(worldIn, x, y, z - 1))
            {
                for (var6 = 0; var6 < 2; ++var6)
                {
                    var7 = (float)x + random.nextFloat();
                    var8 = (float)y + random.nextFloat();
                    var9 = (float)z + random.nextFloat() * 0.1F;
                    worldIn.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canBlockCatchFire(worldIn, x, y, z + 1))
            {
                for (var6 = 0; var6 < 2; ++var6)
                {
                    var7 = (float)x + random.nextFloat();
                    var8 = (float)y + random.nextFloat();
                    var9 = (float)(z + 1) - random.nextFloat() * 0.1F;
                    worldIn.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canBlockCatchFire(worldIn, x, y + 1, z))
            {
                for (var6 = 0; var6 < 2; ++var6)
                {
                    var7 = (float)x + random.nextFloat();
                    var8 = (float)(y + 1) - random.nextFloat() * 0.1F;
                    var9 = (float)z + random.nextFloat();
                    worldIn.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        else
        {
            for (var6 = 0; var6 < 3; ++var6)
            {
                var7 = (float)x + random.nextFloat();
                var8 = (float)y + random.nextFloat() * 0.5F + 0.5F;
                var9 = (float)z + random.nextFloat();
                worldIn.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_149850_M = new IIcon[] {reg.registerIcon(this.getTextureName() + "_layer_0"), reg.registerIcon(this.getTextureName() + "_layer_1")};
    }

    public IIcon getFireIcon(int p_149840_1_)
    {
        return this.field_149850_M[p_149840_1_];
    }

    public IIcon getIcon(int side, int meta)
    {
        return this.field_149850_M[0];
    }

    public MapColor getMapColor(int meta)
    {
        return MapColor.tntColor;
    }
}
