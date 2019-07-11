package cn.ident.nas.tester;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import cn.ident.nas.tester.util.NfcVUtil;

public class ConfirmActivity extends Activity {
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private TextView mText,aText;
    private EditText E1,E2;
    private NfcV nfcv = null;
    private StringBuilder uid3;
    private String uid1,uid2;
    private int a;
    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_confirm);

        mText = (TextView) findViewById(R.id.TextView1);
        aText = (TextView) findViewById(R.id.tvvvv1);
        mText.setText("请放上标签");

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter nfcv = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        mFilters = new IntentFilter[]{nfcv};
        mTechLists = new String[][]{new String[]{NfcV.class.getName()}};
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }

    @Override
    public void onNewIntent(Intent intent) {
        //   Log.d("intent", "intent action="+intent.getAction());
        if (intent.getAction().trim().equals("android.nfc.action.TECH_DISCOVERED")) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//	    Log.d("intent", "intent tag="+NfcAdapter.EXTRA_TAG+", "+NfcAdapter.EXTRA_ID);
            nfcv = NfcV.get(tagFromIntent);
            try {
                if (nfcv.isConnected())
                    nfcv.close();
                nfcv.connect();
                NfcVUtil nNfcVUtil = new NfcVUtil(nfcv);
                //nNfcVUtil.readBlocks(7,2);
                //uid2=nNfcVUtil.getOneBlockSize();
                //uid2=nNfcVUtil.getBlockNumber();
                E1=(EditText)findViewById(R.id.E1);
                E2=(EditText)findViewById(R.id.E2);
                uid1 = nNfcVUtil.readOneBlock(Integer.parseInt(E1.getText().toString()));
                int b=Integer.parseInt(E2.getText().toString());
                a = Integer.parseInt(uid1,16);
                if(a==b){
                    Toast.makeText(ConfirmActivity.this,"密码正确",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ConfirmActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                }
                //uid1 = nNfcVUtil.getUID();
                aText.setText("块区数据"+uid1);
                mText.setText("块区数据转换后："+a);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
