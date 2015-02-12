package com.magapp.order;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.main.OrderInfoActivity;
import com.magapp.main.R;
import org.apache.commons.lang3.text.WordUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Vector;

public class CommentsFragment extends Fragment implements View.OnClickListener, RequestInterface {
	
	public View rootView;
	private LayoutInflater inf;

	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {

            inf=inflater;
			rootView  = inflater.inflate(R.layout.linear_comments, null);

            addCommentForm();

            Button submitBtn = (Button) rootView.findViewById(R.id.submit);
            submitBtn.setOnClickListener(this);

            Addcomments();
			return rootView;
	}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                addCommentTask();
                break;
        }
    }

    public void addCommentForm() {
        LinearLayout current_view = (LinearLayout)rootView.findViewById(R.id.container);
        View vi = inf.inflate(R.layout.add_comment_form, null);
        current_view.addView(vi);
    }

    public void addCommentTask() {
        String status=((OrderInfoActivity)getActivity()).getStatus();
        boolean notify=((CheckBox)rootView.findViewById(R.id.notify_customer)).isChecked();
        String comment_val=((EditText)rootView.findViewById(R.id.comment_value)).getText().toString();
        /*Task*/
        Vector params = new Vector();
        RequestTask task;
        HashMap map_comment = new HashMap();

        String order_increment_id=((OrderInfoActivity)getActivity()).GetOrderIncrementId();

        map_comment.put("orderIncrementId", order_increment_id);
        map_comment.put("status", order_increment_id);
        map_comment.put("comment", comment_val);
        map_comment.put("notify", (notify ? 1: 0));
        task = new RequestTask(this, getActivity(),"sales_order.addComment");
        task.execute(params);
        /*Task*/
    }

    public void Addcomment(){
        LinearLayout current_view = (LinearLayout)rootView.findViewById(R.id.container);
        View vi = inf.inflate(R.layout.comment_item_view, null);

        String status=((OrderInfoActivity)getActivity()).getStatus();
        boolean notify=((CheckBox)rootView.findViewById(R.id.notify_customer)).isChecked();
        String comment_val=((EditText)rootView.findViewById(R.id.comment_value)).getText().toString();
        /*Date*/
        DateFormat created_at_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        created_at_format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date created_at_date = new Date();
        String created_at_date_string= new SimpleDateFormat("LLL dd, yyy HH:mm:ss") .format(created_at_date);
		/*Date*/

        if (!notify)
            ((TextView) vi.findViewById(R.id.customer_notified_value)).setText("Notification Not Applicable");
        else
            ((TextView) vi.findViewById(R.id.customer_notified_value)).setText("Notified");

        ((TextView) vi.findViewById(R.id.card_title)).setText(created_at_date_string+" | "+status);
        ((TextView) vi.findViewById(R.id.comment_value)).setText(comment_val);


        current_view.addView(vi,1);
    }

    public void Addcomments() {
        Object[] comments=((OrderInfoActivity)getActivity()).getComments();

        LinearLayout current_view = (LinearLayout)rootView.findViewById(R.id.container);

        for (Object comment : comments) {
            HashMap comment_data = (HashMap) comment;
            View vi = inf.inflate(R.layout.comment_item_view, null);

            if (comment_data.get("is_customer_notified").toString().equals("2"))
                ((TextView) vi.findViewById(R.id.customer_notified_value)).setText("Notification Not Applicable");
            else
                ((TextView) vi.findViewById(R.id.customer_notified_value)).setText("Notified");

			/*Date*/
            DateFormat created_at_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            created_at_format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date created_at_date = new Date();
            try {
                created_at_date =created_at_format.parse(comment_data.get("created_at").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String created_at_date_string= new SimpleDateFormat("LLL dd, yyy HH:mm:ss") .format(created_at_date);
			/*Date*/
            /*Status*/
            String status=comment_data.get("status").toString();
            status = WordUtils.capitalize(status);
            /*Status*/
            ((TextView) vi.findViewById(R.id.card_title)).setText(created_at_date_string+" | "+status);

            ((TextView) vi.findViewById(R.id.comment_value)).setText(comment_data.get("comment").toString());

            current_view.addView(vi);
        }

    }

    public void onPreExecute(){
        ((OrderInfoActivity)getActivity()).showProgressBar();
    };

    @Override
    public void doPostExecute(Object result) {
        ((OrderInfoActivity)getActivity()).hideProgressBar();
        ShowMessage("Comment has been added");
        Addcomment();
    }


    public void RequestFailed(String error) {
        ((OrderInfoActivity)getActivity()).hideProgressBar();
        ShowMessage(error);
    }

    public void ShowMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
	 
}
