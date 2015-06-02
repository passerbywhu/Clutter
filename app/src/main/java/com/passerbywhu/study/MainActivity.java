package com.passerbywhu.study;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.passerbywhu.study.common.consts.Const;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Nullable;

import de.greenrobot.event.EventBus;


public class MainActivity extends ActionBarActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] dataSet = new String[] {"a", "b", "c", "d", "e", "f", "g",
    "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
    "x", "y", "z"};
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toast toast;

    {
        dataSet = new String[100];
        for (int i = 0; i < 100; i++) {
            dataSet[i] = String.valueOf(i);
        }
    }

    public static class MessageEvent {
        public final String message;

        public MessageEvent(String message) {
            this.message = message;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(MessageEvent event) {
        toast.setText(event.message);
        toast.show();
    }

    private ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
            QualityInfo qualityInfo = imageInfo.getQualityInfo();
            FLog.d("Final image received! " +
            "Size %d x %d",
                    "Quality level %d, good enough: %s, full quality: %s",
                    imageInfo.getWidth(), imageInfo.getHeight(), qualityInfo.getQuality(), qualityInfo.isOfGoodEnoughQuality(), qualityInfo.isOfFullQuality());
        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            FLog.d("tag", "Intermediate image received");
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            FLog.e(getClass(), throwable, "Error loading %s", id);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread() {
            @Override
            public void run() {
                int IO_BUFFER_SIZE = 8 * 1024 * 1024;
                try {
//                    final URL url = new URL(Const.imgUrls[0]);
//                    HttpURLConnection urlConnection = null;
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
                    final DefaultHttpClient client = new DefaultHttpClient();
                    final HttpGet getRequest = new HttpGet(Const.imgUrls[0]);
                    HttpResponse response = client.execute(getRequest);
                    final int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode != HttpStatus.SC_OK) {
                        Header locationHeader = response.getFirstHeader("location");
                        String directUrl = locationHeader.getValue();
                        return;
                    }
                    final HttpEntity entity = response.getEntity();
                    InputStream in = entity.getContent();
//                    Bitmap bitmap = BitmapFactory.decodeStream(in);

                    File file = new File("/mnt/sdcard/cache.jpg");
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream fout = new FileOutputStream(file);
                    byte[] buf = new byte[1024 * 1024];
                    int count = 0;
                    int len = in.read(buf);
                    while (len != -1) {
                        fout.write(buf, 0, len);
                        count += len;
                        len = in.read(buf);
                    }
                    fout.flush();
                    fout.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        setContentView(R.layout.activity_main);
        DraweeController controller = Fresco.newDraweeControllerBuilder().setControllerListener(controllerListener).setUri(Uri.parse(Const.imgUrls[0])).build();
        ((SimpleDraweeView) findViewById(R.id.draweeView)).setController(controller);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Rocko");
        setSupportActionBar(mToolbar);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_share:
                        Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_SHORT).show();
                        if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
                            mDrawerLayout.closeDrawer(Gravity.END);
                        } else {
                            mDrawerLayout.openDrawer(Gravity.END);
                        }
                        break;
                    case R.id.rightDrawerView:
                        if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
                            mDrawerLayout.closeDrawer(Gravity.END);
                        } else {
                            mDrawerLayout.openDrawer(Gravity.END);
                        }
                    default:
                        break;
                }
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.statusbar_bg);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (true) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataSet;
    private int count = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView draweeView;
        public TextView textView;
        public ImageView volleyImageView;
        public NetworkImageView netImageView;
        public ViewHolder(View view) {
            super(view);
            draweeView = (SimpleDraweeView) view.findViewById(R.id.draweeView);
            textView = (TextView) view.findViewById(R.id.textView);
            volleyImageView = (ImageView) view.findViewById(R.id.volleyImageView);
            netImageView = (NetworkImageView) view.findViewById(R.id.netImageView);
        }
    }

    public MyAdapter(String[] myDataSet) {
        mDataSet = myDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        EventBus.getDefault().post(new MainActivity.MessageEvent(count + " ViewHolder created"));
        count ++;
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        EventBus.getDefault().post(new MainActivity.MessageEvent("post " + i + " load data"));
        viewHolder.textView.setText(mDataSet[i] + "");
        viewHolder.draweeView.setImageURI(Uri.parse(Const.imgUrls[i % 10]));
//        ImageRequest request = new ImageRequest(MainActivity.imgUrls[i%10], new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap bitmap) {
//                viewHolder.volleyImageView.setImageBitmap(bitmap);
//            }
//        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                viewHolder.volleyImageView.setImageResource(R.drawable.default_place_holder);
//            }
//        });
//        MyApplication.requestQueue.add(request);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.volleyImageView, R.drawable.default_place_holder, R.drawable.default_place_holder);
        MyApplication.imageLoader.get(Const.imgUrls[i%10], listener, 200, 200);

        viewHolder.netImageView.setDefaultImageResId(R.drawable.default_place_holder);
        viewHolder.netImageView.setErrorImageResId(R.drawable.default_place_holder);
        viewHolder.netImageView.setImageUrl(Const.imgUrls[i%10], MyApplication.imageLoader);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
