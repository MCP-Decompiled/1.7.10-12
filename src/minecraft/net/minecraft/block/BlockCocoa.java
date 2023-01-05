package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCocoa extends BlockDirectional implements IGrowable
{
    private IIcon[] field_149989_a;
    private static final String __OBFID = "CL_00000216";

    public BlockCocoa()
    {
        super(Material.plants);
        this.setTickRandomly(true);
    }

    public IIcon getIcon(int side, int meta)
    {
        return this.field_149989_a[2];
    }

    public IIcon getCocoaIcon(int p_149988_1_)
    {
        if (p_149988_1_ < 0 || p_149988_1_ >= this.field_149989_a.length)
        {
            p_149988_1_ = this.field_149989_a.length - 1;
        }

        return this.field_149989_a[p_149988_1_];
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!this.canBlockStay(worldIn, x, y, z))
        {
            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlock(x, y, z, getBlockById(0), 0, 2);
        }
        else if (worldIn.rand.nextInt(5) == 0)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);
            int var7 = func_149987_c(var6);

            if (var7 < 2)
            {
                ++var7;
                worldIn.setBlockMetadataWithNotify(x, y, z, var7 << 2 | getDirection(var6), 2);
            }
        }
    }

    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        int var5 = getDirection(worldIn.getBlockMetadata(x, y, z));
        x += Direction.offsetX[var5];
        z += Direction.offsetZ[var5];
        Block var6 = worldIn.getBlock(x, y, z);
        return var6 == Blocks.log && BlockLog.func_150165_c(worldIn.getBlockMetadata(x, y, z)) == 3;
    }

    public int getRenderType()
    {
        return 28;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.getCollisionBoundingBoxFromPool(worldIn, x, y, z);
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.getSelectedBoundingBoxFromPool(worldIn, x, y, z);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        int var6 = getDirection(var5);
        int var7 = func_149987_c(var5);
        int var8 = 4 + var7 * 2;
        int var9 = 5 + var7 * 2;
        float var10 = (float)var8 / 2.0F;

        switch (var6)
        {
            case 0:
                this.setBlockBounds((8.0F - var10) / 16.0F, (12.0F - (float)var9) / 16.0F, (15.0F - (float)var8) / 16.0F, (8.0F + var10) / 16.0F, 0.75F, 0.9375F);
                break;

            case 1:
                this.setBlockBounds(0.0625F, (12.0F - (float)var9) / 16.0F, (8.0F - var10) / 16.0F, (1.0F + (float)var8) / 16.0F, 0.75F, (8.0F + var10) / 16.0F);
                break;

            case 2:
                this.setBlockBounds((8.0F - var10) / 16.0F, (12.0F - (float)var9) / 16.0F, 0.0625F, (8.0F + var10) / 16.0F, 0.75F, (1.0F + (float)var8) / 16.0F);
                break;

            case 3:
                this.setBlockBounds((15.0F - (float)var8) / 16.0F, (12.0F - (float)var9) / 16.0F, (8.0F - var10) / 16.0F, 0.9375F, 0.75F, (8.0F + var10) / 16.0F);
        }
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int var7 = ((MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 0) % 4;
        worldIn.setBlockMetadataWithNotify(x, y, z, var7, 2);
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        if (side == 1 || side == 0)
        {
            side = 2;
        }

        return Direction.rotateOpposite[Direction.facingToDirection[side]];
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!this.canBlockStay(worldIn, x, y, z))
        {
            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlock(x, y, z, getBlockById(0), 0, 2);
        }
    }

    public static int func_149987_c(int p_149987_0_)
    {
        return (p_149987_0_ & 12) >> 2;
    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        int var8 = func_149987_c(meta);
        byte var9 = 1;

        if (var8 >= 2)
        {
            var9 = 3;
        }

        for (int var10 = 0; var10 < var9; ++var10)
        {
            this.dropBlockAsItem(worldIn, x, y, z, new ItemStack(Items.dye, 1, 3));
        }
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.dye;
    }

    public int getDamageValue(World worldIn, int x, int y, int z)
    {
        return 3;
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_149989_a = new IIcon[3];

        for (int var2 = 0; var2 < this.field_149989_a.length; ++var2)
        {
            this.field_149989_a[var2] = reg.registerIcon(this.getTextureName() + "_stage_" + var2);
        }
    }

    public boolean canFertilize(World worldIn, int x, int y, int z, boolean isClient)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);
        int var7 = func_149987_c(var6);
        return var7 < 2;
    }

    public boolean shouldFertilize(World worldIn, Random random, int x, int y, int z)
    {
        return true;
    }

    public void fertilize(World worldIn, Random random, int x, int y, int z)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);
        int var7 = BlockDirectional.getDirection(var6);
        int var8 = func_149987_c(var6);
        ++var8;
        worldIn.setBlockMetadataWithNotify(x, y, z, var8 << 2 | var7, 2);
    }
}
