package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background,man;
	Texture[] manframes;
	int manshow=0;
	int pause=0;
	float many=0;
	int score=0;
	BitmapFont font;
	float velocity=0;
	int gamestate=0;
	float gravity=1.5f;
	Random random;
	Texture coin;
	Texture bomb;
	Rectangle manrectangle;
	int coincount;
	int bombcount;
	BitmapFont again;
	ArrayList<Integer> coinXs=new ArrayList<>();
	ArrayList<Integer> coinYs=new ArrayList<>();
	ArrayList<Rectangle> coinrectangle=new ArrayList<>();
    ArrayList<Integer> bombXs=new ArrayList<>();
    ArrayList<Integer> bombYs=new ArrayList<>();
	ArrayList<Rectangle> bombrectangle=new ArrayList<>();
	float textWidth=0;
	float texth=0;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		manframes=new Texture[4];
		manframes[0]=new Texture("frame-1.png");
		manframes[1]=new Texture("frame-2.png");
		manframes[2]=new Texture("frame-3.png");
		manframes[3]=new Texture("frame-4.png");
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		random=new Random();
		again=new BitmapFont();
		font=new BitmapFont();
		font.setColor(Color.RED);
		font.getData().setScale(10);
		again.setColor(Color.RED);
		again.getData().setScale(10);
		many=Gdx.graphics.getHeight() / 9 - manframes[manshow].getHeight() / 2;
		GlyphLayout layout = new GlyphLayout(font, "Tap To Play Again");
		textWidth = layout.width;
		texth=layout.height;

	}
	public void coinmake()
	{
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}
    public void bombmake()
    {
        float height=random.nextFloat()*Gdx.graphics.getHeight();
        bombYs.add((int)height);
        bombXs.add(Gdx.graphics.getWidth());
    }

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if(gamestate==1) {
			//Running
			if (coincount < 60) {
				coincount++;
			} else {
				coincount = 0;
				coinmake();
			}
			coinrectangle.clear();
			for (int i = 0; i < coinYs.size(); i++) {
				batch.draw(coin, coinXs.get(i), coinYs.get(i));
				coinXs.set(i, coinXs.get(i) - 15);
				coinrectangle.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
			}
			if (bombcount < 200) {
				bombcount++;
			} else {
				bombcount = 0;
				bombmake();
			}
			bombrectangle.clear();
			for (int i = 0; i < bombYs.size(); i++) {
				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i) - 30);
				bombrectangle.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}
			if (Gdx.input.justTouched()) {
				velocity = -40;
			}
			if (pause > 0) {
				pause--;
			} else {
				if (manshow < 3) {
					manshow++;
				} else
					manshow = 0;
				velocity += gravity;
				many -= velocity;
				if (many <= (Gdx.graphics.getHeight() / 9 - manframes[manshow].getHeight() / 2)) {
					many = Gdx.graphics.getHeight() / 9 - manframes[manshow].getHeight() / 2;
				}
			}
		}
		else if(gamestate==0)
		{
			//waiting to sart
			if(Gdx.input.justTouched())
			{
				gamestate=1;
			}
		}
		else if(gamestate==2)
		{
			again.draw(batch,"Tap To Play Again",Gdx.graphics.getWidth()/2-textWidth/2,Gdx.graphics.getHeight()/2-texth/2);
			//over
			if(Gdx.input.justTouched())
			{
				gamestate=1;
				many=Gdx.graphics.getHeight() / 9 - manframes[manshow].getHeight() / 2;
				score=0;
				velocity=0;
				coinYs.clear();
				coinXs.clear();
				coinrectangle.clear();
				coincount=0;
				bombYs.clear();
				bombXs.clear();
				bombrectangle.clear();
				bombcount=0;
			}
		}



			batch.draw(manframes[manshow], Gdx.graphics.getWidth() / 8 - manframes[manshow].getWidth() / 2, many, manframes[manshow].getWidth(), manframes[manshow].getHeight());
			manrectangle=new Rectangle(Gdx.graphics.getWidth() / 8 - manframes[manshow].getWidth() / 2, many,manframes[manshow].getWidth(),manframes[manshow].getHeight());
			for(int i=0;i<coinrectangle.size();i++)
			{
				if(Intersector.overlaps(manrectangle,coinrectangle.get(i)))
				{
					score++;
					coinrectangle.remove(i);
					coinXs.remove(i);
					coinYs.remove(i);
					break;
				}
			}
			for(int i=0;i<bombrectangle.size();i++)
			{
				if(Intersector.overlaps(manrectangle,bombrectangle.get(i)))
				{
					Gdx.app.log(":::::Bomb!!",":::::Collided!!");
					gamestate=2;
				}
			}
			font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()-200,Gdx.graphics.getHeight()-100);
			batch.end();
			pause=0;


		}


	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
