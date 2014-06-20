package mytry.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * 继承ImageView 实现了多点触碰的拖动和缩放
 * 
 * @author Administrator
 * 
 */
public class TouchView extends ImageView {
	static final int NONE = 0;
	static final int DRAG = 1; // 拖动中
	static final int ZOOM = 2; // 缩放中
	static final int BIGGER = 3; // 放大ing
	static final int SMALLER = 4; // 缩小ing
	static final int ROTA = 5; // 缩小ing
	
	private int mode = NONE; // 当前的事件

	private float beforeLenght; // 两触点距离
	private float afterLenght; // 两触点距离
	private float scale = 0.04f; // 缩放的比例 X Y方向都是这个值 越大缩放的越快

	private int screenW;
	private int screenH;

	/* 处理拖动 变量 */
	private int start_x;
	private int start_y;
	private int stop_x;
	private int stop_y;

	private TranslateAnimation trans; // 处理超出边界的动画
	
	private boolean able=true;
	private boolean xzFlag = false;
	
	private Matrix savedMatrix;  
	private PointF startPoint;  
	 private Matrix matrix;  
	 private float oldDistance;  
	    private float oldAngle; 
	    private PointF middlePoint;  

	/**
	 * 默认构造函数
	 * 
	 * @param context
	 */
	public TouchView(Context context) {
		super(context);
	
	}

