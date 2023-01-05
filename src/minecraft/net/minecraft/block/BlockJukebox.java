package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockJukebox extends BlockContainer
{
    private IIcon field_149927_a;
    private static final String __OBFID = "CL_00000260";

    protected BlockJukebox()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.field_149927_a : this.blockIcon;
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.getBlockMetadata(x, y, z) == 0)
        {
            return false;
        }
        else
        {
            this.func_149925_e(worldIn, x, y, z);
            return true;
        }
    }

    public void func_149926_b(World p_149926_1_, int p_149926_2_, int p_149926_3_, int p_149926_4_, ItemStack p_149926_5_)
    {
        if (!p_149926_1_.isRemote)
        {
            BlockJukebox.TileEntityJukebox var6 = (BlockJukebox.TileEntityJukebox)p_149926_1_.getTileEntity(p_149926_2_, p_149926_3_, p_149926_4_);

            if (var6 != null)
            {
                var6.func_145857_a(p_149926_5_.copy());
                p_149926_1_.setBlockMetadataWithNotify(p_149926_2_, p_149926_3_, p_149926_4_, 1, 2);
            }
        }
    }

    public void func_149925_e(World p_149925_1_, int p_149925_2_, int p_149925_3_, int p_149925_4_)
    {
        if (!p_149925_1_.isRemote)
        {
            BlockJukebox.TileEntityJukebox var5 = (BlockJukebox.TileEntityJukebox)p_149925_1_.getTileEntity(p_149925_2_, p_149925_3_, p_149925_4_);

            if (var5 != null)
            {
                ItemStack var6 = var5.func_145856_a();

                if (var6 != null)
                {
                    p_149925_1_.playAuxSFX(1005, p_149925_2_, p_149925_3_, p_149925_4_, 0);
                    p_149925_1_.playRecord((String)null, p_149925_2_, p_149925_3_, p_149925_4_);
                    var5.func_145857_a((ItemStack)null);
                    p_149925_1_.setBlockMetadataWithNotify(p_149925_2_, p_149925_3_, p_149925_4_, 0, 2);
                    float var7 = 0.7F;
                    double var8 = (double)(p_149925_1_.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
                    double var10 = (double)(p_149925_1_.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.2D + 0.6D;
                    double var12 = (double)(p_149925_1_.rand.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
                    ItemStack var14 = var6.copy();
                    EntityItem var15 = new EntityItem(p_149925_1_, (double)p_149925_2_ + var8, (double)p_149925_3_ + var10, (double)p_149925_4_ + var12, var14);
                    var15.delayBeforeCanPickup = 10;
                    p_149925_1_.spawnEntityInWorld(var15);
                }
            }
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        this.func_149925_e(worldIn, x, y, z);
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        if (!worldIn.isRemote)
        {
            super.dropBlockAsItemWithChance(worldIn, x, y, z, meta, chance, 0);
        }
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new BlockJukebox.TileEntityJukebox();
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
        this.field_149927_a = reg.registerIcon(this.getTextureName() + "_top");
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side)
    {
        ItemStack var6 = ((BlockJukebox.TileEntityJukebox)worldIn.getTileEntity(x, y, z)).func_145856_a();
        return var6 == null ? 0 : Item.getIdFromItem(var6.getItem()) + 1 - Item.getIdFromItem(Items.record_13);
    }

    public static class TileEntityJukebox extends TileEntity
    {
        private ItemStack field_145858_a;
        private static final String __OBFID = "CL_00000261";

        public void readFromNBT(NBTTagCompound compound)
        {
            super.readFromNBT(compound);

            if (compound.hasKey("RecordItem", 10))
            {
                this.func_145857_a(ItemStack.loadItemStackFromNBT(compound.getCompoundTag("RecordItem")));
            }
            else if (compound.getInteger("Record") > 0)
            {
                this.func_145857_a(new ItemStack(Item.getItemById(compound.getInteger("Record")), 1, 0));
            }
        }

        public void writeToNBT(NBTTagCompound compound)
        {
            super.writeToNBT(compound);

            if (this.func_145856_a() != null)
            {
                compound.setTag("RecordItem", this.func_145856_a().writeToNBT(new NBTTagCompound()));
                compound.setInteger("Record", Item.getIdFromItem(this.func_145856_a().getItem()));
            }
        }

        public ItemStack func_145856_a()
        {
            return this.field_145858_a;
        }

        public void func_145857_a(ItemStack p_145857_1_)
        {
            this.field_145858_a = p_145857_1_;
            this.markDirty();
        }
    }
}
