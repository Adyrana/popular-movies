package com.example.android.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Video;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import lombok.AllArgsConstructor;

/**
 * @author Julia Mattjus
 */
@AllArgsConstructor
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    public enum CLICK_TYPE {
        PLAY, SHARE
    }

    public interface IVideoOnClickHandler {


        void onClick(Video video, CLICK_TYPE clickType);
    }

    private List<Video> videos;
    private final IVideoOnClickHandler mClickHandler;

    private static final String TAG = VideoAdapter.class.getSimpleName();

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        VideoViewHolder videoViewHolder = new VideoViewHolder(view);
        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.videoName.setText(videos.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    /**
     * View holder for the videos
     */
    public class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView videoName;
        ImageView playButton;
        ImageView shareButton;

        /**
         * Constructor
         *
         * @param itemView
         */
        public VideoViewHolder(View itemView) {
            super(itemView);
            videoName = (TextView) itemView.findViewById(R.id.tv_video_name);
            videoName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mClickHandler.onClick(videos.get(position), CLICK_TYPE.PLAY);
                }
            });
            playButton = (ImageView) itemView.findViewById(R.id.iv_play_button);
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mClickHandler.onClick(videos.get(position), CLICK_TYPE.PLAY);
                }
            });
            shareButton = (ImageView) itemView.findViewById(R.id.iv_share_button);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mClickHandler.onClick(videos.get(position), CLICK_TYPE.SHARE);
                }
            });
        }
    }
}
