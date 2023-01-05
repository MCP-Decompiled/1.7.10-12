package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockSapling extends BlockBush implements IGrowable
{
    public static final String[] field_149882_a = new String[] {"oak", "spruce", "birch", "jungle", "acacia", "roofed_oak"};
    private static final IIcon[] field_149881_b = new IIcon[field_149882_a.length];
    private static final String __OBFID = "CL_00000305";

    protected BlockSapling()
    {
        float var1 = 0.4F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var1 * 2.0F, 0.5F + var1);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, x, y, z, random);

            if (worldIn.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(7) == 0)
            {
                this.markOrGrowMarked(worldIn, x, y, z, random);
            }
        }
    }

    public IIcon getIcon(int side, int meta)
    {
        meta &= 7;
        return field_149881_b[MathHelper.clamp_int(meta, 0, 5)];
    }

    public void markOrGrowMarked(World p_149879_1_, int p_149879_2_, int p_149879_3_, int p_149879_4_, Random p_149879_5_)
    {
        int var6 = p_149879_1_.getBlockMetadata(p_149879_2_, p_149879_3_, p_149879_4_);

        if ((var6 & 8) == 0)
        {
            p_149879_1_.setBlockMetadataWithNotify(p_149879_2_, p_149879_3_, p_149879_4_, var6 | 8, 4);
        }
        else
        {
            this.growTree(p_149879_1_, p_149879_2_, p_149879_3_, p_149879_4_, p_149879_5_);
        }
    }

    public void growTree(World p_149878_1_, int p_149878_2_, int p_149878_3_, int p_149878_4_, Random p_149878_5_)
    {
        int var6 = p_149878_1_.getBlockMetadata(p_149878_2_, p_149878_3_, p_149878_4_) & 7;
        Object var7 = p_149878_5_.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        int var8 = 0;
        int var9 = 0;
        boolean var10 = false;

        switch (var6)
        {
            case 0:
            default:
                break;

            case 1:
                label78:
                for (var8 = 0; var8 >= -1; --var8)
                {
                    for (var9 = 0; var9 >= -1; --var9)
                    {
                        if (this.func_149880_a(p_149878_1_, p_149878_2_ + var8, p_149878_3_, p_149878_4_ + var9, 1) && this.func_149880_a(p_149878_1_, p_149878_2_ + var8 + 1, p_149878_3_, p_149878_4_ + var9, 1) && this.func_149880_a(p_149878_1_, p_149878_2_ + var8, p_149878_3_, p_149878_4_ + var9 + 1, 1) && this.func_149880_a(p_149878_1_, p_149878_2_ + var8 + 1, p_149878_3_, p_149878_4_ + var9 + 1, 1))
                        {
                            var7 = new WorldGenMegaPineTree(false, p_149878_5_.nextBoolean());
                            var10 = true;
                            break label78;
                        }
                    }
                }

                if (!var10)
                {
                    var9 = 0;
                    var8 = 0;
                    var7 = new WorldGenTaiga2(true);
                }

                break;

            case 2:
                var7 = new WorldGenForest(true, false);
                break;

            case 3:
                label93:
                for (var8 = 0; var8 >= -1; --var8)
                {
                    for (var9 = 0; var9 >= -1; --var9)
                    {
                        if (this.func_149880_a(p_149878_1_, p_149878_2_ + var8, p_149878_3_, p_149878_4_ + var9, 3) && this.func_149880_a(p_149878_1_, p_149878_2_ + var8 + 1, p_149878_3_, p_149878_4_ + var9, 3) && this.func_149880_a(p_149878_1_, p_149878_2_ + var8, p_149878_3_, p_149878_4_ + var9 + 1, 3) && this.func_149880_a(p_149878_1_, p_149878_2_ + var8 + 1, p_149878_3_, p_149878_4_ + var9 + 1, 3))
                        {
                            var7 = new WorldGenMegaJungle(true, 10, 20, 3, 3);
                            var10 = true;
                            break label93;
                        }
                    }
                }

                if (!var10)
                {
                    var9 = 0;
                    var8 = 0;
                    var7 = new WorldGenTrees(true, 4 + p_149878_5_.nextInt(7), 3, 3, false);
                }

                break;

            case 4:
                var7 = new WorldGenSavannaTree(true);
                break;

            case 5:
                label108:
                for (var8 = 0; var8 >= -1; --var8)
                {
                    for (var9 = 0; var9 >= -1; --var9)
                    {
                        if (this.func_149880_a(p_149878_1_, p_149878_2_ + var8, p_149878_3_, p_149878_4_ + var9, 5) && this.func_149880_a(p_149878_1_, p_149878_2_ + var8 + 1, p_149878_3_, p_149878_4_ + var9, 5) && this.func_149880_a(p_149878_1_, p_149878_2_ + var8, p_149878_3_, p_149878_4_ + var9 + 1, 5) && this.func_149880_a(p_149878_1_, p_149878_2_ + var8 + 1, p_149878_3_, p_149878_4_ + var9 + 1, 5))
                        {
                            var7 = new WorldGenCanopyTree(true);
                            var10 = true;
                            break label108;
                        }
                    }
                }

                if (!var10)
                {
                    return;
                }
        }

        Block var11 = Blocks.air;

        if (var10)
        {
            p_149878_1_.setBlock(p_149878_2_ + var8, p_149878_3_, p_149878_4_ + var9, var11, 0, 4);
            p_149878_1_.setBlock(p_149878_2_ + var8 + 1, p_149878_3_, p_149878_4_ + var9, var11, 0, 4);
            p_149878_1_.setBlock(p_149878_2_ + var8, p_149878_3_, p_149878_4_ + var9 + 1, var11, 0, 4);
            p_149878_1_.setBlock(p_149878_2_ + var8 + 1, p_149878_3_, p_149878_4_ + var9 + 1, var11, 0, 4);
        }
        else
        {
            p_149878_1_.setBlock(p_149878_2_, p_149878_3_, p_149878_4_, var11, 0, 4);
        }

        if (!((WorldGenerator)var7).generate(p_149878_1_, p_149878_5_, p_149878_2_ + var8, p_149878_3_, p_149878_4_ + var9))
        {
            if (var10)
            {
                p_149878_1_.setBlock(p_149878_2_ + var8, p_149878_3_, p_149878_4_ + var9, this, var6, 4);
                p_149878_1_.setBlock(p_149878_2_ + var8 + 1, p_149878_3_, p_149878_4_ + var9, this, var6, 4);
                p_149878_1_.setBlock(p_149878_2_ + var8, p_149878_3_, p_149878_4_ + var9 + 1, this, var6, 4);
                p_149878_1_.setBlock(p_149878_2_ + var8 + 1, p_149878_3_, p_149878_4_ + var9 + 1, this, var6, 4);
            }
            else
            {
                p_149878_1_.setBlock(p_149878_2_, p_149878_3_, p_149878_4_, this, var6, 4);
            }
        }
    }

    public boolean func_149880_a(World p_149880_1_, int p_149880_2_, int p_149880_3_, int p_149880_4_, int p_149880_5_)
    {
        return p_149880_1_.getBlock(p_149880_2_, p_149880_3_, p_149880_4_) == this && (p_149880_1_.getBlockMetadata(p_149880_2_, p_149880_3_, p_149880_4_) & 7) == p_149880_5_;
    }

    public int damageDropped(int meta)
    {
        return MathHelper.clamp_int(meta & 7, 0, 5);
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
        list.add(new ItemStack(itemIn, 1, 2));
        list.add(new ItemStack(itemIn, 1, 3));
        list.add(new ItemStack(itemIn, 1, 4));
        list.add(new ItemStack(itemIn, 1, 5));
    }

    public void registerIcons(IIconRegister reg)
    {
        for (int var2 = 0; var2 < field_149881_b.length; ++var2)
        {
            field_149881_b[var2] = reg.registerIcon(this.getTextureName() + "_" + field_149882_a[var2]);
        }
    }

    public boolean canFertilize(World worldIn, int x, int y, int z, boolean isClient)
    {
        return true;
    }

    public boolean shouldFertilize(World worldIn, Random random, int x, int y, int z)
    {
        return (double)worldIn.rand.nextFloat() < 0.45D;
    }

    public void fertilize(World worldIn, Random random, int x, int y, int z)
    {
        this.markOrGrowMarked(worldIn, x, y, z, random);
    }
}
