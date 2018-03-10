package pl.edu.kasprzak.gdxhellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class MyGdxGame implements Screen, InputProcessor{

    private final GameCore game;
    private OrthographicCamera camera;
    private Texture mapa;
    private Sprite auto;
    private Vector2 auto_position;
    private Music gameplay_music;

    private final float max_speed = 500;
    private final float turn_speed = 10;
    private final float speed_of_accel = 3;
    private final float speed_of_break = 3;
    private float accel_speed = 0;
    private float default_accel_y;
	private float auto_rotation;
    private boolean show_help = true;

	public MyGdxGame (final GameCore game) {
        Gdx.input.setCatchBackKey(true);
        this.game = game;
        Gdx.input.setInputProcessor(this);
        game.font.getData().setScale(2f);
        gameplay_music = Gdx.audio.newMusic(Gdx.files.internal("Gameplay.mp3"));
        gameplay_music.setVolume(1f);
        gameplay_music.setLooping(true);
        gameplay_music.play();
		camera = new OrthographicCamera(800,480);
        camera.setToOrtho(false, 800, 480);
		mapa = new Texture("mapa.png");
		auto = new Sprite(new Texture("Audi.png"));
        auto.setOrigin(auto.getWidth()/2, auto.getHeight()/2);
        //set auto
		auto_position = new Vector2(150,695);
		auto_rotation = 0;
        auto.setRotation(auto_rotation);
        auto.setPosition(0,0);
        camera.position.set(auto.getOriginX() + auto_position.x,auto.getOriginY()+auto_position.y,0);
        camera.rotate(0);
        default_accel_y = Gdx.input.getAccelerometerY();

	}

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(57/255.0f,174/255.0f,194/255.0f,1);
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        //camera update
        camera.update();
        game.batch.begin(); //start rysowania
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.draw(mapa, 0, 0);
        { //auto draw and calculate
            if(Gdx.input.isTouched()){
                if (accel_speed<max_speed) {
                    accel_speed += speed_of_accel;
                }
            }else {
                if (accel_speed > 0) {
                    accel_speed -= speed_of_break;
                    if(accel_speed<0) accel_speed = 0;
                }
            }
            if (accel_speed != 0) {
                float accel_y = Gdx.input.getAccelerometerY();
                auto_add_rotation((accel_y - default_accel_y) * Gdx.graphics.getDeltaTime() * turn_speed);
            }
            auto_move_forward();
            auto.setRotation(auto_rotation);
            auto.setPosition(auto_position.x,auto_position.y);
            auto.draw(game.batch);
        }
        if(show_help){
            game.layout.setText(game.font, "Dotknij aby jechac, obracaj telefonem aby skrecac");
            game.font.draw(game.batch, game.layout, auto.getX() - game.layout.width/2 + auto.getWidth()/2, auto.getY());
        }
        game.batch.end(); //koniec rysowania
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
	public void dispose () {
        gameplay_music.dispose();
		mapa.dispose();
	}

    private void auto_move_forward(){
        float x = accel_speed * Gdx.graphics.getDeltaTime() * MathUtils.cos((float)((Math.PI / 180) * ( auto_rotation + 90 )));
        float y = accel_speed * Gdx.graphics.getDeltaTime() * MathUtils.sin((float)((Math.PI / 180) * ( auto_rotation + 90 )));

        if((auto_position.x + x) < 0 ) {
            auto_position.x = 0;
            camera.position.set(auto_position.x + auto.getOriginX(), auto_position.y + auto.getOriginY(),0);
            if (accel_speed>200){
                accel_speed = 200;
            }
        }else if((auto_position.x + x) > mapa.getWidth() - auto.getHeight()){
            auto_position.x = mapa.getWidth() - auto.getHeight();
            camera.position.set(auto_position.x + auto.getOriginX(), auto_position.y + auto.getOriginY(),0);
            if (accel_speed>200){
                accel_speed = 200;
            }
        }else {
            auto_position.x += x;
            camera.translate(x,0);
        }

        if((auto_position.y + y) < 0) {
            auto_position.y = 0;
            camera.position.set(auto_position.x + auto.getOriginX(), auto_position.y + auto.getOriginY(),0);
            if (accel_speed>200){
                accel_speed = 200;
            }
        }else if((auto_position.y + y) > mapa.getHeight() - auto.getHeight()){
            auto_position.y = mapa.getHeight() - auto.getHeight();
            camera.position.set(auto_position.x + auto.getOriginX(), auto_position.y + auto.getOriginY(),0);
            if (accel_speed>200){
                accel_speed = 200;
            }
        }else {
            auto_position.y += y;
            camera.translate(0,y);
        }
        Gdx.app.log("Accel speed", ""+accel_speed);
    }

    private void auto_add_rotation(float degrees){
        if(auto_rotation>359){
            auto_rotation = 0 + (auto_rotation - 360);
        }
        if(auto_rotation<-359){
            auto_rotation = 0 - (auto_rotation + 360);
        }
        auto_rotation -= degrees;
        camera.rotate(degrees);
    }

    public void auto_move(float x, float y, float degrees){
        auto_rotation = 0 - degrees;
        auto_position.x = x;
        auto_position.y = y;
        camera.position.set(auto_position.x + auto.getOriginX(),auto_position.y + auto.getOriginY(),0);
        camera.rotate(degrees);
    }


    //input handle
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            Gdx.input.setCatchBackKey(false);
            this.dispose();
            game.setScreen(new MainMenu(game));
            return true;
        }
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
        if(show_help){
            show_help = !show_help;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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

