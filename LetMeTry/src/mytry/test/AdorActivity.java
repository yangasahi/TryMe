package mytry.test;

import mytry.image.util.EditImage;
import mytry.image.util.ImageFrameAdder;
import mytry.image.util.ImageSpecific;
import mytry.image.util.ReverseAnimation;
import mytry.image.veiw.CropImageView;
import mytry.image.veiw.ToneView;
import mytry.image.veiw.menu.MenuView;
import mytry.image.veiw.menu.OnMenuClickListener;
import mytry.image.veiw.menu.SecondaryListMenuView;
import mytry.image.veiw.menu.ToneMenuView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class AdorActivity extends Activity implements OnSeekBarChangeListener
{
	public boolean mWaitingToPick; // Whether we are wait the user to pick a face.
    public boolean mSaving; // Whether the "save" button is already clicked.
    
    private Handler mHandler = null;
    private ProgressDialog mProgress;
    
	private Bitmap mBitmap;
	
	/**
	 * ��ʱ����
	 */
	private Bitmap mTmpBmp;
	
	private CropImageView mImageView;
	private EditImage mEditImage;
	private ImageFrameAdder mImageFrame;
	private ImageSpecific mImageSpecific;
	
	/**
	 * һ���˵�
	 */
	private MenuView mMenuView;
	
	private final int[] EDIT_IMAGES = new int[] { R.drawable.ic_menu_crop, R.drawable.ic_menu_rotate_left, R.drawable.ic_menu_mapmode, R.drawable.btn_rotate_horizontalrotate };//�༭һ���˵�ͼƬ
	private final int[] EDIT_TEXTS = new int[] { R.string.crop, R.string.rotate, R.string.resize, R.string.reverse_transform };//�༭һ���˵�����
	
	private final int[] FRAME_IMAGE = new int[] { R.drawable.btn_mainmenu_frame_normal, R.drawable.btn_mainmenu_color_normal, R.drawable.old_remember };//�߿�һ���˵�ͼƬ
	private final int[] FRAME_TEXTS = new int[] { R.string.frame, R.string.doodle, R.string.specific };//�߿�һ���˵�����
	
	/**
	 * �����˵�
	 */
	private SecondaryListMenuView mSecondaryListMenu;
	private final int[] ROTATE_IMGRES = new int[] { R.drawable.ic_menu_rotate_left, R.drawable.ic_menu_rotate_right };//��ת�����˵�
	private final int[] ROTATE_TEXTS = new int[] { R.string.rotate_left, R.string.rotate_right };
	
	private final int[] RESIZE_TEXTS = new int[] { R.string.resize_one_to_two, R.string.resize_one_to_three, R.string.resize_one_to_four };
	
	private final int[] FRAME_ADD_IMAGES = new int[] { R.drawable.frame_around1, R.drawable.frame_around2, R.drawable.frame_small1 };//�߿�����˵�
	
	private final int[] FRAME_DOODLE = new int[] { R.drawable.cloudy, R.drawable.qipao1, R.drawable.qipao2 };//Ϳѻ�����˵�
	
	private final int[] EDIT_REVERSE = new int[] { R.drawable.btn_rotate_horizontalrotate, R.drawable.btn_rotate_verticalrotate };//��ת�����˵�
	
	/**
	 * ��ɫ�˵�
	 */
	private ToneMenuView mToneMenu;
	private ToneView mToneView;
	
	
	/** ��ɫ */
	private final int FLAG_TONE = 0x1;
	/** �߿� */
	private final int FLAG_FRAME = FLAG_TONE + 1;
	/** ��ӱ߿� */
	private final int FLAG_FRAME_ADD = FLAG_TONE + 6;
	/** Ϳѻ */
	private final int FLAG_FRAME_DOODLE = FLAG_TONE + 7;
	/** ��Ч */
	private final int FLAG_FRAME_SPECIFIC = FLAG_TONE + 10;
	/** �༭ */
	private final int FLAG_EDIT = FLAG_TONE + 2;
	/** �ü� */
	private final int FLAG_EDIT_CROP = FLAG_TONE + 3;
	/** ��ת */
	private final int FLAG_EDIT_ROTATE = FLAG_TONE + 4;
	/** ���� */
	private final int FLAG_EDIT_RESIZE = FLAG_TONE + 5;
	/** ��ת */
	private final int FLAG_EDIT_REVERSE = FLAG_TONE + 8;
	

	private View mSaveStep;
	
	private final int STATE_CROP = 0x1;
	private final int STATE_DOODLE = STATE_CROP <<1;
	private final int STATE_NONE = STATE_CROP <<2;
	private final int STATE_TONE = STATE_CROP <<3;
	private final int STATE_REVERSE = STATE_CROP <<4;
	private int mState;
	
	/**
	 * ��ת����
	 */
	private ReverseAnimation mReverseAnim;
	private int mImageViewWidth;
	private int mImageViewHeight;
	
	private ProgressDialog mProgressDialog;
	
	private TextView mShowHandleName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mHandler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				closeProgress();
				reset();
			}
		};
		
		// ȫ����ʾ
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_main);
		
