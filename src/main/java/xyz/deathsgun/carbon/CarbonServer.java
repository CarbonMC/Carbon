package xyz.deathsgun.carbon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.json.JavalinJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.deathsgun.carbon.config.Config;
import xyz.deathsgun.carbon.screen.ScreenController;

public class CarbonServer extends Thread {

    private final Logger logger = LogManager.getLogger(CarbonServer.class);
    private final Javalin javalin;
    private final Config config;

    public CarbonServer(Config config, NetworkHandler networkHandler) {
        setName("Carbon Server");
        this.config = config;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JavalinJson.setFromJsonMapper(gson::fromJson);
        //noinspection NullableProblems
        JavalinJson.setToJsonMapper(gson::toJson);
        this.javalin = Javalin.create(jConfig -> {
            jConfig.defaultContentType = "application/json";
            jConfig.showJavalinBanner = false;
            jConfig.registerPlugin(new RouteOverviewPlugin("/"));
        });
        new ScreenController(this.javalin, networkHandler);
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    @Override
    public void run() {
        logger.info("Starting Carbon server on port {}", config.getPort());
        javalin.start(config.getHost(), config.getPort());
        //noinspection HttpUrlsUsage
        logger.info("Carbon is now available at http://{}:{}", config.getHost(), config.getPort());
    }

    public void shutdown() {
        javalin.stop();
    }

}
