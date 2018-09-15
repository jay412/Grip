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
import com.herokuapp.jordan_chau.grip.model.ReceiptItem;

import java.util.ArrayList;

public class ReceiptCardAdapter extends RecyclerView.Adapter<ReceiptCardAdapter.CardViewHolder>{
    //private static final String TAG = BillitemAdapter.class.getSimpleName();

    private int mNumberItems;
    private ArrayList<ReceiptItem> mReceiptItems;
    final private ReceiptItemClickListener mOnClickListener;

    //TODO change receiptitem to receipt later
    public ReceiptCardAdapter(ArrayList<ReceiptItem> receiptItems, ReceiptItemClickListener listener) {
        mReceiptItems = receiptItems;
        mNumberItems = receiptItems.size();
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
        int currentReceiptDate = mReceiptItems.get(position).getQuantity();
        String currentReceiptLabel = mReceiptItems.get(position).getName();
        double currentReceiptTotal = mReceiptItems.get(position).getPrice();

        holder.bind(currentReceiptImage, Integer.toString(currentReceiptDate), currentReceiptLabel, Double.toString(currentReceiptTotal));
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
            mOnClickListener.onReceiptItemClicked(clickedPosition);
        }
    }

    public interface ReceiptItemClickListener {
        void onReceiptItemClicked(int clickedItemIndex);
    }
}
