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
    private Pixmap pixmap;
    Color position_color = new Color();

    private short state = 0;
    private final float max_speed = 500;
    private final float turn_speed = 10;
    private final float speed_of_accel = 3;
    private final float speed_of_break = 3;
    private float accel_speed = 0;
    private float default_accel_y;
	private float auto_rotation;
    private boolean show_help = true;
    private boolean lose = false;
    private boolean licz = false;

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
        auto.setPosition(auto_position.x,auto_position.y);
        camera.position.set(auto.getOriginX() + auto_position.x,auto.getOriginY()+auto_position.y,0);
        camera.rotate(0);
        pixmap = new Pixmap(Gdx.files.internal("collisionMap.png"));
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
            if(Gdx.input.isTouched() && !lose){
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
                autoAddRotation((accel_y - default_accel_y) * Gdx.graphics.getDeltaTime() * turn_speed);
                autoMoveForward();
                checkState();
                Gdx.app.log("State: ", ""+state);
                if(state == 4) weHaveAWinner();
            }
            auto.setRotation(auto_rotation);
            auto.setPosition(auto_position.x,auto_position.y);
            auto.draw(game.batch);
        }
        if(show_help){
            game.layout.setText(game.font, "Dotknij aby jechac, obracaj telefonem aby skrecac");
            game.font.draw(game.batch, game.layout, auto.getX() - game.layout.width/2 + auto.getWidth()/2, auto.getY());
        }
        if(lose){
            game.layout.setText(game.font, "Wypadles z mapy i musisz zaczac od poczatku...");
            game.font.draw(game.batch, game.layout, auto.getX() - game.layout.width/2 + auto.getWidth()/2, auto.getY());
        }
        game.batch.end(); //koniec rysowania
        if(licz) game.timer += Gdx.graphics.getDeltaTime();
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
        pixmap.dispose();
	}

    private void autoMoveForward(){
        float x = accel_speed * Gdx.graphics.getDeltaTime() * MathUtils.cos((float)((Math.PI / 180) * ( auto_rotation + 90 )));
        float y = accel_speed * Gdx.graphics.getDeltaTime() * MathUtils.sin((float)((Math.PI / 180) * ( auto_rotation + 90 )));
        auto_position.x += x;
        auto_position.y += y;
        Color.rgba8888ToColor(position_color,pixmap.getPixel((int)auto_position.x+(int)auto.getOriginX(),(int)auto_position.y+(int)auto.getOriginY()));
        int blue = (int)(position_color.b * 255f);
        if(blue == 255) {
            auto_position.x = 150;
            auto_position.y = 695;
            auto_rotation = 0;
            accel_speed = 0;
            camera.position.set(auto_position.x,auto_position.y,0);
            camera.up.set(0, 1, 0);
            camera.direction.set(0, 0, -1);
            lose = true;
            licz = false;
            state = 0;
        }else{
            camera.translate(x, y);
        }
    }

    private void autoAddRotation(float degrees){
        if(auto_rotation>359){
            auto_rotation = 0 + (auto_rotation - 360);
        }
        if(auto_rotation<-359){
            auto_rotation = 0 - (auto_rotation + 360);
        }
        auto_rotation -= degrees;
        camera.rotate(degrees);
    }

    private void checkState(){
        if(state == 0){
            if((int)(position_color.r * 255f) == 255) state++;
        }else if(state == 1){
            if((int)(position_color.r * 255f) == 0 && (int)(position_color.g * 255f) == 0 && (int)(position_color.b * 255f) == 0){state++;}
            else if((int)(position_color.g * 255f) == 255) state--;
        }else if(state == 2){
            if((int)(position_color.g * 255f) == 255){state++;}
            else if((int)(position_color.r * 255f) == 255) state--;
        }else if(state == 3) {
            if((int)(position_color.r * 255f) == 255){state++;}
            else if((int)(position_color.r * 255f) == 0 && (int)(position_color.g * 255f) == 0 && (int)(position_color.b * 255f) == 0) state--;
        }
    }

    private void weHaveAWinner(){
        game.setScreen(new GameOver(game),this);
    }
    /*public void autoMove(float x, float y, float degrees){
        auto_rotation = 0 - degrees;
        auto_position.x = x;
        auto_position.y = y;
        camera.position.set(auto_position.x + auto.getOriginX(),auto_position.y + auto.getOriginY(),0);
        camera.rotate(degrees);
    }*/


    //input handle
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            Gdx.input.setCatchBackKey(false);
            game.setScreen(new MainMenu(game),this);
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
            licz = true;
            default_accel_y = Gdx.input.getAccelerometerY();
        }
        if(lose){
            lose = false;
            licz = true;
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

