package cat.aubrey.pvptils.client;

import cat.aubrey.pvptils.client.modules.Spotify;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pvptils implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Pvptils");

    @Override
    public void onInitializeClient() {
        Config.get();

        LOGGER.info("Initializing Spotify module...");
        try {
            Spotify.initializeKeybinds();
            ClientTickEvents.END_CLIENT_TICK.register(client -> Spotify.handleKeybinds());

            if (Config.get().isSpotifyEnabled()) {
                Spotify.setEnabled(true);
            }

            LOGGER.info("Spotify module initialized successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Spotify module", e);
        }

        LOGGER.info("PvPTils initialized!!!");
    }
}