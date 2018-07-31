package monsterstack.io.partner.main;

import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import javax.inject.Inject;

import monsterstack.io.api.InvitationService;
import monsterstack.io.api.PartnerService;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.api.resources.Currency;
import monsterstack.io.api.resources.Frequency;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.api.resources.Identifier;
import monsterstack.io.api.resources.Partner;
import monsterstack.io.partner.Application;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.common.HasDimmingSupport;
import monsterstack.io.partner.domain.Contact;
import monsterstack.io.partner.main.control.GroupCreationControl;
import monsterstack.io.partner.main.presenter.GroupCreationPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupCreationActivity extends BasicActivity implements GroupCreationControl, HasDimmingSupport {
    public static final String EXTRA_INVITES = "invited.contacts";
    public static final Double MIN_GOAL = 150.00;
    private static final Integer CREATE_GROUP_RESULT_CODE = 101;

    private GroupCreationPresenter presenter;
    private List<Contact> contactList;

    @Inject
    PartnerService partnerService;
    @Inject
    InvitationService invitationService;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contactList = (List<Contact>)getIntent().getSerializableExtra(EXTRA_INVITES);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(EXTRA_INVITES, contactList);

        this.presenter = getPresenterFactory().getGroupCreationPresenter(
                this, this);

        this.presenter.present(Optional.of(metadata));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submit_action, menu);

        menu.findItem(R.id.submit_button).setOnMenuItemClickListener(item -> {
            onSubmit(item);
            return false;
        });
        return true;
    }

    @Override
    public void injectDependencies(BasicActivity basicActivity) {
        super.injectDependencies(basicActivity);
        ((Application) getApplication()).component().inject(this);
    }

    @Override
    public int getContentView() {
        return R.layout.group_new;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.group_new;
    }


    @Override
    public void onSubmit(MenuItem menuItem) {
        Partner partner = createPartner();

        if (null == partner.getName() || partner.getName().isEmpty()) {
            presenter.showError(presenter.getRootView(), "You must give the Group a name.");
        } else if (null == partner.getGoal() || partner.getGoal() < MIN_GOAL) {
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            presenter.showError(presenter.getRootView(), "You must set a minimum goal of " + format.format(MIN_GOAL));
        } else {
            final List<Contact> contactList = this.contactList;
            Call<Identifier> call = partnerService.createPartner(partner);
            presenter.showProgressBar();
            call.enqueue(groupCreationCallbackHandler(contactList));
        }
    }

    @Override
    public String calculateBaseContributionInAmountPerMonth() {
        return NumberFormat.getCurrencyInstance().format(calculateBaseContributionPerMonth());
    }

    @Override
    public String calculateBaseContributionInAmountPerWeek() {
        return NumberFormat.getCurrencyInstance().format(calculateBaseContributionPerWeek());
    }

    public Double calculateBaseContributionPerWeek() {
        int numberOfSlots = presenter.getNumberOfSlots();
        double goal = presenter.getGoal();

        return (goal/numberOfSlots)/4;
    }

    public Double calculateBaseContributionPerMonth() {
        int numberOfSlots = presenter.getNumberOfSlots();
        double goal = presenter.getGoal();

        return (goal/numberOfSlots);
    }

    @Override
    public String calculateBaseContributionPercentageOfGoal() {
        int numberOfSlots = presenter.getNumberOfSlots();
        double goal = presenter.getGoal();
        return NumberFormat.getPercentInstance().format((goal/numberOfSlots)/goal);
    }

    @Override
    public int calculateDurationInMonths() {
        return presenter.getNumberOfSlots();
    }

    @Override
    public Context getContext() {
        return this;
    }

    // -- Private
    private Partner createPartner() {
        UserSessionManager sessionManager = new UserSessionManager(getContext());
        AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();
        Partner partner = new Partner();
        partner.setOrganizerId(authenticatedUser.getId());
        partner.setBankerId(authenticatedUser.getId());

        if (presenter.getFrequency().equals(Frequency.weekly)) {
            partner.setContributionFrequency(Frequency.weekly);
            partner.setBaseContribution(this.calculateBaseContributionPerWeek());
        } else {
            partner.setContributionFrequency(Frequency.monthly);
            partner.setBaseContribution(this.calculateBaseContributionPerMonth());
        }

        partner.setCurrency(Currency.usd);
        partner.setDrawFrequency(Frequency.monthly);
        partner.setGoal(presenter.getGoal());
        partner.setName(presenter.getName());
        partner.setNumberOfDrawSlots(presenter.getNumberOfSlots());
        partner.setPublishedAt(new Date());

        return partner;
    }

    private Callback<Identifier> groupCreationCallbackHandler(final List<Contact> contacts) {
        UserSessionManager userSessionManager = new UserSessionManager(getContext());
        final AuthenticatedUser user = userSessionManager.getUserDetails();

        return new Callback<Identifier>() {

            @Override
            public void onResponse(Call<Identifier> call, Response<Identifier> response) {
                if (response.isSuccessful()) {
                    presenter.hideProgressBar();
                    Intent data = new Intent();
                    data.putExtra("groupId", UUID.randomUUID());
                    data.putExtra("members", (Serializable)contactList);
                    setResult(CREATE_GROUP_RESULT_CODE, data);

                    // Send Invites
                    saveInvitation(response.body().getId(), user.getId(), contacts, (error) -> {
                        if (null != error) {

                        } else {
                            finish();
                        }
                        // Void
                        return null;
                    });
                } else {
                    showHttpError("Group", new HttpError(response));
                }
            }

            @Override
            public void onFailure(Call<Identifier> call, Throwable t) {
                showError("Group", "Creation Failed!");
            }
        };
    }

    private void saveInvitation(String groupId, String userId, List<Contact> contacts, Function<HttpError, Void> callback) {
        callback.apply(null);
    }
}