//		mSaveAll = findViewById(R.id.save_all);
		mSaveStep = findViewById(R.id.save_step);
		mShowHandleName = (TextView) findViewById(R.id.handle_name);
		
		Intent intent = getIntent();
		String path = intent.getStringExtra("filepath");
		Log.d("may", "MainActivity--->path="+path);
		if (null == path)
		{
			Toast.makeText(this, R.string.load_failure, Toast.LENGTH_SHORT).show();
			finish();
		}
		
		mBitmap = BitmapFactory.decodeFile(path);
//		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mm);
		mTmpBmp = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
		mImageView = (CropImageView) findViewById(R.id.crop_image);
		mImageView.setImageBitmap(mBitmap);
        mImageView.setImageBitmapResetBase(mBitmap, true);
        
        mEditImage = new EditImage(this, mImageView, mBitmap);
        mImageFrame = new ImageFrameAdder(this, mImageView, mBitmap);
        mImageView.setEditImage(mEditImage);
        mImageSpecific = new ImageSpecific(this);
        
	}
	
	// -----------------------------------�˵��¼�------------------------------------
	
	public void onClick(View v)
	{
		int flag = -1;
		switch (v.getId())
		{
		case R.id.save:
//			onSaveClicked();
			String path = saveBitmap(mBitmap);//����ͼƬ
			if (mProgressDialog != null)
			{
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			Intent data = new Intent();
			data.putExtra("fileadorn", path);
			//setResult(RESULT_OK, data);
			//finish();
			data.setClass(AdorActivity.this, PreviewItemActivity.class);
			startActivity(data);
			return;
		case R.id.cancel:
			setResult(RESULT_CANCELED);
            finish();
			return;
		case R.id.save_step:
			if (mState == STATE_CROP)
			{
				mTmpBmp = mEditImage.cropAndSave(mTmpBmp);
			}
			else if (mState == STATE_DOODLE)
			{
				mTmpBmp = mImageFrame.combinate(mTmpBmp);
			}
			else if(mState == STATE_TONE)
			{
				// TODO �ڲ˵���ʧʱҪ���ɫ״̬ΪNONE״̬
				mTmpBmp = mToneView.getBitmap();
			}
			else if(mState == STATE_REVERSE)
			{
				// ��ת��ɣ�Ҫ��ImageView��ת��������ͼƬ
				mReverseAnim.cancel();
				mReverseAnim = null;
			}
			
			mBitmap = mTmpBmp;
			showSaveAll();
			reset();
			
			mEditImage.mSaving = true;
			mImageViewWidth = mImageView.getWidth();
			mImageViewHeight = mImageView.getHeight();
			return;
		case R.id.cancel_step:
			if (mState == STATE_CROP)
			{
				mEditImage.cropCancel();
			}
			else if (mState == STATE_DOODLE)
			{
				mImageFrame.cancelCombinate();
			}
			else if (mState == STATE_REVERSE)
			{
				mReverseAnim.cancel();
			}
			showSaveAll();
			resetToOriginal();
			return;
		case R.id.edit:
			flag = FLAG_EDIT;
			break;
		case R.id.tone:
			initTone();
			showSaveStep();
			return;
		case R.id.frame:
			flag = FLAG_FRAME;
			break;
		}
		
		initMenu(flag);
	}
	
	private void initTone()
	{
		if (null == mToneMenu)
		{
			mToneMenu = new ToneMenuView(this);
		}
		
		mToneMenu.show();
		
		mState = STATE_TONE;
		
		mToneView = mToneMenu.getToneView();
		mToneMenu.setHueBarListener(this);
		mToneMenu.setLumBarListener(this);
		mToneMenu.setSaturationBarListener(this);
	}
	
	private void initMenu(int flag)
    {
    	if (null == mMenuView)
    	{
    		mMenuView = new MenuView(this);
			mMenuView.setBackgroundResource(R.drawable.popup);
			mMenuView.setTextSize(16);
			
			switch (flag)
			{
			case FLAG_EDIT:
				mMenuView.setImageRes(EDIT_IMAGES);
				mMenuView.setText(EDIT_TEXTS);
				mMenuView.setOnMenuClickListener(editListener());
				break;
			case FLAG_TONE:
				break;
			case FLAG_FRAME:
				mMenuView.setImageRes(FRAME_IMAGE);
				mMenuView.setText(FRAME_TEXTS);
				mMenuView.setOnMenuClickListener(frameListener());
				break;
			}
		}

		mMenuView.show();
    }
	
	/**
	 * �༭������
	 * @return
	 */
	private OnMenuClickListener editListener()
	{
		return new OnMenuClickListener()
		{
			@Override
			public void onMenuItemClick(AdapterView<?> parent, View view,
					int position)
			{
				int[] location = new int[2];
				view.getLocationInWindow(location);
				int left = location[0];
				int flag = -1;
				switch (position)
				{
				case 0: // �ü�
					mMenuView.hide();
					crop();
					showSaveStep();
					return;
				case 1: // ��ת
					flag = FLAG_EDIT_ROTATE;
					break;
				case 2:// ����
					flag = FLAG_EDIT_RESIZE;
					break;
				case 3: // ��ת
					flag = FLAG_EDIT_REVERSE;
					break;
				}
				
				initSecondaryMenu(flag ,left);
			}

			@Override
			public void hideMenu()
			{
				dimissMenu();
			}
			
		};
	}
	
	private OnMenuClickListener frameListener()
	{
		return new OnMenuClickListener()
		{
			@Override
			public void onMenuItemClick(AdapterView<?> parent, View view,
					int position)
			{
				int[] location = new int[2];
				view.getLocationInWindow(location);
				int left = location[0];
				int flag = -1;
				switch (position)
				{
				case 0: // �ӱ߿�
					flag = FLAG_FRAME_ADD;
					break;
				case 1: // Ϳѻ
					flag = FLAG_FRAME_DOODLE;
					break;
				case 2: // ��Ч
					flag = FLAG_FRAME_SPECIFIC;
					break;
				}
				
				initSecondaryMenu(flag ,left);
			}

			@Override
			public void hideMenu()
			{
				dimissMenu();
			}
			
		};
	}
	
	/**
	 * �˵���ʧ����
	 */
	private void dimissMenu()
	{
		mMenuView.dismiss();
		mMenuView = null;
	}
	
	/**
	 * ��ʼ�������˵�
	 * @param flag
	 * @param left
	 */
	private void initSecondaryMenu(int flag, int left)
	{
		mSecondaryListMenu = new SecondaryListMenuView(this);
		mSecondaryListMenu.setBackgroundResource(R.drawable.popup_bottom_tip);
		mSecondaryListMenu.setTextSize(16);
		mSecondaryListMenu.setWidth(300);
		mSecondaryListMenu.setMargin(left);
		switch (flag)
		{
		case FLAG_EDIT_ROTATE: // ��ת
			mSecondaryListMenu.setImageRes(ROTATE_IMGRES);
			mSecondaryListMenu.setText(ROTATE_TEXTS);
			mSecondaryListMenu.setOnMenuClickListener(rotateListener());
			break;
		case FLAG_EDIT_RESIZE: // ����
			mSecondaryListMenu.setText(RESIZE_TEXTS);
			mSecondaryListMenu.setOnMenuClickListener(resizeListener());
			break;
		case FLAG_EDIT_REVERSE: // ��ת
			mSecondaryListMenu.setImageRes(EDIT_REVERSE);
			mSecondaryListMenu.setOnMenuClickListener(reverseListener());
			break;
		case FLAG_FRAME_ADD: // ��ӱ߿�
			mSecondaryListMenu.setWidth(480);
			mSecondaryListMenu.setImageRes(FRAME_ADD_IMAGES);
			mSecondaryListMenu.setOnMenuClickListener(addFrameListener());
			break;
		case FLAG_FRAME_DOODLE: // Ϳѻ
			mSecondaryListMenu.setImageRes(FRAME_DOODLE);
			mSecondaryListMenu.setOnMenuClickListener(doodleListener());
			break;
		case FLAG_FRAME_SPECIFIC: // ��Ч
			mSecondaryListMenu.setWidth(400);
			mSecondaryListMenu.setText(getResources().getStringArray(R.array.specific_item));
			mSecondaryListMenu.setOnMenuClickListener(specificListener());
			break;
		}

		mSecondaryListMenu.show();
	}
	
	/**
	 * �����˵��е���ת�¼�
	 * @return
	 */
	private OnMenuClickListener rotateListener()
	{
		return new OnMenuClickListener()
		{
			@Override
			public void onMenuItemClick(AdapterView<?> parent, View view,
					int position)
			{
				switch (position)
				{
				case 0: // ����ת
					rotate(-90);
					break;
				case 1: // ����ת
					rotate(90);
					break;
				}
				
				// һ���˵�����
				mMenuView.hide();
				showSaveStep();
			}

			@Override
			public void hideMenu()
			{
				dismissSecondaryMenu();
			}
			
		};
	}
	
	/**
	 * �����˵��е������¼�
	 * @return
	 */
	private OnMenuClickListener resizeListener()
	{
		return new OnMenuClickListener()
		{
			@Override
			public void onMenuItemClick(AdapterView<?> parent, View view,
					int position)
			{
				float scale = 1.0F;
				switch (position)
				{
				case 0: // 1:2
					scale /= 2;
					break;
				case 1: // 1:3
					scale /= 3;
					break;
				case 2: // 1:4
					scale /= 4;
					break;
				}
				
				resize(scale);
				// һ���˵�����
				mMenuView.hide();
				showSaveStep();
			}
			
			@Override
			public void hideMenu()
			{
				dismissSecondaryMenu();
			}
			
		};
	}
	
	private OnMenuClickListener reverseListener()
	{
		return new OnMenuClickListener()
		{
			@Override
			public void onMenuItemClick(AdapterView<?> parent, View view,
					int position)
			{
				int flag = -1;
				switch (position)
				{
				case 0: // ˮƽ��ת
					flag = 0;
					break;
				case 1: // ��ֱ��ת
					flag = 1;
					break;
				}
				
				reverse(flag);
				// һ���˵�����
				mMenuView.hide();
				showSaveStep();
			}

			@Override
			public void hideMenu()
			{
				dismissSecondaryMenu();
			}
			
		};
	}
	
	/**
	 * �����˵��е���ӱ߿��¼�
	 * @return
	 */
	private OnMenuClickListener addFrameListener()
	{
		return new OnMenuClickListener()
		{
			@Override
			public void onMenuItemClick(AdapterView<?> parent, View view,
					int position)
			{
				int flag = -1;
				int res = 0;
				switch (position)
				{
				case 0: // �߿�1
					flag = ImageFrameAdder.FRAME_SMALL;
					res = 0;
					break;
				case 1: // �߿�2
					flag = ImageFrameAdder.FRAME_SMALL;
					res = 1;
					break;
				case 2: // �߿�3
					flag = ImageFrameAdder.FRAME_BIG;
					res = R.drawable.frame_big1;
					break;
				case 3: // �߿�4
					flag = ImageFrameAdder.FRAME_BIG;
					res = 2;
					break;
				}
				
				addFrame(flag, res);
//				mImageView.center(true, true);
				
				// һ���˵�����
				mMenuView.hide();
				showSaveStep();
			}
			
			@Override
			public void hideMenu()
			{
				dismissSecondaryMenu();
			}
			
		};
	}
	
	/**
	 * �����˵��е�Ϳѻ�¼�
	 * @return
	 */
	private OnMenuClickListener doodleListener()
	{
		return new OnMenuClickListener()
		{
			@Override
			public void onMenuItemClick(AdapterView<?> parent, View view,
					int position)
			{
				// Ϳѻ״̬
				mImageView.setState(CropImageView.STATE_DOODLE);
				switch (position)
				{
				case 0: // Ϳѻ1
					doodle(R.drawable.cloudy);
					break;
				case 1: // Ϳѻ2
					doodle(R.drawable.qipao1);
					break;
				case 2: // Ϳѻ3
					doodle(R.drawable.qipao2);
					break;
				}
				
				// һ���˵�����
				mMenuView.hide();
				showSaveStep();
			}
			
			@Override
			public void hideMenu()
			{
				dismissSecondaryMenu();
			}
			
		};
	}
	
	/**
	 * ��Ч�����ʶ
	 */
	private int specFlag = -1;
	
	/**
	 * �����˵��е���Ч�¼�
	 * @return
	 */
	private OnMenuClickListener specificListener()
	{
		return new OnMenuClickListener()
		{
			@Override
			public void onMenuItemClick(AdapterView<?> parent, View view,
					int position)
			{
				switch (position)
				{
				case 0: // ����Ч��
					specFlag = ImageSpecific.FLAG_OLD_REMEMBER;
					break;
				case 1: // ģ��Ч��
					specFlag = ImageSpecific.FLAG_BLUR;
					break;
				case 2: // ��Ч��
					specFlag = ImageSpecific.FLAG_SHARPEN;
					break;
				case 3: // ������
					specFlag = ImageSpecific.FLAG_OVERLAY;
					break;
				case 4: // ��Ƭ
					specFlag = ImageSpecific.FLAG_FILM;
					break;
				case 5: // ����
					specFlag = ImageSpecific.FLAG_EMBOSS;
					break;
				case 6: // ����Ч��
					specFlag = ImageSpecific.FLAG_SUNSHINE;
					break;
				case 7: // �޺�
					specFlag = ImageSpecific.FLAG_NEON;
					break;
				case 8: // ����2
					specFlag = ImageSpecific.FLAG_ALPHA_LAYER;
					break;
				}
				
				imageSpecific(specFlag);
				// һ���˵�����
				mMenuView.hide();
				showSaveStep();
			}

			@Override
			public void hideMenu()
			{
				dismissSecondaryMenu();
			}
			
		};
	}
	
	/**
	 * ���ض����˵�
	 */
	private void dismissSecondaryMenu()
	{
		mSecondaryListMenu.dismiss();
		mSecondaryListMenu = null;
	}
	
	private void showSaveStep()
	{
		mSaveStep.setVisibility(View.VISIBLE);
//		mSaveAll.setVisibility(View.GONE);
	}
	
	private void showSaveAll()
	{
		mSaveStep.setVisibility(View.GONE);
//		mSaveAll.setVisibility(View.VISIBLE);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_BACK:
			if (mMenuView != null && mMenuView.isShow() || null != mToneMenu && mToneMenu.isShow())
			{
				mMenuView.hide();
				mToneMenu.hide();
				mToneMenu = null;
			} else
			{
//				if (mSaveAll.getVisibility() == View.GONE)
//				{
//					showSaveAll();
//				}
//				else
//				{
//					finish();
//				}
			}
			break;
		case KeyEvent.KEYCODE_MENU:
			break;
		
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	
	// ----------------------------------------------------����----------------------------------------------------

	/**
	 * ���в���ǰ��׼��
	 * @param state ��ǰ׼������Ĳ���״̬
	 * @param imageViewState ImageViewҪ�����״̬
	 * @param hideHighlight �Ƿ����زü���
	 */
	private void prepare(int state, int imageViewState, boolean hideHighlight)
	{
		resetToOriginal();
		mEditImage.mSaving = false;
		if (null != mReverseAnim)
		{
			mReverseAnim.cancel();
			mReverseAnim = null;
		}
		
		if (hideHighlight)
		{
			mImageView.hideHighlightView();
		}
		mState = state;
		mImageView.setState(imageViewState);
		mImageView.invalidate();
	}
	
	/**
	 * �ü�
	 */
	private void crop()
	{
		// ����ü�״̬
		prepare(STATE_CROP, CropImageView.STATE_HIGHLIGHT, false);
		mShowHandleName.setText(R.string.crop);
		mEditImage.crop(mTmpBmp);
		reset();
	}
	
	/**
	 * ��ת
	 * @param degree
	 */
	private void rotate(float degree)
	{
		// δ��������״̬
		mImageViewWidth = mImageView.getWidth();
		mImageViewHeight = mImageView.getHeight();
		
		prepare(STATE_NONE, CropImageView.STATE_NONE, true);
		mShowHandleName.setText(R.string.rotate);
		Bitmap bm = mEditImage.rotate(mTmpBmp, degree);
		mTmpBmp = bm;
		reset();
	}
	
	private void reverse(int flag)
	{
		// δ��������״̬
		prepare(STATE_REVERSE, CropImageView.STATE_NONE, true);
		mShowHandleName.setText(R.string.reverse_transform);
		int type = 0;
		switch (flag)
		{
		case 0:
			type = ReverseAnimation.HORIZONTAL;
			break;
		case 1:
			type = ReverseAnimation.VERTICAL;
			break;
		}
		
		mReverseAnim = new ReverseAnimation(0F, 180F, mImageViewWidth == 0 ? mImageView.getWidth() / 2 : mImageViewWidth / 2, mImageViewHeight == 0 ? mImageView.getHeight() / 2 : mImageViewHeight / 2, 0, true);
		mReverseAnim.setReverseType(type);
		mReverseAnim.setDuration(1000);
		mReverseAnim.setFillEnabled(true);
		mReverseAnim.setFillAfter(true);
		mImageView.startAnimation(mReverseAnim);
		Bitmap bm = mEditImage.reverse(mTmpBmp, flag);
		mTmpBmp = bm;
//		reset();
	}
	
	/**
	 * ����
	 * @param bm
	 * @param scale
	 */
	private void resize(float scale)
	{
		// δ��������״̬
		prepare(STATE_NONE, CropImageView.STATE_NONE, true);
		mShowHandleName.setText(R.string.resize);
		Bitmap bmp = mEditImage.resize(mTmpBmp, scale);
		mTmpBmp = bmp;
		reset();
	}
	
	/**
	 * ��ӱ߿�
	 */
	private void addFrame(int flag, int res)
	{
		// δ��������״̬
		prepare(STATE_NONE, CropImageView.STATE_NONE, true);
		mShowHandleName.setText(R.string.frame);
		mTmpBmp = mImageFrame.addFrame(flag, mBitmap, res);
		reset();
	}
	
	/**
	 * Ϳѻ
	 */
	private void doodle(int res)
	{
		// ����Ϳѻ״̬
		prepare(STATE_DOODLE, CropImageView.STATE_DOODLE, true);
		mShowHandleName.setText(R.string.frame_doodle);
		mImageFrame.doodle(res);
		reset();
	}
	
	/**
	 * ��Ч����
	 * @param flag
	 */
	private void imageSpecific(final int flag)
	{
		prepare(STATE_NONE, CropImageView.STATE_NONE, true);
		showProgress();
		new Thread(new Runnable()
		{
			public void run()
			{
				mTmpBmp = mImageSpecific.imageSpecific(mTmpBmp, flag);
				mHandler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	/**
	 * ��������һ��ͼƬ
	 */
	private void reset()
	{
		mImageView.setImageBitmap(mTmpBmp);
		mImageView.invalidate();
	}
	
	private void resetToOriginal()
	{
		mTmpBmp = mBitmap;
		mImageView.setImageBitmap(mBitmap);
		// �Ѿ�����ͼƬ
		mEditImage.mSaving = true;
		// ��ղü�����
		mImageView.mHighlightViews.clear();
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser)
	{
		
		int flag = -1;
		switch ((Integer) seekBar.getTag())
		{
		case 1: // ���Ͷ�
			flag = 1;
			mToneView.setSaturation(progress);
			break;
		case 2: // ɫ��
			flag = 0;
			mToneView.setHue(progress);
			break;
		case 3: // ����
			flag = 2;
			mToneView.setLum(progress);
			break;
		}
		
		Bitmap bm = mToneView.handleImage(mTmpBmp, flag);
		mImageView.setImageBitmapResetBase(bm, true);
		mImageView.center(true, true);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
	}
	
	/**
	 * ��ʾ������
	 */
	private void showProgress()
	{
		Context context = this;
		mProgress = ProgressDialog.show(context, null, context.getResources().getString(R.string.handling));
		mProgress.show();
		Log.d("may", "show Progress");
	}
	
	/**
	 * �رս�����
	 */
	private void closeProgress()
	{
		if (null != mProgress)
		{
			mProgress.dismiss();
			mProgress = null;
		}
	}
	
	/**
	 * ����ͼƬ������
	 * @param bm
	 */
	private String saveBitmap(Bitmap bm)
	{
		mProgressDialog = ProgressDialog.show(this, null, getResources().getString(R.string.save_bitmap));
		mProgressDialog.show();
		return mEditImage.saveToLocal(bm);
	}
	
}
