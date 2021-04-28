package xyz.deathsgun.carbon;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import xyz.deathsgun.carbon.model.ActionRequest;
import xyz.deathsgun.carbon.model.ActionResponse;
import xyz.deathsgun.carbon.model.CurrentScreenResponse;
import xyz.deathsgun.carbon.screen.Serializer;

import java.util.ArrayList;
import java.util.Optional;

public class NetworkHandler {

    private final MinecraftClient client;
    private final Serializer serializer = new Serializer();

    public NetworkHandler(MinecraftClient client) {
        this.client = client;
    }

    public CurrentScreenResponse handleCurrentScreen() {
        Screen screen = client.currentScreen;
        CurrentScreenResponse response = new CurrentScreenResponse();
        response.isIngame = false;
        if (screen == null) {
            response.isIngame = true;
            return response;
        }
        response.title = screen.getTitle().getString();
        response.className = screen.getClass().getSimpleName();
        response.clazz = screen.getClass().getName();
        response.children = new ArrayList<>();
        for (net.minecraft.client.gui.Element child : screen.children()) {
            if (child instanceof ButtonWidget) {
                response.children.add(serializer.buttonWidgetToButton((ButtonWidget) child));
            }
        }
        return response;
    }

    public ActionResponse handleAction(ActionRequest request) {
        switch (request.type) {
            case "click_button":
                return clickButton(request);
            default:
                ActionResponse response = new ActionResponse();
                response.success = false;
                return response;
        }
    }

    public ActionResponse clickButton(ActionRequest request) {
        ActionResponse response = new ActionResponse();
        String[] splitter = request.data.split("\\|");
        int x = Integer.parseInt(splitter[0]);
        int y = Integer.parseInt(splitter[1]);
        Screen screen = client.currentScreen;
        if (screen == null) {
            response.success = false;
            response.message = "screen is null";
            return response;
        }
        Optional<? extends Element> element = screen.children().stream().filter(e -> {
            if (!(e instanceof ButtonWidget)) {
                return false;
            }
            ButtonWidget buttonWidget = (ButtonWidget) e;
            return buttonWidget.x == x && buttonWidget.y == y;
        }).findFirst();

        if (!element.isPresent()) {
            response.success = false;
            response.message = "button not found";
            return response;
        }
        ButtonWidget buttonWidget = (ButtonWidget) element.get();
        buttonWidget.onPress();
        response.success = true;
        return response;
    }

}
