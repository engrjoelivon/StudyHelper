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
 * Created by johnanderson1 on 10/30/15.
 */
public class Generate_Question extends DialogFragment implements View.OnClickListener {
    EditText edittext;
    private QuestionValue questionValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.generate_questions, container, false);

        getDialog().setTitle("Generate Question");



        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        edittext=(EditText)getView().findViewById(R.id.questionValue);
        Button saveButton=(Button)getView().findViewById(R.id.saveQuestion);
        saveButton.setOnClickListener(this);


    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        questionValue=(QuestionValue)activity;

    }

    @Override
    public void onClick(View view) {
        if(questionValue != null)
        {
            questionValue.question(edittext.getText().toString());

        }

    }


    public interface QuestionValue
    {
        public void question(String name);

    }

}