	/**
	 * 该构造方法在静态引入XML文件中是必须的
	 * 
	 * @param context
	 * @param paramAttributeSet
	 */
	public TouchView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		
	}
	
	public TouchView(Context context, int w, int h) {
		super(context);
		this.setPadding(0, 0, 0, 0);
		screenW = w;
		screenH = h;

	}

	/**
	 * 就算两点间的距离
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * 处理触碰..
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		 if(xzFlag == true){
				matrix = new Matrix();  
		        savedMatrix = new Matrix();  
		          
		        matrix.setTranslate(0f, 0f);  
		        setScaleType(ScaleType.MATRIX);  
		        setImageMatrix(matrix);  
		          
		        startPoint = new PointF();  
		        middlePoint = new PointF();  
		          
		        oldDistance = 1f;  
			 switch(event.getAction() & MotionEvent.ACTION_MASK)  
             {  
             case MotionEvent.ACTION_DOWN:           // 第一个手指touch  
                 savedMatrix.set(matrix);  
                 startPoint.set(event.getX(), event.getY());  
                 mode = ROTA;  
                 break;  
             case MotionEvent.ACTION_POINTER_DOWN:   // 第二个手指touch  
                 oldDistance = getDistance(event);   // 计算第二个手指touch时，两指之间的距离  
                 oldAngle = getDegree(event);        // 计算第二个手指touch时，两指所形成的直线和x轴的角度  
//                 if(oldDistance > 10f)  
//                 {  
                     savedMatrix.set(matrix);  
                     middlePoint = midPoint(event);  
                    
                         mode = ROTA;  
//                 }  
                 break;  
             case MotionEvent.ACTION_UP:  
                 mode = NONE;  
                 break;  
             case MotionEvent.ACTION_POINTER_UP:  
                 mode = NONE;  
                 break;  
             case MotionEvent.ACTION_MOVE:  
                 if(mode == DRAG)  
                 {  
                     matrix.set(savedMatrix);  
                     matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);  
                 }  
                   
                 if(mode == ZOOM)  
                 {  
                     float newDistance = getDistance(event);  
                       
                     if(newDistance > 10f)  
                     {  
                         matrix.set(savedMatrix);  
                         float scale = newDistance / oldDistance;  
                         matrix.postScale(scale, scale, middlePoint.x, middlePoint.y);  
                     }  
                 }  
                   
                 if(mode == ROTA)  
                 {  
                     float newAngle = getDegree(event);  
                     matrix.set(savedMatrix);  
                     float degrees = newAngle - oldAngle;  
                     matrix.postRotate(degrees, middlePoint.x, middlePoint.y);  
                 }  
                 break;  
             }  
             setImageMatrix(matrix);  
             invalidate();  
           
		 }else{
				if (able) {
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						this.setBackgroundResource(R.drawable.bg_border);
							 mode = DRAG;
								stop_x = (int) event.getRawX();
								stop_y = (int) event.getRawY();
								start_x = (int) event.getX();
								start_y = stop_y - this.getTop();
								if (event.getPointerCount() == 2)
									beforeLenght = spacing(event);
					
						break;
					case MotionEvent.ACTION_POINTER_DOWN:
							  if (spacing(event) > 10f) {
									mode = ZOOM;
									beforeLenght = spacing(event);
								}
						break;
					case MotionEvent.ACTION_POINTER_UP:
						mode = NONE;
						break;
					case MotionEvent.ACTION_UP:
						this.setBackgroundColor(Color.TRANSPARENT);
						mode = NONE;
						break;
						
					case MotionEvent.ACTION_MOVE:
						/* 处理拖动 */
						
						if (mode == DRAG) {
							if (Math.abs(stop_x - start_x - getLeft()) < 88
									&& Math.abs(stop_y - start_y - getTop()) < 85) {
								this.setPosition(stop_x - start_x, stop_y - start_y, stop_x
										+ this.getWidth() - start_x, stop_y - start_y
										+ this.getHeight());
								stop_x = (int) event.getRawX();
								stop_y = (int) event.getRawY();
							}
						}
						/* 处理缩放 */
						else if (mode == ZOOM) {
			                	 if (spacing(event) > 10f) {
			     					afterLenght = spacing(event);
			     					float gapLenght = afterLenght - beforeLenght;
			     					if (gapLenght == 0) {
			     						break;
			     					} else if (Math.abs(gapLenght) > 5f) {
			     						if (gapLenght > 0) {
			     							this.setScale(scale, BIGGER);
			     						} else {
			     							this.setScale(scale, SMALLER);
			     						}
			     						beforeLenght = afterLenght;
			     					}
			     				}
			                 
							
						}
						break;
					}
					}
		 }

		
		return true;
	}

	/**
	 * 实现处理缩放
	 */
	private void setScale(float temp, int flag) {

		if (flag == BIGGER) {
			this.setFrame(this.getLeft() - (int) (temp * this.getWidth()),
					this.getTop() - (int) (temp * this.getHeight()),
					this.getRight() + (int) (temp * this.getWidth()),
					this.getBottom() + (int) (temp * this.getHeight()));
		} else if (flag == SMALLER) {
			this.setFrame(this.getLeft() + (int) (temp * this.getWidth()),
					this.getTop() + (int) (temp * this.getHeight()),
					this.getRight() - (int) (temp * this.getWidth()),
					this.getBottom() - (int) (temp * this.getHeight()));
		}
	}

	/**
	 * 实现处理拖动
	 */
	private void setPosition(int left, int top, int right, int bottom) {
		this.layout(left, top, right, bottom);
	}

	public void setable(boolean b)
	{
		this.able=b;
	}

	public void setxzFlag(boolean f){
		this.xzFlag = f;
	}
	// 计算两个手指之间的距离  
    private float getDistance(MotionEvent event)  
    {  
        float x = event.getX(0) - event.getX(1);  
        float y = event.getY(0) - event.getY(1);  
        return FloatMath.sqrt(x * x + y * y);  
    }  
  
    // 计算两个手指所形成的直线和x轴的角度  
    private float getDegree(MotionEvent event)  
    {  
        return (float)(Math.atan((event.getY(1) - event.getY(0)) / (event.getX(1) - event.getX(0))) * 180f);  
    }  

    // 计算两个手指之间，中间点的坐标  
    private PointF midPoint( MotionEvent event)  
    {  
        PointF point = new PointF();  
        float x = event.getX(0) + event.getX(1);  
        float y = event.getY(0) + event.getY(1);  
        point.set(x / 2, y / 2);  
      
        return point;  
    }  
}
