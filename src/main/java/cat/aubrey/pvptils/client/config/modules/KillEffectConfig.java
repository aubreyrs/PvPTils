package cat.aubrey.pvptils.client.config.modules;

import cat.aubrey.pvptils.client.config.base.ConfigModule;
import cat.aubrey.pvptils.client.config.base.ConfigRegistry;
import com.google.gson.annotations.Expose;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.Text;
import cat.aubrey.pvptils.client.Pvptils;

import java.io.*;

public class KillEffectConfig implements ConfigModule {
    private static final String FILE_NAME = "killeffect.json";

    @Expose private boolean lightningKillEffect = true;

    @Override
    public void save() {
        try (Writer writer = new FileWriter(new File("config/pvptils", FILE_NAME))) {
            ConfigRegistry.GSON.toJson(this, writer);
        } catch (IOException e) {
            Pvptils.LOGGER.error("Failed to save kill effect config", e);
        }
    }

    @Override
    public void load() {
        File file = new File("config/pvptils", FILE_NAME);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                KillEffectConfig loaded = ConfigRegistry.GSON.fromJson(reader, KillEffectConfig.class);
                copyFrom(loaded);
            } catch (IOException e) {
                Pvptils.LOGGER.error("Failed to load kill effect config", e);
            }
        }
    }

    private void copyFrom(KillEffectConfig other) {
        this.lightningKillEffect = other.lightningKillEffect;
    }

    @Override
    public ConfigCategory buildCategory(ConfigBuilder builder) {
        return builder.getOrCreateCategory(getCategoryText())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Lightning Kill Effect"), lightningKillEffect)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Shows a lightning effect when an entity dies (client-side)"))
                        .setSaveConsumer(value -> {
                            lightningKillEffect = value;
                            save();
                        }).build());
    }

    @Override
    public Text getCategoryText() {
        return Text.literal("Kill Effects");
    }

    public boolean isLightningKillEffectEnabled() { return lightningKillEffect; }
}