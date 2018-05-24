package picker.prim.com.primpicker_core.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import picker.prim.com.primpicker_core.R;
import picker.prim.com.primpicker_core.cursors.FileLoaderCallback;
import picker.prim.com.primpicker_core.cursors.FileLoaderHelper;
import picker.prim.com.primpicker_core.entity.Directory;
import picker.prim.com.primpicker_core.entity.SelectSpec;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/24 0024
 * 描    述：这里参考了部分知乎的开源项目代码
 * 修订历史：
 * ================================================
 */
public class PrimPickerActivity extends AppCompatActivity implements FileLoaderCallback.LoaderCallback, View.OnClickListener {

    private ImageView iv_picker_back;

    private TextView tv_picker_type;

    private TextView btn_next;

    private CheckBox cb_compress;

    private static final String TAG = "PrimPickerActivity";

    private FrameLayout container, layout_empty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        iv_picker_back = (ImageView) findViewById(R.id.iv_picker_back);
        tv_picker_type = (TextView) findViewById(R.id.tv_picker_type);
        btn_next = (TextView) findViewById(R.id.btn_next);
        cb_compress = (CheckBox) findViewById(R.id.cb_compress);
        container = (FrameLayout) findViewById(R.id.container);
        layout_empty = (FrameLayout) findViewById(R.id.layout_empty);
        FileLoaderHelper.getInstance().onCreate(this, this);
        FileLoaderHelper.getInstance().onRestoreInstanceState(savedInstanceState);
        FileLoaderHelper.getInstance().getLoadDirs();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FileLoaderHelper.getInstance().onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileLoaderHelper.getInstance().onDestory();
    }

    @Override
    public void loadFinish(final Cursor data) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                data.moveToPosition(0);
                Directory directory = Directory.valueOf(data);
                if (directory.isAll() && SelectSpec.getInstance().capture) {
                    directory.addCaptureCount();
                }
                Log.e(TAG, "run: " + directory.toString());

                setData(directory);

            }
        });
    }

    private void setData(Directory directory) {
        if (directory.isAll() && directory.isEmpty()) {
            container.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
        } else {
            container.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
            PrimSelectFragment fragment = PrimSelectFragment.newInstance(directory);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, PrimSelectFragment.class.getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void loadReset() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_picker_back) {
            finish();
        } else if (i == R.id.btn_next) {

        }
    }
}