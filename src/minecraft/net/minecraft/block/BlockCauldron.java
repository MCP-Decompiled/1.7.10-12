package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCauldron extends Block
{
    private IIcon iconInner;
    private IIcon iconTop;
    private IIcon iconBottom;
    private static final String __OBFID = "CL_00000213";

    public BlockCauldron()
    {
        super(Material.iron);
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.iconTop : (side == 0 ? this.iconBottom : this.blockIcon);
    }

    public void registerIcons(IIconRegister reg)
    {
        this.iconInner = reg.registerIcon(this.getTextureName() + "_" + "inner");
        this.iconTop = reg.registerIcon(this.getTextureName() + "_top");
        this.iconBottom = reg.registerIcon(this.getTextureName() + "_" + "bottom");
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
    }

    public static IIcon getCauldronIcon(String iconName)
    {
        return iconName.equals("inner") ? Blocks.cauldron.iconInner : (iconName.equals("bottom") ? Blocks.cauldron.iconBottom : null);
    }

    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
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
        this.setBlockBoundsForItemRender();
    }

    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int getRenderType()
    {
        return 24;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn)
    {
        int var6 = getPowerFromMeta(worldIn.getBlockMetadata(x, y, z));
        float var7 = (float)y + (6.0F + (float)(3 * var6)) / 16.0F;

        if (!worldIn.isRemote && entityIn.isBurning() && var6 > 0 && entityIn.boundingBox.minY <= (double)var7)
        {
            entityIn.extinguish();
            this.setWaterLevel(worldIn, x, y, z, var6 - 1);
        }
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            ItemStack var10 = player.inventory.getCurrentItem();

            if (var10 == null)
            {
                return true;
            }
            else
            {
                int var11 = worldIn.getBlockMetadata(x, y, z);
                int var12 = getPowerFromMeta(var11);

                if (var10.getItem() == Items.water_bucket)
                {
                    if (var12 < 3)
                    {
                        if (!player.capabilities.isCreativeMode)
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
                        }

                        this.setWaterLevel(worldIn, x, y, z, 3);
                    }

                    return true;
                }
                else
                {
                    if (var10.getItem() == Items.glass_bottle)
                    {
                        if (var12 > 0)
                        {
                            if (!player.capabilities.isCreativeMode)
                            {
                                ItemStack var13 = new ItemStack(Items.potionitem, 1, 0);

                                if (!player.inventory.addItemStackToInventory(var13))
                                {
                                    worldIn.spawnEntityInWorld(new EntityItem(worldIn, (double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D, var13));
                                }
                                else if (player instanceof EntityPlayerMP)
                                {
                                    ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                                }

                                --var10.stackSize;

                                if (var10.stackSize <= 0)
                                {
                                    player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                                }
                            }

                            this.setWaterLevel(worldIn, x, y, z, var12 - 1);
                        }
                    }
                    else if (var12 > 0 && var10.getItem() instanceof ItemArmor && ((ItemArmor)var10.getItem()).getArmorMaterial() == ItemArmor.ArmorMaterial.CLOTH)
                    {
                        ItemArmor var14 = (ItemArmor)var10.getItem();
                        var14.removeColor(var10);
                        this.setWaterLevel(worldIn, x, y, z, var12 - 1);
                        return true;
                    }

                    return false;
                }
            }
        }
    }

    public void setWaterLevel(World worldIn, int x, int y, int z, int level)
    {
        worldIn.setBlockMetadataWithNotify(x, y, z, MathHelper.clamp_int(level, 0, 3), 2);
        worldIn.updateNeighborsAboutBlockChange(x, y, z, this);
    }

    public void fillWithRain(World worldIn, int x, int y, int z)
    {
        if (worldIn.rand.nextInt(20) == 1)
        {
            int var5 = worldIn.getBlockMetadata(x, y, z);

            if (var5 < 3)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, var5 + 1, 2);
            }
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.cauldron;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.cauldron;
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);
        return getPowerFromMeta(var6);
    }

    public static int getPowerFromMeta(int meta)
    {
        return meta;
    }

    public static float getRenderLiquidLevel(int meta)
    {
        int var1 = MathHelper.clamp_int(meta, 0, 3);
        return (float)(6 + 3 * var1) / 16.0F;
    }
}
