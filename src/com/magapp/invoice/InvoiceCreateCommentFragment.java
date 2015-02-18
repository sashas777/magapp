package com.magapp.invoice;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.magapp.main.R;


public class InvoiceCreateCommentFragment extends Fragment    {
    public View rootView;
    private String order_increment_id;
    private CommentListener cListener;
    EditText comment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView  = inflater.inflate(R.layout.invoice_create_comment_form, null);
        order_increment_id=getArguments().getString("order_increment_id");
        comment = (EditText)rootView.findViewById(R.id.comment_value);
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                final String comment_text = comment.getText().toString();
                cListener.onCommentEditTextChanged(comment_text);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            cListener=(CommentListener) getActivity();
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement onCommentEditTextChanged");
        }
    }

    public interface CommentListener {
        public void onCommentEditTextChanged(String string);
    }
}
