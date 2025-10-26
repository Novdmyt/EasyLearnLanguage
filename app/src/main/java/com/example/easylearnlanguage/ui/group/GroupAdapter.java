package com.example.easylearnlanguage.ui.group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easylearnlanguage.R;
import com.example.easylearnlanguage.data.Group;
import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.VH> {

    public interface OnClick { void onClick(Group g); }
    private final OnClick cb;
    private final List<Group> items = new ArrayList<>();

    public GroupAdapter(OnClick cb){ this.cb = cb; }

    public void submit(List<Group> data){
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v){
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_group, p, false);
        return new VH(view);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos){
        Group g = items.get(pos);
        h.title.setText(g.title);
        h.lang.setText(g.fromLang + " → " + g.toLang);
        h.itemView.setOnClickListener(v -> cb.onClick(g));
    }

    @Override public int getItemCount(){ return items.size(); }

    static class VH extends RecyclerView.ViewHolder{
        TextView title, lang;
        VH(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            lang  = itemView.findViewById(R.id.tvLang);
        }
    }
}
