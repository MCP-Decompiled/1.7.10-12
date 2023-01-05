package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.world.World;

public class BlockFlowerPot extends BlockContainer
{
    private static final String __OBFID = "CL_00000247";

    public BlockFlowerPot()
    {
        super(Material.circuits);
        this.setBlockBoundsForItemRender();
    }

    public void setBlockBoundsForItemRender()
    {
        float var1 = 0.375F;
        float var2 = var1 / 2.0F;
        this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var1, 0.5F + var2);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int getRenderType()
    {
        return 33;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        ItemStack var10 = player.inventory.getCurrentItem();

        if (var10 != null && var10.getItem() instanceof ItemBlock)
        {
            TileEntityFlowerPot var11 = this.func_149929_e(worldIn, x, y, z);

            if (var11 != null)
            {
                if (var11.getFlowerPotItem() != null)
                {
                    return false;
                }
                else
                {
                    Block var12 = Block.getBlockFromItem(var10.getItem());

                    if (!this.func_149928_a(var12, var10.getMetadata()))
                    {
                        return false;
                    }
                    else
                    {
                        var11.func_145964_a(var10.getItem(), var10.getMetadata());
                        var11.markDirty();

                        if (!worldIn.setBlockMetadataWithNotify(x, y, z, var10.getMetadata(), 2))
                        {
                            worldIn.markBlockForUpdate(x, y, z);
                        }

                        if (!player.capabilities.isCreativeMode && --var10.stackSize <= 0)
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                        }

                        return true;
                    }
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private boolean func_149928_a(Block p_149928_1_, int p_149928_2_)
    {
        return p_149928_1_ != Blocks.yellow_flower && p_149928_1_ != Blocks.red_flower && p_149928_1_ != Blocks.cactus && p_149928_1_ != Blocks.brown_mushroom && p_149928_1_ != Blocks.red_mushroom && p_149928_1_ != Blocks.sapling && p_149928_1_ != Blocks.deadbush ? p_149928_1_ == Blocks.tallgrass && p_149928_2_ == 2 : true;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        TileEntityFlowerPot var5 = this.func_149929_e(worldIn, x, y, z);
        return var5 != null && var5.getFlowerPotItem() != null ? var5.getFlowerPotItem() : Items.flower_pot;
    }

    public int getDamageValue(World worldIn, int x, int y, int z)
    {
        TileEntityFlowerPot var5 = this.func_149929_e(worldIn, x, y, z);
        return var5 != null && var5.getFlowerPotItem() != null ? var5.getFlowerPotData() : 0;
    }

    public boolean isFlowerPot()
    {
        return true;
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return super.canPlaceBlockAt(worldIn, x, y, z) && World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z);
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z))
        {
            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlockToAir(x, y, z);
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        TileEntityFlowerPot var7 = this.func_149929_e(worldIn, x, y, z);

        if (var7 != null && var7.getFlowerPotItem() != null)
        {
            this.dropBlockAsItem(worldIn, x, y, z, new ItemStack(var7.getFlowerPotItem(), 1, var7.getFlowerPotData()));
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public void onBlockHarvested(World worldIn, int x, int y, int z, int meta, EntityPlayer player)
    {
        super.onBlockHarvested(worldIn, x, y, z, meta, player);

        if (player.capabilities.isCreativeMode)
        {
            TileEntityFlowerPot var7 = this.func_149929_e(worldIn, x, y, z);

            if (var7 != null)
            {
                var7.func_145964_a(Item.getItemById(0), 0);
            }
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.flower_pot;
    }

    private TileEntityFlowerPot func_149929_e(World p_149929_1_, int p_149929_2_, int p_149929_3_, int p_149929_4_)
    {
        TileEntity var5 = p_149929_1_.getTileEntity(p_149929_2_, p_149929_3_, p_149929_4_);
        return var5 != null && var5 instanceof TileEntityFlowerPot ? (TileEntityFlowerPot)var5 : null;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        Object var3 = null;
        byte var4 = 0;

        switch (meta)
        {
            case 1:
                var3 = Blocks.red_flower;
                var4 = 0;
                break;

            case 2:
                var3 = Blocks.yellow_flower;
                break;

            case 3:
                var3 = Blocks.sapling;
                var4 = 0;
                break;

            case 4:
                var3 = Blocks.sapling;
                var4 = 1;
                break;

            case 5:
                var3 = Blocks.sapling;
                var4 = 2;
                break;

            case 6:
                var3 = Blocks.sapling;
                var4 = 3;
                break;

            case 7:
                var3 = Blocks.red_mushroom;
                break;

            case 8:
                var3 = Blocks.brown_mushroom;
                break;

            case 9:
                var3 = Blocks.cactus;
                break;

            case 10:
                var3 = Blocks.deadbush;
                break;

            case 11:
                var3 = Blocks.tallgrass;
                var4 = 2;
                break;

            case 12:
                var3 = Blocks.sapling;
                var4 = 4;
                break;

            case 13:
                var3 = Blocks.sapling;
                var4 = 5;
        }

        return new TileEntityFlowerPot(Item.getItemFromBlock((Block)var3), var4);
    }
}
