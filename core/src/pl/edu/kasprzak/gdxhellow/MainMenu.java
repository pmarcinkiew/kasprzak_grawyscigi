package pl.edu.kasprzak.gdxhellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenu implements Screen {

    final GameCore game;
    OrthographicCamera cam;
    float additional_x = 0;
    float speed_of_jump = 150;
    boolean is_going_up = true;

    public MainMenu(final GameCore game) {
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
        if (Gdx.input.isTouched()){
            game.setScreen(new MyGdxGame(game));
            dispose();
        }
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

}
