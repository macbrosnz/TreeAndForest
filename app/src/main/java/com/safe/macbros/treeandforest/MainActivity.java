package com.safe.macbros.treeandforest;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.safe.macbros.treeandforest.Activities.audits.AuditsMain;
import com.safe.macbros.treeandforest.Activities.emergency.AccidentTab;
import com.safe.macbros.treeandforest.Activities.emergency.EmergencyMain;
import com.safe.macbros.treeandforest.Activities.emergency.FireTab;
import com.safe.macbros.treeandforest.Activities.emergency.HazardousSubstance;
import com.safe.macbros.treeandforest.Activities.equipment.EquipmentMain;
import com.safe.macbros.treeandforest.Activities.hazards.HazardsMain;
import com.safe.macbros.treeandforest.Activities.incidents.IncidentsMain;
import com.safe.macbros.treeandforest.Activities.policies.PoliciesMain;
import com.safe.macbros.treeandforest.Activities.sites.SitesMain;
import com.safe.macbros.treeandforest.Activities.staff.StaffMain;
import com.safe.macbros.treeandforest.Activities.tailgate.TailgateMain;
import com.safe.macbros.treeandforest.Activities.tailgate.adapters.SectionsTailgateAdaptor;
import com.safe.macbros.treeandforest.Activities.tailgate.custom.TailgateHelper;
import com.safe.macbros.treeandforest.Activities.tailgate.tabs.DailyMeetings;
import com.safe.macbros.treeandforest.Activities.tailgate.tabs.MonthlyMeetings;
import com.safe.macbros.treeandforest.Activities.tasks.TasksMain;
import com.safe.macbros.treeandforest.custom.Methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Methods {

    private static final String TAG = "MainActivity";
    //widgets
    private Class fragmentHolder = null;
    private Fragment fragment = null;
    private static Toolbar toolbar;
    private FrameLayout fl;
    private TabLayout tabLayout;
    static DrawerLayout drawerLayout;
    TextView data;
    NavigationView navigationView;
    private ViewPager mViewPager;

    //vars
    TailgateHelper tHelper;
    SectionsTailgateAdaptor sectionsAdapter, mainAdapter;
    static Context context;
    private static WifiManager wifi;
    FragmentTransaction ft;
    HashMap<String, Object> message = new HashMap<>();

    //firestore
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tHelper = new TailgateHelper(getContext());
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        db.setFirestoreSettings(settings);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        setToolbar();

        drawerLayout.openDrawer(Gravity.LEFT);

        navItemOnClick();


    }


    public void initUI() {

        tabLayout = findViewById(R.id.main_tl);
        navigationView = findViewById(R.id.main_nv);
        drawerLayout = findViewById(R.id.main_dl);
        toolbar = findViewById(R.id.toolbar);
        fl = findViewById(R.id.main_fl);
        mViewPager = findViewById(R.id.container);

    }


    public void setToolbar() {
        toolbar.inflateMenu(R.menu.menu_main);
        MenuItem mItem = toolbar.getMenu().findItem(R.id.menu_ic_alert);
        tHelper.checkForAlerts(mItem);
        toolbar.setTitle("Tree & Forest");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    public void navItemtoClass(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_tailgate: {
                fragmentHolder = TailgateMain.class;
                break;
            }
            case R.id.nav_emergency: {
                fragmentHolder = EmergencyMain.class;

                break;
            }
            case R.id.nav_hazards: {
                fragmentHolder = HazardsMain.class;
                break;
            }
            case R.id.nav_incidents: {
                fragmentHolder = IncidentsMain.class;
                break;
            }
            case R.id.nav_block: {
                fragmentHolder = SitesMain.class;
                break;
            }
            case R.id.nav_staff: {
                fragmentHolder = StaffMain.class;
                break;
            }
            case R.id.nav_tasks: {
                fragmentHolder = TasksMain.class;
                break;
            }
            case R.id.nav_equipment: {
                fragmentHolder = EquipmentMain.class;
                break;
            }
            case R.id.nav_audits: {
                fragmentHolder = AuditsMain.class;
                break;
            }
            case R.id.nav_policies: {
                fragmentHolder = PoliciesMain.class;

                break;
            }
            default:
                break;
        }

        if (fragmentHolder != null)
            try {
                fragment = (Fragment) fragmentHolder.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

    }

    public boolean navItemOnClick() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                navItemtoClass(menuItem);
                if (fragment != null) {
                    {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.main_fl, fragment).commit();
                        menuItem.setChecked(true);
                        fl.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.GONE);
                        mViewPager.setVisibility(View.GONE);
                        if (menuItem.getItemId() == R.id.nav_tailgate) {
                            initTailgateTabs();
                        }
                        if (menuItem.getItemId() == R.id.nav_emergency) {
                            initEmergencyTabs();
                        }

                    }

                }

                drawerLayout.closeDrawers();

                return true;
            }
        });
        return true;
    }


    @Override
    public void sendFragMessage(Fragment frag, String tag, HashMap<String, Object> message) {

        fl.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        Log.d(TAG, "sendFragMessage: " + tag);
        Bundle args = new Bundle();
        args.putSerializable(tag, message);
        frag.setArguments(args);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_fl, frag).addToBackStack(tag).commit();

        if (tag == "TailgateMain") {
            initTailgateTabs();
        }

    }

    @Override
    public void sendDialogMessage(DialogFragment dialog, String tag, HashMap<String, Object> message) {

        Bundle args = new Bundle();
        args.putSerializable(tag, message);
        dialog.setArguments(args);
        ft = getSupportFragmentManager().beginTransaction();
        dialog.show(ft, tag);

    }

    @Override
    public void runTabLayout(List<Fragment> fragList, List<String> titles) {

      /*  fl.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        sectionsAdapter = new SectionsTailgateAdaptor(fm);
        sectionsAdapter.addFragmentList(fragList, titles);
        mViewPager.setAdapter(sectionsAdapter);
        tabLayout.setupWithViewPager(mViewPager);*/

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    public void initTailgateTabs() {
        fl.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);

        getSupportFragmentManager().getFragments().clear();
        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
            getSupportFragmentManager().popBackStackImmediate();
            Log.d(TAG, "initTailgateTabs: size of Fragments = " + getSupportFragmentManager().getFragments().size());
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fl, new TailgateMain()).commit();

        mainAdapter = new SectionsTailgateAdaptor(getSupportFragmentManager());

        List<Fragment> fragLista = new ArrayList<>();

        List<String> titlesa = new ArrayList<>();

        fragLista.add(new DailyMeetings());

        fragLista.add(new MonthlyMeetings());

        titlesa.add("Daily Tailgate");
        titlesa.add("Monthly Safety Meeting");
        mainAdapter.addFragmentList(fragLista, titlesa);
        mViewPager.setAdapter(mainAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void initEmergencyTabs(){
        toolbar.setTitle("Emergency");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getDrawerLayout().openDrawer(GravityCompat.START);
            }
        });
        getSupportFragmentManager().getFragments().clear();
        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
            getSupportFragmentManager().popBackStackImmediate();
            Log.d(TAG, "initTailgateTabs: size of Fragments = " + getSupportFragmentManager().getFragments().size());
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fl.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        sectionsAdapter = new SectionsTailgateAdaptor(getSupportFragmentManager());

        List<Fragment> fragListb = new ArrayList<>();
        List<String> titlesb = new ArrayList<>();

        fragListb.add(new AccidentTab());
        fragListb.add(new FireTab());
        fragListb.add(new HazardousSubstance());

        titlesb.add("ACCIDENT");
        titlesb.add("FIRE");
        titlesb.add("CHEMICAL");

        sectionsAdapter.addFragmentList(fragListb, titlesb);

        mViewPager.setAdapter(sectionsAdapter);

        tabLayout.setupWithViewPager(mViewPager);
    }
    public static Toolbar getToolbar() {
        return toolbar;
    }

    public static DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public static Context getContext() {
        return context;
    }

    public static FirebaseFirestore getFireDb() {
        return db;
    }

    public static WifiManager getWifi() {
        return wifi;
    }


}

