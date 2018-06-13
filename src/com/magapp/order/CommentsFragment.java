/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2018 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.order;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.magapp.connect.RequestInterface;
import com.magapp.connect.RequestTask;
import com.magapp.interfaces.ActivityLoadInterface;
import com.magapp.main.R;

import org.apache.commons.lang3.text.WordUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Vector;

public class CommentsFragment extends Fragment implements View.OnClickListener, RequestInterface {

    public View rootView;
    private LayoutInflater inf;
    private String increment_id, status, api_point;
    private ArrayList comments;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inf = inflater;
        rootView = inflater.inflate(R.layout.linear_comments, null);

        Bundle params = getArguments();
        status = params.getString("status");
        increment_id = params.getString("increment_id");
        api_point = params.getString("api_point");
        comments = params.getParcelableArrayList("comments");

        addCommentForm();

        Button submitBtn = rootView.findViewById(R.id.submit);
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
        LinearLayout current_view = rootView.findViewById(R.id.container);
        View vi = inf.inflate(R.layout.add_comment_form, null);
        if (!status.isEmpty())
            ((CheckBox) vi.findViewById(R.id.include_comments)).setHeight(0);

        current_view.addView(vi);
    }

    public void addCommentTask() {

        boolean notify = ((CheckBox) rootView.findViewById(R.id.notify_customer)).isChecked();
        boolean include_comments = ((CheckBox) rootView.findViewById(R.id.include_comments)).isChecked();
        String comment_val = ((EditText) rootView.findViewById(R.id.comment_value)).getText().toString();
        /*Task*/
        Vector params = new Vector();
        RequestTask task;

        params.add(increment_id);

        if (!status.isEmpty())
            params.add(status);

        params.add(comment_val);

        params.add((notify ? 1 : 0));
        if (status.isEmpty())
            params.add((include_comments ? 1 : 0));

        task = new RequestTask(this, getActivity(), api_point);
        task.execute(params);
        /*Task*/
    }

    public void Addcomment() {
        LinearLayout current_view = rootView.findViewById(R.id.container);
        View vi = inf.inflate(R.layout.comment_item_view, null);

        boolean notify = ((CheckBox) rootView.findViewById(R.id.notify_customer)).isChecked();
        String comment_val = ((EditText) rootView.findViewById(R.id.comment_value)).getText().toString();
        /*Date*/
        DateFormat created_at_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        created_at_format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date created_at_date = new Date();
        String created_at_date_string = new SimpleDateFormat("LLL dd, yyy HH:mm:ss").format(created_at_date);
        /*Date*/

        if (!notify)
            ((TextView) vi.findViewById(R.id.customer_notified_value)).setText("Notification Not Applicable");
        else
            ((TextView) vi.findViewById(R.id.customer_notified_value)).setText("Notified");

            /*Status*/
        String comment_status = " | ";
        if (status != null && !status.isEmpty()) {
            String comment_status_value = status.toString();
            comment_status = comment_status + WordUtils.capitalize(comment_status_value);
        } else {
            comment_status = "";
        }
            /*Status*/

        ((TextView) vi.findViewById(R.id.card_title)).setText(created_at_date_string + comment_status);
        ((TextView) vi.findViewById(R.id.comment_value)).setText(comment_val);


        current_view.addView(vi, 1);
    }

    public void Addcomments() {


        LinearLayout current_view = rootView.findViewById(R.id.container);

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
                created_at_date = created_at_format.parse(comment_data.get("created_at").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String created_at_date_string = new SimpleDateFormat("LLL dd, yyy HH:mm:ss").format(created_at_date);
			/*Date*/
            /*Status*/
            String comment_status = " | ";
            if (comment_data.get("status") != null && !comment_data.get("status").toString().isEmpty()) {
                String comment_status_value = comment_data.get("status").toString();
                comment_status = comment_status + WordUtils.capitalize(comment_status_value);
            } else {
                comment_status = "";
            }
            /*Status*/
            ((TextView) vi.findViewById(R.id.card_title)).setText(created_at_date_string + comment_status);

            ((TextView) vi.findViewById(R.id.comment_value)).setText(Html.fromHtml(comment_data.get("comment").toString()));

            current_view.addView(vi);
        }

    }

    public void onPreExecute() {
        ((ActivityLoadInterface) getActivity()).showProgressBar();
    }

    @Override
    public void doPostExecute(Object result, String result_api_point) {
        ((ActivityLoadInterface) getActivity()).hideProgressBar();
        ShowMessage("Comment has been added");
        Addcomment();
    }


    public void RequestFailed(String error) {
        ((ActivityLoadInterface) getActivity()).hideProgressBar();
        ShowMessage(error);
    }

    public void ShowMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}
