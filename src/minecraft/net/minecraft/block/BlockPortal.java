package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortal extends BlockBreakable
{
    public static final int[][] field_150001_a = new int[][] {new int[0], {3, 1}, {2, 0}};
    private static final String __OBFID = "CL_00000284";

    public BlockPortal()
    {
        super("portal", Material.portal, false);
        this.setTickRandomly(true);
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        super.updateTick(worldIn, x, y, z, random);

        if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getGameRuleBooleanValue("doMobSpawning") && random.nextInt(2000) < worldIn.difficultySetting.getDifficultyId())
        {
            int var6;

            for (var6 = y; !World.doesBlockHaveSolidTopSurface(worldIn, x, var6, z) && var6 > 0; --var6)
            {
                ;
            }

            if (var6 > 0 && !worldIn.getBlock(x, var6 + 1, z).isNormalCube())
            {
                Entity var7 = ItemMonsterPlacer.spawnCreature(worldIn, 57, (double)x + 0.5D, (double)var6 + 1.1D, (double)z + 0.5D);

                if (var7 != null)
                {
                    var7.timeUntilPortal = var7.getPortalCooldown();
                }
            }
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return null;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = func_149999_b(worldIn.getBlockMetadata(x, y, z));

        if (var5 == 0)
        {
            if (worldIn.getBlock(x - 1, y, z) != this && worldIn.getBlock(x + 1, y, z) != this)
            {
                var5 = 2;
            }
            else
            {
                var5 = 1;
            }

            if (worldIn instanceof World && !((World)worldIn).isRemote)
            {
                ((World)worldIn).setBlockMetadataWithNotify(x, y, z, var5, 2);
            }
        }

        float var6 = 0.125F;
        float var7 = 0.125F;

        if (var5 == 1)
        {
            var6 = 0.5F;
        }

        if (var5 == 2)
        {
            var7 = 0.5F;
        }

        this.setBlockBounds(0.5F - var6, 0.0F, 0.5F - var7, 0.5F + var6, 1.0F, 0.5F + var7);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean tryToCreatePortal(World p_150000_1_, int p_150000_2_, int p_150000_3_, int p_150000_4_)
    {
        BlockPortal.Size var5 = new BlockPortal.Size(p_150000_1_, p_150000_2_, p_150000_3_, p_150000_4_, 1);
        BlockPortal.Size var6 = new BlockPortal.Size(p_150000_1_, p_150000_2_, p_150000_3_, p_150000_4_, 2);

        if (var5.func_150860_b() && var5.field_150864_e == 0)
        {
            var5.func_150859_c();
            return true;
        }
        else if (var6.func_150860_b() && var6.field_150864_e == 0)
        {
            var6.func_150859_c();
            return true;
        }
        else
        {
            return false;
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        int var6 = func_149999_b(worldIn.getBlockMetadata(x, y, z));
        BlockPortal.Size var7 = new BlockPortal.Size(worldIn, x, y, z, 1);
        BlockPortal.Size var8 = new BlockPortal.Size(worldIn, x, y, z, 2);

        if (var6 == 1 && (!var7.func_150860_b() || var7.field_150864_e < var7.field_150868_h * var7.field_150862_g))
        {
            worldIn.setBlock(x, y, z, Blocks.air);
        }
        else if (var6 == 2 && (!var8.func_150860_b() || var8.field_150864_e < var8.field_150868_h * var8.field_150862_g))
        {
            worldIn.setBlock(x, y, z, Blocks.air);
        }
        else if (var6 == 0 && !var7.func_150860_b() && !var8.func_150860_b())
        {
            worldIn.setBlock(x, y, z, Blocks.air);
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        int var6 = 0;

        if (worldIn.getBlock(x, y, z) == this)
        {
            var6 = func_149999_b(worldIn.getBlockMetadata(x, y, z));

            if (var6 == 0)
            {
                return false;
            }

            if (var6 == 2 && side != 5 && side != 4)
            {
                return false;
            }

            if (var6 == 1 && side != 3 && side != 2)
            {
                return false;
            }
        }

        boolean var7 = worldIn.getBlock(x - 1, y, z) == this && worldIn.getBlock(x - 2, y, z) != this;
        boolean var8 = worldIn.getBlock(x + 1, y, z) == this && worldIn.getBlock(x + 2, y, z) != this;
        boolean var9 = worldIn.getBlock(x, y, z - 1) == this && worldIn.getBlock(x, y, z - 2) != this;
        boolean var10 = worldIn.getBlock(x, y, z + 1) == this && worldIn.getBlock(x, y, z + 2) != this;
        boolean var11 = var7 || var8 || var6 == 1;
        boolean var12 = var9 || var10 || var6 == 2;
        return var11 && side == 4 ? true : (var11 && side == 5 ? true : (var12 && side == 2 ? true : var12 && side == 3));
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public int getRenderBlockPass()
    {
        return 1;
    }

    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn)
    {
        if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null)
        {
            entityIn.setInPortal();
        }
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        if (random.nextInt(100) == 0)
        {
            worldIn.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "portal.portal", 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int var6 = 0; var6 < 4; ++var6)
        {
            double var7 = (double)((float)x + random.nextFloat());
            double var9 = (double)((float)y + random.nextFloat());
            double var11 = (double)((float)z + random.nextFloat());
            double var13 = 0.0D;
            double var15 = 0.0D;
            double var17 = 0.0D;
            int var19 = random.nextInt(2) * 2 - 1;
            var13 = ((double)random.nextFloat() - 0.5D) * 0.5D;
            var15 = ((double)random.nextFloat() - 0.5D) * 0.5D;
            var17 = ((double)random.nextFloat() - 0.5D) * 0.5D;

            if (worldIn.getBlock(x - 1, y, z) != this && worldIn.getBlock(x + 1, y, z) != this)
            {
                var7 = (double)x + 0.5D + 0.25D * (double)var19;
                var13 = (double)(random.nextFloat() * 2.0F * (float)var19);
            }
            else
            {
                var11 = (double)z + 0.5D + 0.25D * (double)var19;
                var17 = (double)(random.nextFloat() * 2.0F * (float)var19);
            }

            worldIn.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
        }
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Item.getItemById(0);
    }

    public static int func_149999_b(int p_149999_0_)
    {
        return p_149999_0_ & 3;
    }

    public static class Size
    {
        private final World field_150867_a;
        private final int field_150865_b;
        private final int field_150866_c;
        private final int field_150863_d;
        private int field_150864_e = 0;
        private ChunkCoordinates field_150861_f;
        private int field_150862_g;
        private int field_150868_h;
        private static final String __OBFID = "CL_00000285";

        public Size(World p_i45415_1_, int p_i45415_2_, int p_i45415_3_, int p_i45415_4_, int p_i45415_5_)
        {
            this.field_150867_a = p_i45415_1_;
            this.field_150865_b = p_i45415_5_;
            this.field_150863_d = BlockPortal.field_150001_a[p_i45415_5_][0];
            this.field_150866_c = BlockPortal.field_150001_a[p_i45415_5_][1];

            for (int var6 = p_i45415_3_; p_i45415_3_ > var6 - 21 && p_i45415_3_ > 0 && this.func_150857_a(p_i45415_1_.getBlock(p_i45415_2_, p_i45415_3_ - 1, p_i45415_4_)); --p_i45415_3_)
            {
                ;
            }

            int var7 = this.func_150853_a(p_i45415_2_, p_i45415_3_, p_i45415_4_, this.field_150863_d) - 1;

            if (var7 >= 0)
            {
                this.field_150861_f = new ChunkCoordinates(p_i45415_2_ + var7 * Direction.offsetX[this.field_150863_d], p_i45415_3_, p_i45415_4_ + var7 * Direction.offsetZ[this.field_150863_d]);
                this.field_150868_h = this.func_150853_a(this.field_150861_f.posX, this.field_150861_f.posY, this.field_150861_f.posZ, this.field_150866_c);

                if (this.field_150868_h < 2 || this.field_150868_h > 21)
                {
                    this.field_150861_f = null;
                    this.field_150868_h = 0;
                }
            }

            if (this.field_150861_f != null)
            {
                this.field_150862_g = this.func_150858_a();
            }
        }

        protected int func_150853_a(int p_150853_1_, int p_150853_2_, int p_150853_3_, int p_150853_4_)
        {
            int var6 = Direction.offsetX[p_150853_4_];
            int var7 = Direction.offsetZ[p_150853_4_];
            int var5;
            Block var8;

            for (var5 = 0; var5 < 22; ++var5)
            {
                var8 = this.field_150867_a.getBlock(p_150853_1_ + var6 * var5, p_150853_2_, p_150853_3_ + var7 * var5);

                if (!this.func_150857_a(var8))
                {
                    break;
                }

                Block var9 = this.field_150867_a.getBlock(p_150853_1_ + var6 * var5, p_150853_2_ - 1, p_150853_3_ + var7 * var5);

                if (var9 != Blocks.obsidian)
                {
                    break;
                }
            }

            var8 = this.field_150867_a.getBlock(p_150853_1_ + var6 * var5, p_150853_2_, p_150853_3_ + var7 * var5);
            return var8 == Blocks.obsidian ? var5 : 0;
        }

        protected int func_150858_a()
        {
            int var1;
            int var2;
            int var3;
            int var4;
            label56:

            for (this.field_150862_g = 0; this.field_150862_g < 21; ++this.field_150862_g)
            {
                var1 = this.field_150861_f.posY + this.field_150862_g;

                for (var2 = 0; var2 < this.field_150868_h; ++var2)
                {
                    var3 = this.field_150861_f.posX + var2 * Direction.offsetX[BlockPortal.field_150001_a[this.field_150865_b][1]];
                    var4 = this.field_150861_f.posZ + var2 * Direction.offsetZ[BlockPortal.field_150001_a[this.field_150865_b][1]];
                    Block var5 = this.field_150867_a.getBlock(var3, var1, var4);

                    if (!this.func_150857_a(var5))
                    {
                        break label56;
                    }

                    if (var5 == Blocks.portal)
                    {
                        ++this.field_150864_e;
                    }

                    if (var2 == 0)
                    {
                        var5 = this.field_150867_a.getBlock(var3 + Direction.offsetX[BlockPortal.field_150001_a[this.field_150865_b][0]], var1, var4 + Direction.offsetZ[BlockPortal.field_150001_a[this.field_150865_b][0]]);

                        if (var5 != Blocks.obsidian)
                        {
                            break label56;
                        }
                    }
                    else if (var2 == this.field_150868_h - 1)
                    {
                        var5 = this.field_150867_a.getBlock(var3 + Direction.offsetX[BlockPortal.field_150001_a[this.field_150865_b][1]], var1, var4 + Direction.offsetZ[BlockPortal.field_150001_a[this.field_150865_b][1]]);

                        if (var5 != Blocks.obsidian)
                        {
                            break label56;
                        }
                    }
                }
            }

            for (var1 = 0; var1 < this.field_150868_h; ++var1)
            {
                var2 = this.field_150861_f.posX + var1 * Direction.offsetX[BlockPortal.field_150001_a[this.field_150865_b][1]];
                var3 = this.field_150861_f.posY + this.field_150862_g;
                var4 = this.field_150861_f.posZ + var1 * Direction.offsetZ[BlockPortal.field_150001_a[this.field_150865_b][1]];

                if (this.field_150867_a.getBlock(var2, var3, var4) != Blocks.obsidian)
                {
                    this.field_150862_g = 0;
                    break;
                }
            }

            if (this.field_150862_g <= 21 && this.field_150862_g >= 3)
            {
                return this.field_150862_g;
            }
            else
            {
                this.field_150861_f = null;
                this.field_150868_h = 0;
                this.field_150862_g = 0;
                return 0;
            }
        }

        protected boolean func_150857_a(Block p_150857_1_)
        {
            return p_150857_1_.blockMaterial == Material.air || p_150857_1_ == Blocks.fire || p_150857_1_ == Blocks.portal;
        }

        public boolean func_150860_b()
        {
            return this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21 && this.field_150862_g >= 3 && this.field_150862_g <= 21;
        }

        public void func_150859_c()
        {
            for (int var1 = 0; var1 < this.field_150868_h; ++var1)
            {
                int var2 = this.field_150861_f.posX + Direction.offsetX[this.field_150866_c] * var1;
                int var3 = this.field_150861_f.posZ + Direction.offsetZ[this.field_150866_c] * var1;

                for (int var4 = 0; var4 < this.field_150862_g; ++var4)
                {
                    int var5 = this.field_150861_f.posY + var4;
                    this.field_150867_a.setBlock(var2, var5, var3, Blocks.portal, this.field_150865_b, 2);
                }
            }
        }
    }
}
