package net.minecraft.network;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.material.Material;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerPlayServer implements INetHandlerPlayServer
{
    private static final Logger logger = LogManager.getLogger();
    public final NetworkManager netManager;
    private final MinecraftServer serverController;
    public EntityPlayerMP playerEntity;
    private int networkTickCount;
    private int floatingTickCount;
    private boolean field_147366_g;
    private int field_147378_h;
    private long lastPingTime;
    private static Random field_147376_j = new Random();
    private long lastSentPingPacket;
    private int chatSpamThresholdCount;
    private int itemDropThreshold;
    private IntHashMap field_147372_n = new IntHashMap();
    private double lastPosX;
    private double lastPosY;
    private double lastPosZ;
    private boolean hasMoved = true;
    private static final String __OBFID = "CL_00001452";

    public NetHandlerPlayServer(MinecraftServer server, NetworkManager networkManagerIn, EntityPlayerMP player)
    {
        this.serverController = server;
        this.netManager = networkManagerIn;
        networkManagerIn.setNetHandler(this);
        this.playerEntity = player;
        player.playerNetServerHandler = this;
    }

    public void onNetworkTick()
    {
        this.field_147366_g = false;
        ++this.networkTickCount;
        this.serverController.theProfiler.startSection("keepAlive");

        if ((long)this.networkTickCount - this.lastSentPingPacket > 40L)
        {
            this.lastSentPingPacket = (long)this.networkTickCount;
            this.lastPingTime = this.currentTimeMillis();
            this.field_147378_h = (int)this.lastPingTime;
            this.sendPacket(new S00PacketKeepAlive(this.field_147378_h));
        }

        if (this.chatSpamThresholdCount > 0)
        {
            --this.chatSpamThresholdCount;
        }

        if (this.itemDropThreshold > 0)
        {
            --this.itemDropThreshold;
        }

        if (this.playerEntity.getLastActiveTime() > 0L && this.serverController.getMaxPlayerIdleMinutes() > 0 && MinecraftServer.getCurrentTimeMillis() - this.playerEntity.getLastActiveTime() > (long)(this.serverController.getMaxPlayerIdleMinutes() * 1000 * 60))
        {
            this.kickPlayerFromServer("You have been idle for too long!");
        }
    }

    public NetworkManager getNetworkManager()
    {
        return this.netManager;
    }

    public void kickPlayerFromServer(String reason)
    {
        final ChatComponentText var2 = new ChatComponentText(reason);
        this.netManager.scheduleOutboundPacket(new S40PacketDisconnect(var2), new GenericFutureListener[] {new GenericFutureListener()
            {
                private static final String __OBFID = "CL_00001453";
                public void operationComplete(Future p_operationComplete_1_)
                {
                    NetHandlerPlayServer.this.netManager.closeChannel(var2);
                }
            }
        });
        this.netManager.disableAutoRead();
    }

    public void processInput(C0CPacketInput packetIn)
    {
        this.playerEntity.setEntityActionState(packetIn.getStrafeSpeed(), packetIn.getForwardSpeed(), packetIn.isJumping(), packetIn.isSneaking());
    }

    public void processPlayer(C03PacketPlayer packetIn)
    {
        WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        this.field_147366_g = true;

        if (!this.playerEntity.playerConqueredTheEnd)
        {
            double var3;

            if (!this.hasMoved)
            {
                var3 = packetIn.getPositionY() - this.lastPosY;

                if (packetIn.getPositionX() == this.lastPosX && var3 * var3 < 0.01D && packetIn.getPositionZ() == this.lastPosZ)
                {
                    this.hasMoved = true;
                }
            }

            if (this.hasMoved)
            {
                double var5;
                double var7;
                double var9;

                if (this.playerEntity.ridingEntity != null)
                {
                    float var34 = this.playerEntity.rotationYaw;
                    float var4 = this.playerEntity.rotationPitch;
                    this.playerEntity.ridingEntity.updateRiderPosition();
                    var5 = this.playerEntity.posX;
                    var7 = this.playerEntity.posY;
                    var9 = this.playerEntity.posZ;

                    if (packetIn.getRotating())
                    {
                        var34 = packetIn.getYaw();
                        var4 = packetIn.getPitch();
                    }

                    this.playerEntity.onGround = packetIn.func_149465_i();
                    this.playerEntity.onUpdateEntity();
                    this.playerEntity.yOffset2 = 0.0F;
                    this.playerEntity.setPositionAndRotation(var5, var7, var9, var34, var4);

                    if (this.playerEntity.ridingEntity != null)
                    {
                        this.playerEntity.ridingEntity.updateRiderPosition();
                    }

                    this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);

                    if (this.hasMoved)
                    {
                        this.lastPosX = this.playerEntity.posX;
                        this.lastPosY = this.playerEntity.posY;
                        this.lastPosZ = this.playerEntity.posZ;
                    }

                    var2.updateEntity(this.playerEntity);
                    return;
                }

                if (this.playerEntity.isPlayerSleeping())
                {
                    this.playerEntity.onUpdateEntity();
                    this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                    var2.updateEntity(this.playerEntity);
                    return;
                }

                var3 = this.playerEntity.posY;
                this.lastPosX = this.playerEntity.posX;
                this.lastPosY = this.playerEntity.posY;
                this.lastPosZ = this.playerEntity.posZ;
                var5 = this.playerEntity.posX;
                var7 = this.playerEntity.posY;
                var9 = this.playerEntity.posZ;
                float var11 = this.playerEntity.rotationYaw;
                float var12 = this.playerEntity.rotationPitch;

                if (packetIn.func_149466_j() && packetIn.getPositionY() == -999.0D && packetIn.getStance() == -999.0D)
                {
                    packetIn.func_149469_a(false);
                }

                double var13;

                if (packetIn.func_149466_j())
                {
                    var5 = packetIn.getPositionX();
                    var7 = packetIn.getPositionY();
                    var9 = packetIn.getPositionZ();
                    var13 = packetIn.getStance() - packetIn.getPositionY();

                    if (!this.playerEntity.isPlayerSleeping() && (var13 > 1.65D || var13 < 0.1D))
                    {
                        this.kickPlayerFromServer("Illegal stance");
                        logger.warn(this.playerEntity.getCommandSenderName() + " had an illegal stance: " + var13);
                        return;
                    }

                    if (Math.abs(packetIn.getPositionX()) > 3.2E7D || Math.abs(packetIn.getPositionZ()) > 3.2E7D)
                    {
                        this.kickPlayerFromServer("Illegal position");
                        return;
                    }
                }

                if (packetIn.getRotating())
                {
                    var11 = packetIn.getYaw();
                    var12 = packetIn.getPitch();
                }

                this.playerEntity.onUpdateEntity();
                this.playerEntity.yOffset2 = 0.0F;
                this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, var11, var12);

                if (!this.hasMoved)
                {
                    return;
                }

                var13 = var5 - this.playerEntity.posX;
                double var15 = var7 - this.playerEntity.posY;
                double var17 = var9 - this.playerEntity.posZ;
                double var19 = Math.min(Math.abs(var13), Math.abs(this.playerEntity.motionX));
                double var21 = Math.min(Math.abs(var15), Math.abs(this.playerEntity.motionY));
                double var23 = Math.min(Math.abs(var17), Math.abs(this.playerEntity.motionZ));
                double var25 = var19 * var19 + var21 * var21 + var23 * var23;

                if (var25 > 100.0D && (!this.serverController.isSinglePlayer() || !this.serverController.getServerOwner().equals(this.playerEntity.getCommandSenderName())))
                {
                    logger.warn(this.playerEntity.getCommandSenderName() + " moved too quickly! " + var13 + "," + var15 + "," + var17 + " (" + var19 + ", " + var21 + ", " + var23 + ")");
                    this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                    return;
                }

                float var27 = 0.0625F;
                boolean var28 = var2.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().contract((double)var27, (double)var27, (double)var27)).isEmpty();

                if (this.playerEntity.onGround && !packetIn.func_149465_i() && var15 > 0.0D)
                {
                    this.playerEntity.jump();
                }

                this.playerEntity.moveEntity(var13, var15, var17);
                this.playerEntity.onGround = packetIn.func_149465_i();
                this.playerEntity.addMovementStat(var13, var15, var17);
                double var29 = var15;
                var13 = var5 - this.playerEntity.posX;
                var15 = var7 - this.playerEntity.posY;

                if (var15 > -0.5D || var15 < 0.5D)
                {
                    var15 = 0.0D;
                }

                var17 = var9 - this.playerEntity.posZ;
                var25 = var13 * var13 + var15 * var15 + var17 * var17;
                boolean var31 = false;

                if (var25 > 0.0625D && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.theItemInWorldManager.isCreative())
                {
                    var31 = true;
                    logger.warn(this.playerEntity.getCommandSenderName() + " moved wrongly!");
                }

                this.playerEntity.setPositionAndRotation(var5, var7, var9, var11, var12);
                boolean var32 = var2.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().contract((double)var27, (double)var27, (double)var27)).isEmpty();

                if (var28 && (var31 || !var32) && !this.playerEntity.isPlayerSleeping())
                {
                    this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, var11, var12);
                    return;
                }

                AxisAlignedBB var33 = this.playerEntity.boundingBox.copy().expand((double)var27, (double)var27, (double)var27).addCoord(0.0D, -0.55D, 0.0D);

                if (!this.serverController.isFlightAllowed() && !this.playerEntity.theItemInWorldManager.isCreative() && !var2.checkBlockCollision(var33))
                {
                    if (var29 >= -0.03125D)
                    {
                        ++this.floatingTickCount;

                        if (this.floatingTickCount > 80)
                        {
                            logger.warn(this.playerEntity.getCommandSenderName() + " was kicked for floating too long!");
                            this.kickPlayerFromServer("Flying is not enabled on this server");
                            return;
                        }
                    }
                }
                else
                {
                    this.floatingTickCount = 0;
                }

                this.playerEntity.onGround = packetIn.func_149465_i();
                this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
                this.playerEntity.handleFalling(this.playerEntity.posY - var3, packetIn.func_149465_i());
            }
            else if (this.networkTickCount % 20 == 0)
            {
                this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
            }
        }
    }

    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch)
    {
        this.hasMoved = false;
        this.lastPosX = x;
        this.lastPosY = y;
        this.lastPosZ = z;
        this.playerEntity.setPositionAndRotation(x, y, z, yaw, pitch);
        this.playerEntity.playerNetServerHandler.sendPacket(new S08PacketPlayerPosLook(x, y + 1.6200000047683716D, z, yaw, pitch, false));
    }

    public void processPlayerDigging(C07PacketPlayerDigging packetIn)
    {
        WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        this.playerEntity.markPlayerActive();

        if (packetIn.getDiggedBlockStatus() == 4)
        {
            this.playerEntity.dropOneItem(false);
        }
        else if (packetIn.getDiggedBlockStatus() == 3)
        {
            this.playerEntity.dropOneItem(true);
        }
        else if (packetIn.getDiggedBlockStatus() == 5)
        {
            this.playerEntity.stopUsingItem();
        }
        else
        {
            boolean var3 = false;

            if (packetIn.getDiggedBlockStatus() == 0)
            {
                var3 = true;
            }

            if (packetIn.getDiggedBlockStatus() == 1)
            {
                var3 = true;
            }

            if (packetIn.getDiggedBlockStatus() == 2)
            {
                var3 = true;
            }

            int var4 = packetIn.getDiggedBlockX();
            int var5 = packetIn.getDiggedBlockY();
            int var6 = packetIn.getDiggedBlockZ();

            if (var3)
            {
                double var7 = this.playerEntity.posX - ((double)var4 + 0.5D);
                double var9 = this.playerEntity.posY - ((double)var5 + 0.5D) + 1.5D;
                double var11 = this.playerEntity.posZ - ((double)var6 + 0.5D);
                double var13 = var7 * var7 + var9 * var9 + var11 * var11;

                if (var13 > 36.0D)
                {
                    return;
                }

                if (var5 >= this.serverController.getBuildLimit())
                {
                    return;
                }
            }

            if (packetIn.getDiggedBlockStatus() == 0)
            {
                if (!this.serverController.isBlockProtected(var2, var4, var5, var6, this.playerEntity))
                {
                    this.playerEntity.theItemInWorldManager.onBlockClicked(var4, var5, var6, packetIn.getDiggingBlockFace());
                }
                else
                {
                    this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var4, var5, var6, var2));
                }
            }
            else if (packetIn.getDiggedBlockStatus() == 2)
            {
                this.playerEntity.theItemInWorldManager.blockRemoving(var4, var5, var6);

                if (var2.getBlock(var4, var5, var6).getMaterial() != Material.air)
                {
                    this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var4, var5, var6, var2));
                }
            }
            else if (packetIn.getDiggedBlockStatus() == 1)
            {
                this.playerEntity.theItemInWorldManager.cancelDestroyingBlock(var4, var5, var6);

                if (var2.getBlock(var4, var5, var6).getMaterial() != Material.air)
                {
                    this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var4, var5, var6, var2));
                }
            }
        }
    }

    public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement packetIn)
    {
        WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        ItemStack var3 = this.playerEntity.inventory.getCurrentItem();
        boolean var4 = false;
        int var5 = packetIn.getPlacedBlockX();
        int var6 = packetIn.getPlacedBlockY();
        int var7 = packetIn.getPlacedBlockZ();
        int var8 = packetIn.getPlacedBlockDirection();
        this.playerEntity.markPlayerActive();

        if (packetIn.getPlacedBlockDirection() == 255)
        {
            if (var3 == null)
            {
                return;
            }

            this.playerEntity.theItemInWorldManager.tryUseItem(this.playerEntity, var2, var3);
        }
        else if (packetIn.getPlacedBlockY() >= this.serverController.getBuildLimit() - 1 && (packetIn.getPlacedBlockDirection() == 1 || packetIn.getPlacedBlockY() >= this.serverController.getBuildLimit()))
        {
            ChatComponentTranslation var9 = new ChatComponentTranslation("build.tooHigh", new Object[] {Integer.valueOf(this.serverController.getBuildLimit())});
            var9.getChatStyle().setColor(EnumChatFormatting.RED);
            this.playerEntity.playerNetServerHandler.sendPacket(new S02PacketChat(var9));
            var4 = true;
        }
        else
        {
            if (this.hasMoved && this.playerEntity.getDistanceSq((double)var5 + 0.5D, (double)var6 + 0.5D, (double)var7 + 0.5D) < 64.0D && !this.serverController.isBlockProtected(var2, var5, var6, var7, this.playerEntity))
            {
                this.playerEntity.theItemInWorldManager.activateBlockOrUseItem(this.playerEntity, var2, var3, var5, var6, var7, var8, packetIn.getPlacedBlockOffsetX(), packetIn.getPlacedBlockOffsetY(), packetIn.getPlacedBlockOffsetZ());
            }

            var4 = true;
        }

        if (var4)
        {
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var5, var6, var7, var2));

            if (var8 == 0)
            {
                --var6;
            }

            if (var8 == 1)
            {
                ++var6;
            }

            if (var8 == 2)
            {
                --var7;
            }

            if (var8 == 3)
            {
                ++var7;
            }

            if (var8 == 4)
            {
                --var5;
            }

            if (var8 == 5)
            {
                ++var5;
            }

            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(var5, var6, var7, var2));
        }

        var3 = this.playerEntity.inventory.getCurrentItem();

        if (var3 != null && var3.stackSize == 0)
        {
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = null;
            var3 = null;
        }

        if (var3 == null || var3.getMaxItemUseDuration() == 0)
        {
            this.playerEntity.isChangingQuantityOnly = true;
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = ItemStack.copyItemStack(this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem]);
            Slot var10 = this.playerEntity.openContainer.getSlotFromInventory(this.playerEntity.inventory, this.playerEntity.inventory.currentItem);
            this.playerEntity.openContainer.detectAndSendChanges();
            this.playerEntity.isChangingQuantityOnly = false;

            if (!ItemStack.areItemStacksEqual(this.playerEntity.inventory.getCurrentItem(), packetIn.getStack()))
            {
                this.sendPacket(new S2FPacketSetSlot(this.playerEntity.openContainer.windowId, var10.slotNumber, this.playerEntity.inventory.getCurrentItem()));
            }
        }
    }

    public void onDisconnect(IChatComponent reason)
    {
        logger.info(this.playerEntity.getCommandSenderName() + " lost connection: " + reason);
        this.serverController.refreshStatusNextTick();
        ChatComponentTranslation var2 = new ChatComponentTranslation("multiplayer.player.left", new Object[] {this.playerEntity.getFormattedCommandSenderName()});
        var2.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.serverController.getConfigurationManager().sendChatMsg(var2);
        this.playerEntity.mountEntityAndWakeUp();
        this.serverController.getConfigurationManager().playerLoggedOut(this.playerEntity);

        if (this.serverController.isSinglePlayer() && this.playerEntity.getCommandSenderName().equals(this.serverController.getServerOwner()))
        {
            logger.info("Stopping singleplayer server as player logged out");
            this.serverController.initiateShutdown();
        }
    }

    public void sendPacket(final Packet packetIn)
    {
        if (packetIn instanceof S02PacketChat)
        {
            S02PacketChat var2 = (S02PacketChat)packetIn;
            EntityPlayer.EnumChatVisibility var3 = this.playerEntity.func_147096_v();

            if (var3 == EntityPlayer.EnumChatVisibility.HIDDEN)
            {
                return;
            }

            if (var3 == EntityPlayer.EnumChatVisibility.SYSTEM && !var2.isChat())
            {
                return;
            }
        }

        try
        {
            this.netManager.scheduleOutboundPacket(packetIn, new GenericFutureListener[0]);
        }
        catch (Throwable var5)
        {
            CrashReport var6 = CrashReport.makeCrashReport(var5, "Sending packet");
            CrashReportCategory var4 = var6.makeCategory("Packet being sent");
            var4.addCrashSectionCallable("Packet class", new Callable()
            {
                private static final String __OBFID = "CL_00001454";
                public String call()
                {
                    return packetIn.getClass().getCanonicalName();
                }
            });
            throw new ReportedException(var6);
        }
    }

    public void processHeldItemChange(C09PacketHeldItemChange packetIn)
    {
        if (packetIn.getSlotId() >= 0 && packetIn.getSlotId() < InventoryPlayer.getHotbarSize())
        {
            this.playerEntity.inventory.currentItem = packetIn.getSlotId();
            this.playerEntity.markPlayerActive();
        }
        else
        {
            logger.warn(this.playerEntity.getCommandSenderName() + " tried to set an invalid carried item");
        }
    }

    public void processChatMessage(C01PacketChatMessage packetIn)
    {
        if (this.playerEntity.func_147096_v() == EntityPlayer.EnumChatVisibility.HIDDEN)
        {
            ChatComponentTranslation var4 = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
            var4.getChatStyle().setColor(EnumChatFormatting.RED);
            this.sendPacket(new S02PacketChat(var4));
        }
        else
        {
            this.playerEntity.markPlayerActive();
            String var2 = packetIn.getMessage();
            var2 = StringUtils.normalizeSpace(var2);

            for (int var3 = 0; var3 < var2.length(); ++var3)
            {
                if (!ChatAllowedCharacters.isAllowedCharacter(var2.charAt(var3)))
                {
                    this.kickPlayerFromServer("Illegal characters in chat");
                    return;
                }
            }

            if (var2.startsWith("/"))
            {
                this.handleSlashCommand(var2);
            }
            else
            {
                ChatComponentTranslation var5 = new ChatComponentTranslation("chat.type.text", new Object[] {this.playerEntity.getFormattedCommandSenderName(), var2});
                this.serverController.getConfigurationManager().sendChatMsgImpl(var5, false);
            }

            this.chatSpamThresholdCount += 20;

            if (this.chatSpamThresholdCount > 200 && !this.serverController.getConfigurationManager().canSendCommands(this.playerEntity.getGameProfile()))
            {
                this.kickPlayerFromServer("disconnect.spam");
            }
        }
    }

    private void handleSlashCommand(String command)
    {
        this.serverController.getCommandManager().executeCommand(this.playerEntity, command);
    }

    public void processAnimation(C0APacketAnimation packetIn)
    {
        this.playerEntity.markPlayerActive();

        if (packetIn.getType() == 1)
        {
            this.playerEntity.swingItem();
        }
    }

    public void processEntityAction(C0BPacketEntityAction packetIn)
    {
        this.playerEntity.markPlayerActive();

        if (packetIn.func_149513_d() == 1)
        {
            this.playerEntity.setSneaking(true);
        }
        else if (packetIn.func_149513_d() == 2)
        {
            this.playerEntity.setSneaking(false);
        }
        else if (packetIn.func_149513_d() == 4)
        {
            this.playerEntity.setSprinting(true);
        }
        else if (packetIn.func_149513_d() == 5)
        {
            this.playerEntity.setSprinting(false);
        }
        else if (packetIn.func_149513_d() == 3)
        {
            this.playerEntity.wakeUpPlayer(false, true, true);
            this.hasMoved = false;
        }
        else if (packetIn.func_149513_d() == 6)
        {
            if (this.playerEntity.ridingEntity != null && this.playerEntity.ridingEntity instanceof EntityHorse)
            {
                ((EntityHorse)this.playerEntity.ridingEntity).setJumpPower(packetIn.func_149512_e());
            }
        }
        else if (packetIn.func_149513_d() == 7 && this.playerEntity.ridingEntity != null && this.playerEntity.ridingEntity instanceof EntityHorse)
        {
            ((EntityHorse)this.playerEntity.ridingEntity).openGUI(this.playerEntity);
        }
    }

    public void processUseEntity(C02PacketUseEntity packetIn)
    {
        WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        Entity var3 = packetIn.getEntityFromWorld(var2);
        this.playerEntity.markPlayerActive();

        if (var3 != null)
        {
            boolean var4 = this.playerEntity.canEntityBeSeen(var3);
            double var5 = 36.0D;

            if (!var4)
            {
                var5 = 9.0D;
            }

            if (this.playerEntity.getDistanceSqToEntity(var3) < var5)
            {
                if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT)
                {
                    this.playerEntity.interactWith(var3);
                }
                else if (packetIn.getAction() == C02PacketUseEntity.Action.ATTACK)
                {
                    if (var3 instanceof EntityItem || var3 instanceof EntityXPOrb || var3 instanceof EntityArrow || var3 == this.playerEntity)
                    {
                        this.kickPlayerFromServer("Attempting to attack an invalid entity");
                        this.serverController.logWarning("Player " + this.playerEntity.getCommandSenderName() + " tried to attack an invalid entity");
                        return;
                    }

                    this.playerEntity.attackTargetEntityWithCurrentItem(var3);
                }
            }
        }
    }

    public void processClientStatus(C16PacketClientStatus packetIn)
    {
        this.playerEntity.markPlayerActive();
        C16PacketClientStatus.EnumState var2 = packetIn.getStatus();

        switch (NetHandlerPlayServer.SwitchEnumState.field_151290_a[var2.ordinal()])
        {
            case 1:
                if (this.playerEntity.playerConqueredTheEnd)
                {
                    this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, true);
                }
                else if (this.playerEntity.getServerForPlayer().getWorldInfo().isHardcoreModeEnabled())
                {
                    if (this.serverController.isSinglePlayer() && this.playerEntity.getCommandSenderName().equals(this.serverController.getServerOwner()))
                    {
                        this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it\'s game over!");
                        this.serverController.deleteWorldAndStopServer();
                    }
                    else
                    {
                        UserListBansEntry var3 = new UserListBansEntry(this.playerEntity.getGameProfile(), (Date)null, "(You just lost the game)", (Date)null, "Death in Hardcore");
                        this.serverController.getConfigurationManager().getBannedPlayers().addEntry(var3);
                        this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it\'s game over!");
                    }
                }
                else
                {
                    if (this.playerEntity.getHealth() > 0.0F)
                    {
                        return;
                    }

                    this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, false);
                }

                break;

            case 2:
                this.playerEntity.getStatFile().func_150876_a(this.playerEntity);
                break;

            case 3:
                this.playerEntity.triggerAchievement(AchievementList.openInventory);
        }
    }

    public void processCloseWindow(C0DPacketCloseWindow packetIn)
    {
        this.playerEntity.closeContainer();
    }

    public void processClickWindow(C0EPacketClickWindow packetIn)
    {
        this.playerEntity.markPlayerActive();

        if (this.playerEntity.openContainer.windowId == packetIn.getWindowId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity))
        {
            ItemStack var2 = this.playerEntity.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getMode(), this.playerEntity);

            if (ItemStack.areItemStacksEqual(packetIn.getClickedItem(), var2))
            {
                this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
                this.playerEntity.isChangingQuantityOnly = true;
                this.playerEntity.openContainer.detectAndSendChanges();
                this.playerEntity.updateHeldItem();
                this.playerEntity.isChangingQuantityOnly = false;
            }
            else
            {
                this.field_147372_n.addKey(this.playerEntity.openContainer.windowId, Short.valueOf(packetIn.getActionNumber()));
                this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
                this.playerEntity.openContainer.setCanCraft(this.playerEntity, false);
                ArrayList var3 = new ArrayList();

                for (int var4 = 0; var4 < this.playerEntity.openContainer.inventorySlots.size(); ++var4)
                {
                    var3.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(var4)).getStack());
                }

                this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, var3);
            }
        }
    }

    public void processEnchantItem(C11PacketEnchantItem packetIn)
    {
        this.playerEntity.markPlayerActive();

        if (this.playerEntity.openContainer.windowId == packetIn.getId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity))
        {
            this.playerEntity.openContainer.enchantItem(this.playerEntity, packetIn.getButton());
            this.playerEntity.openContainer.detectAndSendChanges();
        }
    }

    public void processCreativeInventoryAction(C10PacketCreativeInventoryAction packetIn)
    {
        if (this.playerEntity.theItemInWorldManager.isCreative())
        {
            boolean var2 = packetIn.getSlotId() < 0;
            ItemStack var3 = packetIn.getStack();
            boolean var4 = packetIn.getSlotId() >= 1 && packetIn.getSlotId() < 36 + InventoryPlayer.getHotbarSize();
            boolean var5 = var3 == null || var3.getItem() != null;
            boolean var6 = var3 == null || var3.getMetadata() >= 0 && var3.stackSize <= 64 && var3.stackSize > 0;

            if (var4 && var5 && var6)
            {
                if (var3 == null)
                {
                    this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), (ItemStack)null);
                }
                else
                {
                    this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), var3);
                }

                this.playerEntity.inventoryContainer.setCanCraft(this.playerEntity, true);
            }
            else if (var2 && var5 && var6 && this.itemDropThreshold < 200)
            {
                this.itemDropThreshold += 20;
                EntityItem var7 = this.playerEntity.dropPlayerItemWithRandomChoice(var3, true);

                if (var7 != null)
                {
                    var7.setAgeToCreativeDespawnTime();
                }
            }
        }
    }

    public void processConfirmTransaction(C0FPacketConfirmTransaction packetIn)
    {
        Short var2 = (Short)this.field_147372_n.lookup(this.playerEntity.openContainer.windowId);

        if (var2 != null && packetIn.getUid() == var2.shortValue() && this.playerEntity.openContainer.windowId == packetIn.getId() && !this.playerEntity.openContainer.getCanCraft(this.playerEntity))
        {
            this.playerEntity.openContainer.setCanCraft(this.playerEntity, true);
        }
    }

    public void processUpdateSign(C12PacketUpdateSign packetIn)
    {
        this.playerEntity.markPlayerActive();
        WorldServer var2 = this.serverController.worldServerForDimension(this.playerEntity.dimension);

        if (var2.blockExists(packetIn.getX(), packetIn.getY(), packetIn.getZ()))
        {
            TileEntity var3 = var2.getTileEntity(packetIn.getX(), packetIn.getY(), packetIn.getZ());

            if (var3 instanceof TileEntitySign)
            {
                TileEntitySign var4 = (TileEntitySign)var3;

                if (!var4.getIsEditable() || var4.func_145911_b() != this.playerEntity)
                {
                    this.serverController.logWarning("Player " + this.playerEntity.getCommandSenderName() + " just tried to change non-editable sign");
                    return;
                }
            }

            int var6;
            int var8;

            for (var8 = 0; var8 < 4; ++var8)
            {
                boolean var5 = true;

                if (packetIn.getLines()[var8].length() > 15)
                {
                    var5 = false;
                }
                else
                {
                    for (var6 = 0; var6 < packetIn.getLines()[var8].length(); ++var6)
                    {
                        if (!ChatAllowedCharacters.isAllowedCharacter(packetIn.getLines()[var8].charAt(var6)))
                        {
                            var5 = false;
                        }
                    }
                }

                if (!var5)
                {
                    packetIn.getLines()[var8] = "!?";
                }
            }

            if (var3 instanceof TileEntitySign)
            {
                var8 = packetIn.getX();
                int var9 = packetIn.getY();
                var6 = packetIn.getZ();
                TileEntitySign var7 = (TileEntitySign)var3;
                System.arraycopy(packetIn.getLines(), 0, var7.signText, 0, 4);
                var7.markDirty();
                var2.markBlockForUpdate(var8, var9, var6);
            }
        }
    }

    public void processKeepAlive(C00PacketKeepAlive packetIn)
    {
        if (packetIn.getKey() == this.field_147378_h)
        {
            int var2 = (int)(this.currentTimeMillis() - this.lastPingTime);
            this.playerEntity.ping = (this.playerEntity.ping * 3 + var2) / 4;
        }
    }

    private long currentTimeMillis()
    {
        return System.nanoTime() / 1000000L;
    }

    public void processPlayerAbilities(C13PacketPlayerAbilities packetIn)
    {
        this.playerEntity.capabilities.isFlying = packetIn.isFlying() && this.playerEntity.capabilities.allowFlying;
    }

    public void processTabComplete(C14PacketTabComplete packetIn)
    {
        ArrayList var2 = Lists.newArrayList();
        Iterator var3 = this.serverController.getPossibleCompletions(this.playerEntity, packetIn.getMessage()).iterator();

        while (var3.hasNext())
        {
            String var4 = (String)var3.next();
            var2.add(var4);
        }

        this.playerEntity.playerNetServerHandler.sendPacket(new S3APacketTabComplete((String[])var2.toArray(new String[var2.size()])));
    }

    public void processClientSettings(C15PacketClientSettings packetIn)
    {
        this.playerEntity.func_147100_a(packetIn);
    }

    public void processVanilla250Packet(C17PacketCustomPayload packetIn)
    {
        PacketBuffer var2;
        ItemStack var3;
        ItemStack var4;

        if ("MC|BEdit".equals(packetIn.getChannel()))
        {
            var2 = new PacketBuffer(Unpooled.wrappedBuffer(packetIn.getData()));

            try
            {
                var3 = var2.readItemStackFromBuffer();

                if (var3 != null)
                {
                    if (!ItemWritableBook.validBookPageTagContents(var3.getTagCompound()))
                    {
                        throw new IOException("Invalid book tag!");
                    }

                    var4 = this.playerEntity.inventory.getCurrentItem();

                    if (var4 == null)
                    {
                        return;
                    }

                    if (var3.getItem() == Items.writable_book && var3.getItem() == var4.getItem())
                    {
                        var4.setTagInfo("pages", var3.getTagCompound().getTagList("pages", 8));
                    }

                    return;
                }
            }
            catch (Exception var38)
            {
                logger.error("Couldn\'t handle book info", var38);
                return;
            }
            finally
            {
                var2.release();
            }

            return;
        }
        else if ("MC|BSign".equals(packetIn.getChannel()))
        {
            var2 = new PacketBuffer(Unpooled.wrappedBuffer(packetIn.getData()));

            try
            {
                var3 = var2.readItemStackFromBuffer();

                if (var3 == null)
                {
                    return;
                }

                if (!ItemEditableBook.validBookTagContents(var3.getTagCompound()))
                {
                    throw new IOException("Invalid book tag!");
                }

                var4 = this.playerEntity.inventory.getCurrentItem();

                if (var4 != null)
                {
                    if (var3.getItem() == Items.written_book && var4.getItem() == Items.writable_book)
                    {
                        var4.setTagInfo("author", new NBTTagString(this.playerEntity.getCommandSenderName()));
                        var4.setTagInfo("title", new NBTTagString(var3.getTagCompound().getString("title")));
                        var4.setTagInfo("pages", var3.getTagCompound().getTagList("pages", 8));
                        var4.setItem(Items.written_book);
                    }

                    return;
                }
            }
            catch (Exception var36)
            {
                logger.error("Couldn\'t sign book", var36);
                return;
            }
            finally
            {
                var2.release();
            }

            return;
        }
        else
        {
            DataInputStream var40;
            int var42;

            if ("MC|TrSel".equals(packetIn.getChannel()))
            {
                try
                {
                    var40 = new DataInputStream(new ByteArrayInputStream(packetIn.getData()));
                    var42 = var40.readInt();
                    Container var45 = this.playerEntity.openContainer;

                    if (var45 instanceof ContainerMerchant)
                    {
                        ((ContainerMerchant)var45).setCurrentRecipeIndex(var42);
                    }
                }
                catch (Exception var35)
                {
                    logger.error("Couldn\'t select trade", var35);
                }
            }
            else if ("MC|AdvCdm".equals(packetIn.getChannel()))
            {
                if (!this.serverController.isCommandBlockEnabled())
                {
                    this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notEnabled", new Object[0]));
                }
                else if (this.playerEntity.canCommandSenderUseCommand(2, "") && this.playerEntity.capabilities.isCreativeMode)
                {
                    var2 = new PacketBuffer(Unpooled.wrappedBuffer(packetIn.getData()));

                    try
                    {
                        byte var43 = var2.readByte();
                        CommandBlockLogic var46 = null;

                        if (var43 == 0)
                        {
                            TileEntity var5 = this.playerEntity.worldObj.getTileEntity(var2.readInt(), var2.readInt(), var2.readInt());

                            if (var5 instanceof TileEntityCommandBlock)
                            {
                                var46 = ((TileEntityCommandBlock)var5).func_145993_a();
                            }
                        }
                        else if (var43 == 1)
                        {
                            Entity var48 = this.playerEntity.worldObj.getEntityByID(var2.readInt());

                            if (var48 instanceof EntityMinecartCommandBlock)
                            {
                                var46 = ((EntityMinecartCommandBlock)var48).func_145822_e();
                            }
                        }

                        String var49 = var2.readStringFromBuffer(var2.readableBytes());

                        if (var46 != null)
                        {
                            var46.setCommand(var49);
                            var46.func_145756_e();
                            this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.setCommand.success", new Object[] {var49}));
                        }
                    }
                    catch (Exception var33)
                    {
                        logger.error("Couldn\'t set command block", var33);
                    }
                    finally
                    {
                        var2.release();
                    }
                }
                else
                {
                    this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notAllowed", new Object[0]));
                }
            }
            else if ("MC|Beacon".equals(packetIn.getChannel()))
            {
                if (this.playerEntity.openContainer instanceof ContainerBeacon)
                {
                    try
                    {
                        var40 = new DataInputStream(new ByteArrayInputStream(packetIn.getData()));
                        var42 = var40.readInt();
                        int var47 = var40.readInt();
                        ContainerBeacon var50 = (ContainerBeacon)this.playerEntity.openContainer;
                        Slot var6 = var50.getSlot(0);

                        if (var6.getHasStack())
                        {
                            var6.decrStackSize(1);
                            TileEntityBeacon var7 = var50.func_148327_e();
                            var7.setPrimaryEffect(var42);
                            var7.setSecondaryEffect(var47);
                            var7.markDirty();
                        }
                    }
                    catch (Exception var32)
                    {
                        logger.error("Couldn\'t set beacon", var32);
                    }
                }
            }
            else if ("MC|ItemName".equals(packetIn.getChannel()) && this.playerEntity.openContainer instanceof ContainerRepair)
            {
                ContainerRepair var41 = (ContainerRepair)this.playerEntity.openContainer;

                if (packetIn.getData() != null && packetIn.getData().length >= 1)
                {
                    String var44 = ChatAllowedCharacters.filterAllowedCharacters(new String(packetIn.getData(), Charsets.UTF_8));

                    if (var44.length() <= 30)
                    {
                        var41.updateItemName(var44);
                    }
                }
                else
                {
                    var41.updateItemName("");
                }
            }
        }
    }

    public void onConnectionStateTransition(EnumConnectionState oldState, EnumConnectionState newState)
    {
        if (newState != EnumConnectionState.PLAY)
        {
            throw new IllegalStateException("Unexpected change in protocol!");
        }
    }

    static final class SwitchEnumState
    {
        static final int[] field_151290_a = new int[C16PacketClientStatus.EnumState.values().length];
        private static final String __OBFID = "CL_00001455";

        static
        {
            try
            {
                field_151290_a[C16PacketClientStatus.EnumState.PERFORM_RESPAWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_151290_a[C16PacketClientStatus.EnumState.REQUEST_STATS.ordinal()] = 2;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_151290_a[C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT.ordinal()] = 3;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
