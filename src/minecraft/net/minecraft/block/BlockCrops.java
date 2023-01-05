package net.minecraft.block;

import java.util.Random;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCrops extends BlockBush implements IGrowable
{
    private IIcon[] icons;
    private static final String __OBFID = "CL_00000222";

    protected BlockCrops()
    {
        this.setTickRandomly(true);
        float var1 = 0.5F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F, 0.5F + var1);
        this.setCreativeTab((CreativeTabs)null);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.disableStats();
    }

    protected boolean canPlaceBlockOn(Block ground)
    {
        return ground == Blocks.farmland;
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        super.updateTick(worldIn, x, y, z, random);

        if (worldIn.getBlockLightValue(x, y + 1, z) >= 9)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);

            if (var6 < 7)
            {
                float var7 = this.func_149864_n(worldIn, x, y, z);

                if (random.nextInt((int)(25.0F / var7) + 1) == 0)
                {
                    ++var6;
                    worldIn.setBlockMetadataWithNotify(x, y, z, var6, 2);
                }
            }
        }
    }

    public void fertilize(World p_149863_1_, int p_149863_2_, int p_149863_3_, int p_149863_4_)
    {
        int var5 = p_149863_1_.getBlockMetadata(p_149863_2_, p_149863_3_, p_149863_4_) + MathHelper.getRandomIntegerInRange(p_149863_1_.rand, 2, 5);

        if (var5 > 7)
        {
            var5 = 7;
        }

        p_149863_1_.setBlockMetadataWithNotify(p_149863_2_, p_149863_3_, p_149863_4_, var5, 2);
    }

    private float func_149864_n(World p_149864_1_, int p_149864_2_, int p_149864_3_, int p_149864_4_)
    {
        float var5 = 1.0F;
        Block var6 = p_149864_1_.getBlock(p_149864_2_, p_149864_3_, p_149864_4_ - 1);
        Block var7 = p_149864_1_.getBlock(p_149864_2_, p_149864_3_, p_149864_4_ + 1);
        Block var8 = p_149864_1_.getBlock(p_149864_2_ - 1, p_149864_3_, p_149864_4_);
        Block var9 = p_149864_1_.getBlock(p_149864_2_ + 1, p_149864_3_, p_149864_4_);
        Block var10 = p_149864_1_.getBlock(p_149864_2_ - 1, p_149864_3_, p_149864_4_ - 1);
        Block var11 = p_149864_1_.getBlock(p_149864_2_ + 1, p_149864_3_, p_149864_4_ - 1);
        Block var12 = p_149864_1_.getBlock(p_149864_2_ + 1, p_149864_3_, p_149864_4_ + 1);
        Block var13 = p_149864_1_.getBlock(p_149864_2_ - 1, p_149864_3_, p_149864_4_ + 1);
        boolean var14 = var8 == this || var9 == this;
        boolean var15 = var6 == this || var7 == this;
        boolean var16 = var10 == this || var11 == this || var12 == this || var13 == this;

        for (int var17 = p_149864_2_ - 1; var17 <= p_149864_2_ + 1; ++var17)
        {
            for (int var18 = p_149864_4_ - 1; var18 <= p_149864_4_ + 1; ++var18)
            {
                float var19 = 0.0F;

                if (p_149864_1_.getBlock(var17, p_149864_3_ - 1, var18) == Blocks.farmland)
                {
                    var19 = 1.0F;

                    if (p_149864_1_.getBlockMetadata(var17, p_149864_3_ - 1, var18) > 0)
                    {
                        var19 = 3.0F;
                    }
                }

                if (var17 != p_149864_2_ || var18 != p_149864_4_)
                {
                    var19 /= 4.0F;
                }

                var5 += var19;
            }
        }

        if (var16 || var14 && var15)
        {
            var5 /= 2.0F;
        }

        return var5;
    }

    public IIcon getIcon(int side, int meta)
    {
        if (meta < 0 || meta > 7)
        {
            meta = 7;
        }

        return this.icons[meta];
    }

    public int getRenderType()
    {
        return 6;
    }

    protected Item getSeed()
    {
        return Items.wheat_seeds;
    }

    protected Item getCrop()
    {
        return Items.wheat;
    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, x, y, z, meta, chance, 0);

        if (!worldIn.isRemote)
        {
            if (meta >= 7)
            {
                int var8 = 3 + fortune;

                for (int var9 = 0; var9 < var8; ++var9)
                {
                    if (worldIn.rand.nextInt(15) <= meta)
                    {
                        this.dropBlockAsItem(worldIn, x, y, z, new ItemStack(this.getSeed(), 1, 0));
                    }
                }
            }
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return meta == 7 ? this.getCrop() : this.getSeed();
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return this.getSeed();
    }

    public void registerIcons(IIconRegister reg)
    {
        this.icons = new IIcon[8];

        for (int var2 = 0; var2 < this.icons.length; ++var2)
        {
            this.icons[var2] = reg.registerIcon(this.getTextureName() + "_stage_" + var2);
        }
    }

    public boolean canFertilize(World worldIn, int x, int y, int z, boolean isClient)
    {
        return worldIn.getBlockMetadata(x, y, z) != 7;
    }

    public boolean shouldFertilize(World worldIn, Random random, int x, int y, int z)
    {
        return true;
    }

    public void fertilize(World worldIn, Random random, int x, int y, int z)
    {
        this.fertilize(worldIn, x, y, z);
    }
}
