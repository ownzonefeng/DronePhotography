package com.example.zhaodong.dronephotography.activity;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaodong.dronephotography.WearService;
import com.example.zhaodong.dronephotography.drone.BebopDrone;
import com.example.zhaodong.dronephotography.view.H264VideoView;
import com.example.zhaodong.dronephotography.R;
import com.example.zhaodong.dronephotography.view.myARControllerCodec;
import com.example.zhaodong.dronephotography.view.myARFrame;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_STREAM_CODEC_TYPE_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerCodec;
import com.parrot.arsdk.arcontroller.ARFrame;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BebopActivity extends AppCompatActivity {
    private static final String TAG = "BebopActivity";
    private BebopDrone mBebopDrone;

    private ProgressDialog mConnectionProgressDialog;
    private H264VideoView mVideoView;

    private TextView mBatteryLabel;
    private Button mTakeOffLandBt;
    private boolean isRecording = false;
    private NotificationCompat.Builder mNotifyBuilder;
    private NotificationManager mNotifyManager;


    private TextView timer ;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    private Handler handler;
    private int Seconds, Minutes, MilliSeconds ;
    private int mNbMaxDownload;
    private int mCurrentDownloadIndex;
    private RemoteTakePic RemoteTakePic;

    public static final String RECEIVED_SHOT = "RECEIVE_SHOT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebop);

        //Set up timer:
        timer = (TextView)findViewById(R.id.tvTimer);
        timer.setVisibility(View.INVISIBLE);

        handler = new Handler() ;

        initIHM();

        Intent intent = getIntent();
        ARDiscoveryDeviceService service = intent.getParcelableExtra(DeviceListActivity.EXTRA_DEVICE_SERVICE);
        mBebopDrone = new BebopDrone(this, service);
        mBebopDrone.addListener(mBebopListener);
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // show a loading view while the bebop drone is connecting
        if ((mBebopDrone != null) && !(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING.equals(mBebopDrone.getConnectionState())))
        {
            mConnectionProgressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
            mConnectionProgressDialog.setIndeterminate(true);
            mConnectionProgressDialog.setMessage("Connecting ...");
            mConnectionProgressDialog.setCancelable(false);
            mConnectionProgressDialog.show();

            // if the connection to the Bebop fails, finish the activity
            if (!mBebopDrone.connect()) {
                finish();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        RemoteTakePic = new RemoteTakePic();
        LocalBroadcastManager.getInstance(this).registerReceiver(RemoteTakePic, new
                IntentFilter(RECEIVED_SHOT));

        //Make the navigation bar transparent:
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    @Override
    public void onBackPressed() {
        if (mBebopDrone != null)
        {
            mConnectionProgressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
            mConnectionProgressDialog.setIndeterminate(true);
            mConnectionProgressDialog.setMessage("Disconnecting ...");
            mConnectionProgressDialog.setCancelable(false);
            mConnectionProgressDialog.show();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(RemoteTakePic);

            if (!mBebopDrone.disconnect()) {
                finish();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        mBebopDrone.dispose();
        super.onDestroy();
    }


    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            timer.setText("" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds));

            handler.postDelayed(this, 0);
        }

    };


    public class RemoteTakePic extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            double shot = intent.getDoubleExtra("ShotStatus", 1);
            if(shot == 1){
                mBebopDrone.takePicture();
                mBebopDrone.getLatestMedia();
                Toast.makeText(context.getApplicationContext(), R.string.photo_taken, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context.getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initIHM() {
        mVideoView = (H264VideoView) findViewById(R.id.videoView);

        findViewById(R.id.emergencyBt).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBebopDrone.emergency();
            }
        });

        mTakeOffLandBt = (Button) findViewById(R.id.takeOffOrLandBt);
        mTakeOffLandBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (mBebopDrone.getFlyingState()) {
                    case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED:
                        mBebopDrone.takeOff();
                        break;
                    case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING:
                    case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING:
                        mBebopDrone.land();
                        break;
                    default:
                }
            }
        });

        findViewById(R.id.takePictureBt).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBebopDrone.takePicture();
                Toast.makeText(BebopActivity.this, R.string.photo_taken, Toast.LENGTH_SHORT).show();
                mBebopDrone.getLatestMedia();
            }
        });




        findViewById(R.id.recBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ImageButton recbt = findViewById(R.id.recBt);
                if(isRecording){
                    mBebopDrone.stopRecording();
                    isRecording = false;
                    mBebopDrone.getLatestMedia();

                    MillisecondTime = 0L ;
                    StartTime = 0L ;
                    TimeBuff = 0L ;
                    UpdateTime = 0L ;
                    Seconds = 0 ;
                    Minutes = 0 ;
                    MilliSeconds = 0 ;

                    timer.setVisibility(View.INVISIBLE);
                    timer.setText("00:00");

                    recbt.setImageResource(R.mipmap.record);
                }
                else{
                    mBebopDrone.recordVideo();
                    isRecording = true;

                    timer.setVisibility(View.VISIBLE);
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);


                    recbt.setImageResource(R.mipmap.stoprecord);
                }
            }
        });





        //Go up:
        findViewById(R.id.gazUpBt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        mBebopDrone.setGaz((byte) 50);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        mBebopDrone.setGaz((byte) 0);
                        break;

                    default:

                        break;
                }

                return true;
            }
        });

        findViewById(R.id.gazDownBt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        mBebopDrone.setGaz((byte) -50);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        mBebopDrone.setGaz((byte) 0);
                        break;

                    default:

                        break;
                }

                return true;
            }
        });

        //Turn left:
        findViewById(R.id.yawLeftBt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        mBebopDrone.setYaw((byte) -100);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        mBebopDrone.setYaw((byte) 0);
                        break;

                    default:

                        break;
                }

                return true;
            }
        });

        //Turn right:
        findViewById(R.id.yawRightBt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        mBebopDrone.setYaw((byte) 100);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        mBebopDrone.setYaw((byte) 0);
                        break;

                    default:

                        break;
                }

                return true;
            }
        });

        findViewById(R.id.forwardBt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        mBebopDrone.setPitch((byte) 50);
                        mBebopDrone.setFlag((byte) 1);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        mBebopDrone.setPitch((byte) 0);
                        mBebopDrone.setFlag((byte) 0);
                        break;

                    default:

                        break;
                }

                return true;
            }
        });

        findViewById(R.id.backBt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        mBebopDrone.setPitch((byte) -50);
                        mBebopDrone.setFlag((byte) 1);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        mBebopDrone.setPitch((byte) 0);
                        mBebopDrone.setFlag((byte) 0);
                        break;

                    default:

                        break;
                }

                return true;
            }
        });

        findViewById(R.id.rollLeftBt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        mBebopDrone.setRoll((byte) -50);
                        mBebopDrone.setFlag((byte) 1);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        mBebopDrone.setRoll((byte) 0);
                        mBebopDrone.setFlag((byte) 0);
                        break;

                    default:

                        break;
                }

                return true;
            }
        });

        findViewById(R.id.rollRightBt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        mBebopDrone.setRoll((byte) 50);
                        mBebopDrone.setFlag((byte) 1);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        mBebopDrone.setRoll((byte) 0);
                        mBebopDrone.setFlag((byte) 0);
                        break;

                    default:

                        break;
                }

                return true;
            }
        });

        findViewById(R.id.zoominBt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        //Forward:
                        mBebopDrone.setPitch((byte) 60);
                        mBebopDrone.setFlag((byte) 1);
                        //Down:
                        mBebopDrone.setGaz((byte) -40);
                        mBebopDrone.setFlag((byte) 1);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        mBebopDrone.setPitch((byte) 0);
                        mBebopDrone.setFlag((byte) 0);
                        mBebopDrone.setGaz((byte) 0);
                        mBebopDrone.setFlag((byte) 0);
                        break;

                    default:

                        break;
                }

                Toast.makeText(BebopActivity.this, R.string.zoom_in, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        findViewById(R.id.zoomoutBt).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        //Back:
                        mBebopDrone.setPitch((byte) -60);
                        mBebopDrone.setFlag((byte) 1);
                        //Up:
                        mBebopDrone.setGaz((byte) 40);
                        mBebopDrone.setFlag((byte) 1);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        mBebopDrone.setPitch((byte) 0);
                        mBebopDrone.setFlag((byte) 0);
                        mBebopDrone.setGaz((byte) 0);
                        mBebopDrone.setFlag((byte) 0);
                        break;

                    default:

                        break;
                }

                Toast.makeText(BebopActivity.this, R.string.zoom_out, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mBatteryLabel = (TextView) findViewById(R.id.batteryLabel);
    }

    private final BebopDrone.Listener mBebopListener = new BebopDrone.Listener() {
        @Override
        public void onDroneConnectionChanged(ARCONTROLLER_DEVICE_STATE_ENUM state) {
            switch (state)
            {
                case ARCONTROLLER_DEVICE_STATE_RUNNING:
                    mConnectionProgressDialog.dismiss();
                    break;

                case ARCONTROLLER_DEVICE_STATE_STOPPED:
                    // if the deviceController is stopped, go back to the previous activity
                    mConnectionProgressDialog.dismiss();
                    finish();
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onBatteryChargeChanged(int batteryPercentage) {
            mBatteryLabel.setText(String.format("%d%%", batteryPercentage));
            ImageView battery = findViewById(R.id.battery);

            if(batteryPercentage<100 && batteryPercentage>=70){
                battery.setImageResource(R.drawable.eighty_percent);

            }else if(batteryPercentage<70 && batteryPercentage>=40) {
                battery.setImageResource(R.drawable.half);

            }else if (batteryPercentage < 40) {
                battery.setImageResource(R.drawable.low);
                mBatteryLabel.setTextColor(Color.RED);

            }else{
                battery.setImageResource(R.drawable.full);
            }
        }

        @Override
        public void onPilotingStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state) {
            switch (state) {
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED:
                    mTakeOffLandBt.setText("Take off");
                    mTakeOffLandBt.setEnabled(true);
                    break;
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING:
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING:
                    mTakeOffLandBt.setText("Land");
                    mTakeOffLandBt.setEnabled(true);
                    break;
                default:
                    mTakeOffLandBt.setEnabled(false);
            }
        }

        @Override
        public void onPictureTaken(ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error) {
            Log.i(TAG, "Picture has been taken");
        }

        @Override
        public void configureDecoder(ARControllerCodec codec) {
            mVideoView.configureDecoder(codec);
            myARControllerCodec codec_info = new myARControllerCodec(codec);
            Intent intent = new Intent(BebopActivity.this, WearService.class);
            intent.setAction(WearService.ACTION_SEND.SEND_FRAME.name());
            intent.putExtra(WearService.SEND_FRAME, codec_info);
            startService(intent);
        }

        @Override
        public void onFrameReceived(ARFrame frame) {
            mVideoView.displayFrame(frame);
            myARFrame myFrame = new myARFrame(frame);
            Intent intent = new Intent(BebopActivity.this, WearService.class);
            intent.setAction(WearService.ACTION_SEND.SEND_FRAME.name());
            intent.putExtra(WearService.SEND_FRAME, myFrame);
            startService(intent);

        }

        @Override
        public void onMatchingMediasFound(int nbMedias) {
            //mDownloadProgressDialog.dismiss();

            mNbMaxDownload = nbMedias;
            mCurrentDownloadIndex = 1;

//            if (nbMedias > 0) {
//                mDownloadProgressDialog = new ProgressDialog(BebopActivity.this, R.style.AppCompatAlertDialogStyle);
//                mDownloadProgressDialog.setIndeterminate(false);
//                mDownloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                mDownloadProgressDialog.setMessage("Downloading medias");
//                mDownloadProgressDialog.setMax(mNbMaxDownload * 100);
//                mDownloadProgressDialog.setSecondaryProgress(mCurrentDownloadIndex * 100);
//                mDownloadProgressDialog.setProgress(0);
//                mDownloadProgressDialog.setCancelable(false);
//                mDownloadProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mBebopDrone.cancelGetLastFlightMedias();
//                    }
//                });
//                mDownloadProgressDialog.show();
                mNotifyBuilder = new NotificationCompat.Builder(BebopActivity.this, "download");
                mNotifyBuilder.setContentTitle("DOWNLOAD MEDIA")
                        .setContentText("Download in progress")
                        .setSmallIcon(R.mipmap.app_icon)
                        .setProgress(0, 0,true)
                        .setPriority(NotificationCompat.PRIORITY_LOW);

                mNotifyManager.notify(0,mNotifyBuilder.build());
            }

        @Override
        public void onDownloadProgressed(String mediaName, int progress) {
//            mDownloadProgressDialog.setProgress(((mCurrentDownloadIndex - 1) * 100) + progress);
        }

        @Override
        public void onDownloadComplete(String mediaName) {
//            mCurrentDownloadIndex++;
//            mDownloadProgressDialog.setSecondaryProgress(mCurrentDownloadIndex * 100);
//
//            if (mCurrentDownloadIndex > mNbMaxDownload) {
//                mDownloadProgressDialog.dismiss();
//                mDownloadProgressDialog = null;
//            }
            mNotifyBuilder.setProgress(0,0,false)
                    .setContentText("Download complete");
            mNotifyManager.notify(0,mNotifyBuilder.build());
        }
    };
}
