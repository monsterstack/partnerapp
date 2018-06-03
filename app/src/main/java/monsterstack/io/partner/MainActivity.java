package monsterstack.io.partner;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.api.RedirectHandler;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.User;
import monsterstack.io.api.service.RefreshTokenService;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.partner.adapter.MainPagerAdapter;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.menu.BackupActivity;
import monsterstack.io.partner.menu.BuyCurrencyActivity;
import monsterstack.io.partner.menu.ProfileActivity;
import monsterstack.io.partner.menu.SettingsActivity;
import monsterstack.io.partner.menu.WalletsActivity;
import monsterstack.io.partner.pager.NonSwipeableViewPager;
import monsterstack.io.partner.utils.NavigationUtils;

public class MainActivity extends BasicActivity {
    private static final int PROFILE_UPDATE_REQUEST_CODE = 0;

    private MainPagerAdapter mainPagerAdapter;
    private NonSwipeableViewPager viewPager;

    @BindView(R.id.bottom_navigation)
    BottomNavigationViewEx bottomNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    AvatarView avatar;

    View navHeader;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        if (null != getSupportActionBar())
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_view_headline_white_24dp);

        init();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        if(drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    protected void init() {
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                onItemSelected(item);
                return false;
            }
        });

        final UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());

        User user = sessionManager.getUserDetails();
        user.setAvatarUrl(monsterstack.io.partner.domain.User.avatars[0]);

        Boolean openDrawer = getIntent().getBooleanExtra("openDrawer", false);

        if(openDrawer)
            drawerLayout.openDrawer(Gravity.START);

        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Groups");
        NavigationView navView = findViewById(R.id.nav_view);

        UserSessionManager userSessionManager = new UserSessionManager(this);
        AuthenticatedUser authenticatedUser = userSessionManager.getUserDetails();
        navHeader = navView.getHeaderView(0);
        avatar = navHeader.findViewById(R.id.userImage);
        avatar.setUser(new monsterstack.io.avatarview.User(authenticatedUser.getFullName(),
                authenticatedUser.getAvatarUrl(), R.color.colorAccent));

        TextView profileFullName = navHeader.findViewById(R.id.profile_fullname);
        profileFullName.setText(authenticatedUser.getFullName());
        TextView email = navHeader.findViewById(R.id.profile_email);
        email.setText(authenticatedUser.getEmailAddress());
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileClicked(navHeader);
            }
        });

        Menu menu = navView.getMenu();

        // Order Matters
        this.registerMenuItemActions(menu);

        viewPager = findViewById(R.id.main_pager);
        viewPager.setCurrentItem(0);

        this.mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Fragment fragment = MainActivity.this.mainPagerAdapter.getItem(position);

                if (fragment.getArguments() != null) {
                    String title = (String) fragment.getArguments().get("title");

                    if (null != title) {
                        toolbar.setTitle(title);

                        if (title.equals("Groups"))
                            bottomNavigationView.setSelectedItemId(R.id.action_tabs_groups);
                        else if (title.equals("Friends"))
                            bottomNavigationView.setSelectedItemId(R.id.action_tabs_friends);
                        else if (title.equals("Wallets"))
                            bottomNavigationView.setSelectedItemId(R.id.action_tabs_wallets);
                        else if (title.equals("Messages"))
                            bottomNavigationView.setSelectedItemId(R.id.action_tabs_messages);
                    }
                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.app_name;
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        UserSessionManager sessionManager = new UserSessionManager(this);
        AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();
        avatar.setUser(new monsterstack.io.avatarview.User(authenticatedUser.getFullName(),
                authenticatedUser.getAvatarUrl(), R.color.colorAccent));
        avatar.refreshDrawableState();
    }

    public void onProfileClicked(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivityForResult(intent, PROFILE_UPDATE_REQUEST_CODE);
                overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        }, 300);
    }

    public void onItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.action_tabs_groups) {
            viewPager.setCurrentItem(0);
        } else if(itemId == R.id.action_tabs_friends) {
            viewPager.setCurrentItem(1);
        } else if(itemId == R.id.action_tabs_wallets) {
            viewPager.setCurrentItem(2);
        } else if(itemId == R.id.action_tabs_messages) {
            viewPager.setCurrentItem(3);
        }

    }

    private void registerMenuItemActions(Menu menu) {

        MenuItem buyCurrencyMenuItem = menu.getItem(0);
        MenuItem backUpMenuItem = menu.getItem(1);
        MenuItem walletsMenuItem = menu.getItem(2);
        MenuItem settingsMenuItem = menu.getItem(3);

        // @TODO: support menu needs work
        MenuItem support = menu.getItem(4);
        MenuItem logoutMenuItem = menu.getItem(5);

        final Context context = MainActivity.this;

        buyCurrencyMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(context, BuyCurrencyActivity.class),
                        NavigationUtils.enterStageBottom(MainActivity.this));
                return false;
            }
        });

        backUpMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, BackupActivity.class),
                    NavigationUtils.enterStageBottom(MainActivity.this));
                return false;
            }
        });

        settingsMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class),
                        NavigationUtils.enterStageBottom(MainActivity.this));
                return false;
            }
        });

        walletsMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, WalletsActivity.class),
                        NavigationUtils.enterStageBottom(MainActivity.this));
                return false;
            }
        });

        logoutMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                UserSessionManager sessionManager = new UserSessionManager(MainActivity.this);
                sessionManager.logoutUser(new RedirectHandler() {
                    @Override
                    public void go(Context context) {
                        RefreshTokenService.cancelRefreshTokenCheck(getApplicationContext());

                        Intent intent = new Intent(context, LaunchActivity.class);
                        Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(),
                                R.anim.back_slide_right, R.anim.back_slide_left).toBundle();
                        startActivity(intent, bundle);
                    }
                });

                return false;
            }
        });

    }
}
