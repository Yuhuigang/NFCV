package cn.ident.nas.tester;

import androidx.appcompat.app.AppCompatActivity;

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

public class PschangeActivity extends AppCompatActivity {
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private TextView mText,aText;
    private EditText E1,E2,E3;
    private NfcV nfcv = null;
    private StringBuilder uid3;
    private String uid1,uid2,c,s1,s2,s3,s4;
    private int a;
    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_pschange);

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
                E3=(EditText)findViewById(R.id.E3);
                uid1 = nNfcVUtil.readOneBlock(Integer.parseInt(E1.getText().toString()));
                int b=Integer.parseInt(E2.getText().toString());
                a = Integer.parseInt(uid1,16);
                c=E3.getText().toString();
                s1=c.substring(0,2);
                s2=c.substring(2,4);
                s3=c.substring(4,6);
                s4=c.substring(6,8);
                if (a==b) {
                    nNfcVUtil.writeBlock(Integer.parseInt(E1.getText().toString()), new byte[]{(byte) Integer.parseInt(s1),(byte) Integer.parseInt(s2),(byte) Integer.parseInt(s3),(byte) Integer.parseInt(s4)});
                    Toast.makeText(PschangeActivity.this,"密码修改完毕",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(PschangeActivity.this,"修改失败",Toast.LENGTH_LONG).show();
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
