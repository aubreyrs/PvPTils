package cat.aubrey.pvptils.client.config.base;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.Text;

public interface ConfigModule {
    void save();
    void load();
    ConfigCategory buildCategory(ConfigBuilder builder);
    Text getCategoryText();
}