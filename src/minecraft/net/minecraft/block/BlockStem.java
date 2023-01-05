package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStem extends BlockBush implements IGrowable
{
    private final Block field_149877_a;
    private IIcon field_149876_b;
    private static final String __OBFID = "CL_00000316";

    protected BlockStem(Block p_i45430_1_)
    {
        this.field_149877_a = p_i45430_1_;
        this.setTickRandomly(true);
        float var2 = 0.125F;
        this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, 0.25F, 0.5F + var2);
        this.setCreativeTab((CreativeTabs)null);
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
            float var6 = this.func_149875_n(worldIn, x, y, z);

            if (random.nextInt((int)(25.0F / var6) + 1) == 0)
            {
                int var7 = worldIn.getBlockMetadata(x, y, z);

                if (var7 < 7)
                {
                    ++var7;
                    worldIn.setBlockMetadataWithNotify(x, y, z, var7, 2);
                }
                else
                {
                    if (worldIn.getBlock(x - 1, y, z) == this.field_149877_a)
                    {
                        return;
                    }

                    if (worldIn.getBlock(x + 1, y, z) == this.field_149877_a)
                    {
                        return;
                    }

                    if (worldIn.getBlock(x, y, z - 1) == this.field_149877_a)
                    {
                        return;
                    }

                    if (worldIn.getBlock(x, y, z + 1) == this.field_149877_a)
                    {
                        return;
                    }

                    int var8 = random.nextInt(4);
                    int var9 = x;
                    int var10 = z;

                    if (var8 == 0)
                    {
                        var9 = x - 1;
                    }

                    if (var8 == 1)
                    {
                        ++var9;
                    }

                    if (var8 == 2)
                    {
                        var10 = z - 1;
                    }

                    if (var8 == 3)
                    {
                        ++var10;
                    }

                    Block var11 = worldIn.getBlock(var9, y - 1, var10);

                    if (worldIn.getBlock(var9, y, var10).blockMaterial == Material.air && (var11 == Blocks.farmland || var11 == Blocks.dirt || var11 == Blocks.grass))
                    {
                        worldIn.setBlock(var9, y, var10, this.field_149877_a);
                    }
                }
            }
        }
    }

    public void fertilizeStem(World p_149874_1_, int p_149874_2_, int p_149874_3_, int p_149874_4_)
    {
        int var5 = p_149874_1_.getBlockMetadata(p_149874_2_, p_149874_3_, p_149874_4_) + MathHelper.getRandomIntegerInRange(p_149874_1_.rand, 2, 5);

        if (var5 > 7)
        {
            var5 = 7;
        }

        p_149874_1_.setBlockMetadataWithNotify(p_149874_2_, p_149874_3_, p_149874_4_, var5, 2);
    }

    private float func_149875_n(World p_149875_1_, int p_149875_2_, int p_149875_3_, int p_149875_4_)
    {
        float var5 = 1.0F;
        Block var6 = p_149875_1_.getBlock(p_149875_2_, p_149875_3_, p_149875_4_ - 1);
        Block var7 = p_149875_1_.getBlock(p_149875_2_, p_149875_3_, p_149875_4_ + 1);
        Block var8 = p_149875_1_.getBlock(p_149875_2_ - 1, p_149875_3_, p_149875_4_);
        Block var9 = p_149875_1_.getBlock(p_149875_2_ + 1, p_149875_3_, p_149875_4_);
        Block var10 = p_149875_1_.getBlock(p_149875_2_ - 1, p_149875_3_, p_149875_4_ - 1);
        Block var11 = p_149875_1_.getBlock(p_149875_2_ + 1, p_149875_3_, p_149875_4_ - 1);
        Block var12 = p_149875_1_.getBlock(p_149875_2_ + 1, p_149875_3_, p_149875_4_ + 1);
        Block var13 = p_149875_1_.getBlock(p_149875_2_ - 1, p_149875_3_, p_149875_4_ + 1);
        boolean var14 = var8 == this || var9 == this;
        boolean var15 = var6 == this || var7 == this;
        boolean var16 = var10 == this || var11 == this || var12 == this || var13 == this;

        for (int var17 = p_149875_2_ - 1; var17 <= p_149875_2_ + 1; ++var17)
        {
            for (int var18 = p_149875_4_ - 1; var18 <= p_149875_4_ + 1; ++var18)
            {
                Block var19 = p_149875_1_.getBlock(var17, p_149875_3_ - 1, var18);
                float var20 = 0.0F;

                if (var19 == Blocks.farmland)
                {
                    var20 = 1.0F;

                    if (p_149875_1_.getBlockMetadata(var17, p_149875_3_ - 1, var18) > 0)
                    {
                        var20 = 3.0F;
                    }
                }

                if (var17 != p_149875_2_ || var18 != p_149875_4_)
                {
                    var20 /= 4.0F;
                }

                var5 += var20;
            }
        }

        if (var16 || var14 && var15)
        {
            var5 /= 2.0F;
        }

        return var5;
    }

    public int getRenderColor(int meta)
    {
        int var2 = meta * 32;
        int var3 = 255 - meta * 8;
        int var4 = meta * 4;
        return var2 << 16 | var3 << 8 | var4;
    }

    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        return this.getRenderColor(worldIn.getBlockMetadata(x, y, z));
    }

    public void setBlockBoundsForItemRender()
    {
        float var1 = 0.125F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F, 0.5F + var1);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        this.maxY = (double)((float)(worldIn.getBlockMetadata(x, y, z) * 2 + 2) / 16.0F);
        float var5 = 0.125F;
        this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var5, 0.5F + var5, (float)this.maxY, 0.5F + var5);
    }

    public int getRenderType()
    {
        return 19;
    }

    public int getState(IBlockAccess p_149873_1_, int p_149873_2_, int p_149873_3_, int p_149873_4_)
    {
        int var5 = p_149873_1_.getBlockMetadata(p_149873_2_, p_149873_3_, p_149873_4_);
        return var5 < 7 ? -1 : (p_149873_1_.getBlock(p_149873_2_ - 1, p_149873_3_, p_149873_4_) == this.field_149877_a ? 0 : (p_149873_1_.getBlock(p_149873_2_ + 1, p_149873_3_, p_149873_4_) == this.field_149877_a ? 1 : (p_149873_1_.getBlock(p_149873_2_, p_149873_3_, p_149873_4_ - 1) == this.field_149877_a ? 2 : (p_149873_1_.getBlock(p_149873_2_, p_149873_3_, p_149873_4_ + 1) == this.field_149877_a ? 3 : -1))));
    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, x, y, z, meta, chance, fortune);

        if (!worldIn.isRemote)
        {
            Item var8 = null;

            if (this.field_149877_a == Blocks.pumpkin)
            {
                var8 = Items.pumpkin_seeds;
            }

            if (this.field_149877_a == Blocks.melon_block)
            {
                var8 = Items.melon_seeds;
            }

            for (int var9 = 0; var9 < 3; ++var9)
            {
                if (worldIn.rand.nextInt(15) <= meta)
                {
                    this.dropBlockAsItem(worldIn, x, y, z, new ItemStack(var8));
                }
            }
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return null;
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return this.field_149877_a == Blocks.pumpkin ? Items.pumpkin_seeds : (this.field_149877_a == Blocks.melon_block ? Items.melon_seeds : Item.getItemById(0));
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_disconnected");
        this.field_149876_b = reg.registerIcon(this.getTextureName() + "_connected");
    }

    public IIcon getStemIcon()
    {
        return this.field_149876_b;
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
        this.fertilizeStem(worldIn, x, y, z);
    }
}
