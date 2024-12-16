package cat.aubrey.pvptils.client.config.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import cat.aubrey.pvptils.client.Pvptils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigRegistry {
    private static final String CONFIG_DIR = "config/pvptils";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<String, ConfigModule> modules = new LinkedHashMap<>();

    public void register(String id, ConfigModule module) {
        modules.put(id, module);
    }

    public void loadAll() {
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        modules.values().forEach(module -> {
            try {
                module.load();
            } catch (Exception e) {
                Pvptils.LOGGER.error("Failed to load config module", e);
            }
        });
    }

    public void saveAll() {
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        modules.values().forEach(module -> {
            try {
                module.save();
            } catch (Exception e) {
                Pvptils.LOGGER.error("Failed to save config module", e);
            }
        });
    }

    public Screen buildScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("PvPtils Configuration"));

        modules.values().forEach(module -> module.buildCategory(builder));

        return builder.build();
    }

    public ConfigModule getModule(String id) {
        return modules.get(id);
    }
}