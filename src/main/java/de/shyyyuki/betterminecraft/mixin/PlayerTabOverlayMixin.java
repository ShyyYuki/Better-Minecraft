package de.shyyyuki.betterminecraft.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Tab list mixin (the player list shown while holding TAB).
 *
 * What it does:
 *  - Appends the player's ping in ms behind every player name, colored by quality.
 *  - The vanilla ping bars icon is NOT touched and stays where it is.
 *
 * Where the number comes from:
 *  The client only knows what the server (or proxy such as Velocity) puts into
 *  the tab list entry of each player. We read exactly that value, so the mod works
 *  on proxy networks (e.g. DonutSMP) without any server-side plugin, as long as
 *  the network actually sends real latency values.
 *
 * No version-specific code is needed: the only target (getNameForDisplay) has
 * the same name and signature across the supported versions.
 */
@Environment(EnvType.CLIENT)
@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

    /**
     * Runs at the end of getNameForDisplay(), the method that builds the text
     * shown for each player in the tab list, and appends " <ping> ms".
     *
     * @At("RETURN")  -> the original name (team color, prefix, ...) is already built.
     * cancellable   -> lets us replace the return value with our extended text.
     */
    @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
    private void betterminecraft$addPing(PlayerInfo info, CallbackInfoReturnable<Component> cir) {
        // Latency in milliseconds, as sent by the server/proxy in the tab list data.
        int ping = info.getLatency();

        // A negative value means "unknown" (vanilla shows the red X icon).
        // In that case there is nothing meaningful to print.
        if (ping < 0) {
            return;
        }

        // Keep the original name and add our colored ping behind it.
        Component result = Component.empty()
                .append(cir.getReturnValue())
                .append(Component.literal(" " + ping + " ms").withStyle(getPingColor(ping)));

        cir.setReturnValue(result);
    }

    /**
     * Maps a ping value to a color:
     *   0 -  50 ms -> GREEN
     *  51 - 100 ms -> YELLOW
     * 101 - 200 ms -> RED
     *    > 200 ms  -> DARK_RED
     */
    private static ChatFormatting getPingColor(int ping) {
        if (ping <= 50)  return ChatFormatting.GREEN;
        if (ping <= 100) return ChatFormatting.YELLOW;
        if (ping <= 200) return ChatFormatting.RED;
        return ChatFormatting.DARK_RED;
    }
}