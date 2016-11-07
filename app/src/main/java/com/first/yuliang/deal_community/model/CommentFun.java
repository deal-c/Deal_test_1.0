package com.first.yuliang.deal_community.model;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.pojo.Dynamic;
import com.first.yuliang.deal_community.frament.utiles.HttpUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论相关方法
 */
public class CommentFun {

    public static final int KEY_COMMENT_SOURCE_COMMENT_LIST = -200162;
    public static final int KEY_COMMENT_SOURCE_COMMENT_LIST1 = -200161;

    /**
     * 在页面中显示评论列表
     *
     * @param context
     * @param mCommentList
     * @param commentList
     * @param btnComment
     * @param tagHandler
     */
    public static void parseCommentList(Context context, ArrayList<Comment> mCommentList, LinearLayout commentList,
                                        View btnComment, Html.TagHandler tagHandler, Dynamic dynamic) {
        if (btnComment != null) {
            btnComment.setTag(KEY_COMMENT_SOURCE_COMMENT_LIST, mCommentList);
        }
        TextView textView;
        Comment comment;
        int i;
        String content;
        for (i = 0; i < mCommentList.size(); i++) {
            comment = mCommentList.get(i);
            textView = (TextView) commentList.getChildAt(i);
            if (textView == null) { // 创建评论Videw
                textView = (TextView) View.inflate(context, R.layout.view_comment_list_item, null);
                commentList.addView(textView);
            }
            textView.setVisibility(View.VISIBLE);
            if (comment.mReceiver == null) {
                content = String.format("<html><%s>%s</%s>: <%s>%s</%s></html>", CustomTagHandler.TAG_COMMENTATOR,
                        comment.mCommentator.mName, CustomTagHandler.TAG_COMMENTATOR,
                        CustomTagHandler.TAG_CONTENT, comment.mContent, CustomTagHandler.TAG_CONTENT);
            } else {
                content = String.format("<html><%s>%s</%s> 回复 <%s>%s</%s>: <%s>%s</%s><html>",
                        CustomTagHandler.TAG_COMMENTATOR, comment.mCommentator.mName, CustomTagHandler.TAG_COMMENTATOR,
                        CustomTagHandler.TAG_RECEIVER, comment.mReceiver.mName, CustomTagHandler.TAG_RECEIVER,
                        CustomTagHandler.TAG_CONTENT, comment.mContent, CustomTagHandler.TAG_CONTENT);
            }
            textView.setText(Html.fromHtml(content, null, tagHandler));
            textView.setClickable(true);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setTag(CustomTagHandler.KEY_COMMENTATOR, comment.mCommentator);
            textView.setTag(CustomTagHandler.KEY_RECEIVER, comment.mReceiver);

            textView.setTag(KEY_COMMENT_SOURCE_COMMENT_LIST, mCommentList);
        }
        for (; i < commentList.getChildCount(); i++) {
            commentList.getChildAt(i).setVisibility(View.GONE);
        }
        if (mCommentList.size() > 0) {
            commentList.setVisibility(View.VISIBLE);
        } else {
            commentList.setVisibility(View.GONE);
        }
    }


