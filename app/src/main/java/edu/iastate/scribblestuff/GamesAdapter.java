package edu.iastate.scribblestuff;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {
    private String TAG = "GamesAdapter";

    private List<Game> mData;
    private List<String> gameIds;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private String displayName;
    private Context context;
    private Boolean isGuesser = false;

    // data is passed into the constructor
    GamesAdapter(Context context, List<Game> data, String displayName, List<String> gameIds) {
        this.displayName = displayName;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.gameIds = gameIds;
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
            isGuesser = false;
        } else {
            turnType = context.getResources().getString(R.string.type_guess);
            isGuesser = true;
        }
        turns = context.getResources().getString(R.string.turn_number) + current.getNumTurns();

        holder.nameTextView.setText(opponentName);
        holder.turnTypeTextView.setText(turnType);
        holder.turnNumberTextView.setText(turns);
        holder.isGuesser = isGuesser;
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
        Boolean isGuesser;

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
            if(isGuesser) { // Start guessing
                Intent intent = new Intent(view.getContext(), GuessActivity.class);
                intent.putExtra("gameId", gameIds.get(getAdapterPosition()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);

            } else { // Start drawing
                Intent intent = new Intent(view.getContext(), ChooseWordActivity.class);
                intent.putExtra("gameId",gameIds.get(getAdapterPosition()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }

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