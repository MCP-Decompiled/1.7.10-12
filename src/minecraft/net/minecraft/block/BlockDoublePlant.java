package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoublePlant extends BlockBush implements IGrowable
{
    public static final String[] field_149892_a = new String[] {"sunflower", "syringa", "grass", "fern", "rose", "paeonia"};
    private IIcon[] doublePlantBottomIcons;
    private IIcon[] doublePlantTopIcons;
    public IIcon[] sunflowerIcons;
    private static final String __OBFID = "CL_00000231";

    public BlockDoublePlant()
    {
        super(Material.plants);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.setUnlocalizedName("doublePlant");
    }

    public int getRenderType()
    {
        return 40;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public int func_149885_e(IBlockAccess p_149885_1_, int p_149885_2_, int p_149885_3_, int p_149885_4_)
    {
        int var5 = p_149885_1_.getBlockMetadata(p_149885_2_, p_149885_3_, p_149885_4_);
        return !func_149887_c(var5) ? var5 & 7 : p_149885_1_.getBlockMetadata(p_149885_2_, p_149885_3_ - 1, p_149885_4_) & 7;
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return super.canPlaceBlockAt(worldIn, x, y, z) && worldIn.isAirBlock(x, y + 1, z);
    }

    protected void checkAndDropBlock(World worldIn, int x, int y, int z)
    {
        if (!this.canBlockStay(worldIn, x, y, z))
        {
            int var5 = worldIn.getBlockMetadata(x, y, z);

            if (!func_149887_c(var5))
            {
                this.dropBlockAsItem(worldIn, x, y, z, var5, 0);

                if (worldIn.getBlock(x, y + 1, z) == this)
                {
                    worldIn.setBlock(x, y + 1, z, Blocks.air, 0, 2);
                }
            }

            worldIn.setBlock(x, y, z, Blocks.air, 0, 2);
        }
    }

    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        return func_149887_c(var5) ? worldIn.getBlock(x, y - 1, z) == this : worldIn.getBlock(x, y + 1, z) == this && super.canBlockStay(worldIn, x, y, z);
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        if (func_149887_c(meta))
        {
            return null;
        }
        else
        {
            int var4 = func_149890_d(meta);
            return var4 != 3 && var4 != 2 ? Item.getItemFromBlock(this) : null;
        }
    }

    public int damageDropped(int meta)
    {
        return func_149887_c(meta) ? 0 : meta & 7;
    }

    public static boolean func_149887_c(int p_149887_0_)
    {
        return (p_149887_0_ & 8) != 0;
    }

    public static int func_149890_d(int p_149890_0_)
    {
        return p_149890_0_ & 7;
    }

    public IIcon getIcon(int side, int meta)
    {
        return func_149887_c(meta) ? this.doublePlantBottomIcons[0] : this.doublePlantBottomIcons[meta & 7];
    }

    public IIcon func_149888_a(boolean p_149888_1_, int p_149888_2_)
    {
        return p_149888_1_ ? this.doublePlantTopIcons[p_149888_2_] : this.doublePlantBottomIcons[p_149888_2_];
    }

    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = this.func_149885_e(worldIn, x, y, z);
        return var5 != 2 && var5 != 3 ? 16777215 : worldIn.getBiomeGenForCoords(x, z).getBiomeGrassColor(x, y, z);
    }

    public void func_149889_c(World p_149889_1_, int p_149889_2_, int p_149889_3_, int p_149889_4_, int p_149889_5_, int p_149889_6_)
    {
        p_149889_1_.setBlock(p_149889_2_, p_149889_3_, p_149889_4_, this, p_149889_5_, p_149889_6_);
        p_149889_1_.setBlock(p_149889_2_, p_149889_3_ + 1, p_149889_4_, this, 8, p_149889_6_);
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int var7 = ((MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;
        worldIn.setBlock(x, y + 1, z, this, 8 | var7, 2);
    }

    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta)
    {
        if (worldIn.isRemote || player.getCurrentEquippedItem() == null || player.getCurrentEquippedItem().getItem() != Items.shears || func_149887_c(meta) || !this.func_149886_b(worldIn, x, y, z, meta, player))
        {
            super.harvestBlock(worldIn, player, x, y, z, meta);
        }
    }

    public void onBlockHarvested(World worldIn, int x, int y, int z, int meta, EntityPlayer player)
    {
        if (func_149887_c(meta))
        {
            if (worldIn.getBlock(x, y - 1, z) == this)
            {
                if (!player.capabilities.isCreativeMode)
                {
                    int var7 = worldIn.getBlockMetadata(x, y - 1, z);
                    int var8 = func_149890_d(var7);

                    if (var8 != 3 && var8 != 2)
                    {
                        worldIn.breakBlock(x, y - 1, z, true);
                    }
                    else
                    {
                        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears)
                        {
                            this.func_149886_b(worldIn, x, y, z, var7, player);
                        }

                        worldIn.setBlockToAir(x, y - 1, z);
                    }
                }
                else
                {
                    worldIn.setBlockToAir(x, y - 1, z);
                }
            }
        }
        else if (player.capabilities.isCreativeMode && worldIn.getBlock(x, y + 1, z) == this)
        {
            worldIn.setBlock(x, y + 1, z, Blocks.air, 0, 2);
        }

        super.onBlockHarvested(worldIn, x, y, z, meta, player);
    }

    private boolean func_149886_b(World p_149886_1_, int p_149886_2_, int p_149886_3_, int p_149886_4_, int p_149886_5_, EntityPlayer p_149886_6_)
    {
        int var7 = func_149890_d(p_149886_5_);

        if (var7 != 3 && var7 != 2)
        {
            return false;
        }
        else
        {
            p_149886_6_.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
            byte var8 = 1;

            if (var7 == 3)
            {
                var8 = 2;
            }

            this.dropBlockAsItem(p_149886_1_, p_149886_2_, p_149886_3_, p_149886_4_, new ItemStack(Blocks.tallgrass, 2, var8));
            return true;
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.doublePlantBottomIcons = new IIcon[field_149892_a.length];
        this.doublePlantTopIcons = new IIcon[field_149892_a.length];

        for (int var2 = 0; var2 < this.doublePlantBottomIcons.length; ++var2)
        {
            this.doublePlantBottomIcons[var2] = reg.registerIcon("double_plant_" + field_149892_a[var2] + "_bottom");
            this.doublePlantTopIcons[var2] = reg.registerIcon("double_plant_" + field_149892_a[var2] + "_top");
        }

        this.sunflowerIcons = new IIcon[2];
        this.sunflowerIcons[0] = reg.registerIcon("double_plant_sunflower_front");
        this.sunflowerIcons[1] = reg.registerIcon("double_plant_sunflower_back");
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int var4 = 0; var4 < this.doublePlantBottomIcons.length; ++var4)
        {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    public int getDamageValue(World worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        return func_149887_c(var5) ? func_149890_d(worldIn.getBlockMetadata(x, y - 1, z)) : func_149890_d(var5);
    }

    public boolean canFertilize(World worldIn, int x, int y, int z, boolean isClient)
    {
        int var6 = this.func_149885_e(worldIn, x, y, z);
        return var6 != 2 && var6 != 3;
    }

    public boolean shouldFertilize(World worldIn, Random random, int x, int y, int z)
    {
        return true;
    }

    public void fertilize(World worldIn, Random random, int x, int y, int z)
    {
        int var6 = this.func_149885_e(worldIn, x, y, z);
        this.dropBlockAsItem(worldIn, x, y, z, new ItemStack(this, 1, var6));
    }
}
