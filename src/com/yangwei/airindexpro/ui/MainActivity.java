package com.yangwei.airindexpro.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.scrshot.adapter.UMAppAdapter;
import com.umeng.scrshot.adapter.UMBaseAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMWXHandler;
import com.umeng.socialize.sensor.UMSensor.OnSensorListener;
import com.umeng.socialize.sensor.UMSensor.WhitchButton;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.yangwei.airindexpro.R;
import com.yangwei.airindexpro.quadtree.QuadTree;
import com.yangwei.airindexpro.ui.PreferenceListFragment.OnPreferenceAttachedListener;
import com.yangwei.airindexpro.util.Constant;

public class MainActivity extends FragmentActivity implements OnPreferenceAttachedListener{

	private FragmentTabPager mFragmentTabPager;
	private SlidingMenu menu;
	public static final String TAG = "MainActivity";
	private List<String> validCity;
	private SharedPreferences preferences;
	private PreferenceListFragment sidebar_menu;
	private QuadTree tree;
	private UMSocialService mController;
	private UMShakeService mShakeController;
	private UMAppAdapter appAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferences
				.edit()
				.putStringSet(
						Constant.VALID_CITY_KEY,
						new HashSet<String>(Arrays
								.asList(Constant.valid_city_array))).commit();
		
		setValidCity(Arrays.asList(Constant.valid_city_array));
		
		configUmengTools();
		
