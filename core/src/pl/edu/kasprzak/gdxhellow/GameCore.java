package pl.edu.kasprzak.gdxhellow;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameCore extends Game {

    SpriteBatch batch;
    BitmapFont font;
    Music back_music;
    GlyphLayout layout;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        layout = new GlyphLayout();
        back_music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        font = new BitmapFont();
        back_music.setVolume(1f);
        back_music.setLooping(true);
        back_music.play();
        this.setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        back_music.dispose();
    }


}
