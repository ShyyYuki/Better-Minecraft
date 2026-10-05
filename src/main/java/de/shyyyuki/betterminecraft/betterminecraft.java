package de.shyyyuki.betterminecraft;

import net.fabricmc.api.ClientModInitializer;

/**
 * Main entry point of the "Better Minecraft" mod.
 *
 * This class is registered under the "client" entrypoint in fabric.mod.json,
 * so Fabric Loader calls it once when the Minecraft client starts up.
 *
 * It implements ClientModInitializer (not ModInitializer) because the mod is
 * client-only. ModInitializer is for the "main" entrypoint, which runs on both
 * client and server.
 *
 * The class is intentionally empty: all functionality is implemented through
 * Mixins (see PlayerTabOverlayMixin), which are applied automatically by the
 * Mixin framework and do not need any registration code here.
 */
public class betterminecraft implements ClientModInitializer {

    /**
     * Called once by Fabric Loader when the client is initialized.
     * Put registration code here later (keybinds, config screens, events, ...).
     */
    @Override
    public void onInitializeClient() {
        // Nothing to do yet - the ping display is handled by the mixin.
    }
}