		setupView();
	}

	protected void configUmengTools() {
		//Umeng app analytics
		MobclickAgent.updateOnlineConfig(this);
		// 首先在您的Activity中添加如下成员变量
		mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
		// 设置分享内容
		mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		
		mShakeController = UMShakeServiceFactory
                .getShakeService("com.umeng.share");
		
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wx4c66367849a76018";
		// 微信图文分享必须设置一个url 
		String contentUrl = "http://www.umeng.com/social";
		// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
		UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(this,appID, contentUrl);
		wxHandler.setWXTitle("空气质量专业版提醒您");
		// 支持微信朋友圈
		UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform(this,appID, contentUrl) ;
		circleHandler.setCircleTitle("空气质量专业版提醒您");
	}

	private void setupView() {
		ViewPager vp = (ViewPager) findViewById(R.id.pager);
		mFragmentTabPager = new FragmentTabPager(this, vp);
		
		//setup actionbar
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mFragmentTabPager.addTab(actionBar.newTab(), AirQuaIndexFragmentForLocation.class, new Bundle());
		Set<String> user_cities = preferences.getStringSet(Constant.USER_CITIES_KEY, null);
		if (user_cities != null && user_cities.size() != 0) {
			Iterator<String> iterator = user_cities.iterator();
			while (iterator.hasNext()) {
				String city = (String) iterator.next();
				Bundle b =  new Bundle();
				b.putString("city", city);
				mFragmentTabPager.addTab(actionBar.newTab(), AirQuaIndexFragmentForFixed.class, b);
			}
		}
		// configure the SlidingMenu
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindScrollScale(0);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeEnabled(false);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.menu_frame);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		sidebar_menu = PreferenceListFragment.newInstance(R.xml.sidebar_menu);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, sidebar_menu).commit();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		
		configShakeShare();
	}

	protected void configShakeShare() {
		appAdapter = new UMAppAdapter(this);    
	    // 配置摇一摇截屏分享时用户可选的平台，最多支持五个平台     
	    List<SHARE_MEDIA> platforms = new ArrayList<SHARE_MEDIA>();       
	    platforms.add(SHARE_MEDIA.WEIXIN);        
	    platforms.add(SHARE_MEDIA.WEIXIN_CIRCLE);  
	         // 设置摇一摇分享的文字内容      
	    mShakeController.setShareContent("空气质量专业版提醒您");      
	    // 注册摇一摇截屏分享功能,mSensorListener在2.1.2中定义         
	    mShakeController.registerShakeListender(this, appAdapter,    
	                                        platforms, mSensorListener);  
	    
	    mShakeController.registerShakeToOpenShare(this, true);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		
		//unregister shake listener when app pause
		mShakeController.unregisterShakeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_share) {
			// 选择平台
			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
			//mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
			// 设置分享平台选择面板的平台显示顺序
			//mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,	
					//SHARE_MEDIA.QZONE,SHARE_MEDIA.QQ, SHARE_MEDIA.SINA);
			// 是否只有已登录用户才能打开分享选择页
			mController.openShare(this, false);
		}
		return super.onOptionsItemSelected(item);
	}
	
	public List<String> getValidCity() {
		return validCity;
	}

	public void setValidCity(List<String> validCity) {
		this.validCity = validCity;
	}
	
	public QuadTree getQuadTree() {
		if (tree == null) {
			tree = new QuadTree(20, 50, 80, 150);
			for (int i = 0; i < Constant.valid_city_cor_array.length; i++) {
				tree.set(Constant.valid_city_cor_array[i].getX(), Constant.valid_city_cor_array[i].getY(), Constant.valid_city_cor_array[i].getValue());
			}
		}
		return tree; 
	}

	@Override
	public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
		MultiSelectListPreference mslistpreference = (MultiSelectListPreference) root.findPreference("user_cities");
		Set<String> user_cities = preferences.getStringSet(Constant.USER_CITIES_KEY, null);
		if (user_cities != null && user_cities.size() != 0) {
			mslistpreference.setSummary(user_cities.toString());
		}
		
		mslistpreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Set<String> newValueSet = (Set<String>)newValue;
				if (newValueSet.size() == 0) {
					preference.setSummary(R.string.no_city_select);
					int tabCount = getActionBar().getTabCount();
					if (tabCount > 1) {
						for (int i = tabCount - 1; i > 0; i--) {
							mFragmentTabPager.removeTab(i);
						}
						mFragmentTabPager.setUpdateNeeded(true);
						mFragmentTabPager.notifyDataSetChanged();
					}
				} else {
					preference.setSummary(newValueSet.toString());
					int tabCount = getActionBar().getTabCount();
					if (tabCount > 1) {
						for (int i = tabCount - 1; i > 0; i--) {
							mFragmentTabPager.removeTab(i);
						}
					}
					Iterator<String> iterator = newValueSet.iterator();
					while (iterator.hasNext()) {
						String city = (String) iterator.next();
						Bundle b =  new Bundle();
						b.putString("city", city);
						mFragmentTabPager.addTab(getActionBar().newTab(), AirQuaIndexFragmentForFixed.class, b);
					}
					mFragmentTabPager.setUpdateNeeded(true);
					mFragmentTabPager.notifyDataSetChanged();
				}
				return true;
			}
		});
		
		//Feedback button
		PreferenceScreen feedback = (PreferenceScreen) root.findPreference("feedback");
		feedback.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
			    agent.startFeedbackActivity();
				return true;
			}
		});
	}
	
	/**
	* 传感器监听器，在下面的集成中使用
	*/
	private OnSensorListener mSensorListener = new OnSensorListener() {

	    @Override
	    public void onStart() {}

	    /**
	    * 分享完成后回调 
	    */
	    @Override
	    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
	        Toast.makeText(MainActivity.this, "分享完成", Toast.LENGTH_SHORT).show();
	    }

	    /**
	    * @Description: 摇一摇动作完成后回调 
	    */
	    @Override
	    public void onActionComplete(SensorEvent event) {
	        Toast.makeText(MainActivity.this, "用户摇一摇，可在这暂停游戏", Toast.LENGTH_SHORT).show();
	    }

	    /**
	    * @Description: 用户点击分享窗口的取消和分享按钮触发的回调
	    * @param button 用户在分享窗口点击的按钮，有取消和分享两个按钮
	    */
	    @Override
	    public void onButtonClick(WhitchButton button) {
	        if (button == WhitchButton.BUTTON_CANCEL) {
	        Toast.makeText(MainActivity.this, "取消分享,游戏重新开始", Toast.LENGTH_SHORT).show();
	        } else {
	        // 分享中, ( 用户点击了分享按钮 )
	            }
	    }
	};

}
