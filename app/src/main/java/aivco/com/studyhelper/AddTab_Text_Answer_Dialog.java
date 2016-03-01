package aivco.com.studyhelper;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by johnanderson1 on 11/5/15.
 */
public class AddTab_Text_Answer_Dialog extends DialogFragment implements View.OnClickListener {
    private EditText edittext;
    private SendAnswerText sendAnswerText;

    @Override
    public void onClick(View view) {
        sendAnswerText.sendAnswer(edittext.getText().toString());

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.addtab_text_answer_dialog, container, false);

        //getDialog().setTitle("Enter Answer Below ");




        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        edittext=(EditText)getView().findViewById(R.id.answer_text);
        Button saveButton=(Button)getView().findViewById(R.id.enter_button);
        saveButton.setOnClickListener(this);


    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sendAnswerText=(SendAnswerText)activity;

    }
    public interface SendAnswerText
    {
        public void sendAnswer(String text);

    }
}
