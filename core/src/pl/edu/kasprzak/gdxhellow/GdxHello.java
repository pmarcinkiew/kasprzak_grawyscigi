package pl.edu.kasprzak.gdxhellow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxHello extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;
	Texture mapa;
	Texture grid;
	Texture audi;

	@Override
	public void create () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera();
        mapa = new Texture("mapa.png");
		grid = new Texture("libgdxgridtest.png");
		audi = new Texture("Audi.png");
	}

	float time = 0;

	@Override
	public void render () {
		// Na razie symulejemy ruch gracza korzystając z funkcji trygonomoetrycznych i czasu
		time += Gdx.graphics.getDeltaTime();
		float base = time / 1;
		float x = (float)Math.cos(base) * 200 + 800;
		float y = (float)Math.sin(base) * 200 + 800;

		// Ustawienie podstawowych parametrów kamery
		camera.setToOrtho(false, 800, 480); // Rozmiar wirtualnego ekranu
		camera.translate(-400, -240);       // Przestawiamy na środek ekranu początek dla kamery

		// Przesuwamy kamerę żeby widziała aktualne miejsce gdzie jest gracz
		camera.translate(x, y);
		// Kamera przelicza swoje parametry
		camera.update();

		// Czyścimy ekran - 0, 0, 0 da czarny ale na razie jest zielony żeby grid było widać
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Ustawiamy dla grupy rysowanych obiektów widok oparty na kamerze
		batch.setProjectionMatrix(camera.combined);

		// Grupa rysowanyc obiektów
		batch.begin();
		{
			// Mapa
			batch.draw(mapa, 0, 0);
			// Pomocnicza siatka ze współrzędnymi
			batch.draw(grid, 0, 0);

			// Wyznaczamy współrzędne samochodu
			int swidth = audi.getWidth();   // Szerokość obrazka
			int sheight = audi.getHeight(); // Wysokość obrazka
			float sx = x - swidth / 2;      // Pozycja samochodu na mapie - trzeba przesunąć o pół obrazka
			float sy = y - sheight / 2;
			float rotx = swidth / 2;       // Środek obrotu
			float roty = swidth / 2;
			float sscale = 1.0f;
			float srotdeg = base * 180 / 3.14f; // Kierunkiem będzie sterować gracz
			// Rysujemy samochód
			batch.draw(audi,
					sx, sy,          // Pozycja samochodu na mapie
					rotx, roty,      // Środek obrotu
					swidth, sheight, // Wymiary
					sscale, sscale,  // Współczynnik skalowania
					srotdeg,         // Kąt obrotu [deg]
					0, 0,            // Lewy górny róg samochodu w pliku PNG
					swidth, sheight, // Prawy dolny róg samochodu w pliku PNG
					false, false);   // Brak lustrzanego odbicia
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
        mapa.dispose();
    }
}
