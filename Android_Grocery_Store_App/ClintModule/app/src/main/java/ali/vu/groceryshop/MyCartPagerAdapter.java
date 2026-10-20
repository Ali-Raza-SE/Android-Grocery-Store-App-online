package ali.vu.groceryshop;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyCartPagerAdapter extends FragmentStatePagerAdapter {

    public MyCartPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PendingOrdersFragment();
        } else {
            return new OrderHistoryFragment();
        }
    }

    @Override
    public int getCount() {
        return 2; // Two tabs: Pending Orders and Order History
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Pending Orders";
        } else {
            return "Order History";
        }
    }
}
