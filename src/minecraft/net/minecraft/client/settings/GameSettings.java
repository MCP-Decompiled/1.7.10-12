package net.minecraft.client.settings;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GameSettings
{
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();
    private static final ParameterizedType typeListString = new ParameterizedType()
    {
        private static final String __OBFID = "CL_00000651";
        public Type[] getActualTypeArguments()
        {
            return new Type[] {String.class};
        }
        public Type getRawType()
        {
            return List.class;
        }
        public Type getOwnerType()
        {
            return null;
        }
    };
    private static final String[] GUISCALES = new String[] {"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
    private static final String[] PARTICLES = new String[] {"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
    private static final String[] AMBIENT_OCCLUSIONS = new String[] {"options.ao.off", "options.ao.min", "options.ao.max"};
    private static final String[] STREAM_COMPRESSIONS = new String[] {"options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high"};
    private static final String[] STREAM_CHAT_MODES = new String[] {"options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never"};
    private static final String[] STREAM_CHAT_FILTER_MODES = new String[] {"options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods"};
    private static final String[] STREAM_MIC_MODES = new String[] {"options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk"};
    public float mouseSensitivity = 0.5F;
    public boolean invertMouse;
    public int renderDistanceChunks = -1;
    public boolean viewBobbing = true;
    public boolean anaglyph;
    public boolean advancedOpengl;
    public boolean fboEnable = true;
    public int limitFramerate = 120;
    public boolean fancyGraphics = true;
    public int ambientOcclusion = 2;
    public boolean clouds = true;
    public List resourcePacks = new ArrayList();
    public EntityPlayer.EnumChatVisibility chatVisibility;
    public boolean chatColours;
    public boolean chatLinks;
    public boolean chatLinksPrompt;
    public float chatOpacity;
    public boolean snooperEnabled;
    public boolean fullScreen;
    public boolean enableVsync;
    public boolean hideServerAddress;
    public boolean advancedItemTooltips;
    public boolean pauseOnLostFocus;
    public boolean showCape;
    public boolean touchscreen;
    public int overrideWidth;
    public int overrideHeight;
    public boolean heldItemTooltips;
    public float chatScale;
    public float chatWidth;
    public float chatHeightUnfocused;
    public float chatHeightFocused;
    public boolean showInventoryAchievementHint;
    public int mipmapLevels;
    public int anisotropicFiltering;
    private Map mapSoundLevels;
    public float streamBytesPerPixel;
    public float streamMicVolume;
    public float streamGameVolume;
    public float streamKbps;
    public float streamFps;
    public int streamCompression;
    public boolean streamSendMetadata;
    public String streamPreferredServer;
    public int streamChatEnabled;
    public int streamChatUserFilter;
    public int streamMicToggleBehavior;
    public KeyBinding keyBindForward;
    public KeyBinding keyBindLeft;
    public KeyBinding keyBindBack;
    public KeyBinding keyBindRight;
    public KeyBinding keyBindJump;
    public KeyBinding keyBindSneak;
    public KeyBinding keyBindInventory;
    public KeyBinding keyBindUseItem;
    public KeyBinding keyBindDrop;
    public KeyBinding keyBindAttack;
    public KeyBinding keyBindPickBlock;
    public KeyBinding keyBindSprint;
    public KeyBinding keyBindChat;
    public KeyBinding keyBindPlayerList;
    public KeyBinding keyBindCommand;
    public KeyBinding keyBindScreenshot;
    public KeyBinding keyBindTogglePerspective;
    public KeyBinding keyBindSmoothCamera;
    public KeyBinding keyBindFullscreen;
    public KeyBinding keyBindStreamStartStop;
    public KeyBinding keyBindStreamPauseUnpause;
    public KeyBinding keyBindStreamCommercials;
    public KeyBinding keyBindStreamToggleMic;
    public KeyBinding[] keyBindsHotbar;
    public KeyBinding[] keyBindings;
    protected Minecraft mc;
    private File optionsFile;
    public EnumDifficulty difficulty;
    public boolean hideGUI;
    public int thirdPersonView;
    public boolean showDebugInfo;
    public boolean showDebugProfilerChart;
    public String lastServer;
    public boolean noclip;
    public boolean smoothCamera;
    public boolean debugCamEnable;
    public float noclipRate;
    public float debugCamRate;
    public float fovSetting;
    public float gammaSetting;
    public float saturation;
    public int guiScale;
    public int particleSetting;
    public String language;
    public boolean forceUnicodeFont;
    private static final String __OBFID = "CL_00000650";

    public GameSettings(Minecraft p_i46326_1_, File p_i46326_2_)
    {
        this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
        this.chatColours = true;
        this.chatLinks = true;
        this.chatLinksPrompt = true;
        this.chatOpacity = 1.0F;
        this.snooperEnabled = true;
        this.enableVsync = true;
        this.pauseOnLostFocus = true;
        this.showCape = true;
        this.heldItemTooltips = true;
        this.chatScale = 1.0F;
        this.chatWidth = 1.0F;
        this.chatHeightUnfocused = 0.44366196F;
        this.chatHeightFocused = 1.0F;
        this.showInventoryAchievementHint = true;
        this.mipmapLevels = 4;
        this.anisotropicFiltering = 1;
        this.mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
        this.streamBytesPerPixel = 0.5F;
        this.streamMicVolume = 1.0F;
        this.streamGameVolume = 1.0F;
        this.streamKbps = 0.5412844F;
        this.streamFps = 0.31690142F;
        this.streamCompression = 1;
        this.streamSendMetadata = true;
        this.streamPreferredServer = "";
        this.streamChatEnabled = 0;
        this.streamChatUserFilter = 0;
        this.streamMicToggleBehavior = 0;
        this.keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
        this.keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
        this.keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
        this.keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
        this.keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
        this.keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
        this.keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
        this.keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
        this.keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
        this.keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
        this.keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
        this.keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.gameplay");
        this.keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
        this.keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
        this.keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
        this.keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
        this.keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
        this.keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
        this.keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
        this.keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
        this.keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
        this.keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
        this.keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
        this.keyBindsHotbar = new KeyBinding[] {new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")};
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll(new KeyBinding[] {this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindSprint, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen}, this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.noclipRate = 1.0F;
        this.debugCamRate = 1.0F;
        this.fovSetting = 70.0F;
        this.language = "en_US";
        this.forceUnicodeFont = false;
        this.mc = p_i46326_1_;
        this.optionsFile = new File(p_i46326_2_, "options.txt");
        GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0F);
        this.renderDistanceChunks = p_i46326_1_.isJava64bit() ? 12 : 8;
        this.loadOptions();
    }

    public GameSettings()
    {
        this.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
        this.chatColours = true;
        this.chatLinks = true;
        this.chatLinksPrompt = true;
        this.chatOpacity = 1.0F;
        this.snooperEnabled = true;
        this.enableVsync = true;
        this.pauseOnLostFocus = true;
        this.showCape = true;
        this.heldItemTooltips = true;
        this.chatScale = 1.0F;
        this.chatWidth = 1.0F;
        this.chatHeightUnfocused = 0.44366196F;
        this.chatHeightFocused = 1.0F;
        this.showInventoryAchievementHint = true;
        this.mipmapLevels = 4;
        this.anisotropicFiltering = 1;
        this.mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
        this.streamBytesPerPixel = 0.5F;
        this.streamMicVolume = 1.0F;
        this.streamGameVolume = 1.0F;
        this.streamKbps = 0.5412844F;
        this.streamFps = 0.31690142F;
        this.streamCompression = 1;
        this.streamSendMetadata = true;
        this.streamPreferredServer = "";
        this.streamChatEnabled = 0;
        this.streamChatUserFilter = 0;
        this.streamMicToggleBehavior = 0;
        this.keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
        this.keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
        this.keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
        this.keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
        this.keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
        this.keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
        this.keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
        this.keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
        this.keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
        this.keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
        this.keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
        this.keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.gameplay");
        this.keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
        this.keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
        this.keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
        this.keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
        this.keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
        this.keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
        this.keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
        this.keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
        this.keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
        this.keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
        this.keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
        this.keyBindsHotbar = new KeyBinding[] {new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")};
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll(new KeyBinding[] {this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindSprint, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen}, this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.noclipRate = 1.0F;
        this.debugCamRate = 1.0F;
        this.fovSetting = 70.0F;
        this.language = "en_US";
        this.forceUnicodeFont = false;
    }

    public static String getKeyDisplayString(int p_74298_0_)
    {
        return p_74298_0_ < 0 ? I18n.format("key.mouseButton", new Object[] {Integer.valueOf(p_74298_0_ + 101)}): Keyboard.getKeyName(p_74298_0_);
    }

    public static boolean isKeyDown(KeyBinding p_100015_0_)
    {
        return p_100015_0_.getKeyCode() == 0 ? false : (p_100015_0_.getKeyCode() < 0 ? Mouse.isButtonDown(p_100015_0_.getKeyCode() + 100) : Keyboard.isKeyDown(p_100015_0_.getKeyCode()));
    }

    public void setOptionKeyBinding(KeyBinding p_151440_1_, int p_151440_2_)
    {
        p_151440_1_.setKeyCode(p_151440_2_);
        this.saveOptions();
    }

    public void setOptionFloatValue(GameSettings.Options p_74304_1_, float p_74304_2_)
    {
        if (p_74304_1_ == GameSettings.Options.SENSITIVITY)
        {
            this.mouseSensitivity = p_74304_2_;
        }

        if (p_74304_1_ == GameSettings.Options.FOV)
        {
            this.fovSetting = p_74304_2_;
        }

        if (p_74304_1_ == GameSettings.Options.GAMMA)
        {
            this.gammaSetting = p_74304_2_;
        }

        if (p_74304_1_ == GameSettings.Options.FRAMERATE_LIMIT)
        {
            this.limitFramerate = (int)p_74304_2_;
        }

        if (p_74304_1_ == GameSettings.Options.CHAT_OPACITY)
        {
            this.chatOpacity = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED)
        {
            this.chatHeightFocused = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED)
        {
            this.chatHeightUnfocused = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == GameSettings.Options.CHAT_WIDTH)
        {
            this.chatWidth = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == GameSettings.Options.CHAT_SCALE)
        {
            this.chatScale = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        int var3;

        if (p_74304_1_ == GameSettings.Options.ANISOTROPIC_FILTERING)
        {
            var3 = this.anisotropicFiltering;
            this.anisotropicFiltering = (int)p_74304_2_;

            if ((float)var3 != p_74304_2_)
            {
                this.mc.getTextureMapBlocks().setAnisotropicFiltering(this.anisotropicFiltering);
                this.mc.scheduleResourcesRefresh();
            }
        }

        if (p_74304_1_ == GameSettings.Options.MIPMAP_LEVELS)
        {
            var3 = this.mipmapLevels;
            this.mipmapLevels = (int)p_74304_2_;

            if ((float)var3 != p_74304_2_)
            {
                this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
                this.mc.scheduleResourcesRefresh();
            }
        }

        if (p_74304_1_ == GameSettings.Options.RENDER_DISTANCE)
        {
            this.renderDistanceChunks = (int)p_74304_2_;
        }

        if (p_74304_1_ == GameSettings.Options.STREAM_BYTES_PER_PIXEL)
        {
            this.streamBytesPerPixel = p_74304_2_;
        }

        if (p_74304_1_ == GameSettings.Options.STREAM_VOLUME_MIC)
        {
            this.streamMicVolume = p_74304_2_;
            this.mc.getTwitchStream().func_152915_s();
        }

        if (p_74304_1_ == GameSettings.Options.STREAM_VOLUME_SYSTEM)
        {
            this.streamGameVolume = p_74304_2_;
            this.mc.getTwitchStream().func_152915_s();
        }

        if (p_74304_1_ == GameSettings.Options.STREAM_KBPS)
        {
            this.streamKbps = p_74304_2_;
        }

        if (p_74304_1_ == GameSettings.Options.STREAM_FPS)
        {
            this.streamFps = p_74304_2_;
        }
    }

    public void setOptionValue(GameSettings.Options p_74306_1_, int p_74306_2_)
    {
        if (p_74306_1_ == GameSettings.Options.INVERT_MOUSE)
        {
            this.invertMouse = !this.invertMouse;
        }

        if (p_74306_1_ == GameSettings.Options.GUI_SCALE)
        {
            this.guiScale = this.guiScale + p_74306_2_ & 3;
        }

        if (p_74306_1_ == GameSettings.Options.PARTICLES)
        {
            this.particleSetting = (this.particleSetting + p_74306_2_) % 3;
        }

        if (p_74306_1_ == GameSettings.Options.VIEW_BOBBING)
        {
            this.viewBobbing = !this.viewBobbing;
        }

        if (p_74306_1_ == GameSettings.Options.RENDER_CLOUDS)
        {
            this.clouds = !this.clouds;
        }

        if (p_74306_1_ == GameSettings.Options.FORCE_UNICODE_FONT)
        {
            this.forceUnicodeFont = !this.forceUnicodeFont;
            this.mc.fontRendererObj.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont);
        }

        if (p_74306_1_ == GameSettings.Options.ADVANCED_OPENGL)
        {
            this.advancedOpengl = !this.advancedOpengl;
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_74306_1_ == GameSettings.Options.FBO_ENABLE)
        {
            this.fboEnable = !this.fboEnable;
        }

        if (p_74306_1_ == GameSettings.Options.ANAGLYPH)
        {
            this.anaglyph = !this.anaglyph;
            this.mc.refreshResources();
        }

        if (p_74306_1_ == GameSettings.Options.DIFFICULTY)
        {
            this.difficulty = EnumDifficulty.getDifficultyEnum(this.difficulty.getDifficultyId() + p_74306_2_ & 3);
        }

        if (p_74306_1_ == GameSettings.Options.GRAPHICS)
        {
            this.fancyGraphics = !this.fancyGraphics;
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_74306_1_ == GameSettings.Options.AMBIENT_OCCLUSION)
        {
            this.ambientOcclusion = (this.ambientOcclusion + p_74306_2_) % 3;
            this.mc.renderGlobal.loadRenderers();
        }

        if (p_74306_1_ == GameSettings.Options.CHAT_VISIBILITY)
        {
            this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + p_74306_2_) % 3);
        }

        if (p_74306_1_ == GameSettings.Options.STREAM_COMPRESSION)
        {
            this.streamCompression = (this.streamCompression + p_74306_2_) % 3;
        }

        if (p_74306_1_ == GameSettings.Options.STREAM_SEND_METADATA)
        {
            this.streamSendMetadata = !this.streamSendMetadata;
        }

        if (p_74306_1_ == GameSettings.Options.STREAM_CHAT_ENABLED)
        {
            this.streamChatEnabled = (this.streamChatEnabled + p_74306_2_) % 3;
        }

        if (p_74306_1_ == GameSettings.Options.STREAM_CHAT_USER_FILTER)
        {
            this.streamChatUserFilter = (this.streamChatUserFilter + p_74306_2_) % 3;
        }

        if (p_74306_1_ == GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR)
        {
            this.streamMicToggleBehavior = (this.streamMicToggleBehavior + p_74306_2_) % 2;
        }

        if (p_74306_1_ == GameSettings.Options.CHAT_COLOR)
        {
            this.chatColours = !this.chatColours;
        }

        if (p_74306_1_ == GameSettings.Options.CHAT_LINKS)
        {
            this.chatLinks = !this.chatLinks;
        }

        if (p_74306_1_ == GameSettings.Options.CHAT_LINKS_PROMPT)
        {
            this.chatLinksPrompt = !this.chatLinksPrompt;
        }

        if (p_74306_1_ == GameSettings.Options.SNOOPER_ENABLED)
        {
            this.snooperEnabled = !this.snooperEnabled;
        }

        if (p_74306_1_ == GameSettings.Options.SHOW_CAPE)
        {
            this.showCape = !this.showCape;
        }

        if (p_74306_1_ == GameSettings.Options.TOUCHSCREEN)
        {
            this.touchscreen = !this.touchscreen;
        }

        if (p_74306_1_ == GameSettings.Options.USE_FULLSCREEN)
        {
            this.fullScreen = !this.fullScreen;

            if (this.mc.isFullScreen() != this.fullScreen)
            {
                this.mc.toggleFullscreen();
            }
        }

        if (p_74306_1_ == GameSettings.Options.ENABLE_VSYNC)
        {
            this.enableVsync = !this.enableVsync;
            Display.setVSyncEnabled(this.enableVsync);
        }

        this.saveOptions();
    }

    public float getOptionFloatValue(GameSettings.Options p_74296_1_)
    {
        return p_74296_1_ == GameSettings.Options.FOV ? this.fovSetting : (p_74296_1_ == GameSettings.Options.GAMMA ? this.gammaSetting : (p_74296_1_ == GameSettings.Options.SATURATION ? this.saturation : (p_74296_1_ == GameSettings.Options.SENSITIVITY ? this.mouseSensitivity : (p_74296_1_ == GameSettings.Options.CHAT_OPACITY ? this.chatOpacity : (p_74296_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED ? this.chatHeightFocused : (p_74296_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED ? this.chatHeightUnfocused : (p_74296_1_ == GameSettings.Options.CHAT_SCALE ? this.chatScale : (p_74296_1_ == GameSettings.Options.CHAT_WIDTH ? this.chatWidth : (p_74296_1_ == GameSettings.Options.FRAMERATE_LIMIT ? (float)this.limitFramerate : (p_74296_1_ == GameSettings.Options.ANISOTROPIC_FILTERING ? (float)this.anisotropicFiltering : (p_74296_1_ == GameSettings.Options.MIPMAP_LEVELS ? (float)this.mipmapLevels : (p_74296_1_ == GameSettings.Options.RENDER_DISTANCE ? (float)this.renderDistanceChunks : (p_74296_1_ == GameSettings.Options.STREAM_BYTES_PER_PIXEL ? this.streamBytesPerPixel : (p_74296_1_ == GameSettings.Options.STREAM_VOLUME_MIC ? this.streamMicVolume : (p_74296_1_ == GameSettings.Options.STREAM_VOLUME_SYSTEM ? this.streamGameVolume : (p_74296_1_ == GameSettings.Options.STREAM_KBPS ? this.streamKbps : (p_74296_1_ == GameSettings.Options.STREAM_FPS ? this.streamFps : 0.0F)))))))))))))))));
    }

    public boolean getOptionOrdinalValue(GameSettings.Options p_74308_1_)
    {
        switch (GameSettings.SwitchOptions.optionIds[p_74308_1_.ordinal()])
        {
            case 1:
                return this.invertMouse;

            case 2:
                return this.viewBobbing;

            case 3:
                return this.anaglyph;

            case 4:
                return this.advancedOpengl;

            case 5:
                return this.fboEnable;

            case 6:
                return this.clouds;

            case 7:
                return this.chatColours;

            case 8:
                return this.chatLinks;

            case 9:
                return this.chatLinksPrompt;

            case 10:
                return this.snooperEnabled;

            case 11:
                return this.fullScreen;

            case 12:
                return this.enableVsync;

            case 13:
                return this.showCape;

            case 14:
                return this.touchscreen;

            case 15:
                return this.streamSendMetadata;

            case 16:
                return this.forceUnicodeFont;

            default:
                return false;
        }
    }

    private static String getTranslation(String[] p_74299_0_, int p_74299_1_)
    {
        if (p_74299_1_ < 0 || p_74299_1_ >= p_74299_0_.length)
        {
            p_74299_1_ = 0;
        }

        return I18n.format(p_74299_0_[p_74299_1_], new Object[0]);
    }

    public String getKeyBinding(GameSettings.Options p_74297_1_)
    {
        String var2 = I18n.format(p_74297_1_.getEnumString(), new Object[0]) + ": ";

        if (p_74297_1_.getEnumFloat())
        {
            float var6 = this.getOptionFloatValue(p_74297_1_);
            float var4 = p_74297_1_.normalizeValue(var6);
            return p_74297_1_ == GameSettings.Options.SENSITIVITY ? (var4 == 0.0F ? var2 + I18n.format("options.sensitivity.min", new Object[0]) : (var4 == 1.0F ? var2 + I18n.format("options.sensitivity.max", new Object[0]) : var2 + (int)(var4 * 200.0F) + "%")) : (p_74297_1_ == GameSettings.Options.FOV ? (var6 == 70.0F ? var2 + I18n.format("options.fov.min", new Object[0]) : (var6 == 110.0F ? var2 + I18n.format("options.fov.max", new Object[0]) : var2 + (int)var6)) : (p_74297_1_ == GameSettings.Options.FRAMERATE_LIMIT ? (var6 == p_74297_1_.valueMax ? var2 + I18n.format("options.framerateLimit.max", new Object[0]) : var2 + (int)var6 + " fps") : (p_74297_1_ == GameSettings.Options.GAMMA ? (var4 == 0.0F ? var2 + I18n.format("options.gamma.min", new Object[0]) : (var4 == 1.0F ? var2 + I18n.format("options.gamma.max", new Object[0]) : var2 + "+" + (int)(var4 * 100.0F) + "%")) : (p_74297_1_ == GameSettings.Options.SATURATION ? var2 + (int)(var4 * 400.0F) + "%" : (p_74297_1_ == GameSettings.Options.CHAT_OPACITY ? var2 + (int)(var4 * 90.0F + 10.0F) + "%" : (p_74297_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED ? var2 + GuiNewChat.calculateChatboxHeight(var4) + "px" : (p_74297_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED ? var2 + GuiNewChat.calculateChatboxHeight(var4) + "px" : (p_74297_1_ == GameSettings.Options.CHAT_WIDTH ? var2 + GuiNewChat.calculateChatboxWidth(var4) + "px" : (p_74297_1_ == GameSettings.Options.RENDER_DISTANCE ? var2 + (int)var6 + " chunks" : (p_74297_1_ == GameSettings.Options.ANISOTROPIC_FILTERING ? (var6 == 1.0F ? var2 + I18n.format("options.off", new Object[0]) : var2 + (int)var6) : (p_74297_1_ == GameSettings.Options.MIPMAP_LEVELS ? (var6 == 0.0F ? var2 + I18n.format("options.off", new Object[0]) : var2 + (int)var6) : (p_74297_1_ == GameSettings.Options.STREAM_FPS ? var2 + TwitchStream.func_152948_a(var4) + " fps" : (p_74297_1_ == GameSettings.Options.STREAM_KBPS ? var2 + TwitchStream.func_152946_b(var4) + " Kbps" : (p_74297_1_ == GameSettings.Options.STREAM_BYTES_PER_PIXEL ? var2 + String.format("%.3f bpp", new Object[] {Float.valueOf(TwitchStream.func_152947_c(var4))}): (var4 == 0.0F ? var2 + I18n.format("options.off", new Object[0]) : var2 + (int)(var4 * 100.0F) + "%")))))))))))))));
        }
        else if (p_74297_1_.getEnumBoolean())
        {
            boolean var5 = this.getOptionOrdinalValue(p_74297_1_);
            return var5 ? var2 + I18n.format("options.on", new Object[0]) : var2 + I18n.format("options.off", new Object[0]);
        }
        else if (p_74297_1_ == GameSettings.Options.DIFFICULTY)
        {
            return var2 + I18n.format(this.difficulty.getDifficultyResourceKey(), new Object[0]);
        }
        else if (p_74297_1_ == GameSettings.Options.GUI_SCALE)
        {
            return var2 + getTranslation(GUISCALES, this.guiScale);
        }
        else if (p_74297_1_ == GameSettings.Options.CHAT_VISIBILITY)
        {
            return var2 + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
        }
        else if (p_74297_1_ == GameSettings.Options.PARTICLES)
        {
            return var2 + getTranslation(PARTICLES, this.particleSetting);
        }
        else if (p_74297_1_ == GameSettings.Options.AMBIENT_OCCLUSION)
        {
            return var2 + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
        }
        else if (p_74297_1_ == GameSettings.Options.STREAM_COMPRESSION)
        {
            return var2 + getTranslation(STREAM_COMPRESSIONS, this.streamCompression);
        }
        else if (p_74297_1_ == GameSettings.Options.STREAM_CHAT_ENABLED)
        {
            return var2 + getTranslation(STREAM_CHAT_MODES, this.streamChatEnabled);
        }
        else if (p_74297_1_ == GameSettings.Options.STREAM_CHAT_USER_FILTER)
        {
            return var2 + getTranslation(STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
        }
        else if (p_74297_1_ == GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR)
        {
            return var2 + getTranslation(STREAM_MIC_MODES, this.streamMicToggleBehavior);
        }
        else if (p_74297_1_ == GameSettings.Options.GRAPHICS)
        {
            if (this.fancyGraphics)
            {
                return var2 + I18n.format("options.graphics.fancy", new Object[0]);
            }
            else
            {
                String var3 = "options.graphics.fast";
                return var2 + I18n.format("options.graphics.fast", new Object[0]);
            }
        }
        else
        {
            return var2;
        }
    }

    public void loadOptions()
    {
        try
        {
            if (!this.optionsFile.exists())
            {
                return;
            }

            BufferedReader var1 = new BufferedReader(new FileReader(this.optionsFile));
            String var2 = "";
            this.mapSoundLevels.clear();

            while ((var2 = var1.readLine()) != null)
            {
                try
                {
                    String[] var3 = var2.split(":");

                    if (var3[0].equals("mouseSensitivity"))
                    {
                        this.mouseSensitivity = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("invertYMouse"))
                    {
                        this.invertMouse = var3[1].equals("true");
                    }

                    if (var3[0].equals("fov"))
                    {
                        this.fovSetting = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("gamma"))
                    {
                        this.gammaSetting = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("saturation"))
                    {
                        this.saturation = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("fov"))
                    {
                        this.fovSetting = this.parseFloat(var3[1]) * 40.0F + 70.0F;
                    }

                    if (var3[0].equals("renderDistance"))
                    {
                        this.renderDistanceChunks = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("guiScale"))
                    {
                        this.guiScale = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("particles"))
                    {
                        this.particleSetting = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("bobView"))
                    {
                        this.viewBobbing = var3[1].equals("true");
                    }

                    if (var3[0].equals("anaglyph3d"))
                    {
                        this.anaglyph = var3[1].equals("true");
                    }

                    if (var3[0].equals("advancedOpengl"))
                    {
                        this.advancedOpengl = var3[1].equals("true");
                    }

                    if (var3[0].equals("maxFps"))
                    {
                        this.limitFramerate = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("fboEnable"))
                    {
                        this.fboEnable = var3[1].equals("true");
                    }

                    if (var3[0].equals("difficulty"))
                    {
                        this.difficulty = EnumDifficulty.getDifficultyEnum(Integer.parseInt(var3[1]));
                    }

                    if (var3[0].equals("fancyGraphics"))
                    {
                        this.fancyGraphics = var3[1].equals("true");
                    }

                    if (var3[0].equals("ao"))
                    {
                        if (var3[1].equals("true"))
                        {
                            this.ambientOcclusion = 2;
                        }
                        else if (var3[1].equals("false"))
                        {
                            this.ambientOcclusion = 0;
                        }
                        else
                        {
                            this.ambientOcclusion = Integer.parseInt(var3[1]);
                        }
                    }

                    if (var3[0].equals("clouds"))
                    {
                        this.clouds = var3[1].equals("true");
                    }

                    if (var3[0].equals("resourcePacks"))
                    {
                        this.resourcePacks = (List)gson.fromJson(var2.substring(var2.indexOf(58) + 1), typeListString);

                        if (this.resourcePacks == null)
                        {
                            this.resourcePacks = new ArrayList();
                        }
                    }

                    if (var3[0].equals("lastServer") && var3.length >= 2)
                    {
                        this.lastServer = var2.substring(var2.indexOf(58) + 1);
                    }

                    if (var3[0].equals("lang") && var3.length >= 2)
                    {
                        this.language = var3[1];
                    }

                    if (var3[0].equals("chatVisibility"))
                    {
                        this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(Integer.parseInt(var3[1]));
                    }

                    if (var3[0].equals("chatColors"))
                    {
                        this.chatColours = var3[1].equals("true");
                    }

                    if (var3[0].equals("chatLinks"))
                    {
                        this.chatLinks = var3[1].equals("true");
                    }

                    if (var3[0].equals("chatLinksPrompt"))
                    {
                        this.chatLinksPrompt = var3[1].equals("true");
                    }

                    if (var3[0].equals("chatOpacity"))
                    {
                        this.chatOpacity = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("snooperEnabled"))
                    {
                        this.snooperEnabled = var3[1].equals("true");
                    }

                    if (var3[0].equals("fullscreen"))
                    {
                        this.fullScreen = var3[1].equals("true");
                    }

                    if (var3[0].equals("enableVsync"))
                    {
                        this.enableVsync = var3[1].equals("true");
                    }

                    if (var3[0].equals("hideServerAddress"))
                    {
                        this.hideServerAddress = var3[1].equals("true");
                    }

                    if (var3[0].equals("advancedItemTooltips"))
                    {
                        this.advancedItemTooltips = var3[1].equals("true");
                    }

                    if (var3[0].equals("pauseOnLostFocus"))
                    {
                        this.pauseOnLostFocus = var3[1].equals("true");
                    }

                    if (var3[0].equals("showCape"))
                    {
                        this.showCape = var3[1].equals("true");
                    }

                    if (var3[0].equals("touchscreen"))
                    {
                        this.touchscreen = var3[1].equals("true");
                    }

                    if (var3[0].equals("overrideHeight"))
                    {
                        this.overrideHeight = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("overrideWidth"))
                    {
                        this.overrideWidth = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("heldItemTooltips"))
                    {
                        this.heldItemTooltips = var3[1].equals("true");
                    }

                    if (var3[0].equals("chatHeightFocused"))
                    {
                        this.chatHeightFocused = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("chatHeightUnfocused"))
                    {
                        this.chatHeightUnfocused = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("chatScale"))
                    {
                        this.chatScale = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("chatWidth"))
                    {
                        this.chatWidth = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("showInventoryAchievementHint"))
                    {
                        this.showInventoryAchievementHint = var3[1].equals("true");
                    }

                    if (var3[0].equals("mipmapLevels"))
                    {
                        this.mipmapLevels = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("anisotropicFiltering"))
                    {
                        this.anisotropicFiltering = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("streamBytesPerPixel"))
                    {
                        this.streamBytesPerPixel = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("streamMicVolume"))
                    {
                        this.streamMicVolume = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("streamSystemVolume"))
                    {
                        this.streamGameVolume = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("streamKbps"))
                    {
                        this.streamKbps = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("streamFps"))
                    {
                        this.streamFps = this.parseFloat(var3[1]);
                    }

                    if (var3[0].equals("streamCompression"))
                    {
                        this.streamCompression = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("streamSendMetadata"))
                    {
                        this.streamSendMetadata = var3[1].equals("true");
                    }

                    if (var3[0].equals("streamPreferredServer") && var3.length >= 2)
                    {
                        this.streamPreferredServer = var2.substring(var2.indexOf(58) + 1);
                    }

                    if (var3[0].equals("streamChatEnabled"))
                    {
                        this.streamChatEnabled = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("streamChatUserFilter"))
                    {
                        this.streamChatUserFilter = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("streamMicToggleBehavior"))
                    {
                        this.streamMicToggleBehavior = Integer.parseInt(var3[1]);
                    }

                    if (var3[0].equals("forceUnicodeFont"))
                    {
                        this.forceUnicodeFont = var3[1].equals("true");
                    }

                    KeyBinding[] var4 = this.keyBindings;
                    int var5 = var4.length;
                    int var6;

                    for (var6 = 0; var6 < var5; ++var6)
                    {
                        KeyBinding var7 = var4[var6];

                        if (var3[0].equals("key_" + var7.getKeyDescription()))
                        {
                            var7.setKeyCode(Integer.parseInt(var3[1]));
                        }
                    }

                    SoundCategory[] var10 = SoundCategory.values();
                    var5 = var10.length;

                    for (var6 = 0; var6 < var5; ++var6)
                    {
                        SoundCategory var11 = var10[var6];

                        if (var3[0].equals("soundCategory_" + var11.getCategoryName()))
                        {
                            this.mapSoundLevels.put(var11, Float.valueOf(this.parseFloat(var3[1])));
                        }
                    }
                }
                catch (Exception var8)
                {
                    logger.warn("Skipping bad option: " + var2);
                }
            }

            KeyBinding.resetKeyBindingArrayAndHash();
            var1.close();
        }
        catch (Exception var9)
        {
            logger.error("Failed to load options", var9);
        }
    }

    private float parseFloat(String p_74305_1_)
    {
        return p_74305_1_.equals("true") ? 1.0F : (p_74305_1_.equals("false") ? 0.0F : Float.parseFloat(p_74305_1_));
    }

    public void saveOptions()
    {
        try
        {
            PrintWriter var1 = new PrintWriter(new FileWriter(this.optionsFile));
            var1.println("invertYMouse:" + this.invertMouse);
            var1.println("mouseSensitivity:" + this.mouseSensitivity);
            var1.println("fov:" + (this.fovSetting - 70.0F) / 40.0F);
            var1.println("gamma:" + this.gammaSetting);
            var1.println("saturation:" + this.saturation);
            var1.println("renderDistance:" + this.renderDistanceChunks);
            var1.println("guiScale:" + this.guiScale);
            var1.println("particles:" + this.particleSetting);
            var1.println("bobView:" + this.viewBobbing);
            var1.println("anaglyph3d:" + this.anaglyph);
            var1.println("advancedOpengl:" + this.advancedOpengl);
            var1.println("maxFps:" + this.limitFramerate);
            var1.println("fboEnable:" + this.fboEnable);
            var1.println("difficulty:" + this.difficulty.getDifficultyId());
            var1.println("fancyGraphics:" + this.fancyGraphics);
            var1.println("ao:" + this.ambientOcclusion);
            var1.println("clouds:" + this.clouds);
            var1.println("resourcePacks:" + gson.toJson(this.resourcePacks));
            var1.println("lastServer:" + this.lastServer);
            var1.println("lang:" + this.language);
            var1.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
            var1.println("chatColors:" + this.chatColours);
            var1.println("chatLinks:" + this.chatLinks);
            var1.println("chatLinksPrompt:" + this.chatLinksPrompt);
            var1.println("chatOpacity:" + this.chatOpacity);
            var1.println("snooperEnabled:" + this.snooperEnabled);
            var1.println("fullscreen:" + this.fullScreen);
            var1.println("enableVsync:" + this.enableVsync);
            var1.println("hideServerAddress:" + this.hideServerAddress);
            var1.println("advancedItemTooltips:" + this.advancedItemTooltips);
            var1.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
            var1.println("showCape:" + this.showCape);
            var1.println("touchscreen:" + this.touchscreen);
            var1.println("overrideWidth:" + this.overrideWidth);
            var1.println("overrideHeight:" + this.overrideHeight);
            var1.println("heldItemTooltips:" + this.heldItemTooltips);
            var1.println("chatHeightFocused:" + this.chatHeightFocused);
            var1.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
            var1.println("chatScale:" + this.chatScale);
            var1.println("chatWidth:" + this.chatWidth);
            var1.println("showInventoryAchievementHint:" + this.showInventoryAchievementHint);
            var1.println("mipmapLevels:" + this.mipmapLevels);
            var1.println("anisotropicFiltering:" + this.anisotropicFiltering);
            var1.println("streamBytesPerPixel:" + this.streamBytesPerPixel);
            var1.println("streamMicVolume:" + this.streamMicVolume);
            var1.println("streamSystemVolume:" + this.streamGameVolume);
            var1.println("streamKbps:" + this.streamKbps);
            var1.println("streamFps:" + this.streamFps);
            var1.println("streamCompression:" + this.streamCompression);
            var1.println("streamSendMetadata:" + this.streamSendMetadata);
            var1.println("streamPreferredServer:" + this.streamPreferredServer);
            var1.println("streamChatEnabled:" + this.streamChatEnabled);
            var1.println("streamChatUserFilter:" + this.streamChatUserFilter);
            var1.println("streamMicToggleBehavior:" + this.streamMicToggleBehavior);
            var1.println("forceUnicodeFont:" + this.forceUnicodeFont);
            KeyBinding[] var2 = this.keyBindings;
            int var3 = var2.length;
            int var4;

            for (var4 = 0; var4 < var3; ++var4)
            {
                KeyBinding var5 = var2[var4];
                var1.println("key_" + var5.getKeyDescription() + ":" + var5.getKeyCode());
            }

            SoundCategory[] var7 = SoundCategory.values();
            var3 = var7.length;

            for (var4 = 0; var4 < var3; ++var4)
            {
                SoundCategory var8 = var7[var4];
                var1.println("soundCategory_" + var8.getCategoryName() + ":" + this.getSoundLevel(var8));
            }

            var1.close();
        }
        catch (Exception var6)
        {
            logger.error("Failed to save options", var6);
        }

        this.sendSettingsToServer();
    }

    public float getSoundLevel(SoundCategory p_151438_1_)
    {
        return this.mapSoundLevels.containsKey(p_151438_1_) ? ((Float)this.mapSoundLevels.get(p_151438_1_)).floatValue() : 1.0F;
    }

    public void setSoundLevel(SoundCategory p_151439_1_, float p_151439_2_)
    {
        this.mc.getSoundHandler().setSoundLevel(p_151439_1_, p_151439_2_);
        this.mapSoundLevels.put(p_151439_1_, Float.valueOf(p_151439_2_));
    }

    public void sendSettingsToServer()
    {
        if (this.mc.thePlayer != null)
        {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, this.difficulty, this.showCape));
        }
    }

    public boolean shouldRenderClouds()
    {
        return this.renderDistanceChunks >= 4 && this.clouds;
    }

    public static enum Options
    {
        INVERT_MOUSE("INVERT_MOUSE", 0, "options.invertMouse", false, true),
        SENSITIVITY("SENSITIVITY", 1, "options.sensitivity", true, false),
        FOV("FOV", 2, "options.fov", true, false, 30.0F, 110.0F, 1.0F),
        GAMMA("GAMMA", 3, "options.gamma", true, false),
        SATURATION("SATURATION", 4, "options.saturation", true, false),
        RENDER_DISTANCE("RENDER_DISTANCE", 5, "options.renderDistance", true, false, 2.0F, 16.0F, 1.0F),
        VIEW_BOBBING("VIEW_BOBBING", 6, "options.viewBobbing", false, true),
        ANAGLYPH("ANAGLYPH", 7, "options.anaglyph", false, true),
        ADVANCED_OPENGL("ADVANCED_OPENGL", 8, "options.advancedOpengl", false, true),
        FRAMERATE_LIMIT("FRAMERATE_LIMIT", 9, "options.framerateLimit", true, false, 10.0F, 260.0F, 10.0F),
        FBO_ENABLE("FBO_ENABLE", 10, "options.fboEnable", false, true),
        DIFFICULTY("DIFFICULTY", 11, "options.difficulty", false, false),
        GRAPHICS("GRAPHICS", 12, "options.graphics", false, false),
        AMBIENT_OCCLUSION("AMBIENT_OCCLUSION", 13, "options.ao", false, false),
        GUI_SCALE("GUI_SCALE", 14, "options.guiScale", false, false),
        RENDER_CLOUDS("RENDER_CLOUDS", 15, "options.renderClouds", false, true),
        PARTICLES("PARTICLES", 16, "options.particles", false, false),
        CHAT_VISIBILITY("CHAT_VISIBILITY", 17, "options.chat.visibility", false, false),
        CHAT_COLOR("CHAT_COLOR", 18, "options.chat.color", false, true),
        CHAT_LINKS("CHAT_LINKS", 19, "options.chat.links", false, true),
        CHAT_OPACITY("CHAT_OPACITY", 20, "options.chat.opacity", true, false),
        CHAT_LINKS_PROMPT("CHAT_LINKS_PROMPT", 21, "options.chat.links.prompt", false, true),
        SNOOPER_ENABLED("SNOOPER_ENABLED", 22, "options.snooper", false, true),
        USE_FULLSCREEN("USE_FULLSCREEN", 23, "options.fullscreen", false, true),
        ENABLE_VSYNC("ENABLE_VSYNC", 24, "options.vsync", false, true),
        SHOW_CAPE("SHOW_CAPE", 25, "options.showCape", false, true),
        TOUCHSCREEN("TOUCHSCREEN", 26, "options.touchscreen", false, true),
        CHAT_SCALE("CHAT_SCALE", 27, "options.chat.scale", true, false),
        CHAT_WIDTH("CHAT_WIDTH", 28, "options.chat.width", true, false),
        CHAT_HEIGHT_FOCUSED("CHAT_HEIGHT_FOCUSED", 29, "options.chat.height.focused", true, false),
        CHAT_HEIGHT_UNFOCUSED("CHAT_HEIGHT_UNFOCUSED", 30, "options.chat.height.unfocused", true, false),
        MIPMAP_LEVELS("MIPMAP_LEVELS", 31, "options.mipmapLevels", true, false, 0.0F, 4.0F, 1.0F),
        ANISOTROPIC_FILTERING("ANISOTROPIC_FILTERING", 32, "options.anisotropicFiltering", true, false, 1.0F, 16.0F, 0.0F, null)
        {
            private static final String __OBFID = "CL_00000654";
            protected float snapToStep(float p_148264_1_)
            {
                return (float)MathHelper.roundUpToPowerOfTwo((int)p_148264_1_);
            }
        },
        FORCE_UNICODE_FONT("FORCE_UNICODE_FONT", 33, "options.forceUnicodeFont", false, true),
        STREAM_BYTES_PER_PIXEL("STREAM_BYTES_PER_PIXEL", 34, "options.stream.bytesPerPixel", true, false),
        STREAM_VOLUME_MIC("STREAM_VOLUME_MIC", 35, "options.stream.micVolumne", true, false),
        STREAM_VOLUME_SYSTEM("STREAM_VOLUME_SYSTEM", 36, "options.stream.systemVolume", true, false),
        STREAM_KBPS("STREAM_KBPS", 37, "options.stream.kbps", true, false),
        STREAM_FPS("STREAM_FPS", 38, "options.stream.fps", true, false),
        STREAM_COMPRESSION("STREAM_COMPRESSION", 39, "options.stream.compression", false, false),
        STREAM_SEND_METADATA("STREAM_SEND_METADATA", 40, "options.stream.sendMetadata", false, true),
        STREAM_CHAT_ENABLED("STREAM_CHAT_ENABLED", 41, "options.stream.chat.enabled", false, false),
        STREAM_CHAT_USER_FILTER("STREAM_CHAT_USER_FILTER", 42, "options.stream.chat.userFilter", false, false),
        STREAM_MIC_TOGGLE_BEHAVIOR("STREAM_MIC_TOGGLE_BEHAVIOR", 43, "options.stream.micToggleBehavior", false, false);
        private final boolean enumFloat;
        private final boolean enumBoolean;
        private final String enumString;
        private final float valueStep;
        private float valueMin;
        private float valueMax;

        private static final GameSettings.Options[] $VALUES = new GameSettings.Options[]{INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, SATURATION, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, ADVANCED_OPENGL, FRAMERATE_LIMIT, FBO_ENABLE, DIFFICULTY, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, RENDER_CLOUDS, PARTICLES, CHAT_VISIBILITY, CHAT_COLOR, CHAT_LINKS, CHAT_OPACITY, CHAT_LINKS_PROMPT, SNOOPER_ENABLED, USE_FULLSCREEN, ENABLE_VSYNC, SHOW_CAPE, TOUCHSCREEN, CHAT_SCALE, CHAT_WIDTH, CHAT_HEIGHT_FOCUSED, CHAT_HEIGHT_UNFOCUSED, MIPMAP_LEVELS, ANISOTROPIC_FILTERING, FORCE_UNICODE_FONT, STREAM_BYTES_PER_PIXEL, STREAM_VOLUME_MIC, STREAM_VOLUME_SYSTEM, STREAM_KBPS, STREAM_FPS, STREAM_COMPRESSION, STREAM_SEND_METADATA, STREAM_CHAT_ENABLED, STREAM_CHAT_USER_FILTER, STREAM_MIC_TOGGLE_BEHAVIOR};
        private static final String __OBFID = "CL_00000653";

        public static GameSettings.Options getEnumOptions(int p_74379_0_)
        {
            GameSettings.Options[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3)
            {
                GameSettings.Options var4 = var1[var3];

                if (var4.returnEnumOrdinal() == p_74379_0_)
                {
                    return var4;
                }
            }

            return null;
        }

        private Options(String p_i1015_1_, int p_i1015_2_, String p_i1015_3_, boolean p_i1015_4_, boolean p_i1015_5_)
        {
            this(p_i1015_1_, p_i1015_2_, p_i1015_3_, p_i1015_4_, p_i1015_5_, 0.0F, 1.0F, 0.0F);
        }

        private Options(String p_i45004_1_, int p_i45004_2_, String p_i45004_3_, boolean p_i45004_4_, boolean p_i45004_5_, float p_i45004_6_, float p_i45004_7_, float p_i45004_8_)
        {
            this.enumString = p_i45004_3_;
            this.enumFloat = p_i45004_4_;
            this.enumBoolean = p_i45004_5_;
            this.valueMin = p_i45004_6_;
            this.valueMax = p_i45004_7_;
            this.valueStep = p_i45004_8_;
        }

        public boolean getEnumFloat()
        {
            return this.enumFloat;
        }

        public boolean getEnumBoolean()
        {
            return this.enumBoolean;
        }

        public int returnEnumOrdinal()
        {
            return this.ordinal();
        }

        public String getEnumString()
        {
            return this.enumString;
        }

        public float getValueMax()
        {
            return this.valueMax;
        }

        public void setValueMax(float p_148263_1_)
        {
            this.valueMax = p_148263_1_;
        }

        public float normalizeValue(float p_148266_1_)
        {
            return MathHelper.clamp_float((this.snapToStepClamp(p_148266_1_) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
        }

        public float denormalizeValue(float p_148262_1_)
        {
            return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(p_148262_1_, 0.0F, 1.0F));
        }

        public float snapToStepClamp(float p_148268_1_)
        {
            p_148268_1_ = this.snapToStep(p_148268_1_);
            return MathHelper.clamp_float(p_148268_1_, this.valueMin, this.valueMax);
        }

        protected float snapToStep(float p_148264_1_)
        {
            if (this.valueStep > 0.0F)
            {
                p_148264_1_ = this.valueStep * (float)Math.round(p_148264_1_ / this.valueStep);
            }

            return p_148264_1_;
        }

        Options(String p_i45005_1_, int p_i45005_2_, String p_i45005_3_, boolean p_i45005_4_, boolean p_i45005_5_, float p_i45005_6_, float p_i45005_7_, float p_i45005_8_, Object p_i45005_9_)
        {
            this(p_i45005_1_, p_i45005_2_, p_i45005_3_, p_i45005_4_, p_i45005_5_, p_i45005_6_, p_i45005_7_, p_i45005_8_);
        }
    }

    static final class SwitchOptions
    {
        static final int[] optionIds = new int[GameSettings.Options.values().length];
        private static final String __OBFID = "CL_00000652";

        static
        {
            try
            {
                optionIds[GameSettings.Options.INVERT_MOUSE.ordinal()] = 1;
            }
            catch (NoSuchFieldError var16)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.VIEW_BOBBING.ordinal()] = 2;
            }
            catch (NoSuchFieldError var15)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.ANAGLYPH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var14)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.ADVANCED_OPENGL.ordinal()] = 4;
            }
            catch (NoSuchFieldError var13)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.FBO_ENABLE.ordinal()] = 5;
            }
            catch (NoSuchFieldError var12)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.RENDER_CLOUDS.ordinal()] = 6;
            }
            catch (NoSuchFieldError var11)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.CHAT_COLOR.ordinal()] = 7;
            }
            catch (NoSuchFieldError var10)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.CHAT_LINKS.ordinal()] = 8;
            }
            catch (NoSuchFieldError var9)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.CHAT_LINKS_PROMPT.ordinal()] = 9;
            }
            catch (NoSuchFieldError var8)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.SNOOPER_ENABLED.ordinal()] = 10;
            }
            catch (NoSuchFieldError var7)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.USE_FULLSCREEN.ordinal()] = 11;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.ENABLE_VSYNC.ordinal()] = 12;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.SHOW_CAPE.ordinal()] = 13;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.TOUCHSCREEN.ordinal()] = 14;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.STREAM_SEND_METADATA.ordinal()] = 15;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                optionIds[GameSettings.Options.FORCE_UNICODE_FONT.ordinal()] = 16;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
