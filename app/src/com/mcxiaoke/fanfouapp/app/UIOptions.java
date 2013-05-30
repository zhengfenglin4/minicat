package com.mcxiaoke.fanfouapp.app;

import android.os.Bundle;
import com.mcxiaoke.fanfouapp.R;
import com.mcxiaoke.fanfouapp.fragment.OptionFragment;

/**
 * Project: fanfouapp
 * Package: com.mcxiaoke.fanfouapp.app
 * User: mcxiaoke
 * Date: 13-5-26
 * Time: 下午8:05
 */
public class UIOptions extends UIBaseSupport {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_container);
        setProgressBarIndeterminateVisibility(false);
        getActionBar().setTitle("设置");
        getFragmentManager().beginTransaction().replace(R.id.content, OptionFragment.newInstance()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
