package com.first.yuliang.deal_community.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.pojo.Dynamic;
import com.first.yuliang.deal_community.frament.utiles.HttpUtils;

import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by huangziwei on 16-4-12.
 */
public class MomentAdapter extends BaseAdapter {

    public static final int VIEW_HEADER = 0;
    public static final int VIEW_MOMENT = 1;

    ArrayList<Dynamic> mylist;
    private ArrayList<Comment> mList;
    private Context mContext;
    private CustomTagHandler mTagHandler;

    public MomentAdapter(Context context, ArrayList<Dynamic> dynamicList, ArrayList<Comment> list, CustomTagHandler tagHandler) {
        mList = list;
        mContext = context;
        mTagHandler = tagHandler;
        mylist=dynamicList;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_HEADER : VIEW_MOMENT;
    }

    @Override
    public int getCount() {
        // heanderView
        return mList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            ViewHolder holder = new ViewHolder();


                if (position==0) {


                    convertView = View.inflate(mContext, R.layout.item_header, null);
                    holder.dongtai_userName = (TextView) convertView.findViewById(R.id.dongtai_userName);

                    holder.dongtaiContent = (TextView) convertView.findViewById(R.id.dongtaiContent);

                    holder.headerImg = (ImageView) convertView.findViewById(R.id.headerImg);
                    holder.dongtaiTitle = (TextView) convertView.findViewById(R.id.dongtaiTitle);
                    holder.dongtaizan = (Button) convertView.findViewById(R.id.dongtaizan);
                    holder.dontaipic = (ImageView) convertView.findViewById(R.id.dontaipic);
                    holder.oncreatetime = (TextView) convertView.findViewById(R.id.oncreatetime);
                    convertView.setTag(holder);





                }else{
                convertView = View.inflate(mContext, R.layout.item_moment, null);

holder.btn_input_comment=(TextView)convertView.findViewById(R.id.btn_input_comment);holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.img = (ImageView) convertView.findViewById(R.id.author_icon);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.mCommentList = (LinearLayout) convertView.findViewById(R.id.comment_list);
                holder.mBtnInput = convertView.findViewById(R.id.btn_input_comment);
//                holder.mContent = (TextView) convertView.findViewById(R.id.content);
                convertView.setTag(holder);
            }
        }
        //防止ListView的OnItemClick与item里面子view的点击发生冲突
        ((ViewGroup) convertView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        if (position == 0) {
            int index = position ;
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.oncreatetime.setText(mylist.get(index).getPublishTime());
            holder.dongtaiTitle.setText(mylist.get(index).getTitle());
           // x.image().bind((holder.headerImg), HttpUtils.hostLuoqingshanSchool+"/usys/imgs/" + mylist.get(index).getPic() + ".png");

            holder.dongtai_userName.setText(mylist.get(index).getUserId().getUserName());
            holder.dongtaiContent.setText(mylist.get(index).getContent());



        } else {
            int index = position - 1;
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.name.setText(mList.get(index).getmReceiver().mName);
            holder.time.setText(mList.get(index).getRemaekTime());
            x.image().bind((holder.img), HttpUtils.hostLuoqingshanSchool+"/usys/imgs/" + mList.get(index).getImgs() + ".png");
            holder.content.setText(mList.get(index).mContent);






            CommentFun.parseCommentList(mContext,mList.get(index).getList(),
                    holder.mCommentList, holder.mBtnInput, mTagHandler);
        }
        return convertView;

    }

    private static class ViewHolder {
        LinearLayout mCommentList;
        View mBtnInput;
        TextView mContent;
        TextView name;
        TextView content;
        TextView time;
        ImageView img;

        TextView oncreatetime;

        ImageView headerImg;
        TextView dongtai_userName;
        TextView dongtaiContent;
        TextView dongtaiTitle;
        ImageView dontaipic;
        Button dongtaizan;
        TextView    btn_input_comment;

    }
}
