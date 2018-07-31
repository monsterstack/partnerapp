package monsterstack.io.partner.presenter.impl;

import android.app.Activity;
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

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.User;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.MainPagerAdapter;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.control.MainControl;
import monsterstack.io.partner.pager.NonSwipeableViewPager;
import monsterstack.io.partner.presenter.MainPresenter;

public class MainPresenterImpl implements MainPresenter {

    @BindView(R.id.bottom_navigation)
    BottomNavigationViewEx bottomNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    AvatarView avatar;

    View navHeader;

    private MainPagerAdapter mainPagerAdapter;
    private NonSwipeableViewPager viewPager;

    private MainControl control;

    @Override
    public Presenter<MainControl> present(Optional<Map> metadata) {

        if (null != ((AppCompatActivity)getControl()).getSupportActionBar())
            ((AppCompatActivity)getControl()).getSupportActionBar()
                    .setHomeAsUpIndicator(R.drawable.ic_view_headline_white_24dp);

        init();

        return this;
    }

    @Override
    public Presenter<MainControl> bind(MainControl control) {
        this.control = control;
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public MainControl getControl() {
        return control;
    }

    @Override
    public void updateAvatarWithAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        avatar.setUser(new monsterstack.io.avatarview.User(authenticatedUser.getFullName(),
                authenticatedUser.getAvatarUrl(), R.color.colorAccent));
        avatar.refreshDrawableState();
    }

    @Override
    public void closeDrawer() {
        DrawerLayout drawerLayout = ((Activity)getControl()).findViewById(R.id.drawer_layout);

        if(drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    private void init() {
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            onItemSelected(item);
            return false;
        });

        final UserSessionManager sessionManager = new UserSessionManager(getControl().getContext());

        User user = sessionManager.getUserDetails();
        user.setAvatarUrl(monsterstack.io.partner.domain.User.avatars[0]);

        Boolean openDrawer = ((Activity)getControl()).getIntent().getBooleanExtra("openDrawer", false);

        if(openDrawer)
            drawerLayout.openDrawer(Gravity.START);

        final Toolbar myToolbar = ((Activity)getControl()).findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Groups");
        NavigationView navView = ((Activity)getControl()).findViewById(R.id.nav_view);

        UserSessionManager userSessionManager = new UserSessionManager(getControl().getContext());
        AuthenticatedUser authenticatedUser = userSessionManager.getUserDetails();
        navHeader = navView.getHeaderView(0);
        avatar = navHeader.findViewById(R.id.userImage);
        avatar.setUser(new monsterstack.io.avatarview.User(authenticatedUser.getFullName(),
                authenticatedUser.getAvatarUrl(), R.color.colorAccent));

        TextView profileFullName = navHeader.findViewById(R.id.profile_fullname);
        profileFullName.setText(authenticatedUser.getFullName());
        TextView email = navHeader.findViewById(R.id.profile_email);
        email.setText(authenticatedUser.getEmailAddress());

        // When clicking nav-header -> go to profile
        navHeader.setOnClickListener(v -> onProfileClicked(navHeader));

        Menu menu = navView.getMenu();

        // Order Matters
        this.registerMenuItemActions(menu);

        viewPager = ((Activity)getControl()).findViewById(R.id.main_pager);
        viewPager.setCurrentItem(0);

        this.mainPagerAdapter = new MainPagerAdapter(((AppCompatActivity)getControl()).getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Fragment fragment = MainPresenterImpl.this.mainPagerAdapter.getItem(position);

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

    private void registerMenuItemActions(Menu menu) {

        MenuItem buyCurrencyMenuItem = menu.getItem(0);
        MenuItem backUpMenuItem = menu.getItem(1);
        MenuItem walletsMenuItem = menu.getItem(2);
        MenuItem settingsMenuItem = menu.getItem(3);

        // @TODO: support menu needs work
        MenuItem support = menu.getItem(4);
        MenuItem logoutMenuItem = menu.getItem(5);

        buyCurrencyMenuItem.setOnMenuItemClickListener(item -> {
            getControl().goToBuyCurrency();
            return false;
        });

        backUpMenuItem.setOnMenuItemClickListener(item -> {
            getControl().goToBackup();
            return false;
        });

        settingsMenuItem.setOnMenuItemClickListener(item -> {
            getControl().goToSettings();
            return false;
        });

        walletsMenuItem.setOnMenuItemClickListener(item -> {
            getControl().goToWallets();
            return false;
        });

        logoutMenuItem.setOnMenuItemClickListener(item -> {
            UserSessionManager sessionManager = new UserSessionManager(getControl().getContext());
            sessionManager.logoutUser(sessionContext -> {
                getControl().goToLaunch();
            });

            return false;
        });

    }

    public void onProfileClicked(View view) {
        control.goToProfile();
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
}
