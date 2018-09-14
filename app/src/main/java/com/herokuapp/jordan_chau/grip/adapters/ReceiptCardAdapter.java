package com.herokuapp.jordan_chau.grip.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.herokuapp.jordan_chau.grip.R;

public class ReceiptCardAdapter extends RecyclerView.Adapter<ReceiptCardAdapter.CardViewHolder>{
    //private static final String TAG = BillitemAdapter.class.getSimpleName();

    private int mNumberItems;
    //private ArrayList<Recipe> mRecipes;
    final private ReceiptItemClickListener mOnClickListener;

    public ReceiptCardAdapter(ReceiptItemClickListener listener) {
        //mRecipes = recipes;
        //mNumberItems = recipes.size();
        mNumberItems = 6;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutForRecipeItem = R.layout.list_receipt_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForRecipeItem, parent, shouldAttachToParentImmediately);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        //String currentRecipeName = mRecipes.get(position).getName();
        //int currentRecipeServings = mRecipes.get(position).getServings();
        Image currentReceiptImage = null;
        String currentReceiptDate = "09/13/2018";
        String currentReceiptLabel = "Japanese Food";
        double currentReceiptTotal = 1350.25;

        holder.bind(currentReceiptImage, currentReceiptDate, currentReceiptLabel, Double.toString(currentReceiptTotal));
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView receiptImage;
        TextView receiptDate, receiptLabel, receiptTotal;

        CardViewHolder(View itemView) {
            super(itemView);

            receiptImage = itemView.findViewById(R.id.iv_receipt_image);
            receiptDate = itemView.findViewById(R.id.tv_receipt_date);
            receiptLabel = itemView.findViewById(R.id.tv_receipt_label);
            receiptTotal = itemView.findViewById(R.id.tv_receipt_total);
            itemView.setOnClickListener(this);
        }

        void bind(Image rImage, String rDate, String rLabel, String rTotal) {
            //TODO change image
            receiptImage.setBackground(null);
            receiptDate.setText(rDate);
            receiptLabel.setText(rLabel);
            receiptTotal.setText("$" + rTotal);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRecipeItemClicked(clickedPosition);
        }
    }

    public interface ReceiptItemClickListener {
        void onRecipeItemClicked(int clickedItemIndex);
    }
}
