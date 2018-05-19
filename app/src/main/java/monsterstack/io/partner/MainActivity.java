package monsterstack.io.partner;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.api.RedirectHandler;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.User;
import monsterstack.io.api.service.RefreshTokenService;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.partner.adapter.MainPagerAdapter;
import monsterstack.io.partner.menu.BackupActivity;
import monsterstack.io.partner.menu.BuyCurrencyActivity;
import monsterstack.io.partner.menu.ProfileActivity;
import monsterstack.io.partner.menu.SettingsActivity;
import monsterstack.io.partner.menu.WalletsActivity;
import monsterstack.io.partner.utils.NavigationUtils;


public class MainActivity extends AppCompatActivity {
    private MainPagerAdapter mainPagerAdapter;
    private ViewPager viewPager;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    AvatarView avatar;

    View navHeader;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        asyncInit();
    }

    protected void asyncInit() {
        final UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());

        User user = sessionManager.getUserDetails();
        user.setAvatarUrl(monsterstack.io.partner.domain.User.avatars[0]);

        Boolean openDrawer = getIntent().getBooleanExtra("openDrawer", false);

        if(openDrawer)
            drawerLayout.openDrawer(Gravity.START);

        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
        final TextView toolbarTitle = myToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Groups");

        ImageButton navButton = findViewById(R.id.toolbar_navbutton);
        NavigationView navView = findViewById(R.id.nav_view);


        navHeader = navView.getHeaderView(0);
        avatar = navHeader.findViewById(R.id.userImage);
        avatar.setUser(new monsterstack.io.avatarview.User("Zachary Rote", null, R.color.green));

//        Glide.with(this).load(user.getAvatarUrl()).placeholder(R.drawable.ic_launcher_background)
//                .dontAnimate().into(avatar);
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileClicked(navHeader);
            }
        });

        Menu menu = navView.getMenu();

        // Order Matters
        this.registerMenuItemActions(menu);

        navButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

                if(drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.START);
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });


        viewPager = findViewById(R.id.main_pager);
        viewPager.setCurrentItem(0);

        this.mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);

        // Action Bar at Bottom
        initializeFriendsControl(viewPager);
        initializeGroupsControl(viewPager);
        initializeWalletsControl(viewPager);
        initializeInboxControl(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                Fragment fragment = MainActivity.this.mainPagerAdapter.getItem(position);
                String title = (String)fragment.getArguments().get("title");
                toolbarTitle.setText(title);
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

    public void onProfileClicked(View view) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent, NavigationUtils.enterStageBottom(MainActivity.this));
    }

    private void initializeFriendsControl(final ViewPager viewPager) {
        ImageButton friendsButton = findViewById(R.id.action_tabs_friends);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void initializeGroupsControl(final ViewPager viewPager) {
        ImageButton groupsButton = findViewById(R.id.action_tabs_groups);
        groupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

    }

    private void initializeWalletsControl(final ViewPager viewPager) {
        ImageButton walletsButton = findViewById(R.id.action_tabs_wallets);
        walletsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
    }

    private void initializeInboxControl(final ViewPager viewPager) {
        ImageButton inboxButton = findViewById(R.id.action_tabs_inbox);
        inboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });
    }

    private void registerMenuItemActions(Menu menu) {

        MenuItem buyCurrencyMenuItem = menu.getItem(0);
        MenuItem backUpMenuItem = menu.getItem(1);
        MenuItem walletsMenuItem = menu.getItem(2);
        MenuItem settingsMenuItem = menu.getItem(3);

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
