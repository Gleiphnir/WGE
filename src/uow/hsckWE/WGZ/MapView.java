package uow.hsckWE.WGZ;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.View;

public class MapView extends View {
	private Paint mapPaint;
	private Path path;
		
	public MapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setBackgroundColor(Color.GRAY);
		initPaint();
		//refreshPath();
		this.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	refreshPath();
            	invalidate();
            }
        });
	}
	
	@Override
	protected void onSizeChanged (int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		refreshPath();
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawPath(path, mapPaint);
	}
	
	private void initPaint() {
		mapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mapPaint.setStyle(Paint.Style.STROKE);
	}
	
	public void refreshPath() {
		ArrayList<Point> pointsArray;
		pointsArray=generatePointsArray(100,this.getWidth(),this.getHeight());
		path=getPathFromPoints(pointsArray);
	}
	
	private ArrayList<Point> generatePointsArray(int number, int width, int heigth) {
		ArrayList<Point> pointsArray = new ArrayList<Point>();
		for (int i=0;i<number;i++)
			pointsArray.add(getRandomPoint(width,heigth));
		return pointsArray;
	}
	
	private Path getPathFromPoints(ArrayList<Point> pointsArray) {
		Path path=new Path();
		Point p;
		p=pointsArray.get(0);
		path.moveTo(p.x, p.y);
		for (int i=1;i<pointsArray.size();i++) {
			p=pointsArray.get(i);
			path.lineTo(p.x, p.y);
		}
		return path;
	}
	
	private Point getRandomPoint(int width, int height) {
		Point point;
		point = new Point((int)(Math.random()*width), (int)(Math.random()*height));
		return point;
	}
	
}
