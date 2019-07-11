package cn.ident.nas.tester;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import cn.ident.nas.tester.util.NfcVUtil;

public class WriteActivity extends Activity {
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private TextView mText, nText,aText;
    private String uid1;
    private int uid2;
    private int mCount = 0;
    private NfcV nfcv = null;
    private byte[] UID, UID_zan, uidShow;
    private StringBuilder stringbuilder1, uid3,uid4;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_write);

        mText = (TextView) findViewById(R.id.TextView);
        nText = (TextView) findViewById(R.id.textView);
        aText = (TextView) findViewById(R.id.textView2);
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
                uid4 = new StringBuilder();
                for (int i=0;i<nNfcVUtil.getBlockNumber();i++){
                    uid4.append(nNfcVUtil.readOneBlock(i)+"\n");
                }
                nNfcVUtil.writeBlock(5, new byte[]{13,13,13,13});
                uid3 = new StringBuilder();
                for (int i=0;i<nNfcVUtil.getBlockNumber();i++){
                    uid3.append(nNfcVUtil.readOneBlock(i)+"\n");
                }
                //uid2=nNfcVUtil.getOneBlockSize();
                //uid2=nNfcVUtil.getBlockNumber();
                //uid1 = nNfcVUtil.readOneBlock(1);
                //uid1 = nNfcVUtil.getUID();
                nText.setText("写数据前："+"\n"+uid4);
                aText.setText("写数据后："+"\n"+uid3);
                mText.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

