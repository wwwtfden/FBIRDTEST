package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import org.w3c.dom.Text;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch; //область отрисовки объектов
	//Texture img; //текстура, какая-то
	Background bg;
	Bird bird;
	Obstacles obstacles;
	boolean gameOver;
	Texture restartTexture;

	SpriteBatch spriteBatch;
	BitmapFont bitmapFont;

	String str = "0";

	int pointCounter = 0;
	
	@Override
	public void create () { //запускается один раз, подгружает все необходимые элементы.
		batch = new SpriteBatch();
		bg = new Background();
		bird = new Bird();
		obstacles = new Obstacles();
		gameOver = false;
		restartTexture = new Texture("RestartBtn.png");

		bitmapFont = new BitmapFont();
		bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		bitmapFont.getData().setScale(3);
	}

	@Override
	public void render () { //выз каждый кадр
		update(); //пересчет математики для всей игры
		ScreenUtils.clear(1, 1, 1, 1); //заливка всего игрового поля каким-то цветом (RGB + альфа-канал)
		batch.begin(); //начало отрисовки
		//batch.draw(img, 0, 0); //отрисовка фоновой картинки с параметрами начальных координат (с левого НИЖНЕГО угла)
		bg.render(batch); //рендерим бэкграунд при помощи batch
		obstacles.render(batch);

//		BitmapFont font = new BitmapFont(Gdx.files.internal("data/rayanfont.fnt"), false);
//		font.draw(batch, "Sample Text", 0,0);


		//spriteBatch = new SpriteBatch();
		//spriteBatch.begin();
		bitmapFont.draw(batch, str, 50, 550);
		//spriteBatch.end();


		if (!gameOver){
			bird.render(batch); //также рендерим птицу
		} else {
			batch.draw(restartTexture, 200,200);
			String resultLabel = "COMPLETE! Total points: " + String.valueOf(pointCounter);
			bitmapFont.draw(batch, resultLabel, 120, 100);
		}

		batch.end();
	}

	//функция, в которой мы будем хранить и изменять положение картинки с обновлением
	public void update(){
		bg.update();
		bird.update();
		obstacles.update();
		for (int i = 0; i < Obstacles.obs.length; i++) {
			if (bird.position.x > Obstacles.obs[i].position.x && bird.position.x < Obstacles.obs[i].position.x + 50){
				if (!Obstacles.obs[i].emptySpace.contains(bird.position)){
					gameOver = true;
				}
			}
		}
		if (bird.position.y < 0 || bird.position.y > 600){
			gameOver = true;
		}


		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && gameOver){
			recreate();
		}
		for (int i = 0; i < 4; i++) {
			if (bird.position.x == obstacles.obs[i].position.x + 50){
				//System.out.println("PROLETELI NAHUI " + i);
				if (!gameOver){
					pointCounter++;
				}
				str = String.valueOf(pointCounter);
				//System.out.println("Points: " + pointCounter);
			}
		}

	}
	
	@Override
	public void dispose () { //очистка ресурсов
		batch.dispose(); //типа удаляем...
		//img.dispose();
	}

	public void recreate(){
		pointCounter = 0;
		bird.recreate();
		obstacles.recreate();
		gameOver = false;
	}
}
