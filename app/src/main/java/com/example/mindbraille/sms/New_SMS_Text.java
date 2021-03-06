package com.example.mindbraille.sms;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mindbraille.R;
import com.example.mindbraille.globals.GlobalClass;

import static java.lang.Thread.sleep;

public class New_SMS_Text extends AppCompatActivity {

    boolean running = true;
    final Handler handler = new Handler();
    Thread thread;
    int selector =0;
    TextView textView;
    String Number = "";
    boolean isCapsOn=false;

    final Handler col_handler = new Handler();
    Thread col_thread;
    boolean col_running=true;
    Button test;
    Button test2;
    int row_selector=0;
    int col_selector=0;
    Button[][] kb_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__s_m_s__text);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.mindbraillekb,null);
        RelativeLayout smsrl = findViewById(R.id.relativelayout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //params.addRule(RelativeLayout.BELOW, R.id.new_sms_final_back_btn);
        view.setLayoutParams(params);
        smsrl.addView(view);

        test = findViewById(R.id.kbdone);
        test2 = findViewById(R.id.kbx);

        textView = findViewById(R.id.new_sms_text_box);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Number = extras.getString("Number");
        }
        requestperms();

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });

        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.append(test2.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        thread = new Thread(new Runnable() {
            public void run() {

                while (running) {
                    try {
                        sleep((long) (1000 - (((GlobalClass) getApplication()).getConcentrationValue())));
                        handler.post(updateRunner);
                    }
                    catch (InterruptedException e){
                        running=false;
                    }

                }
            }
        });

        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        thread.interrupt();
    }

    final Runnable colRunner = new Runnable() {

        public void run () {
            if(col_selector> kb_buttons[row_selector].length-1)
            {
                col_selector=0;
            }

            if(row_selector> kb_buttons.length-1) row_selector = 0;

            for(Button i : kb_buttons[row_selector])
            {
                i.setEnabled(false);
            }

            kb_buttons[row_selector][col_selector].setEnabled(true);

            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>50))
            {
                switch (kb_buttons[row_selector][col_selector].getId())
                {
                    case R.id.new_sms_final_back_btn:
                        finish();
                        //sendSMS();
                        break;
                    case R.id.kb1:
                        //finish();
                        textView.append("0");
                        break;
                    case R.id.kb2:
                        textView.append("1");
                        break;
                    case R.id.kb3:
                        textView.append("2");
                        break;
                    case R.id.kb4:
                        textView.append("3");
                        break;
                    case R.id.kb5:
                        textView.append("4");
                        break;
                    case R.id.kb6:
                        textView.append("5");
                        break;
                    case R.id.kb7:
                        textView.append("6");
                        break;
                    case R.id.kb8:
                        textView.append("7");
                        break;
                    case R.id.kb9:
                        textView.append("8");
                        break;
                    case R.id.kb0:
                        textView.append("9");
                        break;
                    case R.id.kbq:
                        //textView.append("0");
                        if(isCapsOn) textView.append("P");
                        else textView.append("p");
                        break;
                    case R.id.kbw:
                        if(isCapsOn) textView.append("Q");
                        else textView.append("q");
                        break;
                    case R.id.kbe:
                        if(isCapsOn) textView.append("W");
                        else textView.append("w");
                        break;
                    case R.id.kbr:
                        if(isCapsOn) textView.append("E");
                        else textView.append("e");
                        break;
                    case R.id.kbt:
                        if(isCapsOn) textView.append("R");
                        else textView.append("r");
                        break;
                    case R.id.kby:
                        if(isCapsOn) textView.append("T");
                        else textView.append("t");
                        break;

                    case R.id.kbu:
                        if(isCapsOn) textView.append("Y");
                        else textView.append("y");
                        break;
                    case R.id.kbi:
                        if(isCapsOn) textView.append("U");
                        else textView.append("u");
                        break;
                    case R.id.kbo:
                        if(isCapsOn) textView.append("I");
                        else textView.append("i");
                        break;
                    case R.id.kbp:
                        if(isCapsOn) textView.append("O");
                        else textView.append("o");
                        break;
                    case R.id.kba:
                        textView.append("#");
                        break;
                    case R.id.kbs:
                        if(isCapsOn) textView.append("A");
                        else textView.append("a");
                        break;
                    case R.id.kbd:
                        if(isCapsOn) textView.append("S");
                        else textView.append("s");
                        break;
                    case R.id.kbf:
                        if(isCapsOn) textView.append("D");
                        else textView.append("d");
                        break;
                    case R.id.kbg:
                        if(isCapsOn) textView.append("F");
                        else textView.append("f");
                        break;
                    case R.id.kbh:
                        if(isCapsOn) textView.append("G");
                        else textView.append("g");
                        break;
                    case R.id.kbj:
                        if(isCapsOn) textView.append("H");
                        else textView.append("h");
                        break;
                    case R.id.kbk:
                        if(isCapsOn) textView.append("J");
                        else textView.append("j");
                        break;
                    case R.id.kbl:
                        if(isCapsOn) textView.append("K");
                        else textView.append("k");
                        break;
                    case R.id.kbhash:
                        if(isCapsOn) textView.append("L");
                        else textView.append("l");
                        break;
                    case R.id.kbcapslock:
                        textView.append("?");
                        break;
                    case R.id.kbz:
                        if(isCapsOn) isCapsOn=false;
                        else isCapsOn=true;
                        break;
                    case R.id.kbx:
                        if(isCapsOn) textView.append("Z");
                        else textView.append("z");
                        break;
                    case R.id.kbc:
                        if(isCapsOn) textView.append("X");
                        else textView.append("x");
                        break;
                    case R.id.kbv:
                        if(isCapsOn) textView.append("C");
                        else textView.append("c");
                        break;
                    case R.id.kbb:
                        if(isCapsOn) textView.append("V");
                        else textView.append("v");
                        break;
                    case R.id.kbn:
                        if(isCapsOn) textView.append("B");
                        else textView.append("b");
                        break;
                    case R.id.kbm:
                        if(isCapsOn) textView.append("N");
                        else textView.append("n");
                        break;
                    case R.id.kbdot:
                        if(isCapsOn) textView.append("M");
                        else textView.append("m");
                        break;
                    case R.id.kbquestion:
                        textView.append(".");
                        break;
                    case R.id.kbstar:
                        textView.append("%");
                        break;
                    case R.id.kbbracketopen:
                        textView.append("*");
                        break;
                    case R.id.kbbracketclose:
                        textView.append("(");
                        break;
                    case R.id.kband:
                        textView.append(")");
                        break;
                    case R.id.kbbackslash:
                        textView.append("&");
                        break;
                    case R.id.kbquotation:
                        textView.append("\\");
                        break;
                    case R.id.kbsinglequot:
                        textView.append("\"");
                        break;
                    case R.id.kbsemicolon:
                        textView.append("'");
                        break;
                    case R.id.kbequal:
                        textView.append(";");
                        break;
                    case R.id.kbpercent:
                        textView.append("=");
                        break;
                    case R.id.kbcomma:
                        textView.append("|");
                        break;
                    case R.id.kbat:
                        textView.append(",");
                        break;
                    case R.id.kbexclamation:
                        textView.append("@");
                        break;
                    case R.id.kbcolon:
                        textView.append("!");
                        break;
                    case R.id.kbforwardslash:
                        textView.append(":");
                        break;
                    case R.id.kbplus:
                        textView.append("/");
                        break;
                    case R.id.kbhyphen:
                        textView.append("+");
                        break;
                    case R.id.kbunderscore:
                        textView.append("-");
                        break;
                    case R.id.kbdollar:
                        textView.append("_");
                        break;
                    case R.id.kbstraightline:
                        textView.append("$");
                        break;
                    case R.id.kbdelete:
                        sendSMS();
                        break;
                    case R.id.kbspace:
                        textView.setText("");
                        break;
                    case R.id.kbdone:
                        textView.append(" ");
                        break;
                }

                if(isCapsOn)
                {
                    for(int j=1;j<kb_buttons.length-1;j++)
                    {
                        for(Button i: kb_buttons[j])
                        {
                            i.setAllCaps(true);

                        }
                    }

                }
                else
                {
                    for(int j=1;j<kb_buttons.length-1;j++)
                    {
                        for(Button i: kb_buttons[j])
                        {
                            i.setAllCaps(false);

                        }
                    }

                }

                ((GlobalClass) getApplication()).setBlinked(false);

                col_selector=0;
                row_selector++;

                thread = new Thread(new Runnable() {
                    public void run() {

                        while (running) {
                            try {
                                sleep((long) (1000 - (((GlobalClass) getApplication()).getConcentrationValue())));
                                handler.post(updateRunner);

                            }
                            catch (InterruptedException e){
                                running=false;
                            }

                        }
                    }
                });
                running=true;

                thread.start();
                col_thread.interrupt();
            }

            col_selector++;


        }
    };

    final Runnable updateRunner = new Runnable() {

        public void run() {

            kb_buttons = new Button[][]{{
                    findViewById(R.id.new_sms_final_back_btn)},
                    {findViewById(R.id.kb1),
                            findViewById(R.id.kb2),
                            findViewById(R.id.kb3),
                            findViewById(R.id.kb4),
                            findViewById(R.id.kb5),
                            findViewById(R.id.kb6),
                            findViewById(R.id.kb7),
                            findViewById(R.id.kb8),
                            findViewById(R.id.kb9),
                            findViewById(R.id.kb0)},
                    {findViewById(R.id.kbq),
                            findViewById(R.id.kbw),
                            findViewById(R.id.kbe),
                            findViewById(R.id.kbr),
                            findViewById(R.id.kbt),
                            findViewById(R.id.kby),
                            findViewById(R.id.kbu),
                            findViewById(R.id.kbi),
                            findViewById(R.id.kbo),
                            findViewById(R.id.kbp)},
                    {findViewById(R.id.kba),
                            findViewById(R.id.kbs),
                            findViewById(R.id.kbd),
                            findViewById(R.id.kbf),
                            findViewById(R.id.kbg),
                            findViewById(R.id.kbh),
                            findViewById(R.id.kbj),
                            findViewById(R.id.kbk),
                            findViewById(R.id.kbl),
                            findViewById(R.id.kbhash)},
                    {findViewById(R.id.kbcapslock),
                            findViewById(R.id.kbz),
                            findViewById(R.id.kbx),
                            findViewById(R.id.kbc),
                            findViewById(R.id.kbv),
                            findViewById(R.id.kbb),
                            findViewById(R.id.kbn),
                            findViewById(R.id.kbm),
                            findViewById(R.id.kbdot),
                            findViewById(R.id.kbquestion)},
                    {findViewById(R.id.kbstar),
                            findViewById(R.id.kbbracketopen),
                            findViewById(R.id.kbbracketclose),
                            findViewById(R.id.kband),
                            findViewById(R.id.kbbackslash),
                            findViewById(R.id.kbquotation),
                            findViewById(R.id.kbsinglequot),
                            findViewById(R.id.kbsemicolon),
                            findViewById(R.id.kbequal),
                            findViewById(R.id.kbpercent)},
                    {findViewById(R.id.kbcomma),
                            findViewById(R.id.kbat),
                            findViewById(R.id.kbexclamation),
                            findViewById(R.id.kbcolon),
                            findViewById(R.id.kbforwardslash),
                            findViewById(R.id.kbplus),
                            findViewById(R.id.kbhyphen),
                            findViewById(R.id.kbunderscore),
                            findViewById(R.id.kbdollar),
                            findViewById(R.id.kbstraightline)},
                    {findViewById(R.id.kbdelete),
                            findViewById(R.id.kbspace),
                            findViewById(R.id.kbdone)}
            };

            if(row_selector>kb_buttons.length-1 || row_selector<0)
            {row_selector=0;}

            for(int j=0;j<kb_buttons.length;j++)
            {
                for(Button i: kb_buttons[j])
                {
                    i.setEnabled(false);

                }
            }

            for(int i=0;i<kb_buttons[row_selector].length;i++)
            {
                kb_buttons[row_selector][i].setEnabled(true);
            }

            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>70))
            {
                ((GlobalClass) getApplication()).setBlinked(false);
                col_selector=0;
                row_selector--;
                col_running=true;
                col_thread = new Thread(new Runnable() {
                    public void run() {

                        while (col_running) {
                            try {
                                sleep((long) (1000 - (((GlobalClass) getApplication()).getConcentrationValue())));
                                col_handler.post(colRunner);

                            }
                            catch (InterruptedException e){
                                col_running=false;
                            }

                        }
                    }
                });
                col_thread.start();
                thread.interrupt();
            }
            /*
            if(((GlobalClass) getApplication()).getBlinked() && (((GlobalClass) getApplication()).getBlinkValue()>70))
            {
                switch (kb_buttons[selector].getId())
                {
                    case R.id.new_sms_final_back_btn:
                        sendSMS();
                        break;
                    case R.id.kb1:
                        finish();
                        break;
                    case R.id.kb2:
                        textView.append("1");
                        break;
                    case R.id.kb3:
                        textView.append("2");
                        break;
                    case R.id.kb4:
                        textView.append("3");
                        break;
                    case R.id.kb5:
                        textView.append("4");
                        break;
                    case R.id.kb6:
                        textView.append("5");
                        break;
                    case R.id.kb7:
                        textView.append("6");
                        break;
                    case R.id.kb8:
                        textView.append("7");
                        break;
                    case R.id.kb9:
                        textView.append("8");
                        break;
                    case R.id.kb0:
                        textView.append("9");
                        break;
                    case R.id.kbq:
                        textView.append("0");
                        break;
                    case R.id.kbw:
                        if(isCapsOn) textView.append("Q");
                        else textView.append("q");
                        break;
                    case R.id.kbe:
                        if(isCapsOn) textView.append("W");
                        else textView.append("w");
                        break;
                    case R.id.kbr:
                        if(isCapsOn) textView.append("E");
                        else textView.append("e");
                        break;
                    case R.id.kbt:
                        if(isCapsOn) textView.append("R");
                        else textView.append("r");
                        break;
                    case R.id.kby:
                        if(isCapsOn) textView.append("T");
                        else textView.append("t");
                        break;

                    case R.id.kbu:
                        if(isCapsOn) textView.append("Y");
                        else textView.append("y");
                        break;
                    case R.id.kbi:
                        if(isCapsOn) textView.append("U");
                        else textView.append("u");
                        break;
                    case R.id.kbo:
                        if(isCapsOn) textView.append("I");
                        else textView.append("i");
                        break;
                    case R.id.kbp:
                        if(isCapsOn) textView.append("O");
                        else textView.append("o");
                        break;
                    case R.id.kba:
                        if(isCapsOn) textView.append("P");
                        else textView.append("p");
                        break;
                    case R.id.kbs:
                        if(isCapsOn) textView.append("A");
                        else textView.append("a");
                        break;
                    case R.id.kbd:
                        if(isCapsOn) textView.append("S");
                        else textView.append("s");
                        break;
                    case R.id.kbf:
                        if(isCapsOn) textView.append("D");
                        else textView.append("d");
                        break;
                    case R.id.kbg:
                        if(isCapsOn) textView.append("F");
                        else textView.append("f");
                        break;
                    case R.id.kbh:
                        if(isCapsOn) textView.append("G");
                        else textView.append("g");
                        break;
                    case R.id.kbj:
                        if(isCapsOn) textView.append("H");
                        else textView.append("h");
                        break;
                    case R.id.kbk:
                        if(isCapsOn) textView.append("J");
                        else textView.append("j");
                        break;
                    case R.id.kbl:
                        if(isCapsOn) textView.append("K");
                        else textView.append("k");
                        break;
                    case R.id.kbhash:
                        if(isCapsOn) textView.append("L");
                        else textView.append("l");
                        break;
                    case R.id.kbcapslock:
                        textView.append("#");
                        break;
                    case R.id.kbz:
                        if(isCapsOn) isCapsOn=false;
                        else isCapsOn=true;
                        break;
                    case R.id.kbx:
                        if(isCapsOn) textView.append("Z");
                        else textView.append("z");
                        break;
                    case R.id.kbc:
                        if(isCapsOn) textView.append("X");
                        else textView.append("x");
                        break;
                    case R.id.kbv:
                        if(isCapsOn) textView.append("C");
                        else textView.append("c");
                        break;
                    case R.id.kbb:
                        if(isCapsOn) textView.append("V");
                        else textView.append("v");
                        break;
                    case R.id.kbn:
                        if(isCapsOn) textView.append("B");
                        else textView.append("b");
                        break;
                    case R.id.kbm:
                        if(isCapsOn) textView.append("N");
                        else textView.append("n");
                        break;
                    case R.id.kbdot:
                        if(isCapsOn) textView.append("M");
                        else textView.append("m");
                        break;
                    case R.id.kbquestion:
                        textView.append(".");
                        break;
                    case R.id.kbstar:
                        textView.append("?");
                        break;
                    case R.id.kbbracketopen:
                        textView.append("*");
                        break;
                    case R.id.kbbracketclose:
                        textView.append("(");
                        break;
                    case R.id.kband:
                        textView.append(")");
                        break;
                    case R.id.kbbackslash:
                        textView.append("&");
                        break;
                    case R.id.kbquotation:
                        textView.append("\\");
                        break;
                    case R.id.kbsinglequot:
                        textView.append("\"");
                        break;
                    case R.id.kbsemicolon:
                        textView.append("'");
                        break;
                    case R.id.kbequal:
                        textView.append(";");
                        break;
                    case R.id.kbpercent:
                        textView.append("=");
                        break;
                    case R.id.kbcomma:
                        textView.append("%");
                        break;
                    case R.id.kbat:
                        textView.append(",");
                        break;
                    case R.id.kbexclamation:
                        textView.append("@");
                        break;
                    case R.id.kbcolon:
                        textView.append("!");
                        break;
                    case R.id.kbforwardslash:
                        textView.append(":");
                        break;
                    case R.id.kbplus:
                        textView.append("/");
                        break;
                    case R.id.kbhyphen:
                        textView.append("+");
                        break;
                    case R.id.kbunderscore:
                        textView.append("-");
                        break;
                    case R.id.kbdollar:
                        textView.append("_");
                        break;
                    case R.id.kbstraightline:
                        textView.append("$");
                        break;
                    case R.id.kbdelete:
                        textView.append("|");
                        break;
                    case R.id.kbspace:
                        textView.setText("");
                        break;
                    case R.id.kbdone:
                        textView.append(" ");
                        break;
                }
            }*/

            if(!running) {
                return;
            }

            row_selector++;
        }
    };

    void requestperms()
    {
        //ActivityCompat.requestPermissions(New_SMS_Text.this, new String[]{Manifest.permission.SEND_SMS},1);
    }
    void sendSMS()
    {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, 1);
            }
        }


        String Message = textView.getText().toString();
        Log.d("This is Number", Number);
        Log.d("This is Message", Message);
        if(!Number.equals("") && !Message.equals(""))
        {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(Number,null,Message,null,null);
                Toast.makeText(getApplicationContext(), "SMS sent successfully",Toast.LENGTH_SHORT).show();
            } catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to send message",Toast.LENGTH_SHORT).show();

            }

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please enter a message first!", Toast.LENGTH_SHORT).show();
        }


    }



    public void buttonsfunc(View view)
    {
        switch(view.getId())
        {
            case R.id.new_sms_final_back_btn:
                finish();
                break;
            case R.id.sms_buttonOk:
                break;
            default:
                Button temp = (Button) view;
                textView.append(temp.getText());
                break;
        }
    }
}