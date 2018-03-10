package pl.edu.kasprzak.gdxhellow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class MyGdxGame implements Screen{

    private final GameCore game;
    private OrthographicCamera camera;
    private Texture mapa;
    private Sprite auto;
    private Vector2 auto_position;

    private final float max_speed = 450;
    private final float turn_speed = 10;
    private final float speed_of_accel = 3;
    private final float speed_of_break = 3;
    private float accel_speed = 0;
    private float default_accel_y;
	private float auto_rotation;

	public MyGdxGame (final GameCore game) {
        this.game = game;
		camera = new OrthographicCamera(800,480);
        camera.setToOrtho(false, 800, 480);
		mapa = new Texture("mapa.png");
		auto = new Sprite(new Texture("Audi.png"));
        auto.setOrigin(auto.getWidth()/2, auto.getHeight()/2);
        //set auto
		auto_position = new Vector2(50,50);
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
		mapa.dispose();
	}

    private void auto_move_forward(){
        float x = accel_speed * Gdx.graphics.getDeltaTime() * MathUtils.cos((float)((Math.PI / 180) * ( auto_rotation + 90 )));
        float y = accel_speed * Gdx.graphics.getDeltaTime() * MathUtils.sin((float)((Math.PI / 180) * ( auto_rotation + 90 )));

        if((auto_position.x + x)>=0) {
            auto_position.x += x;
            camera.translate(x,0);
        }else {
            auto_position.x = 0;
            camera.position.set(auto_position.x + auto.getOriginX(), auto_position.y + auto.getOriginY(),0);
            if (accel_speed>200){
                accel_speed = 200;
            }
        }

        if((auto_position.y + y)>=0) {
            auto_position.y += y;
            camera.translate(0,y);
        }else {
            auto_position.y = 0;
            camera.position.set(auto_position.x + auto.getOriginX(), auto_position.y + auto.getOriginY(),0);
            if (accel_speed>200){
                accel_speed = 200;
            }
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

}

