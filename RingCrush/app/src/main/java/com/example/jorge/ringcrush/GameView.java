package com.example.jorge.ringcrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View implements Runnable{

    Handler handler = new Handler();
    public ArrayList<Ring> rings;
    int[] colors;
    Random random;
    int score;
    Canvas can;

    //variáveis utilizadas para os eventos de Touch e troca de Cor
    int tX,tY,tC,tI;

    public GameView(Context c)
    {
        super(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        int eventaction = event.getAction();

        switch (eventaction)
        {
            case MotionEvent.ACTION_DOWN:
                tX = (int) event.getX();
                tY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                tX = 0;
                tY = 0;
                break;

        }
        return true;
    }

    public int selectColor(int a)
    {
        switch (a)
        {
            case 1:
                return Color.YELLOW;
            case 2:
                return Color.RED;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.BLACK;
            default:
                return Color.BLUE;
        }
    }

    public void init(Canvas c)
    {
        rings = new ArrayList<Ring>();
        random = new Random();
        tX = 0;
        tY = 0;
        tC = 0;
        tI = 0;
        score = 0;
        for(int i = 0; i < 5; i++)
        {
            for(int n = 0; n < 5; n++)
            {
                rings.add(new Ring(i*(c.getWidth()/5) + (c.getWidth()/10),n*(c.getWidth()/5) + 250,(c.getWidth()/10),selectColor(random.nextInt(5))));
            }
        }
    }

    @Override
    public void run()
    {
        handler.postDelayed(this, 30);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if(can == null){
            can = canvas;
            init(can);
        }
        Bitmap rs = BitmapFactory.decodeResource(this.getResources(),R.drawable.rainbowsquare);
        Paint p = new Paint();
        p.setTextSize(40);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Seus pontos: " + score, (canvas.getWidth() / 2), 50, p);
        canvas.drawText("Consiga 30", (canvas.getWidth() / 2), 90,p);
        p.setColor(Color.WHITE);
        crushing();
        for(int i = 0; i < rings.size(); i++)
        {
            rings.get(i).Draw(canvas);
            if(rings.get(i).Collision(tX,tY))
            {
                tX = 0;
                tY = 0;

                if(tC != 0)
                {
                    if(changeColors(tI,i))
                    {
                        crushing();
                    }
                    tC = 0;
                    tI = 0;
                }
                else
                {
                    tC = rings.get(i).color;
                    tI = i;
                }
            }
        }
        super.onDraw(canvas);
        invalidate();
    }

    public boolean changeColors(int i1, int i2)
    {
        if((i2 == i1 - 5) || (i2 == i1 + 5) || (i2 == i1 - 1) || (i2 == i1 + 1))
        {
            rings.get(i1).setColor(rings.get(i2).color);
            rings.get(i2).setColor(tC);
            return true;
        }
        return false;
    }

    public void crushing()
    {
        for(int i = 0; i < rings.size(); i++)
        {
            crush(i,1);
        }
        if(score>30){
            MainActivity m = new MainActivity();
            m.gameOver();
            Log.d("MainActivity","Won!!");
            score = 0;
        }
    }

    public boolean crush(int index, int count)
    {
        if(index + 5 < 25)
        {
            if(rings.get(index).color == rings.get(index + 5).color)
            {
                if(crush(index + 5,count+1))
                {
                    rings.get(index).setColor(selectColor(random.nextInt(5)));
                    return true;
                }
            }
        }

        if(index + 1 < 25)
        {
            if(rings.get(index).color == rings.get(index + 1).color)
            {
                if(crush(index + 1,count+1))
                {
                    rings.get(index).setColor(selectColor(random.nextInt(5)));
                    return true;
                }
            }
        }

        if(count >= 3)
        {
            rings.get(index).setColor(selectColor(random.nextInt(5)));
            score++;
            return true;
        }
        else
            return false;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


}
