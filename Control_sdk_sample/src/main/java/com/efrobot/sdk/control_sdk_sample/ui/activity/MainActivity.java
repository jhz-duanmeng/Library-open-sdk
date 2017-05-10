package com.efrobot.sdk.control_sdk_sample.ui.activity;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.efrobot.library.OnInitCompleteListener;
import com.efrobot.library.RobotManager;
import com.efrobot.library.RobotStatus;
import com.efrobot.sdk.control_sdk_sample.JHZApplication;
import com.efrobot.sdk.control_sdk_sample.R;
import com.efrobot.sdk.control_sdk_sample.ui.adapter.ItemListViewAdapter;
import com.efrobot.sdk.control_sdk_sample.ui.base.BaseActivity;
import com.efrobot.sdk.control_sdk_sample.ui.base.BaseFragment;
import com.efrobot.sdk.control_sdk_sample.ui.fragment.HeadFragment;
import com.efrobot.sdk.control_sdk_sample.ui.fragment.IDFragment;
import com.efrobot.sdk.control_sdk_sample.ui.fragment.LightBeltFragment;
import com.efrobot.sdk.control_sdk_sample.ui.fragment.MaskStatusFragment;
import com.efrobot.sdk.control_sdk_sample.ui.fragment.ProjectFragment;
import com.efrobot.sdk.control_sdk_sample.ui.fragment.PurifierStatusFragment;
import com.efrobot.sdk.control_sdk_sample.ui.fragment.SalverStatusFragment;
import com.efrobot.sdk.control_sdk_sample.ui.fragment.WheelFragment;
import com.efrobot.sdk.control_sdk_sample.ui.fragment.WingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView mTitleLv;
    private List<String> mTitles;
    public static final String TAG = "MainActivity";

    private IDFragment mIDFragment = new IDFragment();
    private HeadFragment mHeadFragment = new HeadFragment();
    private MaskStatusFragment mMaskStatusFragment = new MaskStatusFragment();
    private SalverStatusFragment mSalverStatusFragment = new SalverStatusFragment();
    private LightBeltFragment mLightBeltFragment = new LightBeltFragment();
    private WingFragment mWingFragment = new WingFragment();
    private WheelFragment mWheelFragment = new WheelFragment();
//    private BatteryFragment mBatteryFragment = new BatteryFragment();
    private PurifierStatusFragment mPurifierStatusFragment = new PurifierStatusFragment();
    private ProjectFragment mProjectFragment = new ProjectFragment();
    private BaseFragment[] mAllFrags = {mIDFragment, mHeadFragment, mMaskStatusFragment,
            mSalverStatusFragment, mLightBeltFragment, mWingFragment, mWheelFragment, mPurifierStatusFragment, mProjectFragment};

    private static AlertDialog mAlertDialog;
    private static AlertDialog.Builder builder;
    private RobotManager mRobotManager;

    public static final int CLOSE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLOSE:
//                    Log.d(TAG, "ROS连接成功");
                    mAlertDialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public int setRootView() {
        return R.layout.activity_main;

    }


    @Override
    public void initViews() {
        setTitleCenter("进化者机器人", this);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        mRobotManager = RobotManager.getInstance(mBaseActivity);
        mTitleLv = (ListView) findViewById(R.id.title_lv);
        mTitles = new ArrayList<>();
        mTitles.add("机器人ID和TTS");
        mTitles.add("机器人头部");
        mTitles.add("机器人面罩");
        mTitles.add("机器人托盘");
        mTitles.add("机器人灯带");
        mTitles.add("机器人双翅");
        mTitles.add("机器人双轮");
        mTitles.add("机器人净化器");
        mTitles.add("机器人投影仪");
        RobotStatus.ProjectStatus projectorStatus = mRobotManager.getProjectorStatus();
        RobotStatus.PurifierStatus purifierStatus = mRobotManager.getPurifierStatus();
        RobotStatus.BatteryStatus batteryStatus = mRobotManager.getBatteryStatus();
        RobotStatus.HeadTouchStatus headTouchStatus = mRobotManager.getHeadTouchStatus();
        RobotStatus.MaskStatus maskStatus = mRobotManager.getMaskStatus();
        RobotStatus.SalverStatus salverStatus = mRobotManager.getSalverStatus();
    }

    @Override
    public void initData() {
        ItemListViewAdapter mItemListViewAdapter = new ItemListViewAdapter(this, mTitles);
        mTitleLv.setAdapter(mItemListViewAdapter);
        showFrag(mIDFragment);
        mTitleLv.setOnItemClickListener(this);
        builder = new AlertDialog.Builder(this);


        RobotManager manager = RobotManager.getInstance(this);

        //要判断是否初始化完成
        if (!JHZApplication.initSuccess) {
            //连接中，需要等待
            Log.d(TAG, "认证中，需要等待");
            //注册完成监听
            builder.setTitle("认证中，请等待！")
                    .setMessage("友情提示，请您正确输入在SDK平台注册的验证信息")
                    .create();
            mAlertDialog = builder.show();
            mAlertDialog.setCanceledOnTouchOutside(false);

            manager.registerOnInitCompleteListener(new OnInitCompleteListener() {
                @Override
                public void onInitComplete(RobotManager.ResultCode resultCode) {
                          //可以正常使用sdk
                    Log.e(TAG, "" + resultCode.ordinal());
                    if (resultCode == RobotManager.ResultCode.INIT_SUCCESS) {
                        mAlertDialog.dismiss();
                }
            }});
//            mHandler.sendEmptyMessageDelayed(1,8000);
        } else {
            //可以正常使用sdk
            builder.show().dismiss();
            JHZApplication.initSuccess = false;
            Toast.makeText(this, "连接成功", Toast.LENGTH_SHORT).show();
        }
    }

    //    @Override
//    protected void onPause() {
//        super.onPause();
//        if(mAlertDialog.isShowing()){
//            mAlertDialog.dismiss();
//        }
//    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.e("TAG", "点到了" + mTitleLv.getItemAtPosition(position));
        switch (position) {
            case 0:
                showFrag(mIDFragment);
                break;
            case 1:
                showFrag(mHeadFragment);
                break;
            case 2:
                showFrag(mMaskStatusFragment);
                break;
            case 3:
                showFrag(mSalverStatusFragment);
                break;
            case 4:
                showFrag(mLightBeltFragment);
                break;
            case 5:
                showFrag(mWingFragment);
                break;
            case 6:
                showFrag(mWheelFragment);
                break;
//            case 7:
//                showFrag(mBatteryFragment);
//                break;
            case 7:
                showFrag(mPurifierStatusFragment);
                break;
            case 8:
                showFrag(mProjectFragment);
                break;
        }
    }

    public void showFrag(BaseFragment desFragment) {
        for (BaseFragment baseFragment : mAllFrags) {
            if (baseFragment == desFragment) {
                if (!desFragment.isAdded()) {
                    addFragment(R.id.layout_frag, desFragment);
                }
                showFragment(desFragment);
            } else {
                hideFragment(baseFragment);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
        System.exit(0);
    }


    @Override
    public void onClick(View v) {

    }
}
