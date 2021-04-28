package xyz.deathsgun.carbon;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import xyz.deathsgun.carbon.config.Config;

import java.io.*;
import java.nio.file.Files;

public class CarbonMod implements ModInitializer {

    private final File carbonDir = new File("Carbon");
    private Config config;

    @Override
    public void onInitialize() {
        try {
            loadConfig();
        } catch (IOException e) {
            LogManager.getLogger().error("Failed to load config: ", e);
        }
        NetworkHandler networkHandler = new NetworkHandler(MinecraftClient.getInstance());
        CarbonServer server = new CarbonServer(config, networkHandler);
        server.start();
    }

    private void loadConfig() throws IOException {
        Gson gson = new Gson();
        try {
            this.config = gson.fromJson(new FileReader(new File(carbonDir, "config.json")), Config.class);
        } catch (FileNotFoundException e) {
            //noinspection ResultOfMethodCallIgnored
            carbonDir.mkdirs();
            InputStream configFile = CarbonMod.class.getResourceAsStream("/config.json");
            if (configFile != null) {
                Files.copy(configFile, carbonDir.toPath().resolve("config.json"));
                loadConfig();
            }
        }
    }
}
