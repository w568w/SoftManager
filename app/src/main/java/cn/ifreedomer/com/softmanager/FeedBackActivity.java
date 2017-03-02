package cn.ifreedomer.com.softmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.mail.MailThread;

public class FeedBackActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.sender_et)
    EditText senderEt;
    @InjectView(R.id.topic_tv)
    TextView topicTv;
    @InjectView(R.id.topic_et)
    EditText topicEt;
    @InjectView(R.id.content_tv)
    TextView contentTv;
    @InjectView(R.id.content_et)
    EditText contentEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.inject(this);
        toolbar.setTitle(R.string.feedback);
        toolbar.inflateMenu(R.menu.feedback_menu);
        toolbar.getMenu().findItem(R.id.f_item_send).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new MailThread(FeedBackActivity.this,topicEt.getText().toString(),contentEt.getText().toString(),senderEt.getText().toString()).start();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }
}
