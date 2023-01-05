package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTallGrass extends BlockBush implements IGrowable
{
    private static final String[] field_149871_a = new String[] {"deadbush", "tallgrass", "fern"};
    private IIcon[] field_149870_b;
    private static final String __OBFID = "CL_00000321";

    protected BlockTallGrass()
    {
        super(Material.vine);
        float var1 = 0.4F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.8F, 0.5F + var1);
    }

    public IIcon getIcon(int side, int meta)
    {
        if (meta >= this.field_149870_b.length)
        {
            meta = 0;
        }

        return this.field_149870_b[meta];
    }

    public int getBlockColor()
    {
        double var1 = 0.5D;
        double var3 = 1.0D;
        return ColorizerGrass.getGrassColor(var1, var3);
    }

    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        return this.canPlaceBlockOn(worldIn.getBlock(x, y - 1, z));
    }

    public int getRenderColor(int meta)
    {
        return meta == 0 ? 16777215 : ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }

    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        return var5 == 0 ? 16777215 : worldIn.getBiomeGenForCoords(x, z).getBiomeGrassColor(x, y, z);
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return random.nextInt(8) == 0 ? Items.wheat_seeds : null;
    }

    public int quantityDroppedWithBonus(int maxBonus, Random random)
    {
        return 1 + random.nextInt(maxBonus * 2 + 1);
    }

    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta)
    {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears)
        {
            player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
            this.dropBlockAsItem(worldIn, x, y, z, new ItemStack(Blocks.tallgrass, 1, meta));
        }
        else
        {
            super.harvestBlock(worldIn, player, x, y, z, meta);
        }
    }

    public int getDamageValue(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlockMetadata(x, y, z);
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int var4 = 1; var4 < 3; ++var4)
        {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_149870_b = new IIcon[field_149871_a.length];

        for (int var2 = 0; var2 < this.field_149870_b.length; ++var2)
        {
            this.field_149870_b[var2] = reg.registerIcon(field_149871_a[var2]);
        }
    }

    public boolean canFertilize(World worldIn, int x, int y, int z, boolean isClient)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);
        return var6 != 0;
    }

    public boolean shouldFertilize(World worldIn, Random random, int x, int y, int z)
    {
        return true;
    }

    public void fertilize(World worldIn, Random random, int x, int y, int z)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);
        byte var7 = 2;

        if (var6 == 2)
        {
            var7 = 3;
        }

        if (Blocks.double_plant.canPlaceBlockAt(worldIn, x, y, z))
        {
            Blocks.double_plant.func_149889_c(worldIn, x, y, z, var7, 2);
        }
    }
}
