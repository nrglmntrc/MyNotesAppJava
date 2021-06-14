package com.nurgulmantarci.mynotesappjava.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nurgulmantarci.mynotesappjava.R;
import com.nurgulmantarci.mynotesappjava.adapters.TabLayoutFragmentAdapter;
import com.nurgulmantarci.mynotesappjava.databinding.ActivityMainBinding;
import com.nurgulmantarci.mynotesappjava.fragments.DoingFragment;
import com.nurgulmantarci.mynotesappjava.fragments.DoneFragment;
import com.nurgulmantarci.mynotesappjava.fragments.TodoFragment;
import com.nurgulmantarci.mynotesappjava.helper.UserInformationHelper;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setPager();

    }

    public void setPager(){

        dataBinding.pager.setOffscreenPageLimit(5);
        dataBinding.pager.setClipToPadding(false);

        TabLayoutFragmentAdapter pagerAdapter = new TabLayoutFragmentAdapter(getSupportFragmentManager());

        pagerAdapter.addFrag(new TodoFragment(), "Yapılacak");
        pagerAdapter.addFrag(new DoingFragment(), "Yapılıyor");
        pagerAdapter.addFrag(new DoneFragment(), "Tamamlandı");

        dataBinding.pager.setAdapter(pagerAdapter);
        dataBinding.tabLayout.setupWithViewPager(dataBinding.pager);
        dataBinding.pager.setCurrentItem(0);

        for(int i=0; i <dataBinding.tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) dataBinding.tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 50, 0);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserInformationHelper.deleteUserEmail(this);
    }
}