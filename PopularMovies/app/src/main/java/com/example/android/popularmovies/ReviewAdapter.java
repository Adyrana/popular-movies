package com.example.android.popularmovies;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.data.Review;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import lombok.AllArgsConstructor;

/**
 * Adapter for the reviews so that they can be shown in a recycler view.
 *
 * @author Julia Mattjus
 */
@AllArgsConstructor
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.author.setText(reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    /**
     * View holder class for reviews
     */
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        ExpandableTextView content;

        /**
         * Constructor
         *
         * @param itemView
         */
        public ReviewViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.tv_review_author);
            content = (ExpandableTextView) itemView.findViewById(R.id.expand_tv_review);
        }
    }
}
