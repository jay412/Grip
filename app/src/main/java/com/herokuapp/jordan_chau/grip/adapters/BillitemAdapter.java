package com.herokuapp.jordan_chau.grip.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.herokuapp.jordan_chau.grip.R;
import com.herokuapp.jordan_chau.grip.model.ReceiptItem;

import java.util.ArrayList;

public class BillitemAdapter extends RecyclerView.Adapter<BillitemAdapter.ItemViewHolder>{
    //private static final String TAG = BillitemAdapter.class.getSimpleName();

    private int mNumberItems;
    private ArrayList<ReceiptItem> mItems;
    final private BillitemAdapter.BillItemClickListener mOnClickListener;

    public BillitemAdapter(ArrayList<ReceiptItem> items, BillitemAdapter.BillItemClickListener listener) {
        //mRecipes = recipes;
        //mNumberItems = recipes.size();
        mItems = items;
        mNumberItems = mItems.size();
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutForRecipeItem = R.layout.list_new_bill_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForRecipeItem, parent, shouldAttachToParentImmediately);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        int currentItemQuantity = mItems.get(position).getQuantity();
        String currentItemName = mItems.get(position).getName();
        double currentItemPrice = mItems.get(position).getPrice();

        holder.bind(Integer.toString(currentItemQuantity), currentItemName, Double.toString(currentItemPrice));
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView itemQuantity, itemName, itemPrice;

        ItemViewHolder(View itemView) {
            super(itemView);

            itemQuantity = itemView.findViewById(R.id.tv_ri_quantity);
            itemName = itemView.findViewById(R.id.tv_ri_label);
            itemPrice = itemView.findViewById(R.id.tv_ri_price);
            itemView.setOnClickListener(this);
        }

        void bind(String quantity, String name, String price) {
            itemQuantity.setText(quantity);
            itemName.setText(name);
            itemPrice.setText("$" + price);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onBillItemClicked(clickedPosition);
        }
    }

    public void addItem(ReceiptItem item) {
        mItems.add(item);
        mNumberItems++;
    }

    public ArrayList<ReceiptItem> getItems() {
        return mItems;
    }

    //TODO: implement delete when user clicks item
    public interface BillItemClickListener {
        void onBillItemClicked(int clickedItemIndex);
    }
}

