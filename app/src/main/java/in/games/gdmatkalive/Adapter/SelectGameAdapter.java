package in.games.gdmatkalive.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.gdmatkalive.Model.SelectGameModel;
import in.games.gdmatkalive.R;

public class SelectGameAdapter extends RecyclerView.Adapter<SelectGameAdapter.ViewHolder> {
    Context context;
    ArrayList<SelectGameModel> list;

    public SelectGameAdapter(Context context, ArrayList<SelectGameModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_select_game,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.tv_gameName.setText(list.get(position).getName());
      SelectGameModel model = list.get(position);
//      if (model.getName().equalsIgnoreCase("SINGLE DIGIT"))
//      {
//          holder.lin_game.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow));
//      }else  if (model.getName().equalsIgnoreCase("JODI DIGIT"))
//      {
//          holder.lin_game.setBackgroundColor(ContextCompat.getColor(context,R.color.card2));
//      }else  if (model.getName().equalsIgnoreCase("SINGLE PANA"))
//      {
//          holder.lin_game.setBackgroundColor(ContextCompat.getColor(context,R.color.teal_200));
//      }else  if (model.getName().equalsIgnoreCase("DOUBLE PANA"))
//      {
//          holder.lin_game.setBackgroundColor(ContextCompat.getColor(context,R.color.purple_200));
//
//
//      }else  if (model.getName().equalsIgnoreCase("TRIPLE PANA"))
//      {
//          holder.lin_game.setBackgroundColor(ContextCompat.getColor(context,R.color.card6));
//      }else  if (model.getName().equalsIgnoreCase("HALF SANGAM"))
//      {
//          holder.lin_game.setBackgroundColor(ContextCompat.getColor(context,R.color.card3));
//      }else  if (model.getName().equalsIgnoreCase("FULL SANGAM"))
//      {
//          holder.lin_game.setBackgroundColor(ContextCompat.getColor(context,R.color.card4));
//      }else  if (model.getName().equalsIgnoreCase("SP Motor"))
//      {
//          holder.lin_game.setBackgroundColor(ContextCompat.getColor(context,R.color.card5));
//      }else  if (model.getName().equalsIgnoreCase("DP Motor"))
//      {
//          holder.lin_game.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
//      }else  if (model.getName().equalsIgnoreCase("LEFT DIGIT"))
//      {
//          holder.lin_game.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow));
//      }else  if (model.getName().equalsIgnoreCase("RIGHT DIGIT"))
//      {
//          holder.lin_game.setBackgroundColor(ContextCompat.getColor(context,R.color.card4));
//      }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gameName;
        LinearLayout lin_game;
        //ImageView img_game;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_gameName = itemView.findViewById(R.id.tv_gameName);
            //img_game = itemView.findViewById(R.id.img_game);
            //lin_game = itemView.findViewById(R.id.lin_game);
        }
    }
}
