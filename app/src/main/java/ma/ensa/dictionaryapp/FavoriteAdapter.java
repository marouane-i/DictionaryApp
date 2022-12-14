package ma.ensa.dictionaryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.myViewHolder> {
    private ArrayList<FavoriteModel> list;
    private final FavoriteRecyclerViewInterface favoriteRecyclerViewInterface;

    public FavoriteAdapter(ArrayList<FavoriteModel> list,FavoriteRecyclerViewInterface favoriteRecyclerViewInterface) {
        this.list = list;
        this.favoriteRecyclerViewInterface =favoriteRecyclerViewInterface;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView word,wordType;

        public myViewHolder(@NonNull View itemView,FavoriteRecyclerViewInterface favoriteRecyclerViewInterface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
            word = itemView.findViewById(R.id.textViewItem1);
            wordType = itemView.findViewById(R.id.textViewItem2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(favoriteRecyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            favoriteRecyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }

    }
    @NonNull
    @Override
    public FavoriteAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_recycler_view_item,parent,false);
        return new myViewHolder(view,favoriteRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.myViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getImageRes());
        holder.word.setText(list.get(position).getWord());
        holder.wordType.setText(list.get(position).getWordType());
    }

    @Override
    public int getItemCount() {

        return (list!=null)? list.size():0;
    }
}
