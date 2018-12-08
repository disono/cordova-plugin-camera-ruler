/**
 * Author: Archie, Disono (webmonsph@gmail.com)
 * Website: http://www.webmons.com
 *
 * Created at: 12/08/2018
 */
package disono.com.webmons.ruler.Ruler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Spinner;

import stud.dress.booth.R;

public class InputDialog extends DialogFragment {

    InputDialogListener listener;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface InputDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onStart() {
        super.onStart();

        Spinner _unitInput = getDialog().findViewById(R.id.input_unit_chooser);
        _unitInput.setSelection(3);

        EditText input = getDialog().findViewById(R.id.reference_input);
        // 1 inch
        input.setText("1");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Inflate layout
        builder.setView(inflater.inflate(R.layout.dialog, null));
        //Set buttons
        builder.setPositiveButton(R.string.btn_OK, (dialog, which) -> listener.onDialogPositiveClick(InputDialog.this));
        builder.setNegativeButton(R.string.btn_cancel, (dialog, which) -> listener.onDialogNegativeClick(InputDialog.this));

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (InputDialogListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must implement InputDialogListener");
        }
    }

}
