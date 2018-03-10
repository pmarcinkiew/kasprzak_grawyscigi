package pl.edu.kasprzak.gdxhellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenu implements Screen, InputProcessor {

    final GameCore game;
    OrthographicCamera cam;
    float additional_x = 0;
    float speed_of_jump = 150;
    boolean is_going_up = true;

    public MainMenu(final GameCore game) {
        Gdx.input.setInputProcessor(this);
        this.game = game;
        cam = new OrthographicCamera(800,480);
        cam.position.set(0,0,0);
        game.font.getData().setScale(2);
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
        jumping_text();
        game.batch.begin();
        game.layout.setText(game.font, "Najlepsza gra wyscigowa z samsungiem!!!");
        game.font.draw(game.batch, game.layout, -(game.layout.width/2), game.layout.height + additional_x);
        game.layout.setText(game.font, "Dotknij aby rozpoczac");
        game.font.draw(game.batch, game.layout, -(game.layout.width/2), -(game.layout.height) + additional_x);
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

    }

    private void jumping_text(){
        if (additional_x < 60 && is_going_up){
            additional_x += speed_of_jump * Gdx.graphics.getDeltaTime();
            if(additional_x >= 60) is_going_up = false;
        }else if(!is_going_up){
            additional_x -= speed_of_jump * Gdx.graphics.getDeltaTime();
            if(additional_x <= 0) is_going_up = true;
        }
    }

    //input handle

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
        this.dispose();
        game.setScreen(new MyGdxGame(game));
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
