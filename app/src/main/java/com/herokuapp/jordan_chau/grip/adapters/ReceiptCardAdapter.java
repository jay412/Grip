package com.herokuapp.jordan_chau.grip.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.model.Receipt;

import java.util.ArrayList;

public class ReceiptCardAdapter extends RecyclerView.Adapter<ReceiptCardAdapter.CardViewHolder>{
    //private static final String TAG = BillitemAdapter.class.getSimpleName();

    private int mNumberItems;
    private ArrayList<Receipt> mReceiptItems;
    final private ReceiptItemClickListener mOnClickListener;

    public ReceiptCardAdapter(ArrayList<Receipt> receiptItems, ReceiptItemClickListener listener) {
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

        View view = inflater.inflate(layoutForRecipeItem, parent, false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String currentReceiptImage = mReceiptItems.get(position).getReceiptPicture();
        String currentReceiptDate = mReceiptItems.get(position).getDate();
        String currentReceiptLabel = mReceiptItems.get(position).getLabel();
        double currentReceiptTotal = mReceiptItems.get(position).getGrandTotal();

        holder.bind(currentReceiptImage, currentReceiptDate, currentReceiptLabel, Receipt.roundToMoneyFormat(currentReceiptTotal));
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

        void bind(String rImage, String rDate, String rLabel, String rTotal) {
            Bitmap pictureBitmap = decodeFromFirebase64(rImage);
            receiptImage.setImageBitmap(pictureBitmap);

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

    private static Bitmap decodeFromFirebase64(String imageUrl) {
        byte[] decodedByteArray = android.util.Base64.decode(imageUrl, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public interface ReceiptItemClickListener {
        void onReceiptItemClicked(int clickedItemIndex);
    }
}
