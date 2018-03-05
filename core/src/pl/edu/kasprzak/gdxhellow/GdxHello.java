package pl.edu.kasprzak.gdxhellow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxHello extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera mapCamera;
	OrthographicCamera carCamera;
	Texture mapa;
	Texture grid;
	Texture car;

	@Override
	public void create () {
		batch = new SpriteBatch();
        mapCamera = new OrthographicCamera();
		carCamera = new OrthographicCamera();
        mapa = new Texture("mapa.png");
		grid = new Texture("libgdxgridtest.png");
		car = new Texture("Audi.png");
	}

	float time = 0;

	@Override
	public void render () {
		// Na razie symulejemy ruch gracza korzystając z funkcji trygonomoetrycznych i czasu

		// Zmienna time oznacza czas gry - getDeltaTime zwraca różnicę czasu
		time += Gdx.graphics.getDeltaTime();
		float srotdeg = time * 180 / 3.14f; // Czas traktujemy jako radiany a obroty wymagają stopni
		float x = (float)Math.cos(time) * 200 + 800;
		float y = (float)Math.sin(time) * 200 + 800;

		// Czyścimy ekran - 0, 0, 0 da czarny, ale na razie jest zielony żeby grid było widać
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Ustawienie podstawowych parametrów kamery dla mapy
		mapCamera.setToOrtho(false, 800, 480); // Rozmiar wirtualnego ekranu
		mapCamera.translate(-400, -240);       // Przestawiamy na środek ekranu początek dla kamery
		// Przesuwamy kamerę żeby widziała aktualne miejsce gdzie jest gracz
		mapCamera.translate(x, y);
		// Kamera przelicza swoje parametry
		mapCamera.update();

		// Ustawienie podstawowych parametrów kamery dla samochodu
		carCamera.setToOrtho(false, 800, 480); // Rozmiar wirtualnego ekranu
		carCamera.translate(-400, -240);       // Przestawiamy na środek ekranu początek dla kamery
		carCamera.rotate(srotdeg);             // Obracamy samochód zgodnie z kierunkiem jazdy
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
