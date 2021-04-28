package xyz.deathsgun.carbon.screen;

import io.javalin.Javalin;
import io.javalin.http.Context;
import xyz.deathsgun.carbon.NetworkHandler;
import xyz.deathsgun.carbon.model.ActionRequest;

public class ScreenController {

    private final NetworkHandler networkHandler;

    public ScreenController(Javalin javalin, NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
        javalin.get("/screen/current", this::handleCurrentScreen);
        javalin.post("/screen/current/action", this::handleAction);
    }

    private void handleCurrentScreen(Context ctx) {
        ctx.json(networkHandler.handleCurrentScreen());
    }

    private void handleAction(Context ctx) {
        ActionRequest request = ctx.bodyAsClass(ActionRequest.class);
        if (request == null) {
            return;
        }
        ctx.json(networkHandler.handleAction(request));
    }

}
