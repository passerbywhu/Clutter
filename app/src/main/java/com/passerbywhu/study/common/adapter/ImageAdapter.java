package com.passerbywhu.study.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.passerbywhu.study.MyApplication;
import com.passerbywhu.study.R;
import com.passerbywhu.study.common.consts.Const;
import com.passerbywhu.study.common.utils.Utils;

/**
 * Created by wuwenchao3 on 2015/5/18.
 */
public class ImageAdapter extends AdapterBase<String> {
    public ImageAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.render(position);
        return convertView;
    }

    private class ViewHolder {
        private View mRootView;
        private TextView mTextView;
        private SimpleDraweeView mSimpleDraweeView;
        private ImageView mVolleyImageView;
        private NetworkImageView mNetworkImageView;

        public ViewHolder(View view) {
            mRootView = view;
            mTextView = (TextView) view.findViewById(R.id.textView);
            mSimpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.draweeView);
            mVolleyImageView = (ImageView) view.findViewById(R.id.volleyImageView);
            mNetworkImageView = (NetworkImageView) view.findViewById(R.id.netImageView);
        }

        public void render(int position) {
            mTextView.setText(position + 1 + "");
            //SimpleDraweeView just need to do this.
            mSimpleDraweeView.setImageURI(Uri.parse(Utils.generateImageUrl(position)));

            //ImageRequest way     noCache
//            ImageRequest imageRequest = new ImageRequest(Utils.generateImageUrl(position), new Response.Listener<Bitmap>() {
//                @Override
//                public void onResponse(Bitmap bitmap) {
//                    mVolleyImageView.setImageBitmap(bitmap);
//                }
//            }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    mVolleyImageView.setImageResource(R.drawable.default_place_holder);
//                }
//            });
//            MyApplication.requestQueue.add(imageRequest);

            ImageLoader.ImageListener listener = ImageLoader.getImageListener(mVolleyImageView, R.drawable.default_place_holder, R.drawable.default_place_holder);
            MyApplication.imageLoader.get(Utils.generateImageUrl(position), listener, 200, 200);

            mNetworkImageView.setDefaultImageResId(R.drawable.default_place_holder);
            mNetworkImageView.setErrorImageResId(R.drawable.default_place_holder);
            mNetworkImageView.setImageUrl(Utils.generateImageUrl(position), MyApplication.imageLoader);
        }
    }
}
