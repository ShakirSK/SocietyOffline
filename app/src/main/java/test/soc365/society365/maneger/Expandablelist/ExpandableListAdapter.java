package test.soc365.society365.maneger.Expandablelist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by PC002 on 1/31/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

      private Context _context;
    private List<String> _listDataHeader; //header title
    //child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    //For expandable groupIndicator
    private static final int[] EMPTY_STATE_SET = {};
    private static final int[] GROUP_EXPANDED_STATE_SET = {android.R.attr.state_expanded};
    private static final int[][] GROUP_STATE_SETS = {EMPTY_STATE_SET, GROUP_EXPANDED_STATE_SET };
                                                        //0                 1

    public ExpandableListAdapter(Context context, List<String> Listdatareader, HashMap<String, List<String>> listDataChild)
    {
        this._context=context;
        this._listDataHeader=Listdatareader;
        this._listDataChild=listDataChild;
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {

        Log.d("CHILD", _listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).toString());
        return  this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);

    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String)getChild(groupPosition,childPosition);

        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item,null);
        }
        TextView txtListChild = (TextView)convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);

        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
       // int childCount = 0;
       // if (groupPosition != 2) {
        //    childCount = this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
       // }
        //return childCount;
        List childList = _listDataChild.get(_listDataHeader.get(groupPosition));
        if (childList != null && ! childList.isEmpty()) {
            return childList.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        int i = _listDataHeader.size();
        Log.d("GROUPCOUNT", String.valueOf(i));
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageView imageView = (ImageView)convertView.findViewById(R.id.list_indicator);
        if( getChildrenCount( groupPosition ) == 0 )
        {
            imageView.setVisibility( View.INVISIBLE );
        } else {
            imageView.setVisibility( View.VISIBLE );
           // int stateSetIndex = ( isExpanded ? 1 : 0) ;
            //Drawable drawable = imageView.getDrawable();
            //drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
