package com.droidguru.highlightsearchtext;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder> implements Filterable {

    Context context;
    ArrayList<String> dataList = new ArrayList<>();
    ArrayList<String> filteredList = new ArrayList<>();
    String queryText="";
    //constructor
    public DataAdapter(Context con, ArrayList<String> list)
    {
        context = con;
        dataList = list;
        filteredList = list;

    }
    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_recycler,parent,false);
        DataHolder dataHolder = new DataHolder(view);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
       String dataText = filteredList.get(position);
       if(queryText!=null && !queryText.isEmpty())
       {
           int startPos = dataText.toLowerCase().indexOf(queryText.toLowerCase());
           int endPos = startPos+queryText.length();
           if(startPos!=-1)
           {
              Spannable spannable = new SpannableString(dataText);
              ColorStateList colorStateList = new ColorStateList(new int [][]{new int []{}},new int []{Color.BLUE});
              TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(null,Typeface.BOLD,-1,colorStateList,null);
              spannable.setSpan(textAppearanceSpan,startPos,endPos,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              holder.tv.setText(spannable);
           }else {
               holder.tv.setText(dataText);
           }
       }else {
           holder.tv.setText(dataText);
       }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if(charSequence!=null && !charSequence.equals(""))
            {
                queryText = charSequence.toString();
                List<String> newList = new ArrayList<>();
                for(int i=0;i<dataList.size();i++)
                {
                    if(dataList.get(i).toLowerCase().contains(charSequence.toString().toLowerCase()))
                    {
                        newList.add(dataList.get(i));
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.count = newList.size();
                filterResults.values = newList;
                return filterResults;
            }
            return null;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            if(filterResults!=null && filterResults.count>0)
            {
                filteredList = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    };

    public class DataHolder extends RecyclerView.ViewHolder
    {
        TextView tv;
        public DataHolder(@NonNull View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.text_row_recycler);
        }
    }
}
