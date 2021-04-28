package xyz.deathsgun.carbon.screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import xyz.deathsgun.carbon.model.Button;

public class Serializer {

    public Button buttonWidgetToButton(ButtonWidget widget) {
        Button button = new Button();
        button.id = String.format("%d|%d", widget.x, widget.y);
        button.text = widget.getMessage().getString();
        button.active = widget.active;
        button.visible = widget.visible;
        return button;
    }

}
