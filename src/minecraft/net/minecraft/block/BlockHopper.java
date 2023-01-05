package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHopper extends BlockContainer
{
    private final Random field_149922_a = new Random();
    private IIcon hopperOutsideIcon;
    private IIcon hopperTopIcon;
    private IIcon hopperInsideIcon;
    private static final String __OBFID = "CL_00000257";

    public BlockHopper()
    {
        super(Material.iron);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        float var8 = 0.125F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, var8, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var8);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        this.setBlockBounds(1.0F - var8, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        this.setBlockBounds(0.0F, 0.0F, 1.0F - var8, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        int var10 = Facing.oppositeSide[side];

        if (var10 == 1)
        {
            var10 = 0;
        }

        return var10;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityHopper();
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        super.onBlockPlacedBy(worldIn, x, y, z, placer, itemIn);

        if (itemIn.hasDisplayName())
        {
            TileEntityHopper var7 = func_149920_e(worldIn, x, y, z);
            var7.func_145886_a(itemIn.getDisplayName());
        }
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);
        this.updateBlockData(worldIn, x, y, z);
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityHopper var10 = func_149920_e(worldIn, x, y, z);

            if (var10 != null)
            {
                player.func_146093_a(var10);
            }

            return true;
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        this.updateBlockData(worldIn, x, y, z);
    }

    private void updateBlockData(World p_149919_1_, int p_149919_2_, int p_149919_3_, int p_149919_4_)
    {
        int var5 = p_149919_1_.getBlockMetadata(p_149919_2_, p_149919_3_, p_149919_4_);
        int var6 = getDirectionFromMetadata(var5);
        boolean var7 = !p_149919_1_.isBlockIndirectlyGettingPowered(p_149919_2_, p_149919_3_, p_149919_4_);
        boolean var8 = getActiveStateFromMetadata(var5);

        if (var7 != var8)
        {
            p_149919_1_.setBlockMetadataWithNotify(p_149919_2_, p_149919_3_, p_149919_4_, var6 | (var7 ? 0 : 8), 4);
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        TileEntityHopper var7 = (TileEntityHopper)worldIn.getTileEntity(x, y, z);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = this.field_149922_a.nextFloat() * 0.8F + 0.1F;
                    float var11 = this.field_149922_a.nextFloat() * 0.8F + 0.1F;
                    float var12 = this.field_149922_a.nextFloat() * 0.8F + 0.1F;

                    while (var9.stackSize > 0)
                    {
                        int var13 = this.field_149922_a.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        EntityItem var14 = new EntityItem(worldIn, (double)((float)x + var10), (double)((float)y + var11), (double)((float)z + var12), new ItemStack(var9.getItem(), var13, var9.getMetadata()));

                        if (var9.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                        }

                        float var15 = 0.05F;
                        var14.motionX = (double)((float)this.field_149922_a.nextGaussian() * var15);
                        var14.motionY = (double)((float)this.field_149922_a.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)this.field_149922_a.nextGaussian() * var15);
                        worldIn.spawnEntityInWorld(var14);
                    }
                }
            }

            worldIn.updateNeighborsAboutBlockChange(x, y, z, blockBroken);
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public int getRenderType()
    {
        return 38;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return true;
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.hopperTopIcon : this.hopperOutsideIcon;
    }

    public static int getDirectionFromMetadata(int p_149918_0_)
    {
        return p_149918_0_ & 7;
    }

    public static boolean getActiveStateFromMetadata(int p_149917_0_)
    {
        return (p_149917_0_ & 8) != 8;
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side)
    {
        return Container.calcRedstoneFromInventory(func_149920_e(worldIn, x, y, z));
    }

    public void registerIcons(IIconRegister reg)
    {
        this.hopperOutsideIcon = reg.registerIcon("hopper_outside");
        this.hopperTopIcon = reg.registerIcon("hopper_top");
        this.hopperInsideIcon = reg.registerIcon("hopper_inside");
    }

    public static IIcon getHopperIcon(String p_149916_0_)
    {
        return p_149916_0_.equals("hopper_outside") ? Blocks.hopper.hopperOutsideIcon : (p_149916_0_.equals("hopper_inside") ? Blocks.hopper.hopperInsideIcon : null);
    }

    public String getItemIconName()
    {
        return "hopper";
    }

    public static TileEntityHopper func_149920_e(IBlockAccess p_149920_0_, int p_149920_1_, int p_149920_2_, int p_149920_3_)
    {
        return (TileEntityHopper)p_149920_0_.getTileEntity(p_149920_1_, p_149920_2_, p_149920_3_);
    }
}
