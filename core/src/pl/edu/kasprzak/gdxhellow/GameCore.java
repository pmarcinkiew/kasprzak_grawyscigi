package pl.edu.kasprzak.gdxhellow;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameCore extends Game {

    SpriteBatch batch;
    BitmapFont font;
    GlyphLayout layout;
    float timer = 0;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        layout = new GlyphLayout();
        font = new BitmapFont();
        this.setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }
    public void setScreen(Screen screen,Screen thisScreen) {
        thisScreen.dispose();
        super.setScreen(screen);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
