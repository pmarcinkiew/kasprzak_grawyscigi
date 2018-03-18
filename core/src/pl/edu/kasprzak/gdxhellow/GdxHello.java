package pl.edu.kasprzak.gdxhellow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;

public class GdxHello extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera mapCamera;
	OrthographicCamera carCamera;
	Texture mapa;
	Texture grid;
	Texture car;
	Pixmap colideMap;

	private float rotationD = 0;
	private float rotationSpeed = 150;
	private float forwardD = 250;
	private float srotdeg;
	private float x = 0;
	private float y = 0;

	private static boolean gameover = false;
	private float endtime = -1;

	@Override
	public void create () {
		batch = new SpriteBatch();
		mapCamera = new OrthographicCamera();
		carCamera = new OrthographicCamera();
		mapa = new Texture("mapa.png");
		grid = new Texture("libgdxgridtest.png");
		car = new Texture("Audi.png");
		colideMap = new Pixmap(Gdx.files.internal("collisionMap.png"));
		gameover = false;
	}

	float time = 0;

	@Override
	public void render () {
		// Na razie symulejemy ruch gracza korzystając z funkcji trygonomoetrycznych i czasu


		// Skanujemy dotyk dla maksymalnie 10 palców

		for (int i = 0; i < 10; ++i) {
			if (Gdx.input.isTouched(i)) {
				Gdx.app.log("TOUCH", "touch ID: " + i + " x: " + Gdx.input.getX(i) + " y: " + Gdx.input.getY(i));

				if (Gdx.input.getX(i) < Gdx.graphics.getWidth() * 0.4) {
					Gdx.app.log("TOUCH", "LEFT");
					rotationD -= rotationSpeed;
				}

				if (Gdx.input.getX(i) > Gdx.graphics.getWidth() * 0.6) {
					Gdx.app.log("TOUCH", "RIGHT");
					rotationD += rotationSpeed;
				}
			}
		}


		// Zmienna time oznacza czas gry - getDeltaTime zwraca różnicę czasu
		if (!gameover) {
			time += Gdx.graphics.getDeltaTime();
			srotdeg = 90 - (rotationD * Gdx.graphics.getDeltaTime());
			float forward = forwardD * Gdx.graphics.getDeltaTime();
			float srotrad = (float) (srotdeg / 180 * Math.PI);
			x += Math.cos(srotrad) * forward;
			y += Math.sin(srotrad) * forward;

			int pixelX = (int) Math.floor(x);
			int pixelY = (int) Math.floor(1536 - 1 - y);
			int colorCode = colideMap.getPixel(pixelX, pixelY);
			Color color = new Color(colorCode);

			if (color.b > 0.7) {
				gameover = false;
				endtime = time;
				srotdeg = 90;
			}
		}

		/*
		float radius = 200;
		float centerX = 800;
		float centerY = 800;
		loat srotdeg = time * 180 / 3.14f; // Czas traktujemy jako radiany a obroty wymagają stopni
		float x = (float)Math.cos(time) * radius + centerX;
		float y = (float)Math.sin(time) * radius + centerY;*/

		// Czyścimy ekran - 0, 0, 0 da czarny, ale na razie jest zielony żeby grid było widać
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Ustawienie podstawowych parametrów kamery dla mapy
		mapCamera.setToOrtho(false, 800, 480); // Rozmiar wirtualnego ekranu
		mapCamera.translate(-230, 500);       // Przestawiamy na środek ekranu początek dla kamery
		// Przesuwamy kamerę żeby widziała aktualne miejsce gdzie jest gracz
		mapCamera.translate(x, y);
		// Kamera przelicza swoje parametry
		mapCamera.update();

		// Ustawienie podstawowych parametrów kamery dla samochodu
		carCamera.setToOrtho(false, 800, 480); // Rozmiar wirtualnego ekranu
		carCamera.translate(-400, -240);       // Przestawiamy na środek ekranu początek dla kamery
		carCamera.translate(25, 25);           // Przestawiamy środek obrotu na środek samochodu
		carCamera.rotate(-90 + srotdeg);             // Obracamy samochód zgodnie z kierunkiem jazdy
		carCamera.update();


		// Grupa rysowanych obiektów
		batch.begin();
		{
			// Ustawiamy kamerę dla mapy
			batch.setProjectionMatrix(mapCamera.combined);
			// Mapa
			batch.draw(mapa, 0, 0);
			// Pomocnicza siatka ze współrzędnymi
			batch.draw(grid, 0, 0);

			// Ustawiamy kamerę dla samochodu
			batch.setProjectionMatrix(carCamera.combined);
			// Rysujemy samochód
			batch.draw(car, 0, 0);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		mapa.dispose();
	}
}
