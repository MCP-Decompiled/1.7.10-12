package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSkull extends BlockContainer
{
    private static final String __OBFID = "CL_00000307";

    protected BlockSkull()
    {
        super(Material.circuits);
        this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
    }

    public int getRenderType()
    {
        return -1;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z) & 7;

        switch (var5)
        {
            case 1:
            default:
                this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
                break;

            case 2:
                this.setBlockBounds(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
                break;

            case 3:
                this.setBlockBounds(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
                break;

            case 4:
                this.setBlockBounds(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
                break;

            case 5:
                this.setBlockBounds(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.getCollisionBoundingBoxFromPool(worldIn, x, y, z);
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int var7 = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        worldIn.setBlockMetadataWithNotify(x, y, z, var7, 2);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySkull();
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.skull;
    }

    public int getDamageValue(World worldIn, int x, int y, int z)
    {
        TileEntity var5 = worldIn.getTileEntity(x, y, z);
        return var5 != null && var5 instanceof TileEntitySkull ? ((TileEntitySkull)var5).getSkullType() : super.getDamageValue(worldIn, x, y, z);
    }

    public int damageDropped(int meta)
    {
        return meta;
    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune) {}

    public void onBlockHarvested(World worldIn, int x, int y, int z, int meta, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode)
        {
            meta |= 8;
            worldIn.setBlockMetadataWithNotify(x, y, z, meta, 4);
        }

        super.onBlockHarvested(worldIn, x, y, z, meta, player);
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        if (!worldIn.isRemote)
        {
            if ((meta & 8) == 0)
            {
                ItemStack var7 = new ItemStack(Items.skull, 1, this.getDamageValue(worldIn, x, y, z));
                TileEntitySkull var8 = (TileEntitySkull)worldIn.getTileEntity(x, y, z);

                if (var8.getSkullType() == 3 && var8.func_152108_a() != null)
                {
                    var7.setTagCompound(new NBTTagCompound());
                    NBTTagCompound var9 = new NBTTagCompound();
                    NBTUtil.writeGameProfileToNBT(var9, var8.func_152108_a());
                    var7.getTagCompound().setTag("SkullOwner", var9);
                }

                this.dropBlockAsItem(worldIn, x, y, z, var7);
            }

            super.breakBlock(worldIn, x, y, z, blockBroken, meta);
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.skull;
    }

    public void makeWither(World p_149965_1_, int p_149965_2_, int p_149965_3_, int p_149965_4_, TileEntitySkull p_149965_5_)
    {
        if (p_149965_5_.getSkullType() == 1 && p_149965_3_ >= 2 && p_149965_1_.difficultySetting != EnumDifficulty.PEACEFUL && !p_149965_1_.isRemote)
        {
            int var6;
            EntityWither var7;
            Iterator var8;
            EntityPlayer var9;
            int var10;

            for (var6 = -2; var6 <= 0; ++var6)
            {
                if (p_149965_1_.getBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + var6) == Blocks.soul_sand && p_149965_1_.getBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + var6 + 1) == Blocks.soul_sand && p_149965_1_.getBlock(p_149965_2_, p_149965_3_ - 2, p_149965_4_ + var6 + 1) == Blocks.soul_sand && p_149965_1_.getBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + var6 + 2) == Blocks.soul_sand && this.func_149966_a(p_149965_1_, p_149965_2_, p_149965_3_, p_149965_4_ + var6, 1) && this.func_149966_a(p_149965_1_, p_149965_2_, p_149965_3_, p_149965_4_ + var6 + 1, 1) && this.func_149966_a(p_149965_1_, p_149965_2_, p_149965_3_, p_149965_4_ + var6 + 2, 1))
                {
                    p_149965_1_.setBlockMetadataWithNotify(p_149965_2_, p_149965_3_, p_149965_4_ + var6, 8, 2);
                    p_149965_1_.setBlockMetadataWithNotify(p_149965_2_, p_149965_3_, p_149965_4_ + var6 + 1, 8, 2);
                    p_149965_1_.setBlockMetadataWithNotify(p_149965_2_, p_149965_3_, p_149965_4_ + var6 + 2, 8, 2);
                    p_149965_1_.setBlock(p_149965_2_, p_149965_3_, p_149965_4_ + var6, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_, p_149965_3_, p_149965_4_ + var6 + 1, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_, p_149965_3_, p_149965_4_ + var6 + 2, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + var6, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + var6 + 1, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + var6 + 2, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_, p_149965_3_ - 2, p_149965_4_ + var6 + 1, getBlockById(0), 0, 2);

                    if (!p_149965_1_.isRemote)
                    {
                        var7 = new EntityWither(p_149965_1_);
                        var7.setLocationAndAngles((double)p_149965_2_ + 0.5D, (double)p_149965_3_ - 1.45D, (double)(p_149965_4_ + var6) + 1.5D, 90.0F, 0.0F);
                        var7.renderYawOffset = 90.0F;
                        var7.func_82206_m();

                        if (!p_149965_1_.isRemote)
                        {
                            var8 = p_149965_1_.getEntitiesWithinAABB(EntityPlayer.class, var7.boundingBox.expand(50.0D, 50.0D, 50.0D)).iterator();

                            while (var8.hasNext())
                            {
                                var9 = (EntityPlayer)var8.next();
                                var9.triggerAchievement(AchievementList.spawnWither);
                            }
                        }

                        p_149965_1_.spawnEntityInWorld(var7);
                    }

                    for (var10 = 0; var10 < 120; ++var10)
                    {
                        p_149965_1_.spawnParticle("snowballpoof", (double)p_149965_2_ + p_149965_1_.rand.nextDouble(), (double)(p_149965_3_ - 2) + p_149965_1_.rand.nextDouble() * 3.9D, (double)(p_149965_4_ + var6 + 1) + p_149965_1_.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                    }

                    p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_, p_149965_4_ + var6, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_, p_149965_4_ + var6 + 1, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_, p_149965_4_ + var6 + 2, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + var6, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + var6 + 1, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + var6 + 2, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_ - 2, p_149965_4_ + var6 + 1, getBlockById(0));
                    return;
                }
            }

            for (var6 = -2; var6 <= 0; ++var6)
            {
                if (p_149965_1_.getBlock(p_149965_2_ + var6, p_149965_3_ - 1, p_149965_4_) == Blocks.soul_sand && p_149965_1_.getBlock(p_149965_2_ + var6 + 1, p_149965_3_ - 1, p_149965_4_) == Blocks.soul_sand && p_149965_1_.getBlock(p_149965_2_ + var6 + 1, p_149965_3_ - 2, p_149965_4_) == Blocks.soul_sand && p_149965_1_.getBlock(p_149965_2_ + var6 + 2, p_149965_3_ - 1, p_149965_4_) == Blocks.soul_sand && this.func_149966_a(p_149965_1_, p_149965_2_ + var6, p_149965_3_, p_149965_4_, 1) && this.func_149966_a(p_149965_1_, p_149965_2_ + var6 + 1, p_149965_3_, p_149965_4_, 1) && this.func_149966_a(p_149965_1_, p_149965_2_ + var6 + 2, p_149965_3_, p_149965_4_, 1))
                {
                    p_149965_1_.setBlockMetadataWithNotify(p_149965_2_ + var6, p_149965_3_, p_149965_4_, 8, 2);
                    p_149965_1_.setBlockMetadataWithNotify(p_149965_2_ + var6 + 1, p_149965_3_, p_149965_4_, 8, 2);
                    p_149965_1_.setBlockMetadataWithNotify(p_149965_2_ + var6 + 2, p_149965_3_, p_149965_4_, 8, 2);
                    p_149965_1_.setBlock(p_149965_2_ + var6, p_149965_3_, p_149965_4_, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_ + var6 + 1, p_149965_3_, p_149965_4_, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_ + var6 + 2, p_149965_3_, p_149965_4_, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_ + var6, p_149965_3_ - 1, p_149965_4_, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_ + var6 + 1, p_149965_3_ - 1, p_149965_4_, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_ + var6 + 2, p_149965_3_ - 1, p_149965_4_, getBlockById(0), 0, 2);
                    p_149965_1_.setBlock(p_149965_2_ + var6 + 1, p_149965_3_ - 2, p_149965_4_, getBlockById(0), 0, 2);

                    if (!p_149965_1_.isRemote)
                    {
                        var7 = new EntityWither(p_149965_1_);
                        var7.setLocationAndAngles((double)(p_149965_2_ + var6) + 1.5D, (double)p_149965_3_ - 1.45D, (double)p_149965_4_ + 0.5D, 0.0F, 0.0F);
                        var7.func_82206_m();

                        if (!p_149965_1_.isRemote)
                        {
                            var8 = p_149965_1_.getEntitiesWithinAABB(EntityPlayer.class, var7.boundingBox.expand(50.0D, 50.0D, 50.0D)).iterator();

                            while (var8.hasNext())
                            {
                                var9 = (EntityPlayer)var8.next();
                                var9.triggerAchievement(AchievementList.spawnWither);
                            }
                        }

                        p_149965_1_.spawnEntityInWorld(var7);
                    }

                    for (var10 = 0; var10 < 120; ++var10)
                    {
                        p_149965_1_.spawnParticle("snowballpoof", (double)(p_149965_2_ + var6 + 1) + p_149965_1_.rand.nextDouble(), (double)(p_149965_3_ - 2) + p_149965_1_.rand.nextDouble() * 3.9D, (double)p_149965_4_ + p_149965_1_.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                    }

                    p_149965_1_.notifyBlockChange(p_149965_2_ + var6, p_149965_3_, p_149965_4_, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_ + var6 + 1, p_149965_3_, p_149965_4_, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_ + var6 + 2, p_149965_3_, p_149965_4_, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_ + var6, p_149965_3_ - 1, p_149965_4_, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_ + var6 + 1, p_149965_3_ - 1, p_149965_4_, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_ + var6 + 2, p_149965_3_ - 1, p_149965_4_, getBlockById(0));
                    p_149965_1_.notifyBlockChange(p_149965_2_ + var6 + 1, p_149965_3_ - 2, p_149965_4_, getBlockById(0));
                    return;
                }
            }
        }
    }

    private boolean func_149966_a(World p_149966_1_, int p_149966_2_, int p_149966_3_, int p_149966_4_, int p_149966_5_)
    {
        if (p_149966_1_.getBlock(p_149966_2_, p_149966_3_, p_149966_4_) != this)
        {
            return false;
        }
        else
        {
            TileEntity var6 = p_149966_1_.getTileEntity(p_149966_2_, p_149966_3_, p_149966_4_);
            return var6 != null && var6 instanceof TileEntitySkull ? ((TileEntitySkull)var6).getSkullType() == p_149966_5_ : false;
        }
    }

    public void registerIcons(IIconRegister reg) {}

    public IIcon getIcon(int side, int meta)
    {
        return Blocks.soul_sand.getBlockTextureFromSide(side);
    }

    public String getItemIconName()
    {
        return this.getTextureName() + "_" + ItemSkull.field_94587_a[0];
    }
}
