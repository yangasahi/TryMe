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
 * �̳�ImageView ʵ���˶�㴥�����϶�������
 * 
 * @author Administrator
 * 
 */
public class TouchView extends ImageView {
	static final int NONE = 0;
	static final int DRAG = 1; // �϶���
	static final int ZOOM = 2; // ������
	static final int BIGGER = 3; // �Ŵ�ing
	static final int SMALLER = 4; // ��Сing
	static final int ROTA = 5; // ��Сing
	
	private int mode = NONE; // ��ǰ���¼�

	private float beforeLenght; // ���������
	private float afterLenght; // ���������
	private float scale = 0.04f; // ���ŵı��� X Y���������ֵ Խ�����ŵ�Խ��

	private int screenW;
	private int screenH;

	/* �����϶� ���� */
	private int start_x;
	private int start_y;
	private int stop_x;
	private int stop_y;

	private TranslateAnimation trans; // �������߽�Ķ���
	
	private boolean able=true;
	private boolean xzFlag = false;
	
	private Matrix savedMatrix;  
	private PointF startPoint;  
	 private Matrix matrix;  
	 private float oldDistance;  
	    private float oldAngle; 
	    private PointF middlePoint;  

	/**
	 * Ĭ�Ϲ��캯��
	 * 
	 * @param context
	 */
	public TouchView(Context context) {
		super(context);
	
	}

	/**
	 * �ù��췽���ھ�̬����XML�ļ����Ǳ����
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
	 * ���������ľ���
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * ������..
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
             case MotionEvent.ACTION_DOWN:           // ��һ����ָtouch  
                 savedMatrix.set(matrix);  
                 startPoint.set(event.getX(), event.getY());  
                 mode = ROTA;  
                 break;  
             case MotionEvent.ACTION_POINTER_DOWN:   // �ڶ�����ָtouch  
                 oldDistance = getDistance(event);   // ����ڶ�����ָtouchʱ����ָ֮��ľ���  
                 oldAngle = getDegree(event);        // ����ڶ�����ָtouchʱ����ָ���γɵ�ֱ�ߺ�x��ĽǶ�  
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
						/* �����϶� */
						
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
						/* �������� */
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
	 * ʵ�ִ�������
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
	 * ʵ�ִ����϶�
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
	// ����������ָ֮��ľ���  
    private float getDistance(MotionEvent event)  
    {  
        float x = event.getX(0) - event.getX(1);  
        float y = event.getY(0) - event.getY(1);  
        return FloatMath.sqrt(x * x + y * y);  
    }  
  
    // ����������ָ���γɵ�ֱ�ߺ�x��ĽǶ�  
    private float getDegree(MotionEvent event)  
    {  
        return (float)(Math.atan((event.getY(1) - event.getY(0)) / (event.getX(1) - event.getX(0))) * 180f);  
    }  

    // ����������ָ֮�䣬�м�������  
    private PointF midPoint( MotionEvent event)  
    {  
        PointF point = new PointF();  
        float x = event.getX(0) + event.getX(1);  
        float y = event.getY(0) + event.getY(1);  
        point.set(x / 2, y / 2);  
      
        return point;  
    }  
}
