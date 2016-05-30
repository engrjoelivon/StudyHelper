package aivco.com.studyhelper;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationDialog extends DialogFragment {

    public static final String TITLEKEY ="key for title passed confirmation dialog" ;
    public static final String MESSAGEKEY="key for message passed to confirmation dialog" ;
    private String title,message;
    OnFragmentListener fragmentListener;

    public static void createConfirmationDialog(String title,String message,FragmentManager manager)
    {
        Bundle bundle=new Bundle();
        bundle.putString(TITLEKEY,title);
        bundle.putString(MESSAGEKEY,message);
        ConfirmationDialog cd=new ConfirmationDialog();
        cd.setArguments(bundle);
        cd.show(manager,title);


    }
    public ConfirmationDialog() {
        // Required empty public constructor
    }
    public static ConfirmationDialog getInstance(){


        return new ConfirmationDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle b=getArguments();
        title=b.getString(MainActivity.TITLEKEY);
        message=b.getString(MainActivity.MESSAGEKEY);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        Dialog d=new AlertDialog.Builder(getActivity())// Set Dialog Icon
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setTitle(title)

                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        fragmentListener.onCancel();
                        getDialog().cancel();

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,   int which) {
                        // Do something else
                        getDialog().cancel();
                    }
                }).create();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return d;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentListener=(OnFragmentListener)activity;

    }

    public interface OnFragmentListener{

        void onCancel();
        void onPerform();
    }

}
