package com.marluki.misterymap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.marluki.misterymap.model.ObjetoMapa;

import java.util.ArrayList;

/**
 * Created by lu_lu_000 on 23/05/2017.
 */

public class SearchAdapter extends BaseAdapter implements Filterable{

    private Context context;
    private ArrayList<ObjetoMapa> originalList;
    private ArrayList<ObjetoMapa> suggestions = new ArrayList<>();
    private Filter filter = new CustomFilter();

    public SearchAdapter(Context context, ArrayList<ObjetoMapa> originalList) {
        this.context = context;
        this.originalList = originalList;
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestions.get(position).getNombre_objeto();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.suggestion_layout,
                    parent,
                    false);
            holder = new ViewHolder();
            holder.autoText = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.autoText.setText(suggestions.get(position).getNombre_objeto());



        return null;
    }


    private static class ViewHolder {
        TextView autoText;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    private class  CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();

            if(originalList !=null  && constraint != null){
                for(int i=0;i<originalList.size();i++){
                    if(originalList.get(i).getNombre_objeto().toLowerCase().contains(constraint)){
                        suggestions.add(originalList.get(i));
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=suggestions;
            results.count=suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.count>0){
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }

        }
    }
}
