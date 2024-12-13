package cat.aubrey.pvptils.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pvptils implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Pvptils");
    @Override
    public void onInitializeClient() {
        Config.get();
        LOGGER.info("PvPTils initialized!!!");
    }
}
