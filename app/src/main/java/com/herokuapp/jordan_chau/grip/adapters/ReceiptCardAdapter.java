package com.herokuapp.jordan_chau.grip.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.herokuapp.jordan_chau.grip.R;

public class ReceiptCardAdapter extends RecyclerView.Adapter<ReceiptCardAdapter.CardViewHolder>{
    private static final String TAG = ReceiptCardAdapter.class.getSimpleName();

    private int mNumberItems;
    //private ArrayList<Recipe> mRecipes;
    final private ReceiptItemClickListener mOnClickListener;

    public ReceiptCardAdapter(ReceiptItemClickListener listener) {
        //mRecipes = recipes;
        //mNumberItems = recipes.size();
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

        //holder.bind(currentRecipeName, Integer.toString(currentRecipeServings));
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView recipeNameView, servingsView;

        CardViewHolder(View itemView) {
            super(itemView);

            //recipeNameView = itemView.findViewById(R.id.tv_recipe_name);
            //servingsView = itemView.findViewById(R.id.tv_servings);
            itemView.setOnClickListener(this);
        }

        void bind(String recipeName, String servings) {
            recipeNameView.setText(recipeName);
            servingsView.setText("Servings: " + servings);
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
