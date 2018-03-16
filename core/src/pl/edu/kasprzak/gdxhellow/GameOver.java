package pl.edu.kasprzak.gdxhellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameOver implements Screen, InputProcessor {

    final GameCore game;
    private Music menu_music;
    private OrthographicCamera cam;
    private boolean touched = false;

    public GameOver(GameCore game) {
        this.game = game;
        Gdx.input.setInputProcessor(this);
        menu_music = Gdx.audio.newMusic(Gdx.files.internal("Menu.mp3"));
        menu_music.setVolume(1f);
        menu_music.setLooping(true);
        menu_music.play();
        cam = new OrthographicCamera(800,480);
        cam.position.set(0,0,0);
        game.font.getData().setScale(2);
        if(Gdx.input.isTouched()) touched = true;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(57/255.0f,174/255.0f,194/255.0f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.layout.setText(game.font, "Twój czas wynosi "+String.format("%.2f", game.timer)+"s");
        game.font.draw(game.batch, game.layout, -(game.layout.width/2), game.layout.height);
        game.layout.setText(game.font, "Dotknij aby sprobowac ponownie");
        game.font.draw(game.batch, game.layout, -(game.layout.width/2), -(game.layout.height));
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        menu_music.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(touched){
            touched = false;
            return false;
        }
        game.setScreen(new MyGdxGame(game),this);
        game.timer = 0;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
