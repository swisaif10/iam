package digital.iam.ma.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import digital.iam.ma.R;
import digital.iam.ma.listener.OnRadioChecked;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.ViewHolder> {
    List<String> list;
    Context context;
    OnRadioChecked onRadioChecked;
    private RadioButton lastCheckedRB = null;

    public LineAdapter(Context context, List<String> list, OnRadioChecked onRadioChecked) {
        this.list = list;
        this.context = context;
        this.onRadioChecked = onRadioChecked;
    }

    @NonNull
    @Override
    public LineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_line_drop_down, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.line.setText(list.get(position));
        if (position == 0 && lastCheckedRB == null) {
            holder.selectedLine.setChecked(true);
            lastCheckedRB = holder.selectedLine;
        }
        holder.selectedLine.setOnClickListener(v -> {
            onRadioChecked.onRadioChecked(position);
            RadioButton checked_rb = (RadioButton) v;
            if (lastCheckedRB != checked_rb) {
                lastCheckedRB.setChecked(false);
            }
            lastCheckedRB = checked_rb;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView line;
        RadioButton selectedLine;
        ConstraintLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            line = itemView.findViewById(R.id.phoneLine);
            selectedLine = itemView.findViewById(R.id.selectedLine);
            container = itemView.findViewById(R.id.lineContainer);
        }
    }
}
