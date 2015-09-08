package querytest.test.samer.myapplicationssss;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by SAMER on 29-Jul-15.
 */
public class DriversFragment  extends Fragment {

    ImageView icon ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drivers,container,false);



    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        icon = (ImageView) getActivity().findViewById(R.id.icon_driver);

        icon.setImageResource(R.mipmap.fr_driver);
    }
}
