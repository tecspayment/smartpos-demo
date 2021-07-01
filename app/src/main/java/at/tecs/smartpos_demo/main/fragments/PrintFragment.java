package at.tecs.smartpos_demo.main.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import at.tecs.smartpos.data.PrinterPrintType;
import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.main.MainContract;

public class PrintFragment extends Fragment implements MainContract.View.PrintTab {

    private Callback.PrintTabCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.print_frag, container, false);
        final Button printTextButton = view.findViewById(R.id.printTextButton);
        final Button printQrButton = view.findViewById(R.id.printQrButton);
        final Button printImageButton = view.findViewById(R.id.printImageButton);
        final Button printReceiptButton = view.findViewById(R.id.printReceiptButton);
        final Button feedLinesButton = view.findViewById(R.id.feedLinesButton);

        final EditText feedLinesEditText = view.findViewById(R.id.feedLinesEditText);
        final EditText printTextEdit = view.findViewById(R.id.printTextEdit);
        final EditText printQrEditText = view.findViewById(R.id.printQrEditText);

        printTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(printTextEdit.getText() != null && printTextEdit.getText().length() > 0) {

                    String data = printTextEdit.getText().toString();

                    callback.performPrint(data, PrinterPrintType.TEXT.value);
                }
            }
        });

        printQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(printQrEditText.getText() != null && printQrEditText.getText().length() > 0) {

                    String data = printQrEditText.getText().toString();
                    callback.performPrint(data, PrinterPrintType.QR_MEDIUM.value);
                }
            }
        });

        printImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.performPrint("Qk0mGAAAAAAAAD4AAAAoAAAAQAEAAJkAAAABAAEAAAAAAOgXAADEDgAAxA4AAAAAAAAAAAAAAAAAAP///wAAAAAAP///gAAAA4AAAAH///gAAAB4AAAAf/8AAAAA/AAAAAAAAAA/gAAAAB///4AAAAHAAAAA///4AAAAOAAAAH/+AAAAAeAAAAAAAAAAB4AAAAAP//+AAAAA4AAAAP//+AAAADgAAAB//AAAAAOAAAAAAAAAAAPAAAAAB///gAAAAOAAAAB///gAAAAwAAAAf/wAAAADAAAAAAAAAAAB4AAAAAP//8AAAABwAAAAf//4AAAAMAAAAH/4AAAABgAAAAAAAAAAAeAAAAAD///AAAAAMAAAAD//+AAAADAAAAB/+AAAAAwAAAAAAAAAAADwAAAAAf//wAAAADgAAAA///gAAABwAAAAf/AAAAAMAAAAAAAAAAAA8AAAAAD//+AAAAAcAAAAH//4AAAAcAAAAH/wAAAAGAAAAAAAAAAAAPgAAAAAf//gAAAAHAAAAB///AAAAHAAAAB/4AAAADgAAAAAAAAAAAH4AAAAAH//4AAAAA4AAAAP//wAAABwAAAA/+AAAAB4AAAAAAAAAAAB/AAAAAA//+AAAAAGAAAAD//8AAAAcAAAAP/AAAAAcAAAAAAAAAAAAf4AAAAAH//wAAAABwAAAAf//AAAAHAAAAD/wAAAAPAAAAAAAAAAAAP+AAAAAA//8AAAAAOAAAAD//wAAABwAAAA/4AAAAHwAAAAAAAAAAAD/wAAAAAH//AAAAADgAAAA//8AAAAcAAAAP+AAAAB8AAAAAAAAAAAB/8AAAAAB//wAAAAAcAAAAH//AAAAHAAAAD/AAAAA+AAAAAAAAAAAAf/gAAAAAP/+AAAAADAAAAB//wAAABwAAAA/wAAAAfgAAAAAAAAAAAP/4AAAAAB//gAAAAA4AAAAAAAAAAAcAAAAP4AAAAP4AAAAAAAAAAAD//AAAAAAP/4AAAAAHAAAAAAAAAAAHAAAAH+AAAAD8AAAAAAAAAAAB//4AAAAAD/+AAAAAAwAAAAAAAAAABwAAAB/AAAAB/AAAAAAAAAAAAf/+AAAAAAf/wAAAAAOAAAAAAAAAAAcAAAAfwAAAA/wAAAAAAAAAAAP//wAAAAAD/8AAAAABwAAAAAAAAAAHAAAAH4AAAAP8AAAAAAAAAAAH//8AAAAAAf/AAAAAAcAAAAAAAAAABwAAAB+AAAAH+AAAAAAAAAAAB///gAAAAAD/4AAAAADgAAAAAAAAAAcAAAAfAAAAD/gAAAAAAAAAAA///4AAAAAA/+AAAAAAYAAAAAAAAAAHAAAAHwAAAB/4AAAAAAAAAAAP///AAAAAAH/gAAAAAHAAAAAAAAAABwAAAB4AAAAf8AAAAH8AAAAAH///4AAAAAA/4AAAAAA4AAAAAAAAAAcAAAA8AAAAP/AAAAD/gAAAAB///+AAAAAAH/AAAAAAOAAAAAAAAAAHAAAAPAAAAH/wAAAA/4AAAAA////wAAAAAB/wAAAAABwAAAAAAAAABwAAADgAAAD/8AAAAf+AAAAAP///8AAAAAAP8AAAAAAMAAAAAAAAAAcAAAA4AAAA/+AAAAH/gAAAAH////gAAAAAB/AAAAAADgAAAAAAAAAHAAAAMAAAAf/gAAAB/wAAAAD////8AAAAAAP4AAAAAAcAAAAAAAAABwAAADAAAAP/4AAAA/8AAAAA/////AAAAAAB+AAAAAADAAAAAAAAAAcAAAAgAAAD/8AAAAP+AAAAAf////4AAAAAAfgAAAAAA4AAAAAAAAAHAAAAIAAAB//AAAAH/gAAAAH////+AAAAAAD4AAAAAAGAAAAH8AAABwAAAAAAAA////////4AAAAD/////wAAAAAAfAAAAAABwAAAB/AAAAcAAAAAAAAP///////8AAAAA/////8AAAAAADwAAAAAAOAAAAPwAAAHAAAAAAAAD///////+AAAAAf/////gAAAAAAcAAAAAABgAAAD8AAABwAAAAAAAAP//+AAAEAAAAAH/////8AAAAAAHAAAAAAAcAAAAfAAAAcAAAAAAAAAf/gAAAAAAAAAD//////AAAAAAA4AAAAAADgAAADwAAAHAAAAAAAAAD/gAAAAAAAAAB//////4AAAAAAAAAAAAAAYAAAA+AAABwAAAAAAAAAfwAAAAAAAAAAf/////+AAAAAAAAAAAAAAHAAAAHgAAAcAAAAAAAAAH4AAAAAAAAAAP//////wAAAAAAAAAAAAAAwAAAB4AAAHAAAAAAAAAA8AAAAAAAAAAD//////8AAAAAAAAAAAAAAOAAAAOAAABwAAAAAAAAAPAAAAAAAAAAB///////gAAAAAAAAAAAAABwAAADgAAAcAAAAAAAAADgAAAAAAAAAAf//////8AAAIAAAAAAAAAAMAAAAYAAAHAAAAAAAAAA4AAAAAAAAAAP///////AAABAAAAAAAAAADgAAAGAAABgAAAAAAAAAMAAAAAAAAAAD///////4AAAYAAAAAAgAAAYAAAAgAAAYAAAAAAAAADAAAAAAAAAAB///////+AAACAAAAAAIAAAHAAAAAAAAGAAAAAAAAABwAAAAAAAAAA////////wAAAwAAAAADAAAA4AAAAAAABgAAAAAAAAAcAAAAAAAAAAP///////+AAAGAAAAAAYAAAGAAAAAAAAYAAAAAAAAAGAAAAAAAAAAH////////gAABwAAAAAGAAABwAAAAAAAGAAAAAAAAABgAAAAAAAAAB////////8AAAOAAAAABwAAAMAAAAAAABgAAAAAAAAA4AAAAAAAAAA/////////AAADwAAAAAeAAABgAAAAAAAYAAAAAAAAAMAAAAAAAAAAP////////4AAAeAAAAADgAAAcAAAAAAAGAAAAAAAAADAAAAAAAAAAH////////+AAAHgAAAAA8AAADAAAAAAABgAAAAAAAAAwAAAAAAAAAH/////////wAAA8AAAAAPgAAA4AAAAAAAYAAAAAAAAAcAAAAAAAAAP/////////+AAAPgAAAAB4AAAGAAAAAAAGAAAD/gAAAGAAAAf///////////////gAAB8AAAAAfAAAAwAAAAAABgAAA/8AAABgAAAP///////////////8AAAPgAAAAHwAAAOAAAAAAAYAAAP/AAAAYAAAH////////////////AAAD8AAAAB+AAABgAAAAAAGAAAD/wAAAMAAAB/wAAAA//////////4AAAfgAAAAPwAAAcAAAAAABgAAA/8AAADAAAA/4AAAAf/////////+AAAH8AAAAD8AAADgAAAAAAYAAAP/AAAAwAAAP+AAAAH//////////wAAA/AAAAA/gAAAYAAAAAAGAAAD/gAAAcAAAD/AAAAD//////////+AAAP4AAAAP8AAAHAAAAAABgAAA/4AAAGAAAA/gAAAA///////////gAAB/AAAAB/AAAAwAAAAAAYAAAAAAAABgAAAAAAAAAf//////////8AAAf4AAAAf4AAAGAAAAAAGAAAAAAAAAYAAAAAAAAAP///////////AAAD/AAAAH/AAABwAAAAABgAAAAAAAAMAAAAAAAAAD///////////4AAA/4AAAB/wAAAMAAAAAAYAAAAAAAADAAAAAAAAAB///////////+AAAH/AAAAP+AAADgAAAAAGAAAAAAAAAwAAAAAAAAAf///////////wAAB/4AAAD/wAAAYAAAAABgAAAAAAAAMAAAAAAAAAP///////////+AAAP+AAAA/8AAADAAAAAAYAAAAAAAAGAAAAAAAAAD////////////gAAD/wAAAH/gAAA4AAAAAGAAAAAAAABgAAAAAAAAB////////////8AAAf+AAAB/8AAAGAAAAABgAAAAAAAAYAAAAAAAAAf////////////AAAH/wAAAf/AAABwAAAAAYAAAAAAAAMAAAAAAAAAP////////////4AAA/+AAAH/4AAAMAAAAAGAAAAAAAADAAAAAAAAAD/////////////AAAH/wAAA//AAABgAAAABgAAAAAAAAwAAAAAAAAB/////////////wAAB/+AAAP/wAAAcAAAAAYAAAAAAAAcAAAAAAAAA/////////////+AAAP/gAAD/+AAADAAAAAGAAAAAAAAGAAAAAAAAAP/////////////gAAD/8AAA//wAAAYAAAABgAAAAAAADgAAAAAAAAH/////////////8AAAf/gAAH/8AAAHAAAAAYAAAAAAAA8AAAAAAAAB//////////////AAAH/8AAB//gAAAwAAAAOAAAAAAAAfAAAAAAAAA//////////////4AAA//gAAf/4AAAOAAAADgAAAAAAAf4AAAAAAAAf//////////////AAAP/8AAH//AAABgAAAA4AAAAAAAP/AAAAAAAAf///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////+AAAAAAAAA/wAAAAAAAP/+AAAA4AAAB/8AAAAH///////////////+AAAAAAAAAB+AAAAAAAD//gAAAOAAAA//AAAAD////////////////AAAAAAAAAAPgAAAAAAAf/4AAADgAAAP/gAAAB////////////////wAAAAAAAAAB4AAAAAAAH/+AAAAwAAAD/wAAAA////////////////8AAAAAAAAAAOAAAAgAAB//gAAAcAAAB/4AAAAf////////////////AAAAAAAAAADgAAAIAAAf/4AAAHAAAAf8AAAAP////////////////wAAAAAAAAAAcAAABAAAD/+AAABwAAAH/AAAAH////////////////8AAAAAAAAAAHAAAAQAAA//gAAAcAAAD/gAAAD/////////////////gAAAAAAAAABwAAAGAAAP/4AAAGAAAA/wAAAB/////////////////4AAAAAAAAAAcAAABgAAB/+AAADgAAAP4AAAA//////////////////AAAAAAAAAADgAAAYAAAf/gAAA4AAAH+AAAAf/////////////////wAAAAAAAAAA4AAAHAAAH/4AAAOAAAB/AAAAH/////////////////+AAAAAAAAAAOAAABwAAAAEAAADAAAAfgAAAD//////////////////gAAAAAAAAADgAAAcAAAAAAAAAwAAAPwAAAB//////////////////8AAAAAAAAAAcAAAHgAAAAAAAAcAAAD8AAAA///////////////////gAAAAAAAAAHAAAB4AAAAAAAAHAAAA+AAAAf//////////////////4AAAAAAAAABwAAAPAAAAAAAABwAAAfAAAAP///////////////////AAAAAAAAAAcAAADwAAAAAAAAYAAAHgAAAH///////////////////wAAAAAAAAADAAAA8AAAAAAAAGAAAD4AAAD///////////////////+AAAAH+AAAA4AAAPgAAAAAAADgAAA8AAAB////////////////////gAAAB/wAAAOAAAD4AAAAAAAA4AAAOAAAA////////////////////8AAAA/8AAADgAAA/AAAAAAAAMAAAHAAAAf////////////////////gAAAP/gAAA4AAAPwAAAAAAADAAABwAAAH////////////////////4AAAB/4AAAHAAAD8AAAAAAABwAAAYAAAD/////////////////////AAAAf/AAABwAAA/gAAAAAAAcAAAMAAAB///////////////////////////wAAAcAAAP4AAAAAAAGAAACAAAA///////////////////////////8AAAHAAAB+AAAPwAABgAAAAAAAf///////////////////////////gAAAwAAAfwAAD8AAAYAAAAAAAP///////////////////////////wAAAOAAAH8AAA/AAAOAAAAAAAB///////////////////////////8AAADgAAB/gAAPgAADgAAAAAAAP//////////////////////+AAAAAAAA4AAAf4AAB4AAAwAAAAAAAA//////////////////////+AAAAAAAAGAAAH+AAAeAAAMAAAAAAAAH//////////////////////AAAAAAAABwAAB/wAAHgAADAAAAAAAAB//////////////////////gAAAAAAAAcAAAf8AAB4AABwAAAAAAAAP/////////////////////4AAAAAAAAHAAAH/gAAOAAAYAAAAAAAAD/////////////////////+AAAAAAAAAwAAB/4AADgAAGAAAAAAAAB//////////////////////gAAAAAAAAOAAAP+AAA4AABgAAAAAAAAf/////////////////////8AAAAAAAADgAAD/wAAGAAA4AAAAAAAAP//////////////////////AAAAAAAAA4AAA/8AABgAAOAAAAAAAAD//////////////////////wAAAAAAAAGAAAP/gAAAAADAAAAAAAAB//////////////////////+AAAAAAAABgAAD/4AAAAAAwAAAAAAAAf//////////////////////wAAAAAAAAcAAA/+AAAAAAMAAAAAAAAP//////////////////////8AAAAAAAAHAAAP/wAAAAAHAAAAAAAAD///////////////////////gAAAAAAABwAAD/8AAAAABgAAAAAAAB///////////////////////4AAAAAAAA8AAA//AAAAAAYAAAAAAAA////////////////////////AAAAAAAAfgAAP/4AAAAAGAAAAAAAAP///////////////////////wAAAH////4AAD/+AAAAABgAAH8AAAH///////////////////////+AAAD////+AAAf/wAAAAA4AAD/AAAB////////////////////////wAAA/////gAAH/8AAAAAMAAA/wAAA////////////////////////8AAAP////4AAB//AAAAADAAAP8AAAP////////////////////////gAAB/////AAAf/4AAAAAwAAH+AAAH////////////////////////4AAAH////wAAH/+AAAAAcAAB/AAAB/////////////////////////AAAAAAAAAAAAABwAAAAGAAAAAAAA/////////////////////////wAAAAAAAAAAAAAMAAAABgAAAAAAAf////////////////////////+AAAAAAAAAAAAADAAAAAYAAAAAAAH/////////////////////////wAAAAAAAAAAAAA4AAAAGAAAAAAAD/////////////////////////8AAAAAAAAAAAAAGAAAADgAAAAAAA//////////////////////////gAAAAAAAAAAAABgAAAAwAAAAAAAf/////////////////////////4AAAAAAAAAAAAAMAAAAMAAAAAAAH//////////////////////////AAAAAAAAAAAAADAAAADAAAAAAAD//////////////////////////4AAAAAAAAAAAAA4AAAAwAAAAAAA//////////////////////////+AAAAAAAAAAAAAGAAAAYAAAAAAAf//////////////////////////wAAAAAAAAAAAABgAAAGAAAAAAAP//////////////////////////+AAAAAAAAAAAAAcAAABgAAAAAAD///////////////////////////wAAAAAAAAAAAADAAAAYAAAAAAB////////////////////////////AAAAAAAAAAAAA4AAAEAAAAAAB////////////////////////////+AAAAAAAAAAAAGAAADAAAAAAD/////////////8=", PrinterPrintType.IMAGE.value);
            }
        });

        feedLinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedLinesEditText.getText() != null && feedLinesEditText.getText().length() > 0) {
                    callback.performFeedLine(Integer.parseInt(feedLinesEditText.getText().toString()));
                }
            }
        });

        printReceiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printReceiptButton.setEnabled(false);
                printReceiptButton.setClickable(false);

                callback.printFullReceipt();

                printReceiptButton.setEnabled(true);
                printReceiptButton.setClickable(true);
            }
        });

        callback.onAttach(this);

        return view;
    }

    public void setPrintCallback(Callback.PrintTabCallback printTabCallback) {
        this.callback = printTabCallback;
    }

}