    /**
     * 弹出评论对话框
     */
    public static void inputComment(final Activity activity, final int userId, final ListView listView,
                                    final View btnComment, final int position, final User receiver, final ArrayList<Comment> moments,
                                    final InputCommentListener listener) {


        String hint;
        if (receiver != null) {
            if (receiver.mId == userId) {
                hint = "我也说一句";
            } else {
                hint = "回复 " + receiver.mName;
            }
        } else {
            hint = "我也说一句";
        }
        // 获取评论的位置,不要在CommentDialogListener.onShow()中获取，onShow在输入法弹出后才调用，
        // 此时btnComment所属的父布局可能已经被ListView回收
        final int[] coord = new int[2];
        if (listView != null) {
            btnComment.getLocationOnScreen(coord);
        }

        showInputComment(activity, hint, new CommentDialogListener() {
            @Override
            public void onClickPublish(final Dialog dialog, EditText input, final TextView btn) {
                final String content = input.getText().toString();
                if (content.trim().equals("")) {
                    Toast.makeText(activity, "评论不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ArrayList<Comment> commentList = (ArrayList) btnComment.getTag(KEY_COMMENT_SOURCE_COMMENT_LIST);
                btn.setClickable(false);
                final long receiverId = receiver == null ? -1 : receiver.mId;
                // Comment comment = new Comment(ComMainActivity.sUser, content, receiver);
                Comment tomycomment=null;
                Comment mycomment = null;
                ArrayList<Comment> mylist = new ArrayList();
                Comment comment = new Comment(ComMainActivity.sUser, content, receiver);
                if (receiver == null) {
                    mycomment = new Comment("user1", "2016", 1, ComMainActivity.sUser, content, ComMainActivity.sUser, null);
                    moments.add(mycomment);
                    ArrayList<Comment> myfirstlist = new ArrayList();
                    myfirstlist.add(mycomment);
                    addfirstRemark(myfirstlist, userId);

                } else if(position!=0){
                    ArrayList<Comment> tomyfirstlist = new ArrayList();
                    tomycomment = new Comment(ComMainActivity.sUser,content,receiver);
                    //commentList.add(tomycomment);
                   tomyfirstlist.add(tomycomment);
                    moments.get(position-1).setList(tomyfirstlist);
                    addRemark(tomyfirstlist,userId);
                }
                else {
                    commentList.add(comment);
                    Log.e("看看数据====", "我要看结果，妈的???");
                    mylist.add(comment);
                    addRemark(mylist, userId);
                }
                if (listener != null) {
                    listener.onCommitComment(comment);
                    // Log.e("看看数据————", "难道你也不执行？");
                }

                dialog.dismiss();

                Toast.makeText(activity, "评论成功", Toast.LENGTH_SHORT).show();
            }

            private void addfirstRemark(ArrayList<Comment> myfirstlist, int userId) {

                String remarkUserId = String.valueOf(userId);
                String dynamicId = String.valueOf(ComMainActivity.dynamicArrayList.get(0).getDynamicId());
                String reciever = myfirstlist.get(0).getmReceiver().mName;
                String recieveId = String.valueOf(myfirstlist.get(0).getmReceiver().mId);
                String remarkContent = myfirstlist.get(0).getmContent();
                // String commentator = myfirstlist.get(0).getmCommentator().mName;

                Log.e("看看数据1====", dynamicId + "???");
                Log.e("看看数据2====", reciever + "???");
                //  Log.e("看看数据====", commentator+"???");
                Log.e("看看数据3====", recieveId + "???");
                Log.e("看看数4====", remarkContent + "???");
                Log.e("我自己看看数据5====", remarkUserId + "???");

                RequestParams requestParams = new RequestParams("http://10.40.5.61:8080/usys/MyFirstServlt");
                requestParams.addBodyParameter("remarkContent", remarkContent);
                requestParams.addBodyParameter("recieveId", recieveId);
                requestParams.addBodyParameter("dynamicId", dynamicId);
                requestParams.addBodyParameter("reciever", reciever);
                //requestParams.addBodyParameter("commentator", commentator);
                requestParams.addBodyParameter("remarkUserId", remarkUserId);
                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("看看数据====", result + "???!!!");
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("看看数据====", ex + "???!!!");
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });


            }


            @Override
            public void onShow(int[] inputViewCoordinatesInScreen) {
                if (listView != null) {
                    // 点击某条评论则这条评论刚好在输入框上面，点击评论按钮则输入框刚好挡住按钮
                    int span = btnComment.getId() == R.id.btn_input_comment ? 0 : btnComment.getHeight();
                    listView.smoothScrollBy(coord[1] + span - inputViewCoordinatesInScreen[1], 1000);
                }
            }

            @Override
            public void onDismiss() {

            }
        });

    }

    private static void addRemark(ArrayList<Comment> mylist, int userId) {

        String remarkUserId = String.valueOf(userId);
        String dynamicId = String.valueOf(MomentAdapter.mylist.getDynamicId());
        String reciever = mylist.get(0).getmReceiver().mName;
        String recieveId = String.valueOf(mylist.get(0).getmReceiver().mId);
        String remarkContent = mylist.get(0).getmContent();
        String commentator = mylist.get(0).getmCommentator().mName;

         /*   Log.e("看看数据====", dynamicId+"???");
            Log.e("看看数据====", reciever+"???");
            Log.e("看看数据====", commentator+"???");
            Log.e("看看数据====", recieveId+"???");
            Log.e("看看数据====", remarkContent+"???");
            Log.e("我自己看看数据====", remarkUserId+"???");*/

        RequestParams requestParams = new RequestParams("http://10.40.5.61:8080/usys/MyBackComment");
        requestParams.addBodyParameter("remarkContent", remarkContent);
        requestParams.addBodyParameter("recieveId", recieveId);
        requestParams.addBodyParameter("dynamicId", dynamicId);
        requestParams.addBodyParameter("reciever", reciever);
        requestParams.addBodyParameter("commentator", commentator);
        requestParams.addBodyParameter("remarkUserId", remarkUserId);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //Log.e("看看数据====", result+"???");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //  Log.e("看看数据====", ex+"???");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }


    public static class InputCommentListener {
        //　评论成功时调用
        public void onCommitComment(Comment comment) {


        }

    }


    /**
     * 弹出评论对话框
     */
    public static Dialog showInputComment(Activity activity, CharSequence hint, final CommentDialogListener listener) {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.view_input_comment);
        dialog.findViewById(R.id.input_comment_dialog_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onDismiss();
                }
            }
        });
        final EditText input = (EditText) dialog.findViewById(R.id.input_comment);
        input.setHint(hint);
        final TextView btn = (TextView) dialog.findViewById(R.id.btn_publish_comment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickPublish(dialog, input, btn);
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    int[] coord = new int[2];
                    dialog.findViewById(R.id.input_comment_container).getLocationOnScreen(coord);
                    // 传入 输入框距离屏幕顶部（不包括状态栏）的长度
                    listener.onShow(coord);
                }
            }
        }, 300);
        return dialog;
    }

    public interface CommentDialogListener {
        void onClickPublish(Dialog dialog, EditText input, TextView btn);

        void onShow(int[] inputViewCoordinatesOnScreen);

        void onDismiss();
    }

}
