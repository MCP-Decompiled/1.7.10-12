package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockBrewingStand extends BlockContainer
{
    private Random rand = new Random();
    private IIcon iconBrewingStandBase;
    private static final String __OBFID = "CL_00000207";

    public BlockBrewingStand()
    {
        super(Material.iron);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int getRenderType()
    {
        return 25;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBrewingStand();
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider)
    {
        this.setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        this.setBlockBoundsForItemRender();
        super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
    }

    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityBrewingStand var10 = (TileEntityBrewingStand)worldIn.getTileEntity(x, y, z);

            if (var10 != null)
            {
                player.func_146098_a(var10);
            }

            return true;
        }
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        if (itemIn.hasDisplayName())
        {
            ((TileEntityBrewingStand)worldIn.getTileEntity(x, y, z)).func_145937_a(itemIn.getDisplayName());
        }
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        double var6 = (double)((float)x + 0.4F + random.nextFloat() * 0.2F);
        double var8 = (double)((float)y + 0.7F + random.nextFloat() * 0.3F);
        double var10 = (double)((float)z + 0.4F + random.nextFloat() * 0.2F);
        worldIn.spawnParticle("smoke", var6, var8, var10, 0.0D, 0.0D, 0.0D);
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        TileEntity var7 = worldIn.getTileEntity(x, y, z);

        if (var7 instanceof TileEntityBrewingStand)
        {
            TileEntityBrewingStand var8 = (TileEntityBrewingStand)var7;

            for (int var9 = 0; var9 < var8.getSizeInventory(); ++var9)
            {
                ItemStack var10 = var8.getStackInSlot(var9);

                if (var10 != null)
                {
                    float var11 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var12 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var13 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (var10.stackSize > 0)
                    {
                        int var14 = this.rand.nextInt(21) + 10;

                        if (var14 > var10.stackSize)
                        {
                            var14 = var10.stackSize;
                        }

                        var10.stackSize -= var14;
                        EntityItem var15 = new EntityItem(worldIn, (double)((float)x + var11), (double)((float)y + var12), (double)((float)z + var13), new ItemStack(var10.getItem(), var14, var10.getMetadata()));
                        float var16 = 0.05F;
                        var15.motionX = (double)((float)this.rand.nextGaussian() * var16);
                        var15.motionY = (double)((float)this.rand.nextGaussian() * var16 + 0.2F);
                        var15.motionZ = (double)((float)this.rand.nextGaussian() * var16);
                        worldIn.spawnEntityInWorld(var15);
                    }
                }
            }
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.brewing_stand;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.brewing_stand;
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side)
    {
        return Container.calcRedstoneFromInventory((IInventory)worldIn.getTileEntity(x, y, z));
    }

    public void registerIcons(IIconRegister reg)
    {
        super.registerIcons(reg);
        this.iconBrewingStandBase = reg.registerIcon(this.getTextureName() + "_base");
    }

    public IIcon getIconBrewingStandBase()
    {
        return this.iconBrewingStandBase;
    }
}
