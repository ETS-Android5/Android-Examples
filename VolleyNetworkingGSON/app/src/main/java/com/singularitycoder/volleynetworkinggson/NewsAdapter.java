package com.singularitycoder.volleynetworkinggson;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "NewsAdapter";

    private ArrayList<NewsSubItemArticle> newsList;
    private Context context;
    private ImageLoader volleyImageLoader;

    NewsAdapter(ArrayList<NewsSubItemArticle> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
        volleyImageLoader();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsSubItemArticle newsSubItemArticle = newsList.get(position);
        if (null != holder) {
            NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
            newsViewHolder.tvAuthor.setText("Author: " + newsSubItemArticle.getAuthor());
            newsViewHolder.tvTitle.setText(newsSubItemArticle.getTitle());
            newsViewHolder.tvDescription.setText(newsSubItemArticle.getDescription());
            newsViewHolder.tvPublishedAt.setText("Published at: " + newsSubItemArticle.getPublishedAt());
            newsViewHolder.tvSource.setText("Source: " + newsSubItemArticle.getSource().getName());
            newsViewHolder.ivHeaderImage.setImageUrl(newsSubItemArticle.getUrlToImage(), volleyImageLoader);
            volleyImageLoader.get(
                    newsSubItemArticle.getUrlToImage(),
                    ImageLoader.getImageListener(
                            newsViewHolder.ivHeaderImage,
                            R.mipmap.ic_launcher,
                            R.color.colorAccent));
        }
    }

    private void volleyImageLoader() {
        RequestQueue requestQueue = VolleyRequestQueue.getInstance(context).getRequestQueue();
        volleyImageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tvAuthor, tvTitle, tvDescription, tvPublishedAt, tvSource;
        NetworkImageView ivHeaderImage;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPublishedAt = itemView.findViewById(R.id.tv_published_at);
            ivHeaderImage = itemView.findViewById(R.id.iv_header_image);
            tvSource = itemView.findViewById(R.id.tv_source);
        }
    }
}
