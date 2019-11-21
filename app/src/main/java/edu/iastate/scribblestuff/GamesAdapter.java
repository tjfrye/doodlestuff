package edu.iastate.scribblestuff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    private String TAG = "GameAdapter";

    private List<Game> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private String displayName;
    private Context context;

    // data is passed into the constructor
    GameAdapter(Context context, List<Game> data, String displayName) {
        this.displayName = displayName;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_game, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String opponentName;
        String turnType;
        String turns;

        Game current = mData.get(position);
        if(current.getPartnerName1().equals(displayName)) {
            opponentName = current.getPartnerName2();
        } else {
            opponentName = current.getPartnerName1();
        }

        if(current.getWhoDrawTurn().equals(displayName)) {
            turnType = context.getResources().getString(R.string.type_draw);
        } else {
            turnType = context.getResources().getString(R.string.type_guess);
        }
        turns = context.getResources().getString(R.string.turn_number) + current.getNumTurns();

        holder.nameTextView.setText(opponentName);
        holder.turnTypeTextView.setText(turnType);
        holder.turnNumberTextView.setText(turns);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView turnTypeTextView;
        TextView turnNumberTextView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            turnNumberTextView = itemView.findViewById(R.id.turnNumberTextView);
            turnTypeTextView = itemView.findViewById(R.id.turnTypeTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            //TODO start game
        }
    }

    // convenience method for getting data at click position
    Game getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}