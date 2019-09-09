package tubular.utility;

import io.github.cottonmc.cotton.gui.widget.WSprite;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class WToggleSprite extends WSprite {
    protected Runnable onClick;

    public static Identifier imageRequester = new Identifier("tubular:textures/widget/requester.png");
    public static Identifier imageProvider = new Identifier("tubular:textures/widget/provider.png");
    public static Identifier imageNone = new Identifier("tubular:textures/widget/none.png");

    public static Identifier imageHoveredRequester = new Identifier("tubular:textures/widget/requester_hovered.png");
    public static Identifier imageHoveredProvider = new Identifier("tubular:textures/widget/provider_hovered.png");
    public static Identifier imageHoveredNone = new Identifier("tubular:textures/widget/none_hovered.png");

    public static WSprite spriteRequester = new WSprite(imageRequester);
    public static WSprite spriteProvider = new WSprite(imageProvider);
    public static WSprite spriteNone = new WSprite(imageNone);

    public static WSprite spriteHoveredRequester = new WSprite(imageRequester);
    public static WSprite spriteHoveredProvider = new WSprite(imageProvider);
    public static WSprite spriteHoveredNone = new WSprite(imageNone);

    public WToggleSprite(Identifier image) {
        super(image);
    }

    @Override
	public WSprite setImage(Identifier image) {
        super.setImage(image);
		return this;
	}

    public Identifier getImage() {
        return this.frames[0];
    }

	public void paintBackground(int x, int y, int mouseX, int mouseY) {
        super.paintBackground(x, y, mouseX, mouseY);
        if (mouseX>=0 && mouseY>=0 && mouseX<getWidth() && mouseY<getHeight()) {
            if (this.getImage() == imageNone) {
                this.setImage(imageHoveredNone);
            } else if (this.getImage() == imageRequester) {
                this.setImage(imageHoveredRequester);
            } else if (this.getImage() == imageProvider) {
                this.setImage(imageHoveredProvider);
            }
        } else {
            if (this.getImage() == imageHoveredNone) {
                this.setImage(imageNone);
            } else if (this.getImage() == imageHoveredRequester) {
                this.setImage(imageRequester);
            } else if (this.getImage() == imageHoveredProvider) {
                this.setImage(imageProvider);
            }
        }
    }

    public void setOnClick(Runnable r) {
        this.onClick = r;
    }

    @Override
	public void onClick(int x, int y, int button) {
		super.onClick(x, y, button);
		
		MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

		if (onClick!=null) {
            this.onClick.run();
        }
	}
}