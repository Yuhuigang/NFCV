package cn.ident.nas.tester;

import java.io.IOException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;
import cn.ident.nas.tester.util.NfcVUtil;


public class ReadActivity extends Activity {
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private TextView mText,aText;
    private NfcV nfcv = null;
    private StringBuilder uid3;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_read);

        mText = (TextView) findViewById(R.id.TextView);
        aText = (TextView) findViewById(R.id.tvvvv);
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
                uid3 = new StringBuilder();
                for (int i=0;i<nNfcVUtil.getBlockNumber();i++){
                    uid3.append(nNfcVUtil.readOneBlock(i)+"\n");
                }
                //nNfcVUtil.readBlocks(7,2);
                //uid2=nNfcVUtil.getOneBlockSize();
                //uid2=nNfcVUtil.getBlockNumber();
                //uid1 = nNfcVUtil.readOneBlock(1);
                //uid1 = nNfcVUtil.getUID();
                aText.setText(uid3);
                mText.